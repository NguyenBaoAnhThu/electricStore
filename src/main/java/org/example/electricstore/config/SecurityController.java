package org.example.electricstore.config;
import org.example.electricstore.model.User;
import org.example.electricstore.repository.IUserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.Collection;

@Controller
public class SecurityController {
    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @GetMapping(value = "/login")
    public String loginPage(Model model, @RequestParam(value = "error", defaultValue = "") String error) {
        model.addAttribute("error", error);
        return "login";
    }

    @GetMapping(value = "/home")
    public String homePage(Authentication authentication, Model model) {
        String username = authentication.getName();

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER");

        model.addAttribute("username", username);
        model.addAttribute("role", role);

        // Redirect based on role
        return switch (role) {
            case "ROLE_ADMIN" -> "admin/layout/layout";
            case "ROLE_EMPLOYEE" -> "admin/layout/layout";
            case "ROLE_BUSINESS" -> "admin/layout/layout";
            case "ROLE_SALES" -> "admin/layout/layout";
            case "ROLE_WAREHOUSE" -> "admin/layout/layout";
            case "ROLE_CUSTOMER" -> "customer/index";
            default -> "403";
        };
    }

    @GetMapping(value = "/logout")
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        return "login";
    }

    @GetMapping(value = "/403")
    public String view403Page() {
        return "error";
    }
    @GetMapping("/account")
    public String viewAccount(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "error";
        }
        model.addAttribute("user", user);
        return "account";
    }

    @PostMapping("/account/update")
    public String updateAccount(
            Authentication authentication,
//            @RequestParam String fullName,
            @RequestParam String email,
            Model model) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "error";
        }

        // Update user details
        user.setEmail(email);
        userRepository.save(user);

        model.addAttribute("message", "Cập nhật tài khoản thành công!");
        model.addAttribute("user", user);
        return "account";
    }

    @PostMapping("/account/change-password")
    public String changePassword(
            Authentication authentication,
            @RequestParam String oldPassword,
            @RequestParam String newPassword,
            @RequestParam String confirmPassword,
            Model model) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "error";
        }

        if (!passwordEncoder.matches(oldPassword, user.getEncrytedPassword())) {
            model.addAttribute("error", "Mật khẩu cũ không đúng!");
            model.addAttribute("user", user);
            return "account";
        }

        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu mới và cũ không trùng khớp!");
            model.addAttribute("user", user);
            return "account";
        }

        // Update password
        user.setEncrytedPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        model.addAttribute("message", "Thay đổi mật khẩu thành công!");
        model.addAttribute("user", user);
        return "account";
    }
}