/**
 * Copyright (c) 2014, TP-Link Co,Ltd.
 * Author: C_G <cg.fork@gmail.com>
 * Updated: 2015/10/26
 */
package org.cgfork.grass.common.addon.support;

import org.cgfork.grass.common.addon.Addon;
import org.cgfork.grass.common.addon.Loader;

@Addon(type = Addon.Type.Interface)
@Loader(org.cgfork.grass.common.addon.support.TestAddonLoader.class)
public interface TestAddon {
}
