package me.thef1xer.gateclient.eagler;

import net.minecraft.client.Minecraft;

/**
 * Eaglercraft-native runtime bridge for the original GateClient port.
 *
 * Forge's event bus is intentionally not used here: Eaglercraft invokes this
 * bridge from its own client tick and keyboard loop.
 */
public final class GateClientRuntime {
    private static boolean guiOpenRequested;
    private static boolean sprintEnabled;
    private static boolean fullBrightEnabled;
    private static float previousGamma = 1.0F;

    private GateClientRuntime() {
    }

    public static void onKey(Minecraft mc, int keyCode, boolean pressed) {
        if (pressed && keyCode == 54 && mc.currentScreen == null) {
            mc.displayGuiScreen(new GateClickGui());
        }
    }

    public static void onTick(Minecraft mc) {
        if (mc == null || mc.world == null || mc.player == null) {
            return;
        }

        if (sprintEnabled && mc.player.moveForward > 0.0F
                && !mc.player.collidedHorizontally && !mc.player.isSneaking()) {
            mc.player.setSprinting(true);
        }

        if (fullBrightEnabled) {
            mc.gameSettings.gammaSetting = 16.0F;
        }
    }

    public static boolean isSprintEnabled() {
        return sprintEnabled;
    }

    public static void toggleSprint() {
        sprintEnabled = !sprintEnabled;
    }

    public static boolean isFullBrightEnabled() {
        return fullBrightEnabled;
    }

    public static void toggleFullBright(Minecraft mc) {
        if (!fullBrightEnabled) {
            previousGamma = mc.gameSettings.gammaSetting;
            fullBrightEnabled = true;
            mc.gameSettings.gammaSetting = 16.0F;
        } else {
            fullBrightEnabled = false;
            mc.gameSettings.gammaSetting = previousGamma;
        }
    }
}
