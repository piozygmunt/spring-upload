package uploadfiles.forms;

import org.springframework.stereotype.Component;
import org.springframework.validation.Errors;
import org.springframework.validation.ValidationUtils;

@Component
public class UserEditValidator extends UserRegisterValidator{
    @Override
    public boolean supports(Class<?> aClass) {
        return UserRegisterValidator.class.equals(aClass);
    }

    @Override
    public void validate(Object o, Errors errors) {

        validateCustom(o, errors);
    }
}
