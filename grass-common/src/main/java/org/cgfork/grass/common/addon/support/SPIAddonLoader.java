/**
 * Author:  C_G <cg.fork@gmail.com>
 * Created: 2015-10-21
 */
package org.cgfork.grass.common.addon.support;

import java.util.ServiceLoader;

import org.cgfork.grass.common.addon.AddonLoader;

/**
 * 
 */
public abstract class SPIAddonLoader<T> extends AbstractLoader<T> {
    
    @Override
    public AddonLoader<T> load(Class<T> clazz, ClassLoader classLoader) {
        ServiceLoader<T> loader = ServiceLoader.load(clazz, classLoader);
        
        for (T value : loader) {
            if (checkAnnotations(value.getClass())) {
                this.addon(value);
            }
        }
        return this;
    }
}
