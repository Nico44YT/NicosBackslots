package nazario.nicos_backslots.client;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import nazario.nicos_backslots.api.BackslotItemOverride;
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
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.EquipmentSlot;
import net.minecraft.item.ItemStack;
import net.minecraft.item.Items;

//? <1.19.3 {
import net.minecraft.util.math.Vec3f;
//?} else {
/*import net.minecraft.util.math.RotationAxis;
*///?}

//? >=1.19.4 {
/*import net.minecraft.client.render.model.json.ModelTransformationMode;
*///?} else {
import net.minecraft.client.render.model.json.ModelTransformation;
//?}

import java.util.Optional;

public class BackslotFeatureRenderer extends FeatureRenderer<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> {

    public BackslotFeatureRenderer(FeatureRendererContext<AbstractClientPlayerEntity, PlayerEntityModel<AbstractClientPlayerEntity>> context) {
        super(context);
    }

    private BackslotData cachedData;
    private int hash = 0;

    @Override
    public void render(MatrixStack matrices, VertexConsumerProvider vertexConsumers, int light, AbstractClientPlayerEntity player, float limbAngle, float limbDistance, float tickDelta, float animationProgress, float headYaw, float headPitch) {
        Optional<TrinketComponent> optional = TrinketsApi.getTrinketComponent(player);

        if(optional.isPresent()) {
            TrinketComponent component = optional.get();

            try{
                ItemStack stack = component.getInventory().get("chest").get("backslot").getStack(0);

                if(stack == null) stack = ItemStack.EMPTY;

                if(hash != stack.hashCode()) {
                    hash = stack.hashCode();
                    cachedData = null;
                }

                matrices.push();

                // Adjust position based on cape and chestplate
                boolean hasCape = player.canRenderCapeTexture() && player.isPartVisible(PlayerModelPart.CAPE)
                        && player.getCapeTexture() != null && !player.getEquippedStack(EquipmentSlot.CHEST).isOf(Items.ELYTRA);
                boolean hasChestPlate = !player.getEquippedStack(EquipmentSlot.CHEST).isEmpty();
                matrices.translate(0.0F, 0.25F, 0.1F + (hasCape ? 0.1F : 0.04F) + (hasChestPlate ? 0.1F : 0.04F));

                float angleOffset = 0;
                if (player.isInSneakingPose()) {
                    angleOffset += 25F;
                }

                //? <1.19.3 {
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion(6.0F + angleOffset));
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion(180.0F));
                //?} else {
                /*matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees(6.0F + angleOffset));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(180.0F));
                *///?}

                // Scale and render the item
                if(cachedData == null) {
                    cachedData = BackslotDataLoader.DATA.getOrDefault(stack.getItem().getRegistryEntry().getKey().get().getValue(), BackslotData.DEFAULT).copy();
                }

                if(stack.getItem() instanceof BackslotItemOverride overrider) cachedData = overrider.overrideBackslotRendering(cachedData, stack);

                matrices.translate((float)cachedData.offset.x, (float)-cachedData.offset.y, (float)cachedData.offset.z);
                matrices.scale(0.85F * (float)cachedData.scale.x, 0.85F * (float)cachedData.scale.y, 0.85F * (float)cachedData.scale.z);

                //? <1.19.3 {
                matrices.multiply(Vec3f.POSITIVE_X.getDegreesQuaternion((float)cachedData.rotation.x));
                matrices.multiply(Vec3f.POSITIVE_Y.getDegreesQuaternion((float)cachedData.rotation.y));
                matrices.multiply(Vec3f.POSITIVE_Z.getDegreesQuaternion((float)cachedData.rotation.z));
                //?} else {
                /*matrices.multiply(RotationAxis.POSITIVE_X.rotationDegrees((float)cachedData.rotation.x));
                matrices.multiply(RotationAxis.POSITIVE_Y.rotationDegrees((float)cachedData.rotation.y));
                matrices.multiply(RotationAxis.POSITIVE_Z.rotationDegrees((float)cachedData.rotation.z));
                *///?}

                //? >=1.19.4 {
                //ModelTransformationMode mode = ModelTransformationMode.valueOf(cachedData.mode.toUpperCase());
                //?} else {
                ModelTransformation.Mode mode = ModelTransformation.Mode.valueOf(cachedData.mode.toUpperCase());
                //?}



                MinecraftClient.getInstance().getItemRenderer().renderItem(player, stack, mode, false, matrices, vertexConsumers, player.getWorld(), light, OverlayTexture.DEFAULT_UV, 0);

                matrices.pop();
            } catch (Exception e) {
                cachedData = null;
            }
        }
    }
}
