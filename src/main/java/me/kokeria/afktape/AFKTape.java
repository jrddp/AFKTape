package me.kokeria.afktape;

import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.keybinding.FabricKeyBinding;
import net.fabricmc.fabric.api.client.keybinding.KeyBindingRegistry;
import net.minecraft.client.util.InputUtil;
import net.minecraft.util.Identifier;
import org.lwjgl.glfw.GLFW;

public class AFKTape implements ClientModInitializer {

    public static FabricKeyBinding keyTape;

    @Override
    public void onInitializeClient() {

        keyTape = FabricKeyBinding.Builder.create(new Identifier("afktape", "tapekeys"),
                InputUtil.Type.KEYSYM,
                GLFW.GLFW_KEY_K,
                "key.categories.misc").build();
        KeyBindingRegistry.INSTANCE.register(keyTape);

    }

}
