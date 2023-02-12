package computech.user;


/**
 * Saves all the data to create a new User
 *
 */
public class RegistrationForm {

	private final String userName;

	private final String forename;

	private final String name;

	private final String password;

	private final String matchingpassword;

	private final String email;

	private final String address;

	private final String role;



	public RegistrationForm(String userName, String name, String forename,
							String password, String matchingpassword, String email, String address, String role) {
		this.userName = userName;
		this.name = name;
		this.forename = forename;
		this.password = password;
		this.matchingpassword = matchingpassword;
		this.email = email;
		this.address = address;
		this.role = role;
	}

	public String getUserName(){return userName;}

	public String getName() {
		return name;
	}

	public String getForename() {
		return forename;
	}

	public String getPassword() {
		return password;
	}

	public String getEmail() {
		return email;
	}

	public String getAddress() {
		return address;
	}

	public String getRole() {
		return role;
	}

	public String getMatchingpassword() {
		return matchingpassword;
	}

}
