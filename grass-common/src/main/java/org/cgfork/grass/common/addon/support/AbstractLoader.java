/**
 * Author:  chenbiren <cg.fork@gmail.com>
 * Created: 2015-10-13
 */
package org.cgfork.grass.common.addon.support;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

import org.cgfork.grass.common.addon.Addon;
import org.cgfork.grass.common.addon.AddonLoader;
import org.cgfork.grass.common.addon.Addon.Type;

public abstract class AbstractLoader<T> implements AddonLoader<T> {

    private final ConcurrentMap<Class<?>, Holder> addonMap = new ConcurrentHashMap<>();
    
    private final ConcurrentMap<String, Class<?>> classMap = new ConcurrentHashMap<>();
    
    public AbstractLoader() {}

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
    
    protected boolean checkAnnotations(Class<?> clazz) {
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
