package computech.management;

import java.util.ArrayList;
import java.util.List;
import java.util.LinkedList;

import computech.assignment.*;
import computech.assignment.AssignmentType;

import computech.catalog.*;
import computech.sales.RepairItem;
import computech.user.User;
import computech.user.UserManagement;
import computech.warehouse.WarehouseItem;
import computech.warehouse.WarehouseManagement;
import computech.warehouse.WarehouseRepository;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;
import org.salespointframework.catalog.ProductIdentifier;
import org.salespointframework.inventory.InventoryItemIdentifier;
import org.salespointframework.order.Order;
import org.salespointframework.order.OrderLine;
import org.salespointframework.order.OrderManagement;
import org.salespointframework.payment.Cash;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.Errors;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.security.access.prepost.PreAuthorize;


import computech.user.RegistrationForm;

import javax.validation.constraints.NotNull;

import static org.salespointframework.core.Currencies.EURO;

/**
 * Controller for showing the Management Page
 * @author Jannek Steinke supported by the whole group
 */
@Controller
public class ManagementController {

    private final UserManagement userManagement;
    private final AssignmentManager assignmentManager;
    private final WarehouseRepository warehouseRepository;
	private final OrderManagement<Order> orderManagement;
	private CompuTechCatalog compuTechCatalog;
	private final WarehouseManagement warehouseManagement;


    /**
     * 
     * @param userManagement UserManagement from user package
     * @param assignmentManager AssignmentManagement from Assignment Package
     * @param warehouseRepository WarehouseRepository from warehouse package
     * @param orderManagement OrderManagement from Salespoint package
     * @param compuTechCatalog ComputechCatalog from catalog package
     * @param warehouseManagement WarehouseManagement from warehouse package
     */
    public ManagementController(UserManagement userManagement, AssignmentManager assignmentManager,
                                WarehouseRepository warehouseRepository, WarehouseManagement warehouseManagement,
                                OrderManagement<Order> orderManagement, CompuTechCatalog compuTechCatalog) {
                                 
		this.userManagement = userManagement;
		this.assignmentManager = assignmentManager;
		this.warehouseRepository = warehouseRepository;
		this.orderManagement = orderManagement;
		this.compuTechCatalog = compuTechCatalog;
		this.warehouseManagement = warehouseManagement;
	}

