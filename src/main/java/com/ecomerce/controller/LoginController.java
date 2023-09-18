package com.ecomerce.controller;
import com.ecomerce.model.Role;
import com.ecomerce.model.User;
import com.ecomerce.repository.RoleRepository;
import com.ecomerce.repository.UserRepository;
import com.ecomerce.global.GlobalData;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;

@Controller
public class LoginController {
    @Autowired
    private BCryptPasswordEncoder bCryptPasswordEncoder;
    @Autowired
    RoleRepository roleRepository;
    @Autowired
    UserRepository userRepository;

    @GetMapping("/login")
    public String loginGet(){
        GlobalData.cart.clear();
        return "login";
    }
    @GetMapping("/register")
    public String registerGet(Model model){
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping ("/register")
    public String registerPost(@ModelAttribute ("user") User user, HttpServletRequest request)throws ServletException{
        String password = user.getPassword();
        user.setPassword(bCryptPasswordEncoder.encode(password));
        List<Role> roles = new ArrayList<>();
        roles.add(roleRepository.findById(2).get());
        user.setRoles(roles);
        userRepository.save(user);
        request.login(user.getEmail(),password);
        return "redirect:/home";
    }


}
