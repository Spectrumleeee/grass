package org.cgfork.grass.common.addon.support;

import org.cgfork.grass.common.addon.Addon;
import org.cgfork.grass.common.addon.Loader;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 * Updated: 2015/10/27
 */
@Addon(type = Addon.Type.Interface)
@Loader(org.cgfork.grass.common.addon.support.TestAddonLoader.class)
public interface TestAddon {
}