	/**
     * Confirm a OrderAssignment
     * @param assignmentid Id of Assignment
     * @return String
     */
    @PostMapping("/confirmAssign")
    String confirmOrder(@RequestParam ("assignmentid") Long assignmentid){
        assignmentManager.confirmAssignment(assignmentManager.getAssignmentById(assignmentid));
        return "redirect:/management";
    }
    /**
     * edit a OrderAssignment, only possible with Orders of BusinnesCustomers
     * @param model Model
     * @param assignmentid ID of Assignment
     * @return editorderassignment
     */
    @PostMapping("/editAssign")
    String editOrder(Model model, @RequestParam("assignmentid") Long assignmentid){
        OrderAssignment assignment = (OrderAssignment)assignmentManager.getAssignmentById(assignmentid);
        model.addAttribute("order", assignment.getOrder());
        model.addAttribute("user", assignment.getUser());
        model.addAttribute("assignmentid", assignmentid);
        return "editorderassignment";
    }
    /**
     * removeOrderline during edit process of editing businnes Order
     * @param model Model
     * @param assignmentid ID of Assingment
     * @param delorderLine Orderline which should be deleted
     * @return editorderassignment
     */
    @PostMapping("/removeOrderLine")
    String removeOrderLine(Model model, @RequestParam ("assignmentid") Long assignmentid,
     @RequestParam ("orderlineid") OrderLine delorderLine){
        OrderAssignment assignment = (OrderAssignment) assignmentManager.getAssignmentById(assignmentid);
        Order oldorder = assignment.getOrder();
        Order order = new Order(assignment.getUser().getUserAccount(), Cash.CASH);
        for (OrderLine orderLine : oldorder.getOrderLines()){
            if(!orderLine.equals(delorderLine)){
            ProductIdentifier ident = orderLine.getProductIdentifier();
            WarehouseItem item = warehouseRepository.findByProductIdentifier(ident).get();
            Product product = item.getProduct();
            order.addOrderLine(product, orderLine.getQuantity());
            }
        }
        orderManagement.payOrder(order);
		assignment.setOrder(order);
        assignmentManager.saveAssignment(assignmentid);
        model.addAttribute("order", order);
        model.addAttribute("user", assignment.getUser());
        model.addAttribute("assignmentid", assignmentid);
        return "editorderassignment";
    }
    /**
     * confirm a Service Assignment 
     * @param model Model
     * @param assignmentid Id of Assignment
     * @return confirmSerAssign
     */
    @PostMapping("/confirmSerAssign")
    String answerService(Model model, @RequestParam ("assignmentid") Long assignmentid){
        ServiceAssignment assignment = (ServiceAssignment)assignmentManager.getAssignmentById(assignmentid);
        User user = userManagement.findByID(assignment.getUserid());
		model.addAttribute("customer", user.getName());
        model.addAttribute("confirmSerAssign", assignment);
        return "confirmSerAssign";
    }
    /**
     * confirm a Repair Assignment
     * @param model Model 
     * @param assignmentId ID of Assignment
     * @return confirmRepAssign
     */
	@PostMapping("/confirmRepAssign")
	String confirmRepair(Model model, @RequestParam ("assignmentid") Long assignmentId){
		RepairAssignment ra = (RepairAssignment) assignmentManager.getAssignmentById(assignmentId);
		RepairItem ri = ra.getRepairItem();
		User user = userManagement.findByID(ra.getUserId());
		model.addAttribute("customer", user.getName());
		model.addAttribute("hardwaretype", ri.getType());
		model.addAttribute("nameOfProduct", ri.getName());
		model.addAttribute("flaw", ri.getFlaw());
		model.addAttribute("warranty", ri.hasWarranty());
		model.addAttribute("purchaseDate", ri.getDateWhenBought());
		model.addAttribute("boughtAtOurStore", ri.wasBoughtAtOurStore());
		model.addAttribute("ra", ra);
		return "confirmRepAssign";
	}
    /**
     * confirm a Sell Assignment
     * @param model Model
     * @param assignmentid ID of Assignment 
     * @return salesConfirm
     */
    @PostMapping("/confirmSelAssign")
	String confirmSell(Model model, @RequestParam("assignmentid") Long assignmentid){
        SellAssignment assignment = (SellAssignment)assignmentManager.getAssignmentById(assignmentid);
		model.addAttribute("order" , assignment.getOrder());
		model.addAttribute("assignment" , assignment);
		assignmentManager.confirmAssignment(assignment);
		return "redirect:/management";
    }
    /**
     * confirm a Stock Assignment
     * @param assignmentid ID of Assignment
     * @return redirect:/management
     */
    @PostMapping("/confirmStoAssign")
    String confirmStock(@RequestParam ("assignmentid") Long assignmentid){
        Assignment assignment = assignmentManager.getAssignmentById(assignmentid);
		assignmentManager.confirmAssignment(assignment);
        return "redirect:/management";
    }
    /**
     * delete an Assignment
     * @param assignmentid ID of Assignment
     * @return redirect:/management
     */
    @PostMapping("/deleteAssign")
    String deleteAssignment(@RequestParam ("assignmentid") Long assignmentid){
        assignmentManager.deleteAssignment(assignmentid);
        return "redirect:/management";
    }

    /**
     * delete a Worker
     * @param employeeid ID of User Worker
     * @return redirect:/management
     */
    @PostMapping("/manageDel")
    String deleteWorker(@RequestParam ("id") Long employeeid){
        userManagement.deleteUserById(employeeid);
        return "redirect:/management";
    }
    /**
     * add a Employee
     * @param model Model
     * @param form Registrationform
     * @param result Error
     * @return redirect:/management
     */
    @PostMapping("/manageAddEmployee")
    String addEmployee(Model model, @ModelAttribute("form") RegistrationForm form, @NotNull Errors result){
        if (result.hasErrors()||!form.getPassword().equals(form.getMatchingpassword())) {
			return "addEmployee";
		}
        userManagement.createUser(form);
		return "redirect:/management";
    }
    /**
     * add a Customer
     * @param model Model
     * @param form Form
     * @param result Errors
     * @return redirect:/management
     */
	@PostMapping("/manageAddCustomer")
	String addCustomer(Model model, @ModelAttribute("form") RegistrationForm form, @NotNull Errors result){
		if (result.hasErrors()||!form.getPassword().equals(form.getMatchingpassword())) {
			return "addCustomer";
		}
		userManagement.createUser(form);
		return "redirect:/management";
	}

    /**
     * change the role of a Worker
     * @param role String Role of User
     * @param employeeid ID of User Worker
     * @return redirect:/management
     */
    @PostMapping("/managementchangeRole")
	String changeRole ( @RequestParam ("changeRole") String role,@RequestParam ("id") Long employeeid){
		userManagement.changeRole(employeeid , role);
		return "redirect:/management";
	}

