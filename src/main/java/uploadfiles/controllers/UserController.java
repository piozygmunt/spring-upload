package uploadfiles.controllers;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import uploadfiles.entities.User;
import uploadfiles.forms.UserForm;
import uploadfiles.forms.UserValidator;
import uploadfiles.services.UserService;

import javax.validation.Valid;

@Controller
public class UserController {

    private final Logger logger = Logger.getLogger(MainController.class);


    @Autowired
    private UserValidator userValidator;
    @Autowired
    private UserService userService;

    @GetMapping(value = "/registration")
    public String registration(Model model) {

        model.addAttribute("userForm", new UserForm());

        return "registration";
    }

    @PostMapping(value = "/registration")
    public String registration(@Valid @ModelAttribute("userForm") UserForm userForm, BindingResult bindingResult) {
        userValidator.validate(userForm, bindingResult);

        if (bindingResult.hasErrors()) {
            return "registration";
        }

        logger.info(userForm);

        User user = new User(userForm);

        userService.createNewUser(user);

        return "redirect:/";
    }
}
