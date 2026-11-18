package me.thef1xer.gateclient.eagler.module.impl;

import me.thef1xer.gateclient.eagler.module.GateModule;
import net.minecraft.client.Minecraft;

/** Gamma-based FullBright adaptation. */
public final class FullBrightModule extends GateModule {
    private float previousGamma = 1.0F;

    public FullBrightModule() {
        super("FullBright", Category.RENDER);
    }

    @Override
    public void onEnable(Minecraft mc) {
        previousGamma = mc.gameSettings.gammaSetting;
        mc.gameSettings.gammaSetting = 16.0F;
    }

    @Override
    public void onTick(Minecraft mc) {
        mc.gameSettings.gammaSetting = 16.0F;
    }

    @Override
    public void onDisable(Minecraft mc) {
        mc.gameSettings.gammaSetting = previousGamma;
    }
}
