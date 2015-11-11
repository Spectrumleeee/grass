package org.cgfork.grass.remote.transport;

import org.cgfork.grass.common.NotFoundException;
import org.cgfork.grass.common.addon.AddonLoader;
import org.cgfork.grass.common.addon.support.AddonLoaders;
import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.Codec;
import org.cgfork.grass.remote.Location;

/**
 * @author C_G (cg.fork@gmail.com)
 * @version 1.0
 */
public class AbstractPeer {

    private final Location location;

    private final ChannelHandler handler;

    private final Codec codec;

    public AbstractPeer(Location location, ChannelHandler handler) {
        Checker.Arg.notNull(location, "locator is null");
        Checker.Arg.notNull(handler, "handler is null");
        this.handler = handler;
        this.location = location;
        this.codec = getCodec(location);
    }

    public Codec codec() {
        return codec;
    }

    public Location location() {
        return location;
    }

    public ChannelHandler channelHandler() {
        return handler;
    }

    protected static Codec getCodec(Location location) {
        if (location == null) {
            throw new IllegalArgumentException("locator is null");
        }

        AddonLoader<Codec> loader;
        try {
            loader = AddonLoaders.getOrNewAddonLoader(Codec.class);
        } catch (Exception e) {
            throw new NotFoundException("Not found codec loader", e);
        }
        if (loader == null) {
            throw new NotFoundException("Not found codec loader");
        }

        String codecClass = location.getParameter("codecClass");
        if (codecClass != null) {
            try {
                Class<?> clazz = Class.forName(codecClass);
                return loader.getAddon(clazz);
            } catch (ClassNotFoundException e) {
                throw new NotFoundException("Not found codec class", e);
            }
        }

        String codec = location.getParameter("codec");
        if (codec == null) {
            throw new NotFoundException("Not found codec");
        }

        return loader.getAddon(codec);
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        AbstractPeer that = (AbstractPeer) o;

        return !(codec != null ? !codec.equals(that.codec) : that.codec != null) &&
                !(handler != null ? !handler.equals(that.handler) : that.handler != null) &&
                !(location != null ? !location.equals(that.location) : that.location != null);

    }

    @Override
    public int hashCode() {
        int result = location != null ? location.hashCode() : 0;
        result = 31 * result + (handler != null ? handler.hashCode() : 0);
        result = 31 * result + (codec != null ? codec.hashCode() : 0);
        return result;
    }
}
