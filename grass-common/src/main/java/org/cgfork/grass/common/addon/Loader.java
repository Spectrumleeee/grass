package org.cgfork.grass.common.addon;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Loader {
    Class<?> value();

    enum Config {
        NO_CONFIG, XML, JSON
    }

    Config config() default Config.NO_CONFIG;
}
