package computech.catalog;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;


@Controller
public class CatalogController {
    private final CompuTechCatalog catalog;


	public CatalogController(CompuTechCatalog catalog) {
		this.catalog = catalog;
	}



	/**
	 * Show all items of the catalog
	 * @param model Model view
	 * @return catalog
	 */
    @GetMapping("/catalog")
    String completecatalog(Model model){

    	model.addAttribute("catalog", catalog.findAll().filter(accessoire -> (accessoire.isInCatalogshown()&&!accessoire.getClass().equals(ShadowAccessoire.class) && !accessoire.getType().equals(HardwareType.CUSTOMIZABLE))));
        return "catalog";
    }

	/**
	 * switch to the detail site of the Item
	 * @param hardware chosen Item
	 * @param model Model view
	 * @return
	 */
	@GetMapping("/hardware/{hardware}")
	String detail(@PathVariable Hardware hardware, Model model) {

		if(hardware instanceof PC){
			model.addAttribute("parts", ((PC) hardware).getComponents());
		}
		model.addAttribute("type", hardware.getClass().getName());
		model.addAttribute("hardware", hardware);


		return "hardware";
	}
}
