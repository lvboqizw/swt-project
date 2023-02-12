package computech.catalog;

import org.javamoney.moneta.Money;
import org.salespointframework.catalog.Product;

import javax.money.MonetaryAmount;
import javax.persistence.*;

import static org.salespointframework.core.Currencies.EURO;

@Entity
public abstract class Hardware extends Product{

	private String image;
	private String producer;
	private String date;
	private String detail;



	private boolean inCatalogshown;
	@Enumerated(EnumType.ORDINAL)
	private HardwareType type;


	public Hardware(String name, String producer, String detail,  String date, String image, MonetaryAmount price,boolean inCatalogshown, HardwareType type){
		super(name, price);
		this.image = image;
		this.producer = producer;
		this.date = date;
		this.detail = detail;
		this.inCatalogshown=inCatalogshown;
		this.type = type;
	}

	public Hardware(){}

@Override
public MonetaryAmount getPrice() {


	return super.getPrice();
}

	@Override
	public String getName() {
		return super.getName();
	}

	public String getProducer() {
		return producer;
	}

	public String getDate() {
		return date;
	}

	public String getImage() {
		return image;
	}

	public String getDetail() {
		return detail;
	}

	public HardwareType getType() {
		return type;
	}

	public boolean isInCatalogshown() { return inCatalogshown; }

	public void setInCatalogshown(boolean inCatalogshown) { this.inCatalogshown = inCatalogshown; }
}
