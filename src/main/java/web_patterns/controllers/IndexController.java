package web_patterns.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class IndexController {
    @GetMapping("/")
    public String home(){
        return "index";
    }

    @GetMapping("/user_index")
    public String userIndex(){
        return "user_index";
    }

    @GetMapping("/customer_index")
    public String customerIndex(){
        return "customer_index";
    }

    @GetMapping("/employee_index")
    public String employeeIndex(){
        return "employee_index";
    }

    @GetMapping("/order_index")
    public String orderIndex(){
        return "order_index";
    }
}
