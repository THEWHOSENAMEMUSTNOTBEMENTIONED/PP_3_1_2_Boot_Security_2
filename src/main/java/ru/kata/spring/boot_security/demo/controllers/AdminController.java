package ru.kata.spring.boot_security.demo.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.kata.spring.boot_security.demo.repositories.UserRepository;
import ru.kata.spring.boot_security.demo.services.UserService;
import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Controller
public class AdminController {

    private final UserService userService;
    final UserRepository userRepository;

    @Autowired
    public AdminController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/admin/all-users")
    public String findAllUsers(Model model) {
        List<User> users = userService.findAll();
        model.addAttribute("users", users);
        model.addAttribute("allRoles", userService.getAllRoles());
        return "all-users";
    }

    @GetMapping("/admin/user-create")
    public String createUserForm(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("allRoles", userService.getAllRoles());
        return "user-create";
    }

    @PostMapping("/admin/user-create")
    public String createUser(@ModelAttribute User user, @RequestParam("selectedRoles") List<String> selectedRoles) {
        user.setRoles(mapRoleNamesToRoles(selectedRoles));
        userService.saveUser(user);
        return "redirect:/admin/all-users";
    }

    @GetMapping("/admin/user-delete")
    public String deleteUser(@RequestParam("id") int id) {
        userService.deleteById(id);
        return "redirect:/admin/all-users";
    }

    @PostMapping("/admin/user-update")
    public String updateUser(
            @RequestParam("id") int id,
            @RequestParam("username") String username,
            @RequestParam("surname") String surname,
            @RequestParam("department") String department,
            @RequestParam("email") String email,
            @RequestParam(value = "password", required = false) String password,
            @RequestParam("selectedRoles") List<String> selectedRoles) {
        User user = userService.findById(id);
        if (user == null) {
            return "redirect:/admin/all-users";
        }
        user.setUsername(username);
        user.setSurname(surname);
        user.setDepartment(department);
        user.setEmail(email);
        if (password != null && !password.isEmpty()) {
            user.setPassword(password);
        }
        user.setRoles(mapRoleNamesToRoles(selectedRoles));
        userService.saveUser(user);
        return "redirect:/admin/all-users";
    }

    private Set<Role> mapRoleNamesToRoles(List<String> roleNames) {
        Set<Role> roles = new HashSet<>();
        for (String roleName : roleNames) {
            Role role = userService.findRoleByName(roleName);
            if (role != null) {
                roles.add(role);
            }
        }
        return roles;
    }
}