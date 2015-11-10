package org.cgfork.grass.common.cache.single;

import java.lang.annotation.*;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Singleton {
    String value();
}
