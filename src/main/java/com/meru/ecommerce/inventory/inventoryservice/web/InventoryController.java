package com.meru.ecommerce.inventory.inventoryservice.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;


import com.meru.ecommerce.inventory.inventoryservice.dao.InventoryRepository;
import com.meru.ecommerce.inventory.inventoryservice.entity.Inventory;
import com.meru.ecommerce.inventory.inventoryservice.service.InventoryService;

@Controller
public class InventoryController {
    static String WELCOME_MSG = "Welcome to  Inventory Service. This is not a valid service path.";
    static String CREATE_SUB_COMPONENT_MSG = "Create %s for Inventory %d in Inventory is %s";
    static String UPDATE_SUB_COMPONENT_MSG = "Update %s for Inventory %d in Inventory is %s";
    static String DELETE_MSG = "Delete Inventory for InventoryID: %s %s";
    static String SUCCESS = "Success";
    static String ERROR = "Failed";
    static String COMPONENT_INVENTORY = "Inventory";
    static String RETURN_TEMPLATE = "{\"message\":\"%s\"}";

    @Autowired
    InventoryService is;
    
    @Autowired
	private InventoryRepository inventoryRepository;
	
	
	@RequestMapping(value={"/index"},method = RequestMethod.GET)
	public String index(Model model){
		List<Inventory> inventoryDetails= inventoryRepository.findAll();
		model.addAttribute("inventoryDetails", inventoryDetails);
		return "index";
	}
	
	@RequestMapping(value="search",method = RequestMethod.GET)
	public String search(){
		return "searchInventory";
	}
	
	
	@RequestMapping(value="searchbyid",method = RequestMethod.GET)
	public String searchById(@RequestParam(value = "search", required = false) int id, Model model){		
		Inventory inventory=  is.getInventoryByProductId(id);	 
		 model.addAttribute("search", inventory);
		 
		return "searchInventory";
	
	}
	
	@RequestMapping(value="add")
	public String addInventory(Model model) {
		
		model.addAttribute("inventory", new Inventory());
		return "addInventory";
	}
	
	@RequestMapping(value="save", method=RequestMethod.POST)
	public String saveProduct(Inventory inventory) {
		is.createOrUpdateInventory(inventory);
		return "redirect:/index";
	}
	
	
	@RequestMapping(value="/delete/{inventoryId}", method=RequestMethod.GET)
	public String deleteProduct(@PathVariable("inventoryId") int inventoryId, Model model) {
		inventoryRepository.delete(inventoryId);
		return "redirect:/index";
	}
    

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/")
    public ResponseEntity<String> showInfo() {
        return ResponseEntity.badRequest().headers(new HttpHeaders()).body(WELCOME_MSG);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    public ResponseEntity<Inventory> getInventoryById(@PathVariable int id) {
        Inventory inventory = is.getInventoryById(id);
        HttpStatus status = HttpStatus.OK;
        if (null == inventory) {
            status = HttpStatus.NO_CONTENT;
        }
        return new ResponseEntity<>(inventory, new HttpHeaders(), status);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.GET, value = "/all")
    public ResponseEntity<List<Inventory>> getAllProductsInventory() {
        return ResponseEntity.ok().body(is.getAllProductsInventory());
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<String> deleteInventory(@PathVariable int id) {
        boolean deleted = is.removeInventory(id);
        HttpStatus status = HttpStatus.OK;
        String msg = String.format(DELETE_MSG, id, SUCCESS);
        if (!deleted) {
            msg = String.format(DELETE_MSG, id, ERROR);
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(String.format(RETURN_TEMPLATE, msg), new HttpHeaders(), status);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.POST, value = "/")
    public ResponseEntity<String> addInventory(@RequestBody Inventory inventory) {
        boolean created = is.createOrUpdateInventory(inventory);
        HttpStatus status = HttpStatus.OK;
        String msg = String.format(CREATE_SUB_COMPONENT_MSG, COMPONENT_INVENTORY, inventory.getInventoryId(), SUCCESS);
        if (!created) {
            String.format(CREATE_SUB_COMPONENT_MSG, COMPONENT_INVENTORY, inventory.getInventoryId(), ERROR);
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(String.format(RETURN_TEMPLATE, msg), new HttpHeaders(), status);
    }

    @CrossOrigin
    @RequestMapping(method = RequestMethod.PUT, value = "/{id}")
    public ResponseEntity<String> updateInventory(@PathVariable int id, @RequestBody Inventory inventory) {
        boolean updated = is.createOrUpdateInventory(inventory);
        HttpStatus status = HttpStatus.OK;
        String msg = String.format(UPDATE_SUB_COMPONENT_MSG, COMPONENT_INVENTORY, id, SUCCESS);
        if (!updated) {
            msg = String.format(UPDATE_SUB_COMPONENT_MSG, COMPONENT_INVENTORY, id, ERROR);
            status = HttpStatus.BAD_REQUEST;
        }
        return new ResponseEntity<>(String.format(RETURN_TEMPLATE, msg), new HttpHeaders(), status);
    }
}
