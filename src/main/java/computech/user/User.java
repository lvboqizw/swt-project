package computech.user;


import org.salespointframework.useraccount.Role;
import org.salespointframework.useraccount.UserAccount;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.OneToOne;
import java.util.LinkedList;

/**
 * Services as the main point there the data of the User is saved
 */

@Entity
public class User {
	private @Id @GeneratedValue long id;

	private String address, code, billingAddress;

	private LinkedList<String> assosiated = new LinkedList<String>();

	@OneToOne
	private UserAccount userAccount;

	private User(){}


	/**
	 * Creates a User without an Assosiate
	 *
	 * @param userAccount
	 * @param address
	 * @param code
	 */
	public User(UserAccount userAccount, String address, String code) {
		this.userAccount = userAccount;
		this.address = address;
		this.code = code;
	}


	/**
	 * Creates a User with an Assosiated
	 *
	 * @param userAccount
	 * @param address
	 * @param assosiated
	 * @param code
	 */
	public User(UserAccount userAccount, String address, String assosiated , String code) {
		this.userAccount = userAccount;
		this.address = address;
		this.assosiated.add(assosiated);
		this.code =code;
	}

	public void setAddress(String address) {
		this.address = address;
	}

	public String getAddress() {
		return address;
	}

	public void setBillingAddress(String billingAddress) {
		this.billingAddress = billingAddress;
	}

	public String getBillingAddress() {
		return billingAddress;
	}

	public UserAccount getUserAccount() {
		return userAccount;
	}

	public long getId() {
		return id;
	}

	public String getForename(){
		return userAccount.getFirstname();
	}

	public String getName() {
		return userAccount.getLastname();
	}

	public String getRole() {
		return userAccount.getRoles().toList().get(0).getName();
	}

	public void changeLastname(String lastname) {
		userAccount.setLastname(lastname);
	}

	public void changeFirstname(String forename) {
		userAccount.setFirstname(forename);
	}

	public void changeAddress(String address) {
		this.address = address;
	}

	public void changeEmail(String email) {
		userAccount.setEmail(email);
	}

	public void changeRole(String newrole) {
		userAccount.remove(userAccount.getRoles().toList().get(0));
		userAccount.add(Role.of(newrole));
	}

	public LinkedList<String> getassosiated(){
		return assosiated;
	}

	public void changeAssosiated(String assosiated){
		this.assosiated.clear();
		this.assosiated.add(assosiated);
	}

	public void addassosiated(String username){
		assosiated.addLast(username);
	}

	public String getCode() {
		String zw = code;
		code = null;
		return zw;
	}
}
