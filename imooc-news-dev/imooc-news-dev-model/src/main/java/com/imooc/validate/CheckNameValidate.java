package com.imooc.validate;

import com.imooc.utils.UrlUtil;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CheckNameValidate implements ConstraintValidator<CheckName, String> {

    @Override
    public boolean isValid(String name, ConstraintValidatorContext context) {
        return UrlUtil.verifyName(name.trim());
    }
}
