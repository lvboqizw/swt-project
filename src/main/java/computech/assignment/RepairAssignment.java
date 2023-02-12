package computech.assignment;

import javax.persistence.*;

import computech.sales.RepairItem;

/**
 * Assignment for RepairService
 * @author Jannek Steinke
 */
@Entity
public class RepairAssignment extends Assignment{

	@OneToOne(cascade = {CascadeType.ALL})
	private RepairItem repairItem;
	private Long userid;

	
	public RepairAssignment(){}
	/**
	 * 
	 * @param repairItem
	 * @param userId
	 */
	public RepairAssignment(RepairItem repairItem, Long userId){
		super(AssignmentState.OPEN, AssignmentType.REPAIR);
		this.repairItem = repairItem;
		this.userid = userId;
	}
	/**
	 * 
	 * @return RepairItem
	 */
	public RepairItem getRepairItem(){
		return repairItem;
	}
	/**
	 * 
	 * @return Long
	 */
	public Long getUserId(){
		return userid;
	}
}