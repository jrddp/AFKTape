package me.kokeria.afktape.mixin;

import jdk.internal.jline.internal.Nullable;
import me.kokeria.afktape.AFKTape;
import me.kokeria.afktape.Manager;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.screen.Screen;
import net.minecraft.client.network.ClientPlayerEntity;
import net.minecraft.client.options.GameOptions;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

@Mixin(MinecraftClient.class)
public abstract class MinecraftClientMixin {

    @Shadow
    @Nullable
    public Screen currentScreen;

    @Shadow
    @Final
    public GameOptions options;

    @Shadow @Nullable public ClientPlayerEntity player;

    // disable if running when player is dead or doesn't exist (in game menu, etc)
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/MinecraftClient;tick()V")
    private void tapeModifyTick(CallbackInfo info) {

        if (Manager.INSTANCE.isRunningIgnorePause() && (player == null || !player.isAlive())) Manager.INSTANCE.disable();

    }

    // cancel automatic game pause while running
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/MinecraftClient;openPauseMenu(Z)V", cancellable = true)
    private void tapeModifyOpenPauseMenu(CallbackInfo info) {

        if (Manager.INSTANCE.isRunning()) info.cancel();

    }

    // pause/unpause tape on open/close
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/MinecraftClient;openScreen(Lnet/minecraft/client/gui/screen/Screen;)V")
    private void tapeModifyOpenScreen(Screen screen, CallbackInfo info) {

        if (screen == null && currentScreen != null) {
            Manager.INSTANCE.unpause();
        } else if (currentScreen == null && screen != null) {
            Manager.INSTANCE.pause();
        }

    }

    // press keys if enabled
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/MinecraftClient;handleInputEvents()V")
    private void tapeModifyHandleInputEvents(CallbackInfo info) {

        if (AFKTape.keyTape.wasPressed()) {
            Set<KeyBinding> pressedKeybinds = new HashSet<>();
            for (KeyBinding keyBinding : options.keysAll) {
                if (keyBinding.isPressed()) {
                    if (keyBinding != AFKTape.keyTape)
                    pressedKeybinds.add(keyBinding);
                }
            }
            if (!pressedKeybinds.isEmpty())
                Manager.INSTANCE.enable(pressedKeybinds);
        }

        if (Manager.INSTANCE.isRunning()) {
            if (Manager.INSTANCE.wasPaused) {
                Manager.INSTANCE.enabledKeys.forEach(key -> KeyBinding.onKeyPressed(((KeyBindingAccessor) key).getKeyCode()));
                Manager.INSTANCE.wasPaused = false;
            }
            else {
                Manager.INSTANCE.enabledKeys.forEach(key -> key.setPressed(true));
            }
        }

    }


}
