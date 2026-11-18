package me.thef1xer.gateclient.eagler;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiScreen;

/**
 * Eaglercraft/TeaVM entry point for the GateClient 1.6 compatibility port.
 * The original Forge event bus is replaced by explicit calls from Minecraft.
 */
public final class GateClientPort {
    public static final int CLICK_GUI_KEY = 54; // Right Shift
    private static final List<GateModule> MODULES = new ArrayList<GateModule>();
    private static final Minecraft MC = Minecraft.getMinecraft();
    private static boolean initialized;
    private static GateModule bindingModule;
    private static float previousGamma = -1.0F;

    private GateClientPort() { }

    public static void init() {
        if (initialized) return;
        initialized = true;

        MODULES.add(new GateModule("Sprint", "MOVEMENT") {
            public void onTick() {
                if (MC.player != null && MC.player.movementInput != null
                        && MC.player.movementInput.field_192832_b > 0.8F) {
                    MC.player.setSprinting(true);
                }
            }
        });

        MODULES.add(new GateModule("FullBright", "RENDER") {
            public void onEnable() {
                previousGamma = MC.gameSettings.gammaSetting;
                MC.gameSettings.gammaSetting = 16.0F;
            }
            public void onDisable() {
                if (previousGamma >= 0.0F) MC.gameSettings.gammaSetting = previousGamma;
                previousGamma = -1.0F;
            }
        });

        MODULES.add(new GateModule("Coords", "HUD") { });
        MODULES.add(new GateModule("Watermark", "HUD") { });
        MODULES.add(new GateModule("ModuleList", "HUD") { });
    }

    public static List<GateModule> getModules() {
        init();
        return Collections.unmodifiableList(MODULES);
    }

    public static void onKeyEvent(int keyCode, boolean pressed) {
        init();
        if (!pressed || MC.world == null || MC.player == null) return;

        if (bindingModule != null) {
            bindingModule.setKeyCode(keyCode);
            bindingModule = null;
            return;
        }

        if (keyCode == CLICK_GUI_KEY && MC.currentScreen == null) {
            MC.displayGuiScreen(new GateClickGui());
            return;
        }

        if (MC.currentScreen != null) return;
        for (int i = 0; i < MODULES.size(); ++i) {
            GateModule module = MODULES.get(i);
            if (module.getKeyCode() != 0 && module.getKeyCode() == keyCode) {
                module.toggle();
            }
        }
    }

    public static void beginBinding(GateModule module) {
        bindingModule = module;
    }

    public static boolean isBinding(GateModule module) {
        return bindingModule == module;
    }

    public static void onClientTick() {
        init();
        if (MC.player == null || MC.world == null) return;
        for (int i = 0; i < MODULES.size(); ++i) {
            GateModule module = MODULES.get(i);
            if (module.isEnabled()) module.onTick();
        }
    }

    public static void renderHud() {
        init();
        if (MC.player == null || MC.world == null || MC.gameSettings.hideGUI) return;

        int y = 4;
        for (int i = 0; i < MODULES.size(); ++i) {
            GateModule module = MODULES.get(i);
            if (!module.isEnabled() || !"HUD".equals(module.getCategory())) continue;
            if ("Watermark".equals(module.getName())) {
                MC.fontRendererObj.drawStringWithShadow("GateClient 1.6", 4, y, 0xFFFFFF);
                y += 11;
            } else if ("Coords".equals(module.getName())) {
                String coords = "XYZ " + (int) MC.player.posX + " / "
                        + (int) MC.player.posY + " / " + (int) MC.player.posZ;
                MC.fontRendererObj.drawStringWithShadow(coords, 4, y, 0xFFFFFF);
                y += 11;
            }
        }

        int listY = 4;
        for (int i = 0; i < MODULES.size(); ++i) {
            GateModule module = MODULES.get(i);
            if (module.isEnabled() && "ModuleList".equals(module.getName())) {
                for (int j = 0; j < MODULES.size(); ++j) {
                    GateModule listed = MODULES.get(j);
                    if (listed.isEnabled()) {
                        String label = listed.getName();
                        int x = MC.scaledResolution.getScaledWidth()
                                - MC.fontRendererObj.getStringWidth(label) - 5;
                        MC.fontRendererObj.drawStringWithShadow(label, x, listY, 0x55FFFF);
                        listY += 10;
                    }
                }
                break;
            }
        }
    }
}
