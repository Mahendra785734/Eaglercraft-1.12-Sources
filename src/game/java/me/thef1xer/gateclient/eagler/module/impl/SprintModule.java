package me.thef1xer.gateclient.eagler.module.impl;

import me.thef1xer.gateclient.eagler.module.GateModule;
import net.minecraft.client.Minecraft;

/** Adaptation of GateClient's original Sprint module tick behavior. */
public final class SprintModule extends GateModule {
    public SprintModule() {
        super("Sprint", Category.MOVEMENT);
    }

    @Override
    public void onTick(Minecraft mc) {
        if (mc.player != null && mc.player.moveForward > 0.0F
                && !mc.player.collidedHorizontally && !mc.player.isSneaking()) {
            mc.player.setSprinting(true);
        }
    }
}
