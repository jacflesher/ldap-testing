package com.jdap.jdap;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    UserService userService;

    @Autowired
    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping("/create")
    public String showCreateUserForm(Model model) {
        model.addAttribute("userForm", new UserForm());
        return "createUserForm";
    }

    @PostMapping("/create")
    public String createUser(@ModelAttribute("userForm") UserForm userForm, Model model) {
        try {

            String cn = userForm.getFirstName() + " " + userForm.getLastName();

            userService.createUser(
                    userForm.getUid(),
                    cn,
                    userForm.getLastName(),
                    userForm.getPassword(),
                    userForm.getOrganizationalUnit() // <--- PASS THE OU HERE
            );

            model.addAttribute("message", "User " + userForm.getUid() + " created successfully!");
            return "redirect:/users/create?success";

        } catch (Exception e) {
            model.addAttribute("error", "Error creating user: " + e.getMessage());
            return "createUserForm";
        }
    }

    @GetMapping("/delete")
    public String showDeleteUserForm() {
        return "deleteUserForm";
    }


    @PostMapping("/delete")
    public String deleteUser(@RequestParam("cnToDelete") String cn, Model model) {
        try {
            userService.deleteUser(cn, "IT");

            model.addAttribute("message", "User " + cn + " deleted successfully!");
            return "redirect:/users/delete?deletedCn=" + cn;

        } catch (Exception e) {
            model.addAttribute("error", "Error deleting user " + cn + ": " + e.getMessage());
            return "deleteUserForm";
        }
    }
}
