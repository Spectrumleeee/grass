/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-13
 */
package org.cgfork.grass.common.addon;

import java.util.ServiceLoader;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.cgfork.grass.common.addon.Addon.Type;

/**
 * 
 */
public abstract class AbstractLoader<T> implements AddonLoader<T> {
    
    private static final ConcurrentMap<Class<?>, AddonLoader<?>> loaderMap = new ConcurrentHashMap<>();
    
    private final ConcurrentMap<Class<?>, Holder> addonMap = new ConcurrentHashMap<>();
    
    private final ConcurrentMap<String, Class<?>> classMap = new ConcurrentHashMap<>();
    
    public AbstractLoader() {}
    
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

    @Override
    public AddonLoader<T> load(Class<T> clazz) {
        ServiceLoader<T> loader = ServiceLoader.load(clazz);
        
        for (T value : loader) {
            if (checkAnnotations(value.getClass())) {
                this.addon(value);
            }
        }
        return this;
    }

    @Override
    public T getAddon(Class<?> clazz) {
        return getAddon0(clazz);
    }

    @Override
    public T getAddon(String name){
        Class<?> clazz = classMap.get(name);
        if (clazz == null) {
            return null;
        }
        return getAddon0(clazz);
    }
    
    private T getAddon0(Class<?> clazz) {
        Holder holder = addonMap.get(clazz);
        if (holder == null) {
            return null;
        }
        return holder.value;
    }

    @Override
    public T addon(T value) {
        Holder holder = addonMap.putIfAbsent(value.getClass(), new Holder(value));
        if (holder == null) {
            return null;
        }
        return holder.value;
    }
    
    private boolean checkAnnotations(Class<?> clazz) {
        Addon addon = clazz.getAnnotation(Addon.class);
        
        if (addon != null && Type.Interface.equals(addon.type())) {
            return false;
        }
        
        String name = clazz.getCanonicalName();
        if (addon != null && !("".equals(addon.value()))) {
            name = addon.value();
        }
        classMap.putIfAbsent(name, clazz);
        return true;
    }
    
    class Holder {
        T value;
        
        Holder() {}
        
        Holder(T value) {
            this.value = value;
        }
    }
    
    public static class AnnotationNotFoundException extends Exception {
        private static final long serialVersionUID = 1L;
        
        public AnnotationNotFoundException(String message) {
            super(message);
        }
    }
}
