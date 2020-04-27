package me.kokeria.afktape;

import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.util.Formatting;

import java.util.ArrayList;
import java.util.List;

public class AFKTape {

    //todo add logo and website
    //FIXME cancel on death or kick

    public static final AFKTape INSTANCE = new AFKTape();

    static final int keyK = 75;

    // todo add KeyBinding to keyBinding menu
    public KeyBinding keyToggle = new KeyBinding("key.tapekeys", keyK, "key.catagories.gameplay");
    private boolean paused = false;
    public boolean wasPaused = false;
    private boolean running = false;
    public List<KeyBinding> enabledKeys = new ArrayList<>();

    public boolean isRunning() {
        return running && !paused;
    }

    public boolean isRunningIgnorePause() {
        return running;
    }

    public void enable(List<KeyBinding> keys) {
        running = true;
        enabledKeys.addAll(keys);
        MinecraftClient.getInstance().mouse.unlockCursor();
    }

    public String[] getMessage() {

        String[] msg = new String[2];

        if (MinecraftClient.getInstance().player != null) {
            StringBuilder str = new StringBuilder("Taped down ");
            for (int i = 0; i < enabledKeys.size(); i++) {
                if (i == enabledKeys.size() - 1 && i != 0) str.append("and ");
                str.append(Formatting.AQUA).append(enabledKeys.get(i).getLocalizedName().toUpperCase());
                if (i < enabledKeys.size() - 1) str.append(Formatting.WHITE).append(", ");
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
