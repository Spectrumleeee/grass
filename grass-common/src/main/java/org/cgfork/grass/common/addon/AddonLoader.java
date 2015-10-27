package org.cgfork.grass.common.addon;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public interface AddonLoader<T> {
    Class<T> getInterface();
    
    T getAddon(Class<?> clazz);
    
    T getAddon(String name);

    T addon(T value);
    
    AddonLoader<T> load(Class<T> clazz, ClassLoader classLoader);
}
