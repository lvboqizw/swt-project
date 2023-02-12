package computech.assignment;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Assignment Controller for debugging, not really implemented
 * @author Jannek Steinke
 */
@Controller
public class AssignmentController{
    private final AssignmentManager assignmentManager;

    AssignmentController(AssignmentManager assignmentManager){
        this.assignmentManager = assignmentManager;
    }
    /**
     *  for confirming Assignment
     * @param assignment
     * @return
     */
    @PostMapping("/assignments")
    String confirmAssignment(@RequestParam ("assignmentid") Assignment assignment){
        assignmentManager.confirmAssignment(assignment);
        return "redirect:/assignments";
    }
    /**
     * shows all Assignments
     * @param model
     * @return
     */
    @GetMapping("/assignments")
    String assignments(Model model){

        model.addAttribute("assignments", assignmentManager.getAllAssignments());
        return "assignments";
    }

	/**
	 *  change the sell discount for customer, the customer can only with this discount product sales
	 * @param discount the save place for discount, default 70
	 * @return redirect manage page
	 */

	@PostMapping("/changeDiscount")
	String changeDiscount(@RequestParam("discount") double discount){
		assignmentManager.setDiscount(discount/100);
		return "redirect:/management";
	}
}