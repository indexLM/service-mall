package org.indexlm.frame.log;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 自定义参数校验器
 *
 * @author LiuMing
 * @since 2021/2/27
 */
@Target({ElementType.METHOD}) //只允许用在类的字段上.和方法参数上
@Retention(RetentionPolicy.RUNTIME) //注解保留在程序运行期间，此时可以通过反射获得定义在某个类上的所有注解
public @interface LogPrint {

    /**
     * 日志标题
     */
    String value() default "";

    /**
     * 日志备注
     */
    String remark() default "";
}
