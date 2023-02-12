package computech.customizePC;

import computech.catalog.*;
import org.salespointframework.order.Cart;
import org.salespointframework.quantity.Quantity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.*;

/**
 * This is a Spring MVC Controller to manage the process of choosing your PC-parts
 *
 * @author Lukas
 */
@Controller
@PreAuthorize("isAuthenticated()")
@SessionAttributes("cart")
public class PcCustomizeController {

	private final ProductRepositoryHardware productRepositoryHardware;
	private LinkedList<Hardware> listOfParts;
	private Hardware cpu,gpu,cooler,memory,storage, motherboard, cases;

	public PcCustomizeController(ProductRepositoryHardware productRepositoryHardware){
		this.productRepositoryHardware = productRepositoryHardware;
		listOfParts = new LinkedList<>();
	}


	/**
	 * With these functions you can filter the Repository. Filterfunctions within the repository didnt work
	 */
	/**
	 * @param type Type of HardwareType
	 * @return list of parts, with the given type and not in the list
	 */
	private Iterable<Hardware> getAllButList(HardwareType type){
		Set<Hardware> parts = new HashSet<>();
		for (Hardware e : getAllButType(HardwareType.PC)){
			if(!listOfParts.contains(e) && e.getType().equals(type)){
				parts.add(e);
			}
		}
		return parts;
	}

	/**
	 *
	 * @return list of parts, where Type is not Customizable or PC
	 */
	private Iterable<Hardware> getAllButList(){
		Set<Hardware> parts = new HashSet<>();
		for (Hardware e : getAllButType(HardwareType.PC)){
			if(!listOfParts.contains(e) && !e.getType().equals(HardwareType.CUSTOMIZABLE)){
				parts.add(e);
			}
		}
		return parts;
	}

	/**
	 *
	 * @param type TYpe of HardwareType
	 * @return list of parts except given Type
	 */
	private Iterable<Hardware> getAllButType(HardwareType type){
		Set<Hardware> parts = new HashSet<>();
		for(Hardware e : productRepositoryHardware.findAll().filter(accessoire -> !accessoire.getClass().equals(ShadowAccessoire.class))){
			if(!e.getType().equals(type)){
				parts.add(e);
			}
		}
		return parts;
	}

	/**
	 * Adds all, but the current listOfParts to the Model
	 * @param model Model view
	 * @return PcCreate
	 */
	@GetMapping("/PcCreate")
	String initialize(Model model){
		model.addAttribute("parts", getAllButList());
		return "PcCreate";
	}

	/**
	 * Add only Items of te given Type to the model
	 * @param model Model view
	 * @param type HardwareType searched for
	 * @return PcCreate
	 */
	@PostMapping(path = "/PcCreate")
	String choosePart(Model model, @RequestParam(name= "type") HardwareType type){
		model.addAttribute("parts", productRepositoryHardware.findAll().filter(part -> !part.getClass().equals(ShadowAccessoire.class) && part.getType().equals(type)));

		return "PcCreate";
	}

	/**
	 * Sort the item into the correct part in the listOfParts
	 * @param number count of items added to the list
	 * @param hardware Hardware to be added
	 * @return
	 */
	@PostMapping("/addItem")
	String addItem(@RequestParam(name= "number")int number,@RequestParam(name = "productId") Hardware hardware){
		mapHardwareItem(number, hardware);
		return "redirect:/partPicker";

	}

	/**
	 * mapping the chosen Item to the correct Attribute and adding the right amount to listOfPArts
	 * @param number count of items added to the list
	 * @param hardware Hardware to be added
	 */
	private void mapHardwareItem(int number, Hardware hardware){
		switch (hardware.getType()){
			case PROCESSOR:
				listOfParts.remove(cpu);
				cpu= hardware;
				listOfParts.add(cpu);
				break;
			case COOLING:
				listOfParts.remove(cooler);
				cooler= hardware;
				listOfParts.add(cooler);
				break;
			case GPU:
				listOfParts.remove(gpu);
				gpu= hardware;
				listOfParts.add(gpu);
				break;
			case MAINBOARD:
				listOfParts.remove(motherboard);
				motherboard= hardware;
				listOfParts.add(motherboard);
				break;
			case DISKDRIVE:
				listOfParts.remove(storage);
				storage= hardware;
				for(int i=0; i< number; i++) listOfParts.add(storage);
				break;
			case CUSTOMIZABLE:
				listOfParts.remove(cases);
				cases= hardware;
				listOfParts.add(cases);
				break;
			case RAM:
				listOfParts.remove(memory);
				memory= hardware;
				for(int i=0; i< number; i++){
					listOfParts.add(memory);
				}
				break;
			default:
				listOfParts.add(hardware);
				break;
		}
	}


	/**
	 * add all the CUSTOMIZABLEs to the model
	 * @param model Model view
	 * @return customizePc
	 */
	@GetMapping("/customizePc")
	String customizePc(Model model){
		model.addAttribute("parts" , getAllButList(HardwareType.CUSTOMIZABLE));
		return "customizePc";
	}

	/**
	 * Save all chosen parts in cart
	 * @param cart Customer cart
	 * @param model Model view
	 * @return cart
	 */
	@PostMapping(path = "/addListToCart")
	String customize(Cart cart, Model model){
			for (Hardware hardware : listOfParts) {
				cart.addOrUpdateItem(hardware, Quantity.of(1));
			}


		model.addAttribute("cart", cart);
		listOfParts = new LinkedList<>();
		clearParts();
		return "redirect:/cart";
	}

	/**
	 * Method to clear the Selection of parts
	 */
	private void clearParts(){
		cpu = null;
		gpu = null;
		motherboard = null;
		cooler=null;
		cases = null;
		storage=null;
		memory=null;
	}

	/**
	 * Add all item types and already chosen items to the model
	 * @param model Model view
	 * @return partPicker
	 */
	@GetMapping(path = "/partPicker")
	String partPicker(Model model){
		model.addAttribute("cpuType" , HardwareType.PROCESSOR);
		model.addAttribute("coolerType", HardwareType.COOLING);
		model.addAttribute("gpuType", HardwareType.GPU);
		model.addAttribute("motherboardType", HardwareType.MAINBOARD);
		model.addAttribute("ramType", HardwareType.RAM);
		model.addAttribute("storageType", HardwareType.DISKDRIVE);
		model.addAttribute("casesType",HardwareType.CUSTOMIZABLE);

		if(cpu != null){
			model.addAttribute("cpu",cpu);
		}
		if(cooler != null){
			model.addAttribute("cooler", cooler);
		}
		if(gpu != null){
			model.addAttribute("gpu", gpu);
		}
		if(motherboard != null){
			model.addAttribute("motherboard", motherboard);
		}
		if(storage != null){
			model.addAttribute("storage", storage);
		}
		if(memory != null){
			model.addAttribute("memory", memory);
		}
		if(cases != null){
			model.addAttribute("cases", cases);
		}


		return "partPicker";
	}

	/**
	 * get the number of the parts
	 * @return how many parts are now
	 */
	public int getListOfPartsLength() {
		return listOfParts.size();
	}
}
