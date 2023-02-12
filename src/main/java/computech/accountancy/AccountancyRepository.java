package computech.accountancy;

import org.salespointframework.accountancy.AccountancyEntryIdentifier;
import org.salespointframework.accountancy.ProductPaymentEntry;
import org.salespointframework.useraccount.UserAccount;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.util.Streamable;

import java.time.LocalDateTime;

public interface AccountancyRepository extends CrudRepository<ProductPaymentEntry, AccountancyEntryIdentifier> {

	Streamable<ProductPaymentEntry> findByDateBetween(LocalDateTime from, LocalDateTime to);

	@Override
	Streamable<ProductPaymentEntry> findAll();

	Streamable<ProductPaymentEntry> findByUserAccount(UserAccount userAccount);
}
