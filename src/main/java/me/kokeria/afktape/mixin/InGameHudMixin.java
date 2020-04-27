package me.kokeria.afktape.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import me.kokeria.afktape.Manager;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.gui.hud.InGameHud;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {


    @Shadow private int scaledWidth;

    @Shadow private int scaledHeight;

    @Shadow protected abstract void drawTextBackground(TextRenderer textRenderer, int y, int width);

    @Shadow public abstract TextRenderer getFontRenderer();

    //render HUD when running
    @Inject(at = @At("TAIL"), method = "Lnet/minecraft/client/gui/hud/InGameHud;render(F)V")
    private void testModifyOnKey(CallbackInfo info) {

        if (Manager.INSTANCE.isRunning()) {
            TextRenderer textRenderer = getFontRenderer();
            String[] AFKTapeMsg = Manager.INSTANCE.getMessage();

            RenderSystem.pushMatrix();
            RenderSystem.translatef((float) (scaledWidth / 2), (float) (scaledHeight / 2 - 15), 0.0F);
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            for (int i = AFKTapeMsg.length - 1; i >= 0; i--) {

                drawTextBackground(textRenderer, -4, textRenderer.getStringWidth(AFKTapeMsg[i]));
                textRenderer.draw(AFKTapeMsg[i], (float) (-textRenderer.getStringWidth(AFKTapeMsg[i]) / 2), -4.0F, 0xFFFFFF);

                RenderSystem.translatef(0.0F, -textRenderer.fontHeight - 4, 0.0F);

            }

            RenderSystem.disableBlend();
            RenderSystem.popMatrix();
        }

    }

}
