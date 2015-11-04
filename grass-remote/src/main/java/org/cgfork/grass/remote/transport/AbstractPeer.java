package org.cgfork.grass.remote.transport;

import org.cgfork.grass.common.addon.AddonLoader;
import org.cgfork.grass.common.addon.support.AddonLoaders;
import org.cgfork.grass.common.check.Checker;
import org.cgfork.grass.remote.ChannelHandler;
import org.cgfork.grass.remote.Codec;
import org.cgfork.grass.remote.RemoteLocator;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class AbstractPeer {

    private volatile RemoteLocator locator;

    private final ChannelHandler handler;

    private final Codec codec;

    public AbstractPeer(RemoteLocator locator, ChannelHandler handler) {
        Checker.Arg.notNull(locator, "locator is null");
        Checker.Arg.notNull(handler, "handler is null");
        this.handler = handler;
        this.locator = locator;
        this.codec = getCodec(locator);
    }

    public void setLocator(RemoteLocator locator) {
        Checker.Arg.notNull(locator, "locator is null");
        Checker.Arg.notNull(locator, "locator is null");
        this.locator = locator;
    }

    public Codec getCodec() {
        return codec;
    }

    public RemoteLocator remoteLocator() {
        return locator;
    }

    public ChannelHandler channelHandler() {
        return handler;
    }

    protected static Codec getCodec(RemoteLocator locator) {
        if (locator == null) {
            throw new IllegalArgumentException("locator is null");
        }

        AddonLoader<Codec> loader = null;
        try {
            loader = AddonLoaders.getOrNewAddonLoader(Codec.class);
        } catch (Exception e) {
            // TODO:
        }
        if (loader == null) {
            // TODO: throw not found Exception
            throw new NullPointerException();
        }

        String codecClass = locator.getParameter("codecClass");
        if (codecClass != null) {
            try {
                Class<?> clazz = Class.forName(codecClass);
                return loader.getAddon(clazz);
            } catch (ClassNotFoundException e) {
                // TODO: ignore
            }
        }

        String codec = locator.getParameter("codec");
        if (codec == null) {
            return null;
        }

        return loader.getAddon(codec);
    }
}
