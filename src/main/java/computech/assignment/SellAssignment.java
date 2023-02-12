package computech.assignment;

import javax.persistence.*;

import computech.catalog.Hardware;
import org.salespointframework.order.Order;

/**
 * Assignment for SellService
 * @author Jannek Steinke
 */
@Entity
public class SellAssignment extends Assignment{

	@OneToOne
	private Order order;

	@OneToOne
	private Hardware hardware;

	private String name;

	public SellAssignment(){}
	/**
	 * 
	 * @param name
	 * @param order
	 * @param hardware
	 */
    public SellAssignment(String name, Order order, Hardware hardware){
        super(AssignmentState.OPEN, AssignmentType.SELL);
        this.order = order;
        this.hardware = hardware;
        this.name = name;
	}
	/**
	 * 
	 * @return Order
	 */
	public Order getOrder() {
		return order;
	}

	public Hardware getHardware() {
		return hardware;
	}
}