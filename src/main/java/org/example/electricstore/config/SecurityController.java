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

    // Trang đăng nhập
    @GetMapping(value = "/login")
    public String loginPage(Model model, @RequestParam(value = "error", defaultValue = "") String error) {
        model.addAttribute("error", error);
        return "login";
    }

    // Trang chủ sau khi đăng nhập
    @GetMapping(value = "/home")
    public String homePage(Authentication authentication, Model model) {
        String username = authentication.getName(); // Lấy tên người dùng đã đăng nhập

        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        String role = authorities.stream()
                .findFirst()
                .map(GrantedAuthority::getAuthority)
                .orElse("ROLE_USER"); // Nếu không có vai trò nào thì gán mặc định là ROLE_USER

        model.addAttribute("username", username);
        model.addAttribute("role", role);

        // Điều hướng đến giao diện phù hợp với vai trò người dùng
        return switch (role) {
            case "ROLE_ADMIN", "ROLE_EMPLOYEE", "ROLE_BUSINESS", "ROLE_SALES", "ROLE_WAREHOUSE" -> "admin/layout/layout";
            case "ROLE_CUSTOMER" -> "customer/index";
            default -> "403"; // Trả về trang lỗi nếu vai trò không hợp lệ
        };
    }

    // Xử lý khi đăng xuất thành công
    @GetMapping(value = "/logout")
    public String logoutSuccessfulPage(Model model) {
        model.addAttribute("title", "Logout");
        return "login";
    }

    // Trang lỗi 403 - Không có quyền truy cập
    @GetMapping(value = "/403")
    public String view403Page() {
        return "error";
    }

    // Xem thông tin tài khoản
    @GetMapping("/account")
    public String viewAccount(Authentication authentication, Model model) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "error"; // Trả về trang lỗi nếu không tìm thấy user
        }
        model.addAttribute("user", user);
        return "account"; // Hiển thị trang tài khoản
    }

    // Cập nhật thông tin tài khoản (email)
    @PostMapping("/account/update")
    public String updateAccount(
            Authentication authentication,
            @RequestParam String email,
            Model model) {
        String username = authentication.getName();
        User user = userRepository.findByUsername(username);
        if (user == null) {
            return "error";
        }

        // Cập nhật email mới
        user.setEmail(email);
        userRepository.save(user);

        model.addAttribute("message", "Cập nhật tài khoản thành công!");
        model.addAttribute("user", user);
        return "account";
    }

    // Thay đổi mật khẩu
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

        // Kiểm tra mật khẩu cũ có đúng không
        if (!passwordEncoder.matches(oldPassword, user.getEncrytedPassword())) {
            model.addAttribute("error", "Mật khẩu cũ không đúng!");
            model.addAttribute("user", user);
            return "account";
        }

        // Kiểm tra mật khẩu mới có trùng khớp không
        if (!newPassword.equals(confirmPassword)) {
            model.addAttribute("error", "Mật khẩu mới và xác nhận mật khẩu không trùng khớp!");
            model.addAttribute("user", user);
            return "account";
        }

        // Cập nhật mật khẩu mới
        user.setEncrytedPassword(passwordEncoder.encode(newPassword));
        userRepository.save(user);

        model.addAttribute("message", "Thay đổi mật khẩu thành công!");
        model.addAttribute("user", user);
        return "account";
    }
}
