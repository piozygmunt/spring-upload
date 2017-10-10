package uploadfiles.controllers;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.support.SessionStatus;
import uploadfiles.entities.Role;
import uploadfiles.entities.User;
import uploadfiles.forms.UserEditForm;
import uploadfiles.forms.UserEditValidator;
import uploadfiles.services.RoleService;
import uploadfiles.services.UserService;

import javax.servlet.http.HttpSession;
import java.util.HashSet;


@Controller
@RequestMapping("/admin")
public class AdministratorController {
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserEditValidator userEditValidator;

    @GetMapping("users")
    public String listUsers(Model model)
    {
        Iterable<User> users = userService.findAll();
        model.addAttribute("users" ,  users);
        return "admin/users";
    }

    @GetMapping("users/{id}")
    public String editUser(@PathVariable long id,  Model model, HttpSession session)
    {
        User user = userService.findByID(id);
        Iterable<Role> roles= roleService.findAll();
        UserEditForm userEditForm = new UserEditForm(user);
        model.addAttribute("userEditForm", userEditForm);
        model.addAttribute("allRoles", roles);
        session.setAttribute("allRoles", roles);
        return "admin/userEdit";
    }

    @PostMapping("users/{id}")
    public String updateUser(@PathVariable long id, @ModelAttribute UserEditForm userEditForm,
                             BindingResult bindingResult, @SessionAttribute("allRoles") Iterable<Role> roles,
                             Model model, SessionStatus sessionStatus)
    {
        userEditValidator.validate(userEditForm, bindingResult);


        if (bindingResult.hasErrors()) {
            model.addAttribute("allRoles", roles);
            return "admin/userEdit";
        }

        User user = new User(userEditForm);
        userService.save(user);
        sessionStatus.setComplete();
        return "redirect:/admin/users";
    }

    @GetMapping("users/remove/{id}")
    public String removeUser(@PathVariable long id,  Model model)
    {
        userService.delete(id);
        Iterable<User> users = userService.findAll();
        model.addAttribute("users" ,  users);
        return "admin/users";
    }
}