    /**
     * the one and only get management Controller
     * @param model Model   
     * @param form RegistrationForm
     * @return management
     */
    @GetMapping("/management")
    @PreAuthorize("hasAnyRole('Manager', 'SalesManager', 'Worker')")
    String management(Model model, @ModelAttribute RegistrationForm form){
        model.addAttribute("workers", userManagement.findByRole("Worker"));
        model.addAttribute("salesmanagers", userManagement.findByRole("SalesManager"));
		model.addAttribute("manager", userManagement.findByRole("Manager"));
		if (userManagement.findByRole("Manager").size() > 1){
			model.addAttribute("number", "1");
		} else {
			model.addAttribute("number", "0");
        }
        
        model = addAssignemntToModel(model);
        model.addAttribute("products",
            warehouseRepository.findAll().filter(x->(!x.getProduct().getClass().equals(ShadowAccessoire.class))));
        model.addAttribute("form", form);
        return "management";
    }

    /**
     * update a WarehouseItem
     * @param model
     * @param ProductName
     * @param monetary
     * @param quantity
     * @param minquantity
     * @param inventoryItemIdentifier
     * @param active
     * @return redirect:/management
     */
	@GetMapping ("/updateItem")
	@PreAuthorize("hasAnyRole('Manager', 'SalesManager', 'Worker')")
	String updateItem(Model model, @RequestParam ("productName") String ProductName,
					  @RequestParam("productPrice") String monetary,
					  @RequestParam("quantityItem") String quantity,
					  @RequestParam("minquantityItem") String minquantity,
					  @RequestParam("inventoryIdentifier") String inventoryItemIdentifier,
					  @RequestParam(value="active",required=false) Boolean active){

			if(active == null) {
				active=false;
			}

		System.out.println(active);
    	warehouseManagement.updateShowabilityOfItem(inventoryItemIdentifier,active);
        warehouseManagement.updateItemInCatalogWarehouse(ProductName,monetary,quantity,minquantity,
            inventoryItemIdentifier);

		return "redirect:/management";
	}
    /**
     * delete a WarehouseItem
     * @param item
     * @param id_del
     * @return redirect:/management
     */
	@PostMapping("/deleteWarehouseItem/{id_del}")
	@PreAuthorize("hasAnyRole('Manager', 'SalesManager', 'Worker')")
	String deleteItem(
			@ModelAttribute WarehouseItem item,
			@PathVariable InventoryItemIdentifier id_del
	){

		ProductIdentifier productIdentifier=warehouseManagement.findProductByInventoryItemIdentifier(id_del).getId();
		HardwareType hardwareType=compuTechCatalog.findById(productIdentifier).get().getType();
        warehouseRepository.deleteById(id_del);
        if(hardwareType.equals(HardwareType.PC)){
				System.out.println("Is PC!");
				List<ProductIdentifier> productIdentifiers=new LinkedList<>();
				PC pc=(PC)compuTechCatalog.findById(productIdentifier).get();
				productIdentifiers.addAll(pc.deleteComponents());
				for (ProductIdentifier temp:productIdentifiers){
					warehouseManagement.deleteItem(compuTechCatalog.findById(temp).get());
					compuTechCatalog.deleteById(temp);
				}
				
            }
			compuTechCatalog.deleteById(productIdentifier);
		return "redirect:/management";
    }

	/**
	 * add all the assignment things
	 * @param model
	 * @return return the model
	 */
	public Model addAssignemntToModel(Model model){
        // getting all specific Assignments that are Open in a List
        List<OrderAssignment> pAssignments = new LinkedList<OrderAssignment>();
        List<OrderAssignment> bAssignments = new LinkedList<OrderAssignment>();
        List<ServiceAssignment> serAssignments = new LinkedList<ServiceAssignment>();
        List<SellAssignment> selAssignments = new LinkedList<SellAssignment>();
        List<StockAssignment> stoAssignments = new LinkedList<StockAssignment>();
		List<RepairAssignment> repAssignments = new LinkedList<RepairAssignment>();

        for (Assignment assignment : assignmentManager.getAllAssignments()){
            if(assignment.getState().equals(AssignmentState.OPEN)){
                AssignmentType type = assignment.getType();
                switch(type){
                    case ORDER:
                        if(((OrderAssignment) assignment).isBusinnes()){
                            bAssignments.add((OrderAssignment) assignment);
                        }else{pAssignments.add((OrderAssignment) assignment);}
                        break;
                    case SERVICE:
                        serAssignments.add((ServiceAssignment) assignment);
                        break;
					case REPAIR:
						repAssignments.add((RepairAssignment) assignment);
						break;
                    case SELL:
                        selAssignments.add((SellAssignment) assignment);
                        break;
                    case STOCK:
                        stoAssignments.add((StockAssignment) assignment);
                        break;
                    default:
                        break;
                }
        
            }

        }
        model.addAttribute("pAssignments", pAssignments);
        model.addAttribute("bAssignments", bAssignments);
        model.addAttribute("selAssignments", selAssignments);
        model.addAttribute("serAssignments", serAssignments);
        model.addAttribute("stoAssignments", stoAssignments);
        model.addAttribute("repAssignments", repAssignments);
        return model;
    }

}
