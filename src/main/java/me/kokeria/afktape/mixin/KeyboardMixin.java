package me.kokeria.afktape.mixin;

import me.kokeria.afktape.Tracker;
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

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/Keyboard;onKey(JIIII)V", cancellable = true)
    private void testModifyOnKey(long window, int key, int scancode, int i, int j, CallbackInfo info) {

        boolean keyState = i == 1;

        // stops players from double pressing any keys that are already automated
        if (Tracker.INSTANCE.isRunning()) {
            for (KeyBinding keyBinding : Tracker.INSTANCE.enabledKeys) {
                if (keyBinding.matchesKey(key, scancode)) info.cancel();
            }
        }

        // disable when escape is pressed outside of a gui
        if (key == KEY_ESCAPE && keyState && Tracker.INSTANCE.isRunning() && MinecraftClient.getInstance().currentScreen == null) {

            Tracker.INSTANCE.disable();
            info.cancel();

        }

    }

}
