package web_patterns.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web_patterns.business.Employee;
import web_patterns.persistence.EmployeeDao;
import web_patterns.persistence.EmployeeDaoImpl;

import java.util.List;

@Controller
public class EmployeeController {

    @PostMapping("/editEmail")
    public String editEmail(@RequestParam(name="empId") String empId,
                                 @RequestParam(name="email") String email,
                                 Model model) {
        try {
            int idNum = Integer.parseInt(empId);

            EmployeeDao empDao = new EmployeeDaoImpl("database.properties");
            boolean changed = empDao.updateEmployeeEmail(idNum, email);
            if (changed) {
                Employee e = empDao.getById(idNum);
                model.addAttribute("employee", e);
            }else{
                model.addAttribute("empId", idNum);
            }
            return "updatedEmail";
        }catch(NumberFormatException ex){
            throw new NumberFormatException(empId + "is not a valid id. Employee ID must be a number.");
        }
    }

    @GetMapping("/viewEmployees")
    public String viewEmployee(Model model) {
        EmployeeDao empDao = new EmployeeDaoImpl("database.properties");
        List<Employee> employees = empDao.getAllEmployees();
        model.addAttribute("employees", employees);

        return "employeeList";
    }

    @GetMapping("/getEmployee")
    public String getEmployee(@RequestParam(name="empId") String empId,
                              Model model) {
        EmployeeDao empDao = new EmployeeDaoImpl("database.properties");
        int id = Integer.parseInt(empId);
        Employee emp = empDao.getById(id);
        if(emp != null) {
            model.addAttribute("employee", emp);
        }else{
            model.addAttribute("empId", id);
        }
        return "viewEmployee";
    }

    @GetMapping("/greet")
    public String greet(@RequestParam(name="empId", required=false, defaultValue="1002") String id,
                        Model model) {
        int idNum = Integer.parseInt(id);

        EmployeeDao empDao = new EmployeeDaoImpl("database.properties");
        Employee e = empDao.getById(idNum);
        model.addAttribute("employee", e);
        model.addAttribute("empId", id);

        return "greeting";
    }
}