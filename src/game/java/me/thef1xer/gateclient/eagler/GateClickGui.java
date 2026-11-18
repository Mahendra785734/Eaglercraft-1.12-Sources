package me.thef1xer.gateclient.eagler;

import java.io.IOException;
import java.util.List;

import net.minecraft.client.gui.GuiScreen;

/**
 * Lightweight first native ClickGUI pass. Left-click toggles a module;
 * right-click waits for a key press to bind it.
 */
public class GateClickGui extends GuiScreen {
    private static final String[] CATEGORIES = {
        "COMBAT", "HUD", "MOVEMENT", "PLAYER", "RENDER"
    };
    private int selectedCategory = 1;
    private int panelLeft;
    private int panelTop;

    public void initGui() {
        panelLeft = (this.width - 330) / 2;
        panelTop = (this.height - 230) / 2;
    }

    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();
        int left = panelLeft;
        int top = panelTop;
        drawRect(left, top, left + 330, top + 230, 0xE9161922);
        drawRect(left, top, left + 330, top + 27, 0xFF242B3A);
        this.fontRendererObj.drawStringWithShadow("GateClient 1.6", left + 10, top + 9, 0xFFFFFFFF);

        int tabX = left + 8;
        for (int i = 0; i < CATEGORIES.length; ++i) {
            int color = i == selectedCategory ? 0xFF39B7C9 : 0xFF303746;
            drawRect(tabX, top + 36, tabX + 61, top + 56, color);
            this.fontRendererObj.drawString(CATEGORIES[i], tabX + 4, top + 42, 0xFFFFFFFF);
            tabX += 64;
        }

        List<GateModule> modules = GateClientPort.getModules();
        int rowY = top + 68;
        for (int i = 0; i < modules.size(); ++i) {
            GateModule module = modules.get(i);
            if (!CATEGORIES[selectedCategory].equals(module.getCategory())) continue;
            int color = module.isEnabled() ? 0xFF244D4B : 0xFF292F3B;
            drawRect(left + 9, rowY, left + 321, rowY + 22, color);
            this.fontRendererObj.drawStringWithShadow(module.getName(), left + 17, rowY + 7, 0xFFFFFFFF);
            String bind = GateClientPort.isBinding(module) ? "Press a key..."
                    : module.getKeyCode() == 0 ? "NONE" : "KEY " + module.getKeyCode();
            this.fontRendererObj.drawStringWithShadow(bind, left + 245, rowY + 7, 0xFFB8C5D8);
            rowY += 25;
        }

        this.fontRendererObj.drawStringWithShadow("Left click: toggle   Right click: bind key",
                left + 10, top + 211, 0xFFB8C5D8);
        super.drawScreen(mouseX, mouseY, partialTicks);
    }

    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        int left = panelLeft;
        int top = panelTop;
        if (mouseY >= top + 36 && mouseY <= top + 56) {
            int tab = (mouseX - (left + 8)) / 64;
            if (tab >= 0 && tab < CATEGORIES.length) selectedCategory = tab;
            return;
        }

        List<GateModule> modules = GateClientPort.getModules();
        int rowY = top + 68;
        for (int i = 0; i < modules.size(); ++i) {
            GateModule module = modules.get(i);
            if (!CATEGORIES[selectedCategory].equals(module.getCategory())) continue;
            if (mouseX >= left + 9 && mouseX <= left + 321
                    && mouseY >= rowY && mouseY <= rowY + 22) {
                if (mouseButton == 1) GateClientPort.beginBinding(module);
                else module.toggle();
                return;
            }
            rowY += 25;
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    public boolean doesGuiPauseGame() {\n        return false;\n    }\n\n    protected void keyTyped(char typedChar, int keyCode) throws IOException {
        if (keyCode == 1) {
            GateClientPort.beginBinding(null);
            this.mc.displayGuiScreen(null);
            return;
        }
        super.keyTyped(typedChar, keyCode);
    }
}
