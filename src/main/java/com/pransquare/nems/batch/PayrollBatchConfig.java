//package ai.sigmasoft.emsportal.batch;
//
//import ai.sigmasoft.emsportal.entities.PayrollStagingEntity;
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.launch.support.RunIdIncrementer;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.batch.item.file.mapping.FieldSetMapper;
//import org.springframework.batch.item.file.mapping.DefaultLineMapper;
//import org.springframework.batch.item.file.transform.DelimitedLineTokenizer;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Qualifier;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.core.io.FileSystemResource;
//
//import java.io.IOException;
//import java.util.Map;
//
//@Configuration
//@EnableBatchProcessing
//public class PayrollBatchConfig {
//
//    @Bean
//    public Job importPayrollJob(JobBuilder jobBuilder, Step importPayrollStep) {
//        return jobBuilder.incrementer(new RunIdIncrementer())
//                .flow(importPayrollStep)
//                .end()
//                .build();
//    }
//
//    @Bean
//    public Step importPayrollStep(StepBuilder stepBuilder,
//                                  @Qualifier("itemReaderPayroll") ItemReader<PayrollStagingEntity> reader,
//                                  @Qualifier("itemProcessorPayroll") ItemProcessor<PayrollStagingEntity, PayrollStagingEntity> processor,
//                                  @Qualifier("itemWriterPayroll") ItemWriter<PayrollStagingEntity> writer) {
//    	return  stepBuilder
//    		      
//    		      .reader()
//    		      .processor(processor)
//    		      .writer(writer)
//    		      .faultTolerant()
//    		      .skipLimit(2)
//    		      .skip(MissingUsernameException.class)
//    		      .skip(NegativeAmountException.class)
//    		      .build();
//    }
//
//    @Bean
//    @Qualifier("itemReaderPayroll")
//    public ItemReader<PayrollStagingEntity> itemReaderPayroll() throws IOException {
//        PoiItemReader<PayrollStagingEntity> reader = new PoiItemReader<>();
//        reader.setLinesToSkip(1);
//        reader.setResource(new FileSystemResource("path/to/your/excel/file.xlsx"));
//        reader.setRowMapper(new DefaultLineMapper<PayrollStagingEntity>() {{
//            setLineTokenizer(new DelimitedLineTokenizer() {{
//                setNames(new String[]{"column1", "column2", "column3"}); // replace with your column names
//            }});
//            setFieldSetMapper(new FieldSetMapper<PayrollStagingEntity>() {{
//                @Override
//                public PayrollStagingEntity mapFieldSet(Map<String, String> fieldSet) throws Exception {
//                    PayrollStagingEntity entity = new PayrollStagingEntity();
//                    entity.setColumn1(fieldSet.get("column1"));
//                    entity.setColumn2(fieldSet.get("column2"));
//                    entity.setColumn3(fieldSet.get("column3"));
//                    return entity;
//                }
//            }});
//        }});
//        return reader;
//    }
//
//    @Bean
//    @Qualifier("itemProcessorPayroll")
//    public ItemProcessor<PayrollStagingEntity, PayrollStagingEntity> itemProcessorPayroll() {
//        return new ItemProcessor<PayrollStagingEntity, PayrollStagingEntity>() {
//            @Override
//            public PayrollStagingEntity process(PayrollStagingEntity entity) throws Exception {
//                // add your processing logic here
//                return entity;
//            }
//        };
//    }
//
//    @Bean
//    @Qualifier("itemWriterPayroll")
//    public ItemWriter<PayrollStagingEntity> itemWriterPayroll() {
//        return new ListItemWriter<PayrollStagingEntity>() {{
//            setResource(new FileSystemResource("path/to/your/output/excel/file.xlsx"));
//            setRowMapper(new DefaultLineMapper<PayrollStagingEntity>() {{
//                setLineTokenizer(new DelimitedLineTokenizer() {{
//                    setNames(new String[]{"column1", "column2", "column3"}); // replace with your column names
//                }});
//                setFieldSetMapper(new FieldSetMapper<PayrollStagingEntity>() {{
//                    @Override
//                    public PayrollStagingEntity mapFieldSet(Map<String, String> fieldSet) throws Exception {
//                        PayrollStagingEntity entity = new PayrollStagingEntity();
//                        entity.setColumn1(fieldSet.get("column1"));
//                        entity.setColumn2(fieldSet.get("column2"));
//                        entity.setColumn3(fieldSet.get("column3"));
//                        return entity;
//                    }
//                }});
//            }});
//        }};
//    }
//}
//
//}



