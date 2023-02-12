package computech.warehouse;

import computech.assignment.AssignmentManager;
import org.salespointframework.order.*;
import org.salespointframework.quantity.Quantity;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
/**
 * The listener interface for receiving  events. The class that
 * is interested in processing a OrderEvent event implements this
 * interface. When the OrderEvent event occurs, that object's appropriate
 * method is invoked. As well this Class fixes OrderCancelled bug when the
 * canceled amount of Inventory Item is not put back.
 * @see OrderCompleted
 * @see OrderCanceled
 */
@Component
public class WarehouseOrderListener {


	private OrderManagement orderManagement;

	private AssignmentManager assignmentManager;

	private WarehouseRepository warehouseRepository;

	public WarehouseOrderListener(OrderManagement orderManagement, AssignmentManager assignmentManager, WarehouseRepository warehouseRepository) {
		this.orderManagement = orderManagement;
		this.assignmentManager = assignmentManager;
		this.warehouseRepository = warehouseRepository;
	}

	/**
	 * Fixies the OrderCanceled event when the amount that was
	 * cancelled is not returned.
	 * @param event the OrderCanceled
	 */
	@EventListener
	public void fixCancelledEvent(OrderEvents.OrderCanceled event){
		System.out.println("Cancceled event happened");
		Assert.notNull(event, "Order canceled should not be null. #fixCancelledEvent() WarehouseOrderListener");
		Order oderTemp= event.getOrder();
		Totalable<OrderLine> orders = oderTemp.getOrderLines();
		for (OrderLine order : orders) {
			System.out.println();
			WarehouseItem warehouseItem = warehouseRepository.findByProductIdentifier(order.getProductIdentifier()).get();
			int amounttobereodered=order.getQuantity().getAmount().intValue();
			warehouseItem.increaseQuantity(Quantity.of(amounttobereodered));
			warehouseRepository.save(warehouseItem);
			int amountOfItems = warehouseItem.getQuantity().getAmount().intValue();
		}

	}
	/**
	 * When order is completed the sufficient amount is checked
	 * @param event the OrderCompleted
	 */
	@EventListener
	public void handleEvent(OrderEvents.OrderCompleted event) {

		System.out.println("Warehouse Assigment event happened");

		Assert.notNull(event, "Order completed should not be null. #handleEvent() WarehouseOrderListener");
		Order oderTemp = event.getOrder();
		oderTemp.getOrderLines();
		Totalable<OrderLine> orders = oderTemp.getOrderLines();
		for (OrderLine order : orders) {
			System.out.println();
			WarehouseItem warehouseItem = warehouseRepository.findByProductIdentifier(order.getProductIdentifier()).get();
			int amountOfItems = warehouseItem.getQuantity().getAmount().intValue();
			if (amountOfItems < warehouseItem.minQuantity) {
				assignmentManager.addStockAssignment(warehouseItem.minQuantity * 2, warehouseItem);
			}
		}

	}
}