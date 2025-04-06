package org.example.electricstore.controller.admin;


import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;


@Controller
@RequestMapping("/Admin")
public class AdminHomeController {
    @GetMapping()
    public String showAdminHome(Authentication authentication, Model model) {
        String username = authentication.getName();
        model.addAttribute("username", username);
        return "admin/layout/layout";
    }
}
