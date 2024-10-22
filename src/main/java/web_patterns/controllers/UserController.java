package web_patterns.controllers;

import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import web_patterns.business.User;
import web_patterns.persistence.UserDao;
import web_patterns.persistence.UserDaoImpl;

@Controller
public class UserController {

    @PostMapping("registerUser")
    public String registerUser(
            @RequestParam(name="username") String username,
            @RequestParam(name="password") String password,
            @RequestParam(name="first", required = false) String first,
            @RequestParam(name="last", required = false) String last,
            @RequestParam(name="email") String email,
            Model model, HttpSession session){
        // VALIDATION
        String view = "";
        UserDao userDao = new UserDaoImpl("database.properties");
        User u = new User(username, password, first, last, email);
        boolean added = userDao.register(u);
        if(added){
            view = "registerSuccess";
            model.addAttribute("registeredUser", u);
        }else{
            view = "registerFailed";
        }

        return view;
    }
}
