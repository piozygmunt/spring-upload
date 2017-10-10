package uploadfiles.forms;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;
import org.springframework.validation.Validator;
import uploadfiles.services.UserService;

@Component
public class UserRegisterValidator implements Validator {

    @Autowired
    private UserService userService;

    @Override
    public boolean supports(Class<?> aClass) {
        return UserForm.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {
        UserForm userForm = (UserForm) o;

        validateCustom(o, errors);

        if (userService.findByUsername(userForm.getUsername()) != null) {
            errors.rejectValue("username", "Duplicate.userForm.username");
        }


    }

    protected void validateCustom(Object o, Errors errors){
        UserForm userForm = (UserForm) o;

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "username", "NotEmpty", "Username can't be empty.");
        if (userForm.getUsername().length() < 3 || userForm.getUsername().length() > 10) {
            errors.rejectValue("username", "Size.userForm.username");
        }

        if (userForm.getPassword().length() < 3 || userForm.getPassword().length() > 10) {
            errors.rejectValue("password", "Size.userForm.password");
        }

        if (!userForm.doPasswordMatch()) {
            errors.rejectValue("passwordConfirm", "Diff.userForm.passwordConfirm");
        }

        ValidationUtils.rejectIfEmptyOrWhitespace(errors, "email", "NotEmpty", "Emails can't be empty.");
    }
}
