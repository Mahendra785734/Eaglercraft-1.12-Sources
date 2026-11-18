package me.thef1xer.gateclient.eagler;

/**
 * Small Eaglercraft-native module base. This deliberately avoids Forge,
 * Mixin, reflection, and desktop-only APIs so it can be compiled by TeaVM.
 */
public abstract class GateModule {
    private final String name;
    private final String category;
    private boolean enabled;
    private int keyCode;

    protected GateModule(String name, String category) {
        this.name = name;
        this.category = category;
    }

    public final String getName() { return name; }
    public final String getCategory() { return category; }
    public final boolean isEnabled() { return enabled; }
    public final int getKeyCode() { return keyCode; }
    public final void setKeyCode(int keyCode) { this.keyCode = keyCode; }

    public final void toggle() {
        setEnabled(!enabled);
    }

    public final void setEnabled(boolean value) {
        if (enabled == value) return;
        enabled = value;
        if (enabled) onEnable();
        else onDisable();
    }

    public void onEnable() { }
    public void onDisable() { }
    public void onTick() { }
    public void onRenderHud() { }
}
