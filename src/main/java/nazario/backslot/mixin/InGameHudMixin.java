package nazario.backslot.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawableHelper;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin extends DrawableHelper {
    @Shadow private int scaledHeight;

    @Shadow private int scaledWidth;

    @Shadow protected abstract void renderHotbarItem(int x, int y, float tickDelta, PlayerEntity player, ItemStack stack, int seed);

    @Shadow protected abstract PlayerEntity getCameraPlayer();

    @Shadow @Final private static Identifier WIDGETS_TEXTURE;

    @Inject(method = "renderHotbar", at = @At(value = "TAIL"))
    public void backslot$renderCustomSlot(float tickDelta, MatrixStack matrices, CallbackInfo ci) {
        try{
            matrices.push();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            Optional<TrinketComponent> comp = TrinketsApi.getTrinketComponent(MinecraftClient.getInstance().player);
            if(comp.isEmpty()) return;

            ItemStack itemStack = comp.get().getInventory().get("chest").get("backslot").getStack(0);

            PlayerEntity playerEntity = this.getCameraPlayer();
            Arm arm = playerEntity.getMainArm().getOpposite();
            int i = this.scaledWidth / 2;

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.setShader(GameRenderer::getPositionTexShader);
            RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);

            if(itemStack == null) return;
            if(itemStack.getItem() == Items.AIR) return;

            if (!itemStack.isEmpty()) {
                if (arm == Arm.RIGHT) {
                    this.drawTexture(matrices, i - 91 - 29, this.scaledHeight - 23, 24, 22, 29, 24);
                } else {
                    this.drawTexture(matrices, i + 91, this.scaledHeight - 23, 53, 22, 29, 24);
                }
            }

            if (!itemStack.isEmpty()) {
                int n = this.scaledHeight - 16 - 3;
                if (arm == Arm.RIGHT) {
                    this.renderHotbarItem(i - 91 - 26, n, tickDelta, playerEntity, itemStack, 0);
                } else {
                    this.renderHotbarItem(i + 91 + 10, n, tickDelta, playerEntity, itemStack, 0);
                }
            }

            matrices.pop();
            RenderSystem.disableBlend();
        }catch (Exception e) {

        }
    }
}
