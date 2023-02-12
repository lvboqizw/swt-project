package computech.sales;

import computech.assignment.*;
import computech.catalog.HardwareType;
import computech.user.User;
import computech.user.UserManagement;
import org.salespointframework.useraccount.UserAccount;
import org.salespointframework.useraccount.web.LoggedIn;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import java.util.LinkedList;
import java.util.List;


/**
 * Spring Controller for managing all repair requests made by the customer
 * @author Sophian
 */

@Controller
@PreAuthorize("isAuthenticated()")

public class RepairController{

	private final AssignmentManager assignmentManager;
	private final UserManagement userManagement;
	RepairRepository repairRepository;
	AssignmentRepository assignmentRepository;



	public RepairController(AssignmentRepository assignmentRepository, AssignmentManager assignmentManager, UserManagement userManagement, RepairRepository repairRepository){
		this.assignmentManager = assignmentManager;
		this.userManagement = userManagement;
		this.repairRepository = repairRepository;
		this.assignmentRepository = assignmentRepository;
	}


	/**
	 * displays the input data form and shows the customer a list of their repair requests
	 * @param model Model view
	 * @param userAccount Session Customer
	 * @return repairRequest
	 */
	@PostMapping("/service/makeRepairRequest")
	String makeRepairRequest(@LoggedIn UserAccount userAccount, Model model){
		User user = userManagement.findByUsername(userAccount.getUsername());
		Long id = user.getId();
		List<RepairAssignment> myRepairRequests = new LinkedList<>();
		repairRepository.findAll().forEach(assignment ->{
			if(id == assignment.getUserId()){
				myRepairRequests.add(assignment);
			}
		});
		model.addAttribute("myRepairRequests", myRepairRequests);
		return "repairRequest";
	}


	/**
	 * handles the parameters after the customer made his request
	 * @param userAccount Session Customer
	 * @param boughtHere determines if the item was bought at Computech
	 * @param year year of the purchase date
	 * @param month month of the purchase date
	 * @param warranty determines if the item has valid warranty
	 * @param type hardware type of the item
	 * @param name name of the item
	 * @param flaw describes what needs to be repaired
	 * @return confirmedRepairPage
	 */
	@PostMapping("/sendRepairRequest")
	String sendRepairRequest(@LoggedIn UserAccount userAccount, @RequestParam(name = "boughtHere")String boughtHere, @RequestParam(name = "year")int year, @RequestParam(name = "month")int month, @RequestParam(name = "warranty")String warranty,@RequestParam(name = "type")HardwareType type ,@RequestParam(name = "name") String name, @RequestParam(name = "flaw")String flaw){
		boolean wasBoughtAtOurStore = true;
		boolean hasWarranty = true;
		if(warranty.equals("Nein")){
			hasWarranty = false;
		}
		if(boughtHere.equals("Nein")){
			wasBoughtAtOurStore = false;
		}
		RepairItem repairItem = new RepairItem(name, flaw, type, month, year, hasWarranty, wasBoughtAtOurStore);
		User user = userManagement.findByUsername(userAccount.getUsername());
		Long id = user.getId();
		assignmentManager.addRepairAssignment(id, repairItem);
		return "confirmedRepairPage";
	}

	/**
	 * displays page that tells the customer his request was submitted
	 * @return redirect:/service
	 */
	@PostMapping("/confirmedRepairPage")
	String confirmedRepairPage(){
		return "redirect:/service";
	}

	/**
	 * (employee) confirms the repair assignment made by the customer
	 * @param id assignment Id
	 * @return redirect:/management
	 */
	@PostMapping("/acceptRepairRequest")
	String acceptRepairRequest(@RequestParam(name = "id")Long id){
		RepairAssignment repA = (RepairAssignment)assignmentManager.getAssignmentById(id);
		assignmentManager.confirmAssignment(repA);
		return "redirect:/management";
	}



}
