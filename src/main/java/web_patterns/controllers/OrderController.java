package web_patterns.controllers;


import jakarta.servlet.http.HttpSession;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import web_patterns.business.Order;
import web_patterns.persistence.OrderDao;
import web_patterns.persistence.OrderDaoImpl;

import java.util.List;

@Controller
@Slf4j
public class OrderController {
    @GetMapping("/viewOrders")
    public String viewOrders(Model model){
        OrderDao orderDao = new OrderDaoImpl("database.properties");
        List<Order> orders = orderDao.getAllOrders();
        model.addAttribute("orders", orders);

        return "orderList";
    }
}
