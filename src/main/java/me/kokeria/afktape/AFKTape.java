package me.kokeria.afktape;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import org.lwjgl.glfw.GLFW;

public class AFKTape implements ClientModInitializer {

    public static KeyBinding keyTape;
    public static KeyBinding keyGrabMouse;

    @Override
    public void onInitializeClient() {

        keyTape = new KeyBinding("key.afktape.tapekeys",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "key.categories.afktape");
        KeyBindingHelper.registerKeyBinding(keyTape);

        keyGrabMouse = new KeyBinding("key.afktape.grabmouse",
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_LEFT_ALT,
                "key.categories.afktape");
        KeyBindingHelper.registerKeyBinding(keyGrabMouse);

    }

}
