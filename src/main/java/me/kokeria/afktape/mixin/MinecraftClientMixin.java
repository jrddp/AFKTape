package me.kokeria.afktape.mixin;

import jdk.internal.jline.internal.Nullable;
import me.kokeria.afktape.Tracker;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow
    @Nullable
    public Screen currentScreen;

    @Shadow
    @Final
    public GameOptions options;

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/MinecraftClient;openPauseMenu(Z)V", cancellable = true)
    private void testModifyOpenPauseMenu(CallbackInfo info) {

        if (Tracker.INSTANCE.isRunning()) info.cancel();

    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/MinecraftClient;openScreen(Lnet/minecraft/client/gui/screen/Screen;)V", cancellable = true)
    private void testModifyOpenScreen(Screen screen, CallbackInfo info) {

        if (screen == null && currentScreen != null) {
            Tracker.INSTANCE.unpause();
        } else if (currentScreen == null && screen != null) {
            Tracker.INSTANCE.pause();
        }

    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/MinecraftClient;handleInputEvents()V")
    private void testModifyHandleInputEvents(CallbackInfo info) {

        if (Tracker.INSTANCE.keyToggle.wasPressed()) {
            ArrayList<KeyBinding> pressedKeybinds = new ArrayList<>();
            for (KeyBinding keyBinding : options.keysAll) {
                if (keyBinding.isPressed()) {
                    pressedKeybinds.add(keyBinding);
                }
            }
            if (!pressedKeybinds.isEmpty())
                Tracker.INSTANCE.enable(pressedKeybinds);
        }

        if (Tracker.INSTANCE.isRunning()) {
            if (Tracker.INSTANCE.wasPaused) {
                Tracker.INSTANCE.enabledKeys.forEach(key -> KeyBinding.onKeyPressed(((KeyBindingMixin) key).getKeyCode()));
                Tracker.INSTANCE.wasPaused = false;
            }
            Tracker.INSTANCE.enabledKeys.forEach(key -> key.setPressed(true));
        }

    }


}
