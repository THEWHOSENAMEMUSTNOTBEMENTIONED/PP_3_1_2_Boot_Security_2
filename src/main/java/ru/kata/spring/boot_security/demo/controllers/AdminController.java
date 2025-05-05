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
    private final UserRepository userRepository;

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

    @DeleteMapping("/admin/user-delete/{id}")
    public String deleteUser(@PathVariable("id") int id) {
        userService.deleteById(id);
        return "redirect:/admin/all-users";
    }

    @PostMapping("/admin/user-update")
    public String updateUser(@ModelAttribute User user, @RequestParam("selectedRoles") List<String> selectedRoles) {
        try {
            user.setRoles(mapRoleNamesToRoles(selectedRoles));
            userService.updateUser(user);
            return "redirect:/admin/all-users";
        } catch (IllegalArgumentException e) {
            return "redirect:/admin/all-users?error=user_not_found";
        }
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