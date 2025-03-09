package nazario.nicos_backslots.networking.packets;

import dev.emi.trinkets.api.TrinketsApi;
import nazario.nicos_backslots.networking.BackSlotPackets;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.codec.PacketCodecs;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;
import net.minecraft.util.Uuids;

import java.util.UUID;

public record BackslotSwitchPayload(NbtCompound payloadData) implements CustomPayload {
    public static final CustomPayload.Id<BackslotSwitchPayload> ID = new CustomPayload.Id<>(BackSlotPackets.SWITCH_PACKET_ID);
    public static final PacketCodec<RegistryByteBuf, BackslotSwitchPayload> CODEC = PacketCodec.tuple(PacketCodecs.NBT_COMPOUND, BackslotSwitchPayload::payloadData, BackslotSwitchPayload::new);

    @Override
    public Id<? extends CustomPayload> getId() {
        return ID;
    }

    public void handler(ServerPlayNetworking.Context context) {
        ServerPlayerEntity playerEntity = context.player();

        ItemStack handStack = playerEntity.getMainHandStack().copy();
        ItemStack backStack = TrinketsApi.getTrinketComponent(playerEntity).get().getInventory().get("chest").get("backslot").getStack(0).copy();

        playerEntity.setStackInHand(Hand.MAIN_HAND, backStack);

        TrinketsApi.getTrinketComponent(playerEntity).get().getInventory().get("chest").get("backslot").setStack(0, handStack);
    }
}
