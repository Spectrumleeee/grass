package org.cgfork.grass.common.addon.support;

import org.cgfork.grass.common.UnsupportedException;
import org.cgfork.grass.common.addon.AddonLoader;
import org.cgfork.grass.common.addon.Loader;
import org.cgfork.grass.common.addon.support.AbstractLoader.AnnotationNotFoundException;

import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class AddonLoaders {

    private static final String LOADER_CONFIG_HOME = "/META-INF/configs/";

    private static AddonLoaders instance = null;
    
    private final ConcurrentMap<Class<?>, AddonLoader<?>> loaderMap = new ConcurrentHashMap<>();

    private final ClassLoader classLoader;

    public AddonLoaders() {
        this(Thread.currentThread().getContextClassLoader());
    }

    public AddonLoaders(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    @SuppressWarnings("unchecked")
    public <T> AddonLoader<T> getAddonLoader(Class<T> clazz) throws AnnotationNotFoundException {
        AddonLoader<?> addonLoader = loaderMap.get(clazz);
        if (addonLoader != null) {
            return (AddonLoader<T>) addonLoader;
        }

        return createAddonLoader(clazz);
    }

    @SuppressWarnings("unchecked")
    public <T> AddonLoader<T> createAddonLoader(Class<T> clazz) throws AnnotationNotFoundException {
        Loader loader = clazz.getAnnotation(Loader.class);
        if (loader == null) {
            throw new AnnotationNotFoundException("loader");
        }

        Class<?> loaderClass = loader.value();
        Loader.Config config = loader.config();

        try {
            AddonLoader<T> newLoader = null;

            switch (config) {
                case NO_CONFIG:
                    newLoader = (AddonLoader<T>) loaderClass.newInstance();
                    newLoader = newLoader.load(clazz, classLoader);
                    break;
                case XML:
                    throw new UnsupportedException();
                case JSON:
                    throw new UnsupportedException();
            }

            if (newLoader == null) {
                throw new NullPointerException();
            }
            loaderMap.putIfAbsent(loaderClass, newLoader);
            return newLoader;
        } catch (Exception e) {
            throw new RuntimeException("can not create instance: " + loaderClass.getCanonicalName());
        }
    }

    public static <T> AddonLoader<T> getOrNewAddonLoader(Class<T> clazz) throws AnnotationNotFoundException {
        return getInstance().getAddonLoader(clazz);
    }

    private static AddonLoaders getInstance() {
        if (instance == null) {
            synchronized (AddonLoaders.class) {
                if (instance == null) {
                    instance = new AddonLoaders();
                }
            }
        }
        return instance;
    }
}
