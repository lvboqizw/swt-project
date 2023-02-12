package computech.user;



import org.salespointframework.useraccount.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.util.Streamable;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.LinkedList;
import java.util.Optional;
import java.util.Random;

/**
 * This is a Spring MVC conmtroller to manage all Users
 *
 */

@Service
@Transactional
public class UserManagement {

	private final UserRepository userRepository;
	private final UserAccountManagement userAccounts;
	@Autowired
	private JavaMailSender emailSender = new JavaMailSenderImpl();
	private final String alphabet = "ABCDEFGHIJKLMNOPQRSTUVWXYZ";
	private String code;

	/**
	 * Creates a {@link UserManagement}
	 *
	 * @param userRepository
	 * @param userAccounts
	 */
	public UserManagement(UserRepository userRepository, UserAccountManagement userAccounts) {
		this.userRepository = userRepository;
		this.userAccounts = userAccounts;
	}


	/**
	 * Creates a new User. Sends an Email to his Email address, with his activation code.
	 * Disables his UserAccount and saves his UserAccount into the
	 * UserAccountManagement and his User objekt into the userRepository
	 *
	 * @param form
	 * @return
	 */
	public User createUser(RegistrationForm form) {
		User nutzer;
		var password = Password.UnencryptedPassword.of(form.getPassword());
		var userAccount = userAccounts.create(form.getUserName(), password, form.getEmail(), Role.of(form.getRole()));
		userAccount.setFirstname(form.getForename());
		userAccount.setLastname(form.getName());
		userAccounts.save(userAccount);
		userAccounts.disable(userAccount.getId());

		Random r = new Random();
		code = "";
		for(int i=0; i<5 ; i++){
			code = code + alphabet.charAt(r.nextInt(alphabet.length()));
		}

		SimpleMailMessage message = new SimpleMailMessage();
		message.setFrom("swtproject2020@gmail.com");
		message.setTo(form.getEmail());
		message.setSubject("Validation Link");
		message.setText("Dear " + form.getName() +", " +
				"this is your activation Link: http://localhost:8080/activation/" + form.getUserName() + "/" + code);
		emailSender.send(message);

		if (form.getRole().equals("BusinessCustomer")){
			Random zahl = new Random();
			LinkedList<User> SalesManager = this.findByRole("SalesManager");
			String username = SalesManager.get(zahl.nextInt(SalesManager.size())).getUserAccount().getUsername();
			nutzer = new User(userAccount, form.getAddress(), username, code);
			User zw = this.findByUsername(username);
			zw.addassosiated(nutzer.getUserAccount().getUsername());
			this.saveUser(zw);
		} else {
			nutzer = new User(userAccount, form.getAddress(), code);
		}
		return userRepository.save(nutzer);
	}


	/**
	 * Creates a new User, saves his UserAccount into the UserAccountManagement and his User objekt into the userRepository
	 *
	 * @param form
	 * @return
	 */
	public User addUser(RegistrationForm form) {
		User nutzer;
		var password = Password.UnencryptedPassword.of(form.getPassword());
		var userAccount = userAccounts.create(form.getUserName(), password, form.getEmail(), Role.of(form.getRole()));
		userAccount.setFirstname(form.getForename());
		userAccount.setLastname(form.getName());
		userAccounts.save(userAccount);
		if (form.getRole().equals("BusinessCustomer")){
			Random zahl = new Random();
			LinkedList<User> SalesManager = this.findByRole("SalesManager");
			String username = SalesManager.get(zahl.nextInt(SalesManager.size())).getUserAccount().getUsername();
			nutzer = new User(userAccount, form.getAddress(), username, null);
			User zw = this.findByUsername(username);
			zw.addassosiated(nutzer.getUserAccount().getUsername());
			this.saveUser(zw);
		} else {
			nutzer = new User(userAccount, form.getAddress(), null);
		}
		return userRepository.save(nutzer);
	}


	/**
	 * Returns all Users
	 *
	 * @return
	 */
	public Streamable<User> findAll() {
		return userRepository.findAll();
	}


	/**
	 * Returns the User with that username
	 *
	 * @param username
	 * @return
	 */
	public User findByUsername(String username) {
		User result = null;
		Streamable<User> user = userRepository.findAll();
		for (User nutzer: user) {
			if (nutzer.getUserAccount().getUsername().equals(username)) {
				result = nutzer;
			}
		}
		return result;
	}


