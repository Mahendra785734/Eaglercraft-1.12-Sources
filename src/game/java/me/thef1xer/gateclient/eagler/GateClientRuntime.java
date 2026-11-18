package me.thef1xer.gateclient.eagler;

import net.minecraft.client.Minecraft;
import me.thef1xer.gateclient.eagler.module.GateModuleManager;

/**
 * Eaglercraft-native runtime bridge for the original GateClient port.
 * Forge's event bus is replaced by calls from Eaglercraft's client loops.
 */
public final class GateClientRuntime {
    private static final GateModuleManager MODULES = new GateModuleManager();

    private GateClientRuntime() {
    }

    public static GateModuleManager getModules() {
        return MODULES;
    }

    public static void onKey(Minecraft mc, int keyCode, boolean pressed) {
        if (!pressed) return;

        if (keyCode == 54 && mc.currentScreen == null) {
            mc.displayGuiScreen(new GateClickGui());
            return;
        }

        if (mc.currentScreen == null) {
            MODULES.onKey(mc, keyCode);
        }
    }

    public static void onTick(Minecraft mc) {
        if (mc == null || mc.world == null || mc.player == null) return;
        MODULES.onTick(mc);
    }
}
