package me.kokeria.afktape.mixin;

import jdk.internal.jline.internal.Nullable;
import me.kokeria.afktape.AFKTape;
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

        if (AFKTape.INSTANCE.isRunningIgnorePause() && (player == null || !player.isAlive())) AFKTape.INSTANCE.disable();

    }

    // cancel automatic game pause while running
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/MinecraftClient;openPauseMenu(Z)V", cancellable = true)
    private void testModifyOpenPauseMenu(CallbackInfo info) {

        if (AFKTape.INSTANCE.isRunning()) info.cancel();

    }

    // pause/unpause tape on open/close
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/MinecraftClient;openScreen(Lnet/minecraft/client/gui/screen/Screen;)V", cancellable = true)
    private void testModifyOpenScreen(Screen screen, CallbackInfo info) {

        if (screen == null && currentScreen != null) {
            AFKTape.INSTANCE.unpause();
        } else if (currentScreen == null && screen != null) {
            AFKTape.INSTANCE.pause();
        }

    }

    // press keys if enabled
    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/MinecraftClient;handleInputEvents()V")
    private void testModifyHandleInputEvents(CallbackInfo info) {

        if (AFKTape.INSTANCE.keyToggle.wasPressed()) {
            ArrayList<KeyBinding> pressedKeybinds = new ArrayList<>();
            for (KeyBinding keyBinding : options.keysAll) {
                if (keyBinding.isPressed()) {
                    pressedKeybinds.add(keyBinding);
                }
            }
            if (!pressedKeybinds.isEmpty())
                AFKTape.INSTANCE.enable(pressedKeybinds);
        }

        if (AFKTape.INSTANCE.isRunning()) {
            if (AFKTape.INSTANCE.wasPaused) {
                AFKTape.INSTANCE.enabledKeys.forEach(key -> KeyBinding.onKeyPressed(((KeyBindingAccessor) key).getKeyCode()));
                AFKTape.INSTANCE.wasPaused = false;
            }
            else {
                AFKTape.INSTANCE.enabledKeys.forEach(key -> key.setPressed(true));
            }
        }

    }


}
