//package ai.sigmasoft.emsportal.configurations;
//
//
//import javax.sql.DataSource;
//
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//import com.zaxxer.hikari.HikariDataSource;
//
//import java.util.Map;
//
//@Configuration
//public class DataSourceConfig {
//
//    @Autowired
//    private VaultSecretFetcher vaultSecretFetcher;
//
//    @Bean
//    public DataSource dataSource() {
//        Map<String, Object> credentials = vaultSecretFetcher.getDbCredentials();
//
//        String username = credentials.get("username").toString();
//        String password = credentials.get("password").toString();
//
//        HikariDataSource dataSource = new HikariDataSource();
//        dataSource.setJdbcUrl("jdbc:mysql://localhost:3306/emsportalprod");
//        dataSource.setUsername(username);
//        dataSource.setPassword(password);
//        return dataSource;
//    }
//}



