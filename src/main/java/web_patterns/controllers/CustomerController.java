package web_patterns.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web_patterns.business.Customer;
import web_patterns.persistence.CustomerDao;
import web_patterns.persistence.CustomerDaoImpl;

import java.util.List;

@Controller
public class CustomerController {

    @GetMapping("/viewCustomers")
    public String processRequest(Model model) {
        CustomerDao custDao = new CustomerDaoImpl("database.properties");
        List<Customer> customers = custDao.getAllCustomers();
        model.addAttribute("customers", customers);

        return "customers";
    }
}