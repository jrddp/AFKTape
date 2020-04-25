package net.fabricmc.example;

import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;

import java.util.ArrayList;
import java.util.List;

public class Tracker {

    public static final Tracker INSTANCE = new Tracker();

    static final int keyK = 75;

    public KeyBinding keyToggle = new KeyBinding("key.afkhelperToggle", keyK, "key.catagories.gameplay");
    private boolean running = false;
    public List<KeyBinding> enabledKeys = new ArrayList<KeyBinding>();

    public boolean onKeyPressed(int key, int scancode, int action, int modifiers) {
        for (KeyBinding keyBinding : MinecraftClient.getInstance().options.keysAll) {
            if (keyBinding.isPressed()) {
                enabledKeys.add(keyBinding);
            }
        }
        return false;
    }

    public boolean isRunning() {
        return running;
    }

    public void enable(List<KeyBinding> keys) {
        running = true;
        enabledKeys.addAll(keys);
    }

    public void disable() {
        enabledKeys.clear();
        running = false;
    }

}
