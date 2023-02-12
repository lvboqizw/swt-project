package computech.catalog;

import static org.salespointframework.core.Currencies.EURO;

import com.google.common.collect.Multimap;
import org.javamoney.moneta.Money;
import org.salespointframework.catalog.ProductIdentifier;

import javax.money.MonetaryAmount;
import javax.persistence.*;
import java.util.*;

@Entity
public class PC extends Hardware{

	@OneToMany(targetEntity =  Hardware.class, cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	private Set<Hardware> components;


	//Check if this is really necessary
	public PC(){
	}

	public PC(Set<Hardware> parts, String name, String producer, String detail,  String date, MonetaryAmount price, String image,boolean inCatalogshown, HardwareType type){
		super(name, producer, detail, date, image, price,inCatalogshown, type);
		components = parts;
		super.setPrice(this.calculatePrice());
	}


	/**
	 * Add a new Hardware item to the Pc's parts
	 * @param newComponent Component to be added
	 * @param number amount of new Item to be added
	 */
	public void addPart(Hardware newComponent, int number){
		components.add(newComponent);
		MonetaryAmount price=newComponent.getPrice().multiply(number);
		setPrice(price);
	}


	public Set<Hardware> getComponents(){
		return components;
	}

	/**
	 * Delete all Components
	 * @return all ProductIdentifiers
	 */
	public List<ProductIdentifier> deleteComponents(){
		List<ProductIdentifier> productIdentifiers=new LinkedList<>();
		for(Hardware e : components){
			productIdentifiers.add(e.getId());


		}
		this.components.clear();
		return productIdentifiers;
	}


	public MonetaryAmount calculatePrice() {
	int ret = 0;
	for(Hardware e : components){
			//int occur = Collections.frequency(components,e);
			ret += e.getPrice().getNumber().intValue();
		}
		return Money.of(ret,EURO);
	}


	@Override
	public String getProducer() {
		return null;
	}
}
