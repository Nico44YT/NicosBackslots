package nazario.nicos_backslots.client;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import nazario.nicos_backslots.data.BackslotData;
import nazario.nicos_backslots.data.BackslotDataLoader;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.network.AbstractClientPlayerEntity;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.PlayerModelPart;
import net.minecraft.client.render.entity.feature.FeatureRenderer;
import net.minecraft.client.render.entity.feature.FeatureRendererContext;
import net.minecraft.client.render.entity.model.PlayerEntityModel;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

import java.util.Optional;

public class BackslotFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public BackslotFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
    }

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        Optional<TrinketComponent> comp = TrinketsApi.getTrinketComponent(player);

        if(comp.isPresent()) {
            try{
                ItemStack stack = comp.get().getInventory().get("chest").get("backslot").getStack(0);

                if(stack == null) stack = ItemStack.EMPTY;

                matrices.push();

                // Adjust position based on cape and chestplate
                boolean hasCape = player.canRenderCapeTexture() && player.isPartVisible(PlayerModelPart.CAPE)
                        && player.getCapeTexture() != null && !player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA);
                boolean hasChestPlate = !player.getEquippedStack(EquipmentSlot.CHEST).isEmpty();
                matrices.translate(0.0F, 0.25F, 0.1F + (hasCape ? 0.1F : 0.05F) + (hasChestPlate ? 0.1F : 0.05F));

                // Apply dynamic rotation based on movement
                float sway = MathHelper.sin(player.age + tickDelta) * 0.1F;
                if (player.isInSneakingPose()) {
                    sway += 0.25F;
                }
                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(6.0F + sway));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));

                // Scale and render the item
                BackslotData customData = BackslotDataLoader.DATA.getOrDefault(stack.getItem().getRegistryEntry().getKey().get().getValue(), BackslotData.DEFAULT);

                matrices.translate(customData.offset.getX(), -customData.offset.getY(), customData.offset.getZ());
                matrices.scale(0.85F * (float)customData.scale.getX(), 0.85F * (float)customData.scale.getY(), 0.85F * (float)customData.scale.getZ());

                matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float)customData.rotation.getX()));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)customData.rotation.getY()));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)customData.rotation.getZ()));

                ModelTransformationMode mode = switch(customData.mode.toLowerCase()) {
                    case "none" -> ModelTransformationMode.NONE;
                    case "third_person_left_hand" -> ModelTransformationMode.THIRD_PERSON_LEFT_HAND;
                    case "third_person_right_hand" -> ModelTransformationMode.THIRD_PERSON_RIGHT_HAND;
                    case "first_person_left_hand" -> ModelTransformationMode.FIRST_PERSON_LEFT_HAND;
                    case "first_person_right_hand" -> ModelTransformationMode.FIRST_PERSON_RIGHT_HAND;
                    case "head" -> ModelTransformationMode.HEAD;
                    case "gui" -> ModelTransformationMode.GUI;
                    case "ground" -> ModelTransformationMode.GROUND;
                    case "fixed" -> ModelTransformationMode.FIXED;
                    default -> ModelTransformationMode.FIXED;
                };

                MinecraftClient.getInstance().getItemRenderer().renderItem(player, stack, mode, false, matrices, vertexConsumers, player.getWorld(), light, OverlayTexture.DEFAULT_UV, 0);

                matrices.pop();
            }catch (Exception e) {
            }
        }
    }
}
