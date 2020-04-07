package com.pricess.omc.validator;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.util.Set;

/**
 * @author pricess.wang
 * @date 2019/12/31 19:05
 */
public class DefaultActionValidator implements ActionValidator {

    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();

    @Override
    public <T> ValidatorResult validate(T t) {
        Set<ConstraintViolation<T>> validates = validator.validate(t);

        if (validates.size() == 0) {
            return null;
        }
        StringBuilder sb = new StringBuilder("\n");

        for (ConstraintViolation<T> violation : validates) {
            sb
                    .append(violation.getPropertyPath())
                    .append(":")
                    .append(violation.getMessage())
                    .append("\n");
        }
        return new ValidatorResult(sb.toString());
    }

}
