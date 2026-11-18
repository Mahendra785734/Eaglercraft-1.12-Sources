# GateClient -> Eaglercraft 1.12.2 port notes

This branch is dedicated to porting the original GateClient source into this Eaglercraft source tree. It is not a replacement client.

## Confirmed source compatibility

- The Eaglercraft client has the vanilla 1.12.2 `GuiScreen` base class at `src/game/java/net/minecraft/client/gui/GuiScreen.java`.
- Eaglercraft `GuiScreen` already exposes the expected GUI lifecycle: `drawScreen`, `keyTyped`, mouse click/release handlers, and `doesGuiPauseGame`. It uses Eagler's `Keyboard` and `Mouse` classes.
- `Minecraft.runTick()` processes the current screen via `handleInput()` and `updateScreen()`.
- `Minecraft.runTickKeyboard()` consumes Eagler keyboard events, dispatches key presses, and forwards keyboard input to the current screen.
- `Minecraft.runGameLoop()` performs the game tick loop and world/HUD rendering.
- Original GateClient's ClickGUI extends the same `net.minecraft.client.gui.GuiScreen` class and has the expected panel structure: Combat, HUD, Movement, Player, Render, and Presets. Its default open key is Right Shift.

## Main incompatibility to remove

Original GateClient bootstraps through Forge `@Mod` lifecycle methods and registers `ModuleEventHandler` / `CommonEventHandler` on `MinecraftForge.EVENT_BUS`. Those Forge event annotations and event types are not a usable Eaglercraft runtime integration. Its event handler also relies on custom Forge events and mixin/coremod hooks for packet, movement, world, and render interception.

## Port strategy

1. Build an Eagler-native GateClient bootstrap and runtime dispatcher; do not load the Forge mod entrypoint.
2. Adapt the module base and key input to Eaglercraft's keyboard API.
3. Port the original ClickGUI and its component/settings classes onto Eaglercraft's existing GuiScreen API.
4. Wire client tick, key, HUD render, and world render dispatch at verified Eaglercraft lifecycle locations.
5. Port individual modules only where their required hooks exist; mark Forge/Mixin-dependent modules as unsupported until an equivalent hook is implemented.
6. Adapt config/preset persistence to the browser/Eagler filesystem layer.
7. Run the TeaVM build and fix compile/runtime errors before publishing any HTML.

## Important

This file records verified integration points and the implementation plan. It is not a completed port and does not claim that any module or build is working yet.
