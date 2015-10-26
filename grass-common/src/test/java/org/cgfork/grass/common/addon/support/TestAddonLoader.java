/**
 * Copyright (c) 2014, TP-Link Co,Ltd.
 * Author: C_G <cg.fork@gmail.com>
 * Updated: 2015/10/26
 */
package org.cgfork.grass.common.addon.support;

public class TestAddonLoader extends SPIAddonLoader<TestAddon> {
    @Override
    public Class<TestAddon> getInterface() {
        return TestAddon.class;
    }
}
