package computech.sales;

import computech.assignment.RepairAssignment;
import org.springframework.data.repository.CrudRepository;

public interface RepairRepository extends CrudRepository<RepairAssignment, Long> {
}
