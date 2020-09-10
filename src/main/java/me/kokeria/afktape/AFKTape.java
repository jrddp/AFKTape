package me.kokeria.afktape;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.options.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AFKTape implements ClientModInitializer {

    public static KeyBinding keyTape;

    @Override
    public void onInitializeClient() {

        keyTape = new KeyBinding("key.afktape.tapekeys",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "key.categories.misc");
        KeyBindingHelper.registerKeyBinding(keyTape);

    }

}
