package ru.kata.spring.boot_security.demo.services;

import ru.kata.spring.boot_security.demo.models.Role;
import ru.kata.spring.boot_security.demo.models.User;

import java.util.List;

public interface UserService {
    List<User> findAll();
    User findById(int id);
    void saveUser(User user);
    void deleteById(int id);
    List<Role> getAllRoles();
    Role findRoleByName(String roleName);
    void updateUser(User user); // Н
}