package me.thef1xer.gateclient.eagler;

import java.io.IOException;

import net.minecraft.client.gui.GuiScreen;

/**
 * First Eagler-native adaptation of GateClient's category-panel ClickGUI.
 * It uses the game's existing GuiScreen input and rendering pipeline.
 */
public class GateClickGui extends GuiScreen {
    private static final String[] CATEGORIES = { "COMBAT", "HUD", "MOVEMENT", "PLAYER", "RENDER" };
    private static final int PANEL_WIDTH = 112;
    private static final int PANEL_GAP = 7;
    private static final int HEADER_HEIGHT = 19;
    private static final int ROW_HEIGHT = 18;

    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks) {
        drawDefaultBackground();

        int totalWidth = CATEGORIES.length * PANEL_WIDTH + (CATEGORIES.length - 1) * PANEL_GAP;
        int startX = Math.max(8, (this.width - totalWidth) / 2);
        int top = Math.max(28, this.height / 4);

        drawRect(startX - 8, top - 29, startX + totalWidth + 8, top + 102, 0xD9101118);
        fontRendererObj.drawStringWithShadow("GATECLIENT", startX, top - 20, 0xFFFFFFFF);
        fontRendererObj.drawStringWithShadow("Eaglercraft 1.12.2 port", startX + 76, top - 19, 0xFF9B9BA8);

        for (int i = 0; i < CATEGORIES.length; i++) {
            int x = startX + i * (PANEL_WIDTH + PANEL_GAP);
            drawRect(x, top, x + PANEL_WIDTH, top + HEADER_HEIGHT, 0xFF315FCB);
            fontRendererObj.drawStringWithShadow(CATEGORIES[i], x + 5, top + 5, 0xFFFFFFFF);
            drawRect(x, top + HEADER_HEIGHT, x + PANEL_WIDTH, top + 102, 0xC9000000);
        }

        drawModule(startX + 1 * (PANEL_WIDTH + PANEL_GAP), top + HEADER_HEIGHT, "FullBright",
                GateClientRuntime.isFullBrightEnabled(), mouseX, mouseY);
        drawModule(startX + 2 * (PANEL_WIDTH + PANEL_GAP), top + HEADER_HEIGHT, "Sprint",
                GateClientRuntime.isSprintEnabled(), mouseX, mouseY);

        fontRendererObj.drawStringWithShadow("ESC to close", startX, top + 110, 0xFFAAAAAA);
        fontRendererObj.drawStringWithShadow("Original GateClient modules are being adapted individually.",
                startX, top + 123, 0xFF888888);
    }

    private void drawModule(int x, int y, String name, boolean enabled, int mouseX, int mouseY) {
        boolean hovered = mouseX >= x && mouseX < x + PANEL_WIDTH
                && mouseY >= y && mouseY < y + ROW_HEIGHT;
        int background = enabled ? 0xFFB85C13 : (hovered ? 0xFF34343C : 0xA9000000);
        drawRect(x, y, x + PANEL_WIDTH, y + ROW_HEIGHT, background);
        fontRendererObj.drawStringWithShadow(name, x + 5, y + 5,
                enabled ? 0xFFFFFFFF : 0xFFB8B8C0);
        String state = enabled ? "ON" : "OFF";
        fontRendererObj.drawStringWithShadow(state,
                x + PANEL_WIDTH - fontRendererObj.getStringWidth(state) - 5, y + 5,
                enabled ? 0xFFFFFFFF : 0xFF777780);
    }

    @Override
    protected void mouseClicked(int mouseX, int mouseY, int mouseButton) throws IOException {
        if (mouseButton == 0) {
            int totalWidth = CATEGORIES.length * PANEL_WIDTH + (CATEGORIES.length - 1) * PANEL_GAP;
            int startX = Math.max(8, (this.width - totalWidth) / 2);
            int top = Math.max(28, this.height / 4);
            int rowY = top + HEADER_HEIGHT;
            int hudX = startX + PANEL_WIDTH + PANEL_GAP;
            int movementX = startX + 2 * (PANEL_WIDTH + PANEL_GAP);

            if (inside(mouseX, mouseY, hudX, rowY)) {
                GateClientRuntime.toggleFullBright(this.mc);
            } else if (inside(mouseX, mouseY, movementX, rowY)) {
                GateClientRuntime.toggleSprint();
            }
        }
        super.mouseClicked(mouseX, mouseY, mouseButton);
    }

    private boolean inside(int mouseX, int mouseY, int x, int y) {
        return mouseX >= x && mouseX < x + PANEL_WIDTH
                && mouseY >= y && mouseY < y + ROW_HEIGHT;
    }

    @Override
    protected void keyTyped(char typedChar, int keyCode) throws IOException {
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
