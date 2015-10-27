package org.cgfork.grass.common.addon.support;

/**
 * @author C_G <cg.fork@gmail.com>
 * @version 1.0
 * Updated: 2015/10/27
 */
public class TestAddonLoader extends SPIAddonLoader<TestAddon> {
    @Override
    public Class<TestAddon> getInterface() {
        return TestAddon.class;
    }
}
