package web_patterns.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web_patterns.business.Employee;
import web_patterns.persistence.EmployeeDao;
import web_patterns.persistence.EmployeeDaoImpl;

import java.util.List;

@Controller
public class ViewEmployees {

    @GetMapping("/viewEmployees")
    public String processRequest(@RequestParam(name="id", required=false, defaultValue="1002") String id, Model model) {
        EmployeeDao empDao = new EmployeeDaoImpl("database.properties");
        List<Employee> employees = empDao.getAllEmployees();
        model.addAttribute("employees", employees);

        return "employees";
    }

}