package web_patterns.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web_patterns.business.Employee;
import web_patterns.persistence.EmployeeDao;
import web_patterns.persistence.EmployeeDaoImpl;

@Controller
public class FrontController {

    @GetMapping("/controller")
    public String processRequest(@RequestParam(name="id", required=false, defaultValue="1002") String id, Model model) {
        int idNum = Integer.parseInt(id);

        EmployeeDao empDao = new EmployeeDaoImpl("database.properties");
        Employee e = empDao.getById(idNum);
        model.addAttribute("employee", e);

        return "greeting";
    }

}