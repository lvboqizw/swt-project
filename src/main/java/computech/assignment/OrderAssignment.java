package computech.assignment;

import javax.persistence.*;
import org.salespointframework.order.Order;

import computech.user.User;

/**
 * Order Assignemnt
 * @author Jannek Steinke
 */
@Entity
public class OrderAssignment extends Assignment{
    
    @OneToOne
    private Order order;
    @OneToOne
    private User user;

    private Long mitarbeiterid;
    private boolean business;

    public OrderAssignment(){}
    /**
     * 
     * @param mitarbeiterid
     * @param order
     * @param user
     */
    public OrderAssignment(Long mitarbeiterid, Order order, User user){
        
        super(AssignmentState.OPEN, AssignmentType.ORDER);
        this.order = order;
        this.mitarbeiterid = mitarbeiterid;
        this.user = user;
        // if mitarbeiterid is -1 then the Customer is private because he has no associated worker
        // otherwise customer is businness 
        if(mitarbeiterid == -1){
        	this.business = false;
        }else{
        	this.business = true;
        }
    }
    /**
     * 
     * @return Order
     */
    public Order getOrder(){
        return order;
    }
    /**
     * set new Order
     * @param order
     */
    public void setOrder(Order order){
        this.order = order;
    }
    /**
     * 
     * @return Long
     */
    public Long getMitarbeiterId(){
        return mitarbeiterid;
    }
    /**
     * 
     * @return User
     */
    public User getUser(){
        return user;
    }
    /**
     * 
     * @return boolean
     */
    public boolean isBusinnes(){
        return business;
    }
}