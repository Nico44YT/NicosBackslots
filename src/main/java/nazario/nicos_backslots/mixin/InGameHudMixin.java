package nazario.nicos_backslots.mixin;

import com.mojang.blaze3d.systems.RenderSystem;
import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.gui.DrawContext;
import net.minecraft.client.gui.hud.InGameHud;
import net.minecraft.client.render.GameRenderer;
import net.minecraft.client.render.RenderTickCounter;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.Arm;
import net.minecraft.util.Identifier;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(InGameHud.class)
public abstract class InGameHudMixin {

    @Shadow @Nullable protected abstract PlayerEntity getCameraPlayer();

    @Shadow protected abstract void renderHotbarItem(DrawContext context, int x, int y, RenderTickCounter tickCounter, PlayerEntity player, ItemStack stack, int seed);

    @Shadow @Final private static Identifier HOTBAR_OFFHAND_LEFT_TEXTURE;
    @Shadow @Final private static Identifier HOTBAR_OFFHAND_RIGHT_TEXTURE;

    @Inject(method = "renderHotbar", at = @At(value = "TAIL"))
    public void backslot$renderCustomSlot(DrawContext context, RenderTickCounter tickCounter, CallbackInfo ci) {
        try{
            context.getMatrices().push();
            RenderSystem.enableBlend();
            RenderSystem.defaultBlendFunc();

            Optional<TrinketComponent> comp = TrinketsApi.getTrinketComponent(MinecraftClient.getInstance().player);
            if(comp.isEmpty()) return;

            ItemStack itemStack = comp.get().getInventory().get("chest").get("backslot").getStack(0);

            PlayerEntity playerEntity = this.getCameraPlayer();
            Arm arm = playerEntity.getMainArm().getOpposite();
            int i = context.getScaledWindowWidth() / 2;

            RenderSystem.setShaderColor(1.0f, 1.0f, 1.0f, 1.0f);
            RenderSystem.setShader(GameRenderer::getPositionTexProgram);
            //RenderSystem.setShaderTexture(0, WIDGETS_TEXTURE);

            if(itemStack == null) return;
            if(itemStack.getItem() == Items.AIR) return;

            if (!itemStack.isEmpty()) {
                if (arm == Arm.RIGHT) {
                    context.drawGuiTexture(HOTBAR_OFFHAND_LEFT_TEXTURE, i - 91 - 29, context.getScaledWindowHeight() - 23,29, 24);
                } else {
                    context.drawGuiTexture(HOTBAR_OFFHAND_RIGHT_TEXTURE, i + 91, context.getScaledWindowHeight() - 23, 29, 24);
                }
            }

            if (!itemStack.isEmpty()) {
                int n = context.getScaledWindowHeight() - 16 - 3;
                if (arm == Arm.RIGHT) {
                    this.renderHotbarItem(context,i - 91 - 26, n, tickCounter, playerEntity, itemStack, 0);
                } else {
                    this.renderHotbarItem(context, i + 91 + 10, n, tickCounter, playerEntity, itemStack, 0);
                }
            }

            context.getMatrices().pop();
            RenderSystem.disableBlend();
        }catch (Exception e) {

        }
    }
}
