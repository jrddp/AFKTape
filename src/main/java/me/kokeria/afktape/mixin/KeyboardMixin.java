package me.kokeria.afktape.mixin;

import me.kokeria.afktape.Manager;
import net.minecraft.client.Keyboard;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.options.KeyBinding;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Keyboard.class)
public abstract class KeyboardMixin {

    final int KEY_ESCAPE = 256;
    // used to keep track of double pressing escape in order to get out of sticky situations (such as constant reopening of guis)
    long lastEscapePressTime = 0;

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/Keyboard;onKey(JIIII)V", cancellable = true)
    private void tapeModifyOnKey(long window, int key, int scancode, int i, int j, CallbackInfo info) {

        boolean keyState = i == 1;

        // stops players from double pressing any keys that are already automated
        if (Manager.INSTANCE.isRunning()) {
            for (KeyBinding keyBinding : Manager.INSTANCE.enabledKeys) {
                if (keyBinding.matchesKey(key, scancode)) info.cancel();
            }
        }

        // disable when escape is pressed outside of a gui
        if (key == KEY_ESCAPE && keyState && Manager.INSTANCE.isRunningIgnorePause()) {
            if (MinecraftClient.getInstance().currentScreen == null) {
                Manager.INSTANCE.disable();
                info.cancel();
            } else if (System.currentTimeMillis() - lastEscapePressTime <= 300) {
                Manager.INSTANCE.disable();
            }

            lastEscapePressTime = System.currentTimeMillis();
        }
    }

}
