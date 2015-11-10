package org.cgfork.grass.rpc.serialize;

import org.cgfork.grass.common.NotFoundException;
import org.cgfork.grass.common.addon.AddonLoader;
import org.cgfork.grass.common.addon.support.AddonLoaders;
import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.remote.Channel;
import org.cgfork.grass.remote.Location;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class Serializers {
    public static Serializer loadSerializer(Location location) throws NotFoundException {
        Checker.Arg.notNull(location);
        AddonLoader<Serializer> loader;
        try {
            loader = AddonLoaders.getOrNewAddonLoader(Serializer.class);
        } catch (Exception e) {
            throw new NotFoundException("Failed to load serializer", e);
        }

        if (loader == null) {
            throw new NotFoundException("Not found serializer loader");
        }

        String serializerClass = location.getParameter("serializerClass");
        if (serializerClass != null) {
            try {
                Class<?> clazz = Class.forName(serializerClass);
                return loader.getAddon(clazz);
            } catch (ClassNotFoundException e) {
                throw new NotFoundException("Not found serializer loader class", e);
            }
        }

        String serializer = location.getParameter("serializer");
        if (serializer == null) {
            throw new NotFoundException("Not found serializer loader");
        }

        return loader.getAddon(serializer);
    }

    public static Serializer loadSerializer(Channel channel) throws NotFoundException {
        Checker.Arg.notNull(channel);
        return loadSerializer(channel.location());
    }
}
