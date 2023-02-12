package computech.assignment;

import javax.persistence.*;

import computech.warehouse.WarehouseItem;

/**
 * Assignment for Buying Products to Warehouse
 * @author Jannek Steinke
 */
@Entity
public class StockAssignment extends Assignment{
	private int amountToBeReodered;
	@OneToOne
	private WarehouseItem warehouseItem;
	private String name;


	public StockAssignment(){}
	/**
	 * 
	 * @param name
	 * @param amountToBeReodered
	 * @param warehouseItem
	 */
    public StockAssignment(String name, int amountToBeReodered, WarehouseItem warehouseItem){
    	super(AssignmentState.OPEN, AssignmentType.STOCK);
    	this.amountToBeReodered= amountToBeReodered;
    	this.warehouseItem= warehouseItem;
    	this.name = name;
    }
	/**
	 * 
	 * @return WarehouseItem
	 */
	public WarehouseItem getWarehouseItem() {
		return warehouseItem;
	}
	/**
	 * 
	 * @return int
	 */
	public int getAmountToBeReodered() {
		return amountToBeReodered;
	}
}