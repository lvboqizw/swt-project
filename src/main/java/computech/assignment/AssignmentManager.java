package computech.assignment;

import computech.sales.RepairItem;
import computech.user.User;

import computech.sales.Question;
import computech.catalog.Hardware;

import computech.warehouse.WarehouseItem;
import computech.warehouse.WarehouseManagement;

import org.salespointframework.order.Order;
import org.salespointframework.order.*;
import org.springframework.context.annotation.Role;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

/**
 * managed AssignmentRepository and Assignments
 * @author Jannek Steinke
 */
@Service
@Transactional
public class AssignmentManager{
    
    private AssignmentRepository assignments;
    private final WarehouseManagement warehouseManagement;
    private final OrderManagement<Order> orderManagement;
	private double discount;
    /**
     * 
     * @param assignments
     * @param orderManagement
     * @param warehouseManagement
     */
    public AssignmentManager(AssignmentRepository assignments, OrderManagement<Order> orderManagement, 
    WarehouseManagement warehouseManagement){
        this.assignments = assignments;
        this.warehouseManagement = warehouseManagement;
        this.orderManagement = orderManagement;
        this.discount = 0.7;

    }
    // add Assignments to Manager, 
    /**
     * 
     * @param id id for mitarbeiterid -> -1 = no associated worker, <0 associated worker for businnesscustomer
     * @param order from user ordered Items in Salespoint Order Object
     * @param user User who ordered the Order
     */
    public void addOrderAssignment(Long id, Order order, User user){
        Assignment assignment = new OrderAssignment(id, order, user);
        assignments.save(assignment);
    }
    /**
     * 
     * @param id Assignment ID
     */
    public void saveAssignment(Long id){
        Assignment assignment = this.getAssignmentById(id);
        assignments.save(assignment);
    }
    /**
     *  
     * @param id UserID
     * @param question Question of User
     */
    public void addServiceAssignment(Long id, Question question){
        Assignment assignment = new ServiceAssignment(question, id);
        assignments.save(assignment);
    }
    /**
     * 
     * @param order Order Object contains items to sell
     * @param hardware Hardware which would be sold
     */
    public void addSellAssignment(Order order, Hardware hardware){
        Assignment assignment = new SellAssignment("name", order, hardware);
        assignments.save(assignment);
    }
    /**
     * 
     * @param amountToBeReodered Int Number how many would be Reordered
     * @param warehouseItem Item of Warehouse which should be Reordered
     */
    public void addStockAssignment(int amountToBeReodered,WarehouseItem warehouseItem){
        Assignment assignment = new StockAssignment("Reoder Assignment",amountToBeReodered, warehouseItem);
        assignments.save(assignment);
    }
    /**
     * 
     * @param id userid
     * @param repairItem Item which should be repaired
     */
    public void addRepairAssignment(Long id, RepairItem repairItem){
    	Assignment assignment = new RepairAssignment(repairItem, id);
    	assignments.save(assignment);
	}

    /**
     * confirm a Assignment
     * @param assignment any Assignment, every could confirmed
     */
    public void confirmAssignment(Assignment assignment){
        assignment.setStateConfirmed();
        AssignmentType type = assignment.getType();
        switch(type){
            case ORDER:
				OrderAssignment assignment1= (OrderAssignment) assignment;
                Order order = assignment1.getOrder();
				warehouseManagement.confirmoder(order);
                break;
            case SERVICE:
				break;
            case SELL:
            	warehouseManagement.reselling(((SellAssignment)assignment).getHardware(),1);
                break;
            case STOCK:
				StockAssignment assignment2=(StockAssignment)assignment;
				int amountTobeReodered= assignment2.getAmountToBeReodered();
				WarehouseItem warehouseItemTemp=assignment2.getWarehouseItem();
				warehouseManagement.confirmReoderAssigment(amountTobeReodered,warehouseItemTemp);
				break;
			case REPAIR:
				break;
            default:
                System.out.println("AssignemntType not found!");
                break;
        }
    }
    /**
     * 
     * @param assignmentid ID of Assignment
     */
    public void deleteAssignment(Long assignmentid){
        Assignment assignment = this.getAssignmentById(assignmentid);
        assignment.setStateDeleted();
        if(assignment.getType().equals(AssignmentType.ORDER)){
            Order order = ((OrderAssignment)assignment).getOrder();
            orderManagement.cancelOrder(order, "deleted");
        }
    }
    /**
     * 
     * @param assignmentid ID of Assignment
     * @return Returns a assignment with the given assignmentid
     */
    public Assignment getAssignmentById(Long assignmentid){
        return assignments.findById(assignmentid).get();
    }
    /**
     * 
     * @return all assignments from repository
     */
    public Iterable<Assignment> getAllAssignments(){
        return assignments.findAll();
    }


	@PreAuthorize("hasAnyRole('Manager','SalesManager')")
	void setDiscount(double newDiscount){
    	this.discount = newDiscount;
	}

	public double getDiscount() {
		return discount;
	}
}
