package computech.catalog;

import javassist.bytecode.LineNumberAttribute;
import org.javamoney.moneta.Money;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


import javax.money.MonetaryAmount;

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
import static org.salespointframework.core.Currencies.EURO;


class PCTest {
	PC pc;
	Accessoire prod;

	@BeforeEach
	void setUp() {
		pc = new PC(new LinkedHashSet<>(), "name", "producer", "detail", "date", Money.of(5, EURO), "image", true,HardwareType.PC);
		 prod = new Accessoire("AMD Ryzen 2600", "AMD", "2.5 GHz, 6 Kerne 12 Threads", "2020-11-22", "cpu2", Money.of(200,EURO),true, HardwareType.PROCESSOR);

	}

    @Test //#302
    void addPart() {
		assertEquals(0,pc.getComponents().size());

		MonetaryAmount price=prod.getPrice().multiply(3);

		pc.addPart(prod,3);
		assertEquals(1,pc.getComponents().size());
		assertEquals(price,pc.getPrice());
    }

    @Test //#303
    void deleteComponents() {

		pc.addPart(prod,1);
		assertEquals(1, pc.getComponents().size());
		pc.deleteComponents();
		assertEquals(0,pc.getComponents().size());
    }

    @Test //#304
    void calculatePrice() {
		pc.addPart(prod,3);
		assertEquals(prod.getPrice().multiply(3),pc.calculatePrice().multiply(3));
    }
}