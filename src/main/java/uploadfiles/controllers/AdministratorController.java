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

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpSession;
import java.security.Principal;
import java.util.HashSet;
import java.util.logging.Logger;


@Controller
@RequestMapping("/admin")
@SessionAttributes({"allRoles"})
public class AdministratorController {
    private static final Logger logger = Logger.getLogger(AdministratorController.class.toString());
    @Autowired
    private UserService userService;
    @Autowired
    private RoleService roleService;
    @Autowired
    private UserEditValidator userEditValidator;

    @GetMapping("users")
    public String listUsers(Model model, Principal principal, HttpServletRequest request)
    {
        Iterable<User> users = userService.findAll();
        model.addAttribute("users" ,  users);
        logger.info("principal: " + principal);
        logger.info("request: " + request.getUserPrincipal());

        return "admin/users";
    }

    @ModelAttribute("allRoles")
    public Iterable<Role> getAllRoles()
    {
        logger.info("getting roles");
        return roleService.findAll();
    }

    @GetMapping("users/{id}")
    public String editUser(@PathVariable long id,  Model model)
    {
        User user = userService.findByID(id);
        UserEditForm userEditForm = new UserEditForm(user);
        model.addAttribute("userEditForm", userEditForm);
        return "admin/userEdit";
    }

    @PostMapping("users/{id}")
    public String updateUser(@PathVariable long id, @ModelAttribute UserEditForm userEditForm,
                             BindingResult bindingResult, @ModelAttribute("allRoles") Iterable<Role> roles,
                             SessionStatus sessionStatus)
    {
        userEditValidator.validate(userEditForm, bindingResult);


        if (bindingResult.hasErrors()) {
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
