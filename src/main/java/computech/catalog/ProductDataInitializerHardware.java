package computech.catalog;

import com.google.common.collect.Multimap;

import org.javamoney.moneta.Money;
import org.salespointframework.core.DataInitializer;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.mail.search.SearchTerm;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import static org.salespointframework.core.Currencies.EURO;

@Component
@Order(20)
public class ProductDataInitializerHardware implements DataInitializer {

	/**
	 * @author Lukas
	 * This class is a test to see if we can not simplify what Fedor had in mind
	 */
	private final ProductRepositoryHardware productRepositoryHardware;

	public ProductDataInitializerHardware( ProductRepositoryHardware productRepositoryHardware){
		this.productRepositoryHardware = productRepositoryHardware;
	}

	@Override
	public void initialize(){
		if(productRepositoryHardware.findAll().iterator().hasNext()){
			return;
		}

		productRepositoryHardware.save(new Accessoire("AMD Ryzen 2600", "AMD", "2.5 GHz, 6 Kerne 12 Threads", "2020-11-22", "cpu2", Money.of(200,EURO),true, HardwareType.PROCESSOR));
		productRepositoryHardware.save(new Accessoire("AMD Opteron", "AMD", "2.0 GHz, 4 Kerne 10 Threads", "2014-10-11", "cpu3", Money.of(150,EURO),true, HardwareType.PROCESSOR));
		productRepositoryHardware.save(new Accessoire("Intel Core i3", "Intel", "1.9 GHz, 4 Kerne 10 Threads", "2013-8-9", "cpu6", Money.of(100,EURO),true, HardwareType.PROCESSOR));
		productRepositoryHardware.save(new Accessoire("Intel Core i5", "Intel", "2.2 GHz, 6 Kerne 12 Threads", "2016-10-2", "cpu5", Money.of(300,EURO),true, HardwareType.PROCESSOR));
		productRepositoryHardware.save(new Accessoire("Intel Core i7", "Intel", "2.9 GHz, 8 Kerne 16 Threads", "2019-10-22", "cpu4", Money.of(500,EURO),true, HardwareType.PROCESSOR));
		productRepositoryHardware.save(new Accessoire("Intel Pentium", "Intel", "1.5 GHz, 2 Kerne 6 Threads", "2019-10-22", "cpu7", Money.of(50,EURO),true, HardwareType.PROCESSOR));

		productRepositoryHardware.save(new Accessoire("RGB RAM", "Corsair","16GB, DDR4, black", "2013-5-2", "RAM2", Money.of(80, EURO), true,HardwareType.RAM));
		productRepositoryHardware.save(new Accessoire("DDR4-RAM", "DELL","32GB, DDR4, black-green", "2015-7-1", "RAM1", Money.of(60, EURO), true,HardwareType.RAM));
		productRepositoryHardware.save(new Accessoire("ValueRAM", "Kingston","16GB, DDR3, green", "2017-1-2", "RAM3", Money.of(30, EURO), true,HardwareType.RAM));
		productRepositoryHardware.save(new Accessoire("RAM", "HyperX","8GB, DDR3, black", "2012-5-9", "ram4", Money.of(55, EURO), true,HardwareType.RAM));
		productRepositoryHardware.save(new Accessoire("PC4-2130 Memory Upgrade", "OCW","128GB, DDR4, blue", "2014-1-2", "ram5", Money.of(600, EURO), true,HardwareType.RAM));
		productRepositoryHardware.save(new Accessoire("RGB RAM", "Ballistix","16GB, DDR4, black", "2017-2-7", "ram6", Money.of(90, EURO), true,HardwareType.RAM));

		productRepositoryHardware.save(new Accessoire("Supafast SSD", "SanDisk","2000PB, 100GB/s", "2020-11-5", "SSD1", Money.of(10000, EURO), true,HardwareType.DISKDRIVE));
		productRepositoryHardware.save(new Accessoire("SanDisk Solid State Drive", "SanDisk","1TB, 100GB/s", "2014-3-2", "SSD2", Money.of(500, EURO),true, HardwareType.DISKDRIVE));
		productRepositoryHardware.save(new Accessoire("Samsung integrated SSD", "Samsung","500GB, 50GB/s", "2014-3-2", "SSD3", Money.of(500, EURO),true, HardwareType.DISKDRIVE));
		productRepositoryHardware.save(new Accessoire("Crucial BX500 SSD", "Crucial","120GB, 1GB/s", "2014-3-2", "SSD11", Money.of(24000, EURO),true, HardwareType.DISKDRIVE));
		productRepositoryHardware.save(new Accessoire("Seagate Nytro 3330", "Seagate","15,360TB, 100GB/s", "2016-6-5", "SSD4", Money.of(6500, EURO),true, HardwareType.DISKDRIVE));
		productRepositoryHardware.save(new Accessoire("Micron Technology", "Crucial","500GB, 50GB/s", "2014-8-2", "SSD10", Money.of(40, EURO),true, HardwareType.DISKDRIVE));

		productRepositoryHardware.save(new Accessoire("coolste Cooler", "Enermax","150W TDP, 38db", "2016-8-6", "Co1", Money.of(70, EURO),true, HardwareType.COOLING));
		productRepositoryHardware.save(new Accessoire("Pure Rock Slim Tower Cooler", "be quiet!","180W TDP, 30db", "2018-9-6", "Co2", Money.of(30, EURO),true, HardwareType.COOLING));
		productRepositoryHardware.save(new Accessoire("Cooler Master", "NotePal","140W TDP, 37db", "2015-6-9", "Co3", Money.of(70, EURO),true, HardwareType.COOLING));
		productRepositoryHardware.save(new Accessoire("Hydro Series Cooler", "Corsair","150W TDP, 38db", "2013-3-5", "Co4", Money.of(132, EURO),true, HardwareType.COOLING));
		productRepositoryHardware.save(new Accessoire("Xilence Topblow Cooler", "Xilence","110W TDP, 35db", "2020-12-9", "Co5", Money.of(10, EURO), true,HardwareType.COOLING));
		productRepositoryHardware.save(new Accessoire("cooler Cooling Cooler", "coolingfirm","100W TDP, 40db", "2020-12-25", "Co6", Money.of(300, EURO), true,HardwareType.COOLING));

		productRepositoryHardware.save(new Accessoire("Motherboard", "MSI","2x USB 3.0, 5Mbit LAN, Sockel: 190x", "2014-3-1", "MB2", Money.of(70, EURO), true,HardwareType.MAINBOARD));
		productRepositoryHardware.save(new Accessoire("Gaming Mainboard AM4", "ASUS","3x USB 4.0, 10Mbit LAN, Sockel: 180x", "2018-8-1", "MB1", Money.of(172, EURO), true,HardwareType.MAINBOARD));
		productRepositoryHardware.save(new Accessoire("Motherboard PLUS", "MSI","4x USB 3.0, 10Mbit LAN, Sockel: 190x", "2012-3-1", "MB3", Money.of(108, EURO), true,HardwareType.MAINBOARD));
		productRepositoryHardware.save(new Accessoire("HP 2345", "HP","3x USB 4.0, 5Mbit LAN, Sockel: 180x", "2017-3-8", "MB4", Money.of(50, EURO), true,HardwareType.MAINBOARD));
		productRepositoryHardware.save(new Accessoire("MSI MPG Gaming", "MSI","4x USB 3.0, 15Mbit LAN, Sockel: 190x", "2017-7-7", "MB5", Money.of(120, EURO), true,HardwareType.MAINBOARD));
		productRepositoryHardware.save(new Accessoire("PowerEdge R320 Mainboard", "DELL","3x USB 3.0, 5Mbit LAN, Sockel: 190x", "2015-7-1", "MB7", Money.of(130, EURO), true,HardwareType.MAINBOARD));

		productRepositoryHardware.save(new Accessoire("EVGA GeForce RTX3090", "NVIDIA","1x HDMI, Memoryspeed: 7000MHz, 24GB", "2019-3-1", "gpu2", Money.of(2500, EURO), true,HardwareType.GPU));
		productRepositoryHardware.save(new Accessoire("ASUS TUF GeForce RTX3070", "NVIDIA","2x HDMI, Memoryspeed: 5000MHz, 16GB", "2018-8-1", "gpu3", Money.of(1700, EURO), true,HardwareType.GPU));
		productRepositoryHardware.save(new Accessoire("MSI GeForce RTX3090", "NVIDIA","2x HDMI, Memoryspeed: 6000MHz, 20GB", "2012-3-1", "gpu4", Money.of(2200, EURO), true,HardwareType.GPU));
		productRepositoryHardware.save(new Accessoire("HP Enterprise K80", "NVIDIA","1x HDMI, Memoryspeed: 1GHz, 32GB", "2017-3-8", "gpu5", Money.of(12000, EURO), true,HardwareType.GPU));
		productRepositoryHardware.save(new Accessoire("ASUSU Radeon ROG-STRIX", "NVIDIA","2x HDMI, Memoryspeed: 6000MHz, 24GB", "2017-7-7", "gpu6", Money.of(1120, EURO), true,HardwareType.GPU));
		productRepositoryHardware.save(new Accessoire("RTX2080", "NVIDIA","2x HDMI, Memoryspeed: 6000MHz, 16GB", "2015-7-1", "gpu7", Money.of(1850, EURO), true,HardwareType.GPU));


		productRepositoryHardware.save(new Accessoire("DAS MAINBAORD", "Asus","1x USB 2.0, 1Mbit LAN, Sockel: 203x", "2012-4-2", "MB1", Money.of(1, EURO), false,HardwareType.MAINBOARD));
		productRepositoryHardware.save(new Accessoire("Compact Triangle", "LTT", "created by God himself", "2020-7-2", "pyramidCase", Money.of(90,EURO), false,HardwareType.CUSTOMIZABLE));


		Set<Hardware> pc1 = new HashSet<>();
		pc1.add(new ShadowAccessoire("AMD Ryzen 2600 Mobil", "AMD", "2.5 GHz, 6 Kerne 12 Threads", "2020-11-22", "cpu2", Money.of(200,EURO),true, HardwareType.PROCESSOR));
		pc1.add(new ShadowAccessoire("Supafast SSD Mobil", "SanDisk","2000PB, 100GB/s", "2020-11-5", "SSD1", Money.of(10000, EURO),true, HardwareType.DISKDRIVE));
		pc1.add(new ShadowAccessoire("RGB RAM", "Corsair","16GB, DDR4, black", "2013-5-2", "RAM2", Money.of(80, EURO), true,HardwareType.RAM));
		pc1.add(new ShadowAccessoire("Pure Rock Slim Tower Cooler", "be quiet!","180W TDP, 30db", "2018-9-6", "Co2", Money.of(30, EURO),true, HardwareType.COOLING));
		pc1.add(new ShadowAccessoire("Motherboard PLUS", "MSI","4x USB 3.0, 10Mbit LAN, Sockel: 190x", "2012-3-1", "MB3", Money.of(108, EURO), true,HardwareType.MAINBOARD));
		pc1.add(new ShadowAccessoire("HP Enterprise K80", "NVIDIA","1x HDMI, Memoryspeed: 1GHz, 32GB", "2017-3-8", "gpu5", Money.of(12000, EURO), true,HardwareType.GPU));
		productRepositoryHardware.save(new PC(pc1,"Lenovo IdeaPad","Lenovo", "nice pc", "2020-9-1", Money.of(0, EURO), "pc1",true, HardwareType.PC));

		Set<Hardware> pc2 = new HashSet<>();
		pc2.add(new ShadowAccessoire("Intel Core i7", "Intel", "2.9 GHz, 8 Kerne 16 Threads", "2019-10-22", "cpu4", Money.of(500,EURO),true, HardwareType.PROCESSOR));
		pc2.add(new ShadowAccessoire("RGB RAM", "Ballistix","16GB, DDR4, black", "2017-2-7", "ram6", Money.of(90, EURO), true,HardwareType.RAM));
		pc2.add(new ShadowAccessoire("Samsung integrated SSD", "Samsung","500GB, 50GB/s", "2014-3-2", "SSD3", Money.of(500, EURO),true, HardwareType.DISKDRIVE));
		pc2.add(new ShadowAccessoire("Hydro Series Cooler", "Corsair","150W TDP, 38db", "2013-3-5", "Co4", Money.of(132, EURO),true, HardwareType.COOLING));
		pc2.add(new ShadowAccessoire("MSI MPG Gaming", "MSI","4x USB 3.0, 15Mbit LAN, Sockel: 190x", "2017-7-7", "MB5", Money.of(120, EURO), true,HardwareType.MAINBOARD));
		pc2.add(new ShadowAccessoire("MSI GeForce RTX3090", "NVIDIA","2x HDMI, Memoryspeed: 6000MHz, 20GB", "2012-3-1", "gpu4", Money.of(2200, EURO), true,HardwareType.GPU));
		productRepositoryHardware.save(new PC(pc2,"Acer PC","Acer", "awesome pc", "2018-9-8", Money.of(0, EURO), "pc3",true, HardwareType.PC));

	}
}
