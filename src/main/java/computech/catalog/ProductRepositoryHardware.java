package computech.catalog;

import org.salespointframework.catalog.ProductIdentifier;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import java.util.Set;

import static computech.catalog.HardwareType.COOLING;
import static computech.catalog.HardwareType.PC;

public interface ProductRepositoryHardware extends CrudRepository<Hardware, Long> {

	Iterable<Hardware> findByType(HardwareType type, Sort sort);
	static final Sort DEFAULT_SORT = Sort.by("productIdentifier").descending();

	Iterable<Hardware> findByName(String name, Sort sort);

	@Override
	Streamable<Hardware> findAll();

	default Iterable<Hardware> findByType(HardwareType type) {
		return findByType(type, DEFAULT_SORT);
	}

	default Iterable<Hardware> findByName(String name){
		return findByName(name, DEFAULT_SORT);
	}

}