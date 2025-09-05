package com.pransquare.nems.services;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Collections;
import java.util.Date;
import java.util.Locale;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.microsoft.aad.msal4j.ClientCredentialFactory;
import com.microsoft.aad.msal4j.ClientCredentialParameters;
import com.microsoft.aad.msal4j.ConfidentialClientApplication;
import com.microsoft.aad.msal4j.IAuthenticationResult;
import com.pransquare.nems.entities.EmailWrite;
import com.pransquare.nems.repositories.EmailWriteRepository;
import com.pransquare.nems.utils.ClientUtil;

import jakarta.persistence.EntityManager;
import jakarta.persistence.Query;

@Service
public class InvoiceExtract {
	
	private static final Logger logger = LogManager.getLogger(InvoiceExtract.class);
	@Value("${azure.client.id}")
    private static String clientId;

    @Value("${azure.client.secret}")
    private static String clientSecret;

    @Value("${azure.tenant.id}")
    private static String tenantId;
    
    @Value("${azure.tenant.authority}")
    private static String authority;

    @Value("${azure.scope}")
    private static String scope;

    @Value("${azure.graph.api.url}")
    private  static String graphApiUrl;

	private static final String CLIENT_ID = clientId;

    private static final String CLIENT_SECRET = clientSecret;
//    private static final String CLIENT_SECRET = "gQn8Q~ssyhd0tMJvPNWN7d.AEwX0dnG3W-SikdjK";
    private static final String TENANT_ID = clientSecret;
    private static final String AUTHORITY = authority+ TENANT_ID;
    private static final String SCOPE = scope;
    private static final String GRAPH_API_URL = graphApiUrl;


//    private static final String GRAPH_API_URL = "https://graph.microsoft.com/v1.0/users/venkataramana.prasad@pransquare.in/messages";

	    @Autowired
	    EntityManager entityManager;
	    
	    @Autowired
	    EmailWriteRepository emailWriteRepository;
	    
	    @Value("${remittancefile.path}")
		private String remittanceFilepath;
		
		@Value("${remittancefolder.path}")
		private String remittancefolder;


	   

	        public void getAccessToken() throws Exception {
	            String accessToken = "";
	            ConfidentialClientApplication app = ConfidentialClientApplication.builder(
	                    CLIENT_ID,
	                    ClientCredentialFactory.createFromSecret(CLIENT_SECRET))
	                    .authority(AUTHORITY)
	                    .build();

	            ClientCredentialParameters parameters = ClientCredentialParameters.builder(
	                    Collections.singleton(SCOPE)).build();

	            CompletableFuture<IAuthenticationResult> future = app.acquireToken(parameters);
	            accessToken = future.get().accessToken();
	            readEmails(accessToken, "donotreply@pransquare.in");

	        }


	        
	        public void readEmails(String accessToken, String userId) {
	            int totalEmailsProcessed = 0;
	            int mailsExtractedCount=0;
	            ObjectMapper mapper = new ObjectMapper();
	            
	            try {
	                String requestUrl = GRAPH_API_URL.replace("{user-id}", userId);
	                int count = 0;
	                
	                while (requestUrl != null && count < 5000) {
	                    HttpURLConnection conn = createHttpConnection(requestUrl, accessToken);
	                    int responseCode = conn.getResponseCode();
	                    
	                    if (responseCode != 200) {
	                        logger.error("Failed to fetch emails: HTTP error code " + responseCode);
	                        break;
	                    }
	                    
	                    String jsonResponse = readResponse(conn);
	                    JsonNode rootNode = mapper.readTree(jsonResponse);
	                    JsonNode messages = rootNode.path("value");

	                    for (JsonNode message : messages) {
	                        try {
	                            String messageId = message.path("id").asText();
	                            String subject = message.path("subject").asText();
	                            String fromPayer = extractDomain(message.path("from"), "emailAddress");
	                            String toPayee = extractDomain(message.path("toRecipients"), "emailAddress");
	                            String receivedDateStr = message.path("receivedDateTime").asText();
	                            LocalDateTime receivedDate = parseDate(receivedDateStr);
	                            
	                            String bodyContent = message.path("body").path("content").asText();
	                            fromPayer = ClientUtil.getClient(bodyContent);
	                            toPayee = ClientUtil.getPayee(bodyContent);
	                            logger.info(bodyContent.toCharArray());
	                            
	                            mailsExtractedCount++;

	                            if (fetchMaxRecievedDate(messageId)) {
	                            	saveEmailRecord(receivedDate, subject, messageId);
	                                processEmail(bodyContent, fromPayer, toPayee);
	                                totalEmailsProcessed++;
	                           
	                            }
	                            logger.info("Extracted email count: " + mailsExtractedCount);
	                            logger.info("Processed email count: " + totalEmailsProcessed);
	                        } catch (Exception e) {
	                            logger.error("Error processing email: " + e.getMessage(), e);
	                        }
	                    }

	                    requestUrl = rootNode.path("@odata.nextLink").asText(null);
	                    count += messages.size();
	                }

	            } catch (Exception e) {
	                logger.error("Error reading emails: " + e.getMessage(), e);
	            }
	            
	            logger.info("Total emails processed: " + totalEmailsProcessed);
	        }

