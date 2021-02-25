package org.indexlm.frame.bean.common.violation.annotation;

import javax.validation.Constraint;
import javax.validation.Payload;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义参数校验器
 *
 * @author LiuMing
 * @since 2021/2/25
 */
@Target({ElementType.FIELD, ElementType.PARAMETER}) //只允许用在类的字段上.和方法参数上
@Retention(RetentionPolicy.RUNTIME) //注解保留在程序运行期间，此时可以通过反射获得定义在某个类上的所有注解
@Constraint(validatedBy = MyParamConstraintValidated.class)
public @interface Check {
    /**
     * 合法的参数值
     */
    String[] paramValues();

    /**
     * 提示信息
     */
    String message() default "参数不为指定值";

    Class<?>[] groups() default {};

    Class<? extends Payload>[] payload() default {};
}
