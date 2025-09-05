//package ai.sigmasoft.emsportal.batch;
//import java.io.InputStream;
//
//import org.springframework.batch.core.Job;
//import org.springframework.batch.core.Step;
//import org.springframework.batch.core.configuration.annotation.EnableBatchProcessing;
//import org.springframework.batch.core.job.builder.JobBuilder;
//import org.springframework.batch.core.repository.JobRepository;
//import org.springframework.batch.core.step.builder.StepBuilder;
//import org.springframework.batch.core.step.tasklet.TaskletStep;
//import org.springframework.batch.item.ItemProcessor;
//import org.springframework.batch.item.ItemReader;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//import org.springframework.transaction.PlatformTransactionManager;
//import org.springframework.core.io.ClassPathResource;
//import org.springframework.core.io.Resource;
//import ai.sigmasoft.emsportal.entities.PayrollStagingEntity;
//
//@Configuration
//@EnableBatchProcessing
//public class BatchConfig {
//
//    @Bean
//    public Job importUserJob(JobRepository jobRepository, Step importStep) {
//        return new JobBuilder("importUserJob", jobRepository)
//                .start(importStep)
//                .build();
//    }
//
//    @Bean
//    public Step importStep(JobRepository jobRepository, PlatformTransactionManager transactionManager,
//                           ItemReader<PayrollStagingEntity> reader, 
//                           ItemProcessor<PayrollStagingEntity, PayrollStagingEntity> processor, 
//                           ItemWriter<PayrollStagingEntity> writer) {
//        return new StepBuilder("importStep", jobRepository)
//                .<PayrollStagingEntity, PayrollStagingEntity>chunk(50, transactionManager)
//                .reader(reader)
//                .processor(processor)
//                .writer(writer)
//                .build();
//    }
//    
//    
//    @Bean
//    public ItemReader<PayrollStagingEntity> itemReader() {
//        try {
//            Resource resource = new ClassPathResource("C:\\Users\\VenkataramanaPrasad\\Downloads\\Sample.xlsx");
//            InputStream inputStream = resource.getInputStream();
//            return new ExcelItemReader(inputStream);
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to create ExcelItemReader", e);
//        }
//    }
//}
//



