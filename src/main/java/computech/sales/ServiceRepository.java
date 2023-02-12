package computech.sales;


import computech.assignment.ServiceAssignment;
import org.springframework.data.repository.CrudRepository;



public interface ServiceRepository extends CrudRepository<ServiceAssignment, Long> {


}
