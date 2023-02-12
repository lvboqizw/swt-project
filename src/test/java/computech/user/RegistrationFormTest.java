package computech.user;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;

import static org.junit.jupiter.api.Assertions.*;


@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class RegistrationFormTest {

	RegistrationForm form;

	@BeforeAll
	void setUp() {
		form = new RegistrationForm("tester","Tester-Name",
				"Tester-firstname" , "1234", "1234",
				"tester@tu-dresden.com","TestAddress","Tester");
	}

	@Test//201
	void getUserName() {
		assertEquals("tester", form.getUserName(),"Wrong with getUserName()");
	}

	@Test//202
	void getName() {
		assertEquals("Tester-Name", form.getName(),"Wrong with getName()");
	}

	@Test//203
	void getForename() {
		assertEquals("Tester-firstname", form.getForename(),"Wrong with getForeName()");
	}

	@Test//204
	void getPassword() {
		assertEquals("1234", form.getPassword(),"Wrong with getPassword()");
	}

	@Test//205
	void getEmail() {
		assertEquals("tester@tu-dresden.com", form.getEmail(),"Wrong with getEmail()");
	}

	@Test//206
	void getAddress() {
		assertEquals("TestAddress", form.getAddress(),"Wrong with getAddress()");
	}

	@Test//207
	void getRole() {
		assertEquals("Tester", form.getRole(),"Wrong with getRole()");
	}

	@Test//208
	void getMatchingpassword() {
		assertEquals("1234", form.getMatchingpassword(),"Wrong with getMatchingpassword()");
	}
}