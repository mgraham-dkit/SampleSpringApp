package web_patterns.controllers;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web_patterns.business.Employee;
import web_patterns.persistence.EmployeeDao;
import web_patterns.persistence.EmployeeDaoImpl;

@Controller
public class ChangeEmailController {

    @PostMapping("/editEmail")
    public String processRequest(@RequestParam(name="empId") String empId,
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
}