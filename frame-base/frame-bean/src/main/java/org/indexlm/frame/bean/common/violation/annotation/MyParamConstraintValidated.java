package org.indexlm.frame.bean.common.violation.annotation;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;
import java.util.Arrays;
import java.util.List;

/**
 * @author LiuMing
 * @since 2021/2/25
 */
public class MyParamConstraintValidated implements ConstraintValidator<Check, Object> {
    /**
     * 合法的参数值，从注解中获取
     */
    private List<String> paramValues;

    @Override
    public void initialize(Check constraintAnnotation) {
        //初始化时获取注解上的值
        paramValues = Arrays.asList(constraintAnnotation.paramValues());
    }

    @Override
    public boolean isValid(Object o, ConstraintValidatorContext constraintValidatorContext) {
        if(o==null||"".equals(o)){
            return true;
        }
        String str = o.toString();
        boolean falg = false;
        for (String paramValue : paramValues) {
            boolean equals = paramValue.equals(str);
            if (equals) {
                falg = true;
            }
        }
        //不在指定的参数列表中
        return falg;
    }
}
