/**
 * Author:  C_G <cg.fork@gmail.com>
 * Created: 2015-10-21
 */
package org.cgfork.grass.common.addon.support;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.cgfork.grass.common.addon.AddonLoader;
import org.cgfork.grass.common.addon.Loader;
import org.cgfork.grass.common.addon.support.AbstractLoader.AnnotationNotFoundException;

/**
 * 
 */
public class AddonLoaders {
    
    private static final ConcurrentMap<Class<?>, AddonLoader<?>> loaderMap = new ConcurrentHashMap<>();
    
    @SuppressWarnings("unchecked")
    public static <T> AddonLoader<T> getAddonLoader(Class<T> clazz) throws AnnotationNotFoundException {
        AddonLoader<?> addonLoader = loaderMap.get(clazz);
        if (addonLoader != null) {
            return (AddonLoader<T>) addonLoader;
        }
        
        return createAddonLoader(clazz);
    }
    
    @SuppressWarnings("unchecked")
    public static <T> AddonLoader<T> createAddonLoader(Class<T> clazz) throws AnnotationNotFoundException {
        Loader loader = clazz.getAnnotation(Loader.class);
        if (loader == null) {
            throw new AnnotationNotFoundException("loader");
        }
        Class<?> loaderClass = loader.value();
        try {
            AddonLoader<T> addonLoader = (AddonLoader<T>) loaderClass.newInstance();
            addonLoader.load(clazz);
            loaderMap.putIfAbsent(loaderClass, addonLoader);
            return addonLoader;
        } catch (Exception e) {
            throw new RuntimeException("can not create instance: " + loaderClass.getCanonicalName());
        }
    }
}
