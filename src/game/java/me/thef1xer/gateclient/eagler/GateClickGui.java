package me.thef1xer.gateclient.eagler;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;
import me.thef1xer.gateclient.eagler.module.GateModule;
import me.thef1xer.gateclient.eagler.module.GateModule.Category;

/**
 * Eagler-native adaptation of GateClient's five-column category ClickGUI.
 * Left click toggles a module; right click enters keybind capture.
 */
public class GateClickGui extends GuiScreen {
    private static final Category[] CATEGORIES = Category.values();
    private static final int PANEL_WIDTH = 112;
    private static final int PANEL_GAP = 7;
    private static final int HEADER_HEIGHT = 19;
    private static final int ROW_HEIGHT = 18;
    private GateModule listeningForKey;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        int totalWidth = CATEGORIES.length * PANEL_WIDTH + (CATEGORIES.length - 1) * PANEL_GAP;
        int startX = Math.max(8, (this.width - totalWidth) / 2);
        int top = Math.max(28, this.height / 4);

        drawRect(startX - 8, top - 29, startX + totalWidth + 8, top + 112, 0xD9101118);
        fontRendererObj.drawStringWithShadow("GATECLIENT", startX, top - 20, 0xFFFFFFFF);
        fontRendererObj.drawStringWithShadow("Eaglercraft 1.12.2 port", startX + 76, top - 19, 0xFF9B9BA8);

        List<GateModule> modules = GateClientRuntime.getModules().getModules();
        for (int i = 0; i < CATEGORIES.length; i++) {
            int x = startX + i * (PANEL_WIDTH + PANEL_GAP);
            int y = top + HEADER_HEIGHT;
            drawRect(x, top, x + PANEL_WIDTH, top + HEADER_HEIGHT, 0xFF315FCB);
            fontRendererObj.drawStringWithShadow(CATEGORIES[i].getDisplayName(), x + 5, top + 5, 0xFFFFFFFF);
            drawRect(x, y, x + PANEL_WIDTH, top + 103, 0xC9000000);

            int row = 0;
            for (GateModule module : modules) {
                if (module.getCategory() != CATEGORIES[i]) continue;
                drawModule(x, y + row * ROW_HEIGHT, module, mouseX, mouseY);
                row++;
            }
            if (row == 0) {
                fontRendererObj.drawStringWithShadow("Port pending", x + 5, y + 6, 0xFF777780);
            }
        }

        fontRendererObj.drawStringWithShadow(
                listeningForKey == null ? "Left click: toggle  |  Right click: set keybind  |  ESC: close"
                        : "Press a key for " + listeningForKey.getName() + " (ESC cancels)",
                startX, top + 110, 0xFFAAAAAA);
    }

    private void drawModule(int x, int y, GateModule module, int mouseX, int mouseY) {
        boolean hovered = mouseX >= x && mouseX < x + PANEL_WIDTH
                && mouseY >= y && mouseY < y + ROW_HEIGHT;
        int background = module.isEnabled() ? 0xFFB85C13 : (hovered ? 0xFF34343C : 0xA9000000);
        drawRect(x, y, x + PANEL_WIDTH, y + ROW_HEIGHT, background);
        fontRendererObj.drawStringWithShadow(module.getName(), x + 5, y + 5,
                module.isEnabled() ? 0xFFFFFFFF : 0xFFB8B8C0);
        String state = module.getKeyCode() == 0 ? "NONE" : Integer.toString(module.getKeyCode());
        fontRendererObj.drawStringWithShadow(state,
                x + PANEL_WIDTH - fontRendererObj.getStringWidth(state) - 5, y + 5,
                0xFF9999A5);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int totalWidth = CATEGORIES.length * PANEL_WIDTH + (CATEGORIES.length - 1) * PANEL_GAP;
        int startX = Math.max(8, (this.width - totalWidth) / 2);
        int top = Math.max(28, this.height / 4);
        int startY = top + HEADER_HEIGHT;

        for (int i = 0; i < CATEGORIES.length; i++) {
            int x = startX + i * (PANEL_WIDTH + PANEL_GAP);
            int row = 0;
            for (GateModule module : GateClientRuntime.getModules().getModules()) {
                if (module.getCategory() != CATEGORIES[i]) continue;
                int y = startY + row * ROW_HEIGHT;
                if (mouseX >= x && mouseX < x + PANEL_WIDTH
                        && mouseY >= y && mouseY < y + ROW_HEIGHT) {
                    if (mouseButton == 0) {
                        module.toggle(this.mc);
                        listeningForKey = null;
                    } else if (mouseButton == 1) {
                        listeningForKey = module;
                    }
                    return;
                }
                row++;
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (listeningForKey != null) {
            if (keyCode != 1) listeningForKey.setKeyCode(keyCode);
            listeningForKey = null;
            return;
        }
        if (keyCode == 1 || keyCode == 54) {
            this.mc.displayGuiScreen(null);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }

    @Override
    public boolean doesGuiPauseGame() {
        return false;
    }
}
