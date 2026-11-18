package me.thef1xer.gateclient.eagler.module;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import net.minecraft.client.Minecraft;
import me.thef1xer.gateclient.eagler.module.impl.FullBrightModule;
import me.thef1xer.gateclient.eagler.module.impl.SprintModule;

/** Owns the Eagler-compatible subset of GateClient modules. */
public final class GateModuleManager {
    private final List<GateModule> modules = new ArrayList<GateModule>();

    public GateModuleManager() {
        modules.add(new FullBrightModule());
        modules.add(new SprintModule());
    }

    public List<GateModule> getModules() {
        return Collections.unmodifiableList(modules);
    }

    public void onKey(Minecraft mc, int keyCode) {
        if (keyCode == 0) return;
        for (GateModule module : modules) {
            if (module.getKeyCode() == keyCode) module.toggle(mc);
        }
    }

    public void onTick(Minecraft mc) {
        for (GateModule module : modules) {
            if (module.isEnabled()) module.onTick(mc);
        }
    }
}
