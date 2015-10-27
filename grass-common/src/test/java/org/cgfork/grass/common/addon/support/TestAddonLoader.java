package org.cgfork.grass.common.addon.support;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 */
public class TestAddonLoader extends SPIAddonLoader<TestAddon> {
    @Override
    public Class<TestAddon> getInterface() {
        return TestAddon.class;
    }
}
