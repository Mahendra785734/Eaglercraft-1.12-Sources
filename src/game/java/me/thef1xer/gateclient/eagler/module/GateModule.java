package me.thef1xer.gateclient.eagler.module;

import net.minecraft.client.Minecraft;

/** Eagler-native module base; deliberately independent of Forge. */
public abstract class GateModule {
    public enum Category {
        COMBAT("COMBAT"), HUD("HUD"), MOVEMENT("MOVEMENT"), PLAYER("PLAYER"), RENDER("RENDER");
        private final String displayName;
        Category(String displayName) { this.displayName = displayName; }
        public String getDisplayName() { return displayName; }
    }

    private final String name;
    private final Category category;
    private boolean enabled;
    private int keyCode;

    protected GateModule(String name, Category category) {
        this.name = name;
        this.category = category;
    }

    public final String getName() { return name; }
    public final Category getCategory() { return category; }
    public final boolean isEnabled() { return enabled; }
    public final int getKeyCode() { return keyCode; }
    public final void setKeyCode(int keyCode) { this.keyCode = keyCode; }

    public final void toggle(Minecraft mc) { setEnabled(mc, !enabled); }

    public final void setEnabled(Minecraft mc, boolean enabled) {
        if (this.enabled == enabled) return;
        this.enabled = enabled;
        if (enabled) onEnable(mc); else onDisable(mc);
    }

    public void onEnable(Minecraft mc) { }
    public void onDisable(Minecraft mc) { }
    public void onTick(Minecraft mc) { }
}
