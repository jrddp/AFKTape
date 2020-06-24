package me.kokeria.afktape.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kokeria.afktape.Manager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.util.math.MatrixStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {


    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    @Shadow protected abstract void drawTextBackground(MatrixStack matrixStack, TextRenderer textRenderer, int i, int j, int k);

    @Shadow public abstract TextRenderer getFontRenderer();

    //render HUD when running
    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/gui/hud/InGameHud;render(Lnet/minecraft/client/util/math/MatrixStack;F)V")
    private void tapeModifyOnKey(MatrixStack stack, float f, CallbackInfo info) {

        if (Manager.INSTANCE.isRunning()) {
            TextRenderer textRenderer = getFontRenderer();
            String[] AFKTapeMsg = Manager.INSTANCE.getMessage();

            stack.push();
            stack.translate(scaledWidth / 2f, scaledHeight / 2f - 15, 0f);

            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            for (int i = AFKTapeMsg.length - 1; i >= 0; i--) {

                drawTextBackground(stack, textRenderer, -4, textRenderer.getWidth(AFKTapeMsg[i]), 16777215);
                textRenderer.draw(stack, AFKTapeMsg[i], (float) (-textRenderer.getWidth(AFKTapeMsg[i]) / 2), -4.0F, 0xFFFFFF);

                stack.translate(0.0F, -textRenderer.fontHeight - 4, 0.0F);

            }

            RenderSystem.disableBlend();
            stack.pop();
        }

    }

}
