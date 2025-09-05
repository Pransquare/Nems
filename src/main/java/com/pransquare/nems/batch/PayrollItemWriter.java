//package ai.sigmasoft.emsportal.batch;
//
//	import java.util.List;
//
//import org.springframework.batch.item.Chunk;
//import org.springframework.batch.item.ItemWriter;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//import ai.sigmasoft.emsportal.entities.PayrollStagingEntity;
//import jakarta.persistence.EntityManager;
//import jakarta.persistence.EntityManagerFactory;
//
//	@Component
//	public  class PayrollItemWriter implements ItemWriter<PayrollStagingEntity> {
//
//	    @Autowired
//	    private EntityManagerFactory entityManagerFactory;
//
//	    public void write(List<? extends PayrollStagingEntity> items) {
//	        EntityManager entityManager = entityManagerFactory.createEntityManager();
//	        entityManager.getTransaction().begin();
//	        for (PayrollStagingEntity item : items) {
//	            entityManager.persist(item);
//	        }
//	        entityManager.getTransaction().commit();
//	        entityManager.close();
//	    }
//
//		@Override
//		public void write(Chunk<? extends PayrollStagingEntity> chunk) throws Exception {
//			// TODO Auto-generated method stub
//			
//		}
//	}
//
//