	        // Utility Methods

	        private HttpURLConnection createHttpConnection(String url, String accessToken) throws IOException {
	            HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
	            conn.setRequestMethod("GET");
	            conn.setRequestProperty("Authorization", "Bearer " + accessToken);
	            conn.setRequestProperty("Accept", "application/json");
	            return conn;
	        }

	        private String readResponse(HttpURLConnection conn) throws IOException {
	            try (BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()))) {
	                return in.lines().collect(Collectors.joining());
	            }
	        }

	        private String extractDomain(JsonNode node, String field) {
	            if (node != null && node.has(field)) {
	                String email = node.path(field).path("address").asText("");
	                if (email.contains("@")) {
	                    return removeAfterFirstDot(email.split("@")[1]);
	                }
	            }
	            return "";
	        }

	        private LocalDateTime parseDate(String dateStr) {
	            return LocalDateTime.parse(dateStr.substring(0, dateStr.length() - 1)); // Handle trailing 'Z'
	        }

	        private void processEmail(String body, String from, String to) throws IOException {
//	            String excelFilePath = "C:\\Users\\VenkataramanaPrasad\\OneDrive - pransquare INC\\Desktop\\Email Read in single Sheet\\email_table_data_from_mailNew.xlsx";
	        	String excelFilePath= remittanceFilepath;
	        	new EmailTableToExcelNew().renderTableToExcel(body, excelFilePath, "MsoNormalTable", from, to);
	        }

	        private void saveEmailRecord(LocalDateTime receivedAt, String subject, String messageId) {
	            EmailWrite emailWrite = new EmailWrite();
	            emailWrite.setReceivedAt(receivedAt);
	            emailWrite.setSubject(subject);
	            emailWrite.setCreatedDate(LocalDateTime.now());
	            emailWrite.setCreatedBy("System");
	            emailWrite.setMessageId(messageId);
	            emailWriteRepository.save(emailWrite);
	        }

	            public String removeAfterFirstDot(String input) {
	        		// Find the index of the first dot
	        		int dotIndex = input.indexOf(".");
	        		// If a dot is found, return the substring before it; otherwise, return the
	        		// input
	        		return (dotIndex != -1) ? input.substring(0, dotIndex) : input;
	        	}

	        	public Boolean fetchMaxRecievedDate(String messageId) {
	        		String sql = "select recieved_at from emsportalprod.email_write where message_id='"+messageId+"'";



	        		// Execute native query
	        		Query query = entityManager.createNativeQuery(sql);
	        		logger.info("sql: "+query.getResultList());
	        		Boolean messageUid=false;
	        		// Fetch result as a single string
	        		if(query.getResultList().isEmpty()) {
	        			messageUid =true;
	        		}

	        		return messageUid;
	        	}

	        	public String checkDateFormat(String dateStr) {
	        		logger.info("dateStr:"+dateStr);
	        		String dateFormat="";
	        		if (dateStr.matches("^\\d{2}-[A-Za-z]{3}-\\d{4}$") || dateStr.matches("^\\d{2}-[A-Z]{3}-\\d{4}$")||
	        				dateStr.matches("^\\d{1}-[A-Za-z]{3}-\\d{4}$") || dateStr.matches("^\\d{1}-[A-Z]{3}-\\d{4}$")) {
	        			logger.info("dateStr:"+dateStr);
	        		    try {
	        		        // Define the format for '30-APR-2024'
	        		        SimpleDateFormat sourceFormat = new SimpleDateFormat("dd-MMM-yyyy", Locale.ENGLISH);
	        		        SimpleDateFormat targetFormat = new SimpleDateFormat("MMM dd, yyyy", Locale.ENGLISH);

	        		        // Parse the date and format it to 'MMM dd, yyyy'
	        		        Date date = sourceFormat.parse(dateStr);
	        		        dateFormat= targetFormat.format(date);
	        		    } catch (java.text.ParseException e) {
	        		        // Handle parse exception (optional)
	        		        e.printStackTrace();
	        		    }
	        		
	        		}
	        		logger.info("dateFormat:="+dateFormat);
	        		return dateFormat;
	        	}



				public String fileRename() {
					String dateTime=LocalDate.now().toString()+
							
                            +  LocalDateTime.now().getHour()+LocalDateTime.now().getMinute()+LocalDateTime.now().getSecond();
					
					 Path source = Paths.get(remittanceFilepath);
				        Path target = Paths.get(remittancefolder+"InvoiceExtractAR"+dateTime+".xlsx");

				        try {
				            Files.move(source, target, StandardCopyOption.REPLACE_EXISTING);
				            return "File Archived successfully!" ;
				        } catch (Exception e) {
				        	 return "Error Archiving file: " + e.getMessage();
				        }
				    }
					// TODO Auto-generated method stub
					
				}
	        	
	        	
	    

	    




