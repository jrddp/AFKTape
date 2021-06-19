package me.kokeria.afktape;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.util.Formatting;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

import static net.minecraft.util.Formatting.RED;
import static net.minecraft.util.Formatting.WHITE;

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
                str.append(Formatting.AQUA).append(keyBinding.getBoundKeyLocalizedText().shallowCopy().getString().toUpperCase());
                if (!isLast) str.append(WHITE).append(", ");
            }

            msg[0] = str.toString();
            msg[1] = (WHITE + "Press " + RED + "ESCAPE" + WHITE + " to exit");
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
