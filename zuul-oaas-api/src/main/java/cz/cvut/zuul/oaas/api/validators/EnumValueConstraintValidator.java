package cz.cvut.zuul.oaas.api.validators;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.ArrayList;
import java.util.List;

/**
 * JSR-303 validator for {@link EnumValue} constraint.
 */
public class EnumValueConstraintValidator implements ConstraintValidator<EnumValue, String> {

    private boolean nullable;
    private boolean caseSensitive;
    private List<String> values;


    public void initialize(EnumValue constraint) {
        Class<? extends Enum> clazz = constraint.value();
        nullable = constraint.nullable();
        caseSensitive = constraint.caseSensitive();
        values = new ArrayList<>(clazz.getEnumConstants().length);

        for (Enum e : clazz.getEnumConstants()) {
            String value = e.toString();
            values.add(caseSensitive ? value : value.toUpperCase());
        }
    }

    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return nullable;
        } else {
            return values.contains(caseSensitive ? value : value.toUpperCase());
        }
    }
}
