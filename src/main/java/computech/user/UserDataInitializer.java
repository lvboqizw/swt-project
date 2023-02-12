package computech.user;

import org.salespointframework.core.DataInitializer;
import org.salespointframework.useraccount.Password;
import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccountManagement;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;

import java.util.List;

/**
 * Initializes the Standard Users into the UserManagment
 */
@Component
@Order(10)
class UserDataInitializer implements DataInitializer {

	private static final Logger LOG = LoggerFactory.getLogger(UserDataInitializer.class);

	private final UserAccountManagement userAccountManagement;
	private final UserManagement userManagement;


	UserDataInitializer(UserAccountManagement userAccountManagement, UserManagement userManagement) {

		Assert.notNull(userAccountManagement, "UserAccountManagement must not be null!");
		Assert.notNull(userManagement, "UserRepository must not be null!");

		this.userAccountManagement = userAccountManagement;
		this.userManagement = userManagement;
	}


	@Override
	public void initialize() {

		if (userAccountManagement.findByUsername("Boss").isPresent()) {
			return;
		}

		LOG.info("Creating default users and customers.");


		var password = "1234";

		List.of(//
				new RegistrationForm("SalesManager","SalesManager-Name", "SalesManager-firstname" , password,
						password, "SalesManager@tudresde.de","SalesManageraddress","SalesManager"),
				new RegistrationForm("Rick","Sanchez", "Rick" , password,
						password, "RickSanchez@space.de","Platz der Republik 6","SalesManager"),
				new RegistrationForm("PrivateCustomer","PrivateCustomer-Name", "PrivateCustomer-firstname" , password,
						password, "PrivateCustomer@tudresde.de","PrivateCustomer-address","PrivateCustomer"),
				new RegistrationForm("Bending Unit 22","Rodríguez", "Bender Bending" , password,
						password, "2716057@beer.com","Space Street 1729","PrivateCustomer"),
				new RegistrationForm("Tom","Araya", "Tom" , password,
						password, "whiskey@cola.com","Hellroad 666","PrivateCustomer"),
				new RegistrationForm("Lemmy","Kilmister", "Lemmy" , password,
						password, "tomaraya@SLAYER.com","Forest Lawn Memorial Park 6300","PrivateCustomer"),
				new RegistrationForm("BusinessCustomer","BusinessCustomer-Name", "BusinessCustomer-firstname" , password,
						password, "BusinessCustomer@tudresde.de","BusinessCustomeraddress","BusinessCustomer"),
				new RegistrationForm("Angus","Young", "Angus McKinnon" , password,
						password, "AC@DC.uk","Highway to Hell","BusinessCustomer"),
				new RegistrationForm("Manager","Manager-Name", "Manager-firstname" , password,
						password, "Manager@tudresde.de","Manageraddress","Manager"),
				new RegistrationForm("Vulkan","Vulkan", "Primarch" , password,
						password, "flammer@melter.de","Nocturne","Worker"),
				new RegistrationForm("Worker","Worker-Name", "Worker-firstname" , password,
						password, "Worker@tudresde.de","Workeraddress","Worker")//
		).forEach(userManagement::addUser);


	}
}