/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-13
 */
package org.cgfork.grass.common.addon;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 
 */
@Documented
@Retention(RetentionPolicy.RUNTIME)
@Target({ElementType.TYPE})
public @interface Loader {
    Class<?> value();
}
