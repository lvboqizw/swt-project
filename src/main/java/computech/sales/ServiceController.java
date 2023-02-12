package computech.sales;

import computech.assignment.*;
import computech.user.User;
import computech.user.UserManagement;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.GetMapping;
import java.util.LinkedList;
import java.util.List;


/**
 * Spring Controller for managing all service requests made by the customer
 * @author Sophian
 */

@Controller
@PreAuthorize("isAuthenticated()")

public class ServiceController {

	private final AssignmentManager assignmentManager;
	private final UserManagement userManagement;
	ServiceRepository serviceRepository;


	public ServiceController(AssignmentManager assignmentManager, UserManagement userManagement, ServiceRepository serviceRepository){
		this.assignmentManager = assignmentManager;
		this.userManagement = userManagement;
		this.serviceRepository = serviceRepository;
	}


	/**
	 * displays the main service page
	 * @return service
	 */
	@GetMapping("/service")
	String makeServiceRequest(){
		return "service";
	}


	/**
	 * opens the page where customers can contact employees
	 * displays a list of the customers messages once they're answered
	 * the subject is the message sent by the customer, the answer is the message from an employee
	 * @param model Model view
	 * @param userAccount Session Customer
	 * @return forum
	 */
	@PostMapping("/service/forum")
	String openForum(Model model, @LoggedIn UserAccount userAccount){
		User user = userManagement.findByUsername(userAccount.getUsername());
		Long id = user.getId();
		List<Question> userQuestions = new LinkedList<>();
		serviceRepository.findAll().forEach(serviceAssignment -> {
		long serId = serviceAssignment.getUserid();
			if(serId == id && serviceAssignment.getState() == AssignmentState.CONFIRMED){
					 String subject = serviceAssignment.getQuestion().getSubject();
					 String content = serviceAssignment.getQuestion().getContent();
					Question answer = new Question(subject, content);
					userQuestions.add(answer);
			}
		});
		model.addAttribute("userQuestions", userQuestions);
		return "forum";
	}


	/**
	 * handles the input messages by the customer
	 * @param userAccount Session Customer
	 * @param content content of the message
	 * @param subject subject of the message
	 * @return redirect:/service
	 */
	@PostMapping("/sendQuestion")
	String sendQuestion(@LoggedIn UserAccount userAccount, @RequestParam(name = "content") String content, @RequestParam(name = "subject") String subject){
		User user = userManagement.findByUsername(userAccount.getUsername());

		Question question = new Question(subject, content);
		Long id = user.getId();
		assignmentManager.addServiceAssignment(id, question);
		return "redirect:/service";
	}


	/**
	 * (employee) confirms the service assignment made by the customer by answering the message
	 * @param assignmentid assignment Id
	 * @param answer contains the answer
	 * @return redirect:/management
	 */
	@PostMapping("/confirmAnswer")
	String confirmAnswer(@RequestParam(name = "id") Long assignmentid, @RequestParam(name = "answer") String answer){
		ServiceAssignment serA = (ServiceAssignment)assignmentManager.getAssignmentById(assignmentid);
		serA.getQuestion().setSubject(serA.getQuestion().getContent());
		serA.getQuestion().setContent(answer);
		serviceRepository.save(serA);
		assignmentManager.confirmAssignment(serA);
		return "redirect:/management";
	}


}