	/**
	 * Returns a LinkedList<User> with all users with the given role
	 *
	 * @param role
	 * @return
	 */
	public LinkedList<User> findByRole(String role) {
		LinkedList<User> result = new LinkedList();
		Streamable<User> user = userRepository.findAll();
		for (User nutzer: user) {
			if (nutzer.getUserAccount().hasRole(Role.of(role)) == true) {
				result.add(nutzer);
			}
		}
		return result;
	}


	/**
	 * Deletes the User
	 *
	 * @param nutzer
	 */
	public void delete(User nutzer){
		userAccounts.disable(nutzer.getUserAccount().getId());
		userRepository.delete(nutzer);
	}


	/**
	 * Returns the User with the given ID
	 *
	 * @param id
	 * @return
	 */
	public User findByID(Long id) {
		Optional<User> nutzer = userRepository.findById(id);
		return nutzer.get();
	}


	/**
	 * Deletes the User with the given ID
	 *
	 * @param id
	 */
	public void deleteUserById(Long id){
		User nutzer= this.findByID(id);
		this.delete(nutzer);
	}


	/**
	 * Changes the password of the User that has the given username
	 *
	 * @param username
	 * @param password
	 */
	public void changepassword(String username, String password){
		User nutzer = this.findByUsername(username);
		userAccounts.changePassword(nutzer.getUserAccount(), Password.UnencryptedPassword.of(password));
	}


	/**
	 * Changes the lastname of the User that has the given username
	 *
	 * @param username
	 * @param lastname
	 */
	public void changeLastname(String username, String lastname) {
		User nutzer = this.findByUsername(username);
		nutzer.changeLastname(lastname);
		userRepository.save(nutzer);
	}


	/**
	 * Changes the first of the User that has the given username
	 *
	 * @param username
	 * @param firstname
	 */
	public void changeFirstname(String username, String firstname) {
		User nutzer = this.findByUsername(username);
		nutzer.changeFirstname(firstname);
		userRepository.save(nutzer);
	}


	/**
	 * Changes the address of the User that has the given username
	 *
	 * @param username
	 * @param address
	 */
	public void changeAddress(String username, String address) {
		User nutzer = this.findByUsername(username);
		nutzer.changeAddress(address);
		userRepository.save(nutzer);
	}


	/**
	 * Changes the billing address of the User that has the given username
	 *
	 * @param username
	 * @param address
	 */
	public void changeBillingAddress(String username, String address){
		User nutzer = this.findByUsername(username);
		nutzer.setBillingAddress(address);
		userRepository.save(nutzer);
	}


	/**
	 * Changes the Email address of the User that has the given username
	 *
	 * @param username
	 * @param email
	 */
	public void changeEmail(String username, String email) {
		User nutzer = this.findByUsername(username);
		nutzer.changeEmail(email);
		userRepository.save(nutzer);
		userAccounts.save(nutzer.getUserAccount());
	}


	/**
	 * Changes the Role of the User that has the given id
	 *
	 * @param id
	 * @param role
	 */
	public void changeRole(long id, String role) {
		User nutzer = this.findByID(id);
		nutzer.changeRole(role);
		userRepository.save(nutzer);
		userAccounts.save(nutzer.getUserAccount());
	}


	/**
	 * Changes the Assosiated of the User that has the given username
	 *
	 * @param username
	 * @param newassosiated
	 */
	public void changeAssosiated(String username, String newassosiated){
		User nutzer = this.findByUsername(username);
		nutzer.changeAssosiated(newassosiated);
		userRepository.save(nutzer);
	}


	/**
	 * Deactivates the User that has the Given username
	 *
	 * @param userName
	 */
	public void deaktivateUser(String userName) {
		Optional<UserAccount> user = userAccounts.findByUsername(userName);
		userAccounts.disable(user.get().getId());
	}


	/**
	 * Activates the User with that given Username if the code matches with the one that is saved in the User
	 *
	 * @param userName
	 * @param code
	 */
	public void aktivateUser(String userName, String code){
		Optional<UserAccount> user = userAccounts.findByUsername(userName);
		User nutzer = this.findByUsername(userName);
		if (nutzer.getCode().equals(code)) {
			userAccounts.enable(user.get().getId());
		}
	}


	/**
	 * Saves a user into the UserRepository
	 *
	 * @param user
	 */
	public void saveUser(User user){
		userRepository.save(user);
		userAccounts.save(user.getUserAccount());
	}


	/**
	 * Returns the ID of the Assosiate of the given UserAccount
	 *
	 * @param user
	 * @return
	 */
	public long getAssosiatedID (UserAccount user){
		User nutzer;
		nutzer = this.findByUsername(user.getUsername());
		String name = nutzer.getassosiated().getFirst();
		nutzer = findByUsername(name);
		return nutzer.getId();
	}
}
