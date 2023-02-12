package computech.sales;

import computech.catalog.HardwareType;
import org.hibernate.annotations.GenericGenerator;
import org.springframework.data.annotation.Id;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;

/**
 * Class to manage a repair item
 */
@Entity
public class RepairItem {

	@javax.persistence.Id
	@Id @GeneratedValue(generator = "system-uuid")
	@GenericGenerator(name="system-uuid", strategy = "uuid")
	String id;

	private HardwareType type;
	private String name, flaw;
	private int year, month;
	private boolean warranty, boughtAtOurStore;

	public RepairItem(){}
	public RepairItem(String name, String flaw, HardwareType type, int month, int year, boolean warranty, boolean boughtAtOurStore){
		this.name = name;
		this.flaw = flaw;
		this.type = type;
		this.year = year;
		this.month = month;
		this.warranty = warranty;
		this.boughtAtOurStore = boughtAtOurStore;
	}

	public HardwareType getType() {
		return type;
	}

	public String getName() {
		return name;
	}

	public String getFlaw() {
		return flaw;
	}

	public int getYear() {
		return year;
	}

	public String wasBoughtAtOurStore() {
		if(boughtAtOurStore == false){
			return "no";
		}
		return "yes";
	}

	public int getMonth() {
		return month;
	}

	public String hasWarranty() {
		if(warranty == false){
			return "no";
		}
		return "yes";
	}

	public String getId(){
		return id;
	}

	public String getDateWhenBought(){
		return month + "/" + year;
	}
}
