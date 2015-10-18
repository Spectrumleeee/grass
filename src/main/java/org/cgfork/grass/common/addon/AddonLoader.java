/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-13
 */
package org.cgfork.grass.common.addon;

/**
 * 
 */
public interface AddonLoader<T> {
    Class<T> getInterface();
    
    T getAddon(Class<?> clazz);
    
    T getAddon(String name);

    T addon(T value);
    
    AddonLoader<T> load(Class<T> clazz);
}
