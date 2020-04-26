package net.fabricmc.example;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.text.LiteralText;

import java.util.ArrayList;
import java.util.List;

public class Tracker {

    public static final Tracker INSTANCE = new Tracker();

    static final int keyK = 75;

    // todo add KeyBinding to keyBinding menu
    public KeyBinding keyToggle = new KeyBinding("key.afkhelperToggle", keyK, "key.catagories.gameplay");
    private boolean paused = false;
    public boolean wasPaused = false;
    private boolean running = false;
    public List<KeyBinding> enabledKeys = new ArrayList<>();

    public boolean isRunning() {
        return running && !paused;
    }

    // todo add a clear HUD that shows what keys are enabled and tells the user how to escape
    public void enable(List<KeyBinding> keys) {
        running = true;
        enabledKeys.addAll(keys);
        MinecraftClient.getInstance().mouse.unlockCursor();
        if (MinecraftClient.getInstance().player != null) {
            StringBuilder str = new StringBuilder("AfkHelper enabled with ");
            enabledKeys.forEach(key -> str.append(key.getLocalizedName()).append(", "));
            MinecraftClient.getInstance().player.addChatMessage(new LiteralText(str.toString()), true);
        }
    }

    public void disable() {
        enabledKeys.forEach(key -> key.setPressed(false));
        enabledKeys.clear();
        running = false;
        if (MinecraftClient.getInstance().currentScreen == null) MinecraftClient.getInstance().mouse.lockCursor();
        if (MinecraftClient.getInstance().player != null)
            MinecraftClient.getInstance().player.addChatMessage(new LiteralText("AfkHelper disabled."), true);
    }

    public void pause() {
        paused = true;
        wasPaused = true;
    }

    public void unpause() {
        paused = false;
    }

}
