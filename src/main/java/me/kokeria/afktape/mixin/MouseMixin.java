package me.kokeria.afktape.mixin;

import me.kokeria.afktape.AFKTape;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.Mouse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Mouse.class)
public abstract class MouseMixin {


    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/Mouse;isCursorLocked()Z", cancellable = true)
    private void testModifyIsCursorLocked(CallbackInfoReturnable<Boolean> info) {

        if (MinecraftClient.getInstance().currentScreen == null) {
            info.setReturnValue(true);
            info.cancel();
        }

    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/Mouse;lockCursor()V", cancellable = true)
    private void testModifyLockCursor(CallbackInfo info) {

        if (AFKTape.INSTANCE.isRunning()) info.cancel();

    }

    @Inject(at = @At("HEAD"), method = "Lnet/minecraft/client/Mouse;onCursorPos(JDD)V", cancellable = true)
    private void testModifyOnCursorPos(CallbackInfo info) {

        if (AFKTape.INSTANCE.isRunning()) info.cancel();

    }

}
