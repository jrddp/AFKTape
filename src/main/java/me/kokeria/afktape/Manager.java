package me.kokeria.afktape;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.util.Formatting;

import java.util.*;

public class Manager {

    public static final Manager INSTANCE = new Manager();


    private boolean paused = false;
    public boolean wasPaused = false;
    private boolean running = false;
    public Set<KeyBinding> enabledKeys = new HashSet<>();

    public boolean isRunning() {
        return running && !paused;
    }

    public boolean isRunningIgnorePause() {
        return running;
    }

    public void enable(Set<KeyBinding> keys) {
        running = true;
        enabledKeys.addAll(keys);
        MinecraftClient.getInstance().mouse.unlockCursor();
    }

    public String[] getMessage() {

        String[] msg = new String[2];

        if (MinecraftClient.getInstance().player != null) {
            StringBuilder str = new StringBuilder("Taped down ");
            Iterator<KeyBinding> keyIterator = enabledKeys.iterator();
            while (keyIterator.hasNext()) {
                KeyBinding keyBinding = keyIterator.next();
                boolean isLast = !keyIterator.hasNext();
                if (isLast && enabledKeys.size() > 1) str.append("and ");
                str.append(Formatting.AQUA).append(keyBinding.getLocalizedName().toUpperCase());
                if (!isLast) str.append(Formatting.WHITE).append(", ");
            }

            msg[0] = str.toString();
            msg[1] = (Formatting.WHITE + "Press " + Formatting.RED + "ESCAPE" + Formatting.WHITE + " to exit");
            return msg;
        }

        return new String[0];

    }

    public void disable() {
        enabledKeys.forEach(key -> key.setPressed(false));
        enabledKeys.clear();
        running = false;
        unpause();
        wasPaused = false;
        if (MinecraftClient.getInstance().currentScreen == null) MinecraftClient.getInstance().mouse.lockCursor();
    }

    public void pause() {
        paused = true;
        wasPaused = true;
    }

    public void unpause() {
        paused = false;
    }

}
