package nazario.backslot.networking.packets;

import dev.emi.trinkets.api.TrinketsApi;
import net.fabricmc.fabric.api.networking.v1.PacketSender;
import net.minecraft.item.ItemStack;
import net.minecraft.network.PacketByteBuf;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.network.ServerPlayNetworkHandler;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.util.Hand;

public class BackslotSwitchPacketC2S {

    public static void receive(MinecraftServer minecraftServer, ServerPlayerEntity serverPlayerEntity, ServerPlayNetworkHandler serverPlayNetworkHandler, PacketByteBuf packetByteBuf, PacketSender packetSender) {
        ItemStack handStack = serverPlayerEntity.getMainHandStack().copy();
        ItemStack backStack = TrinketsApi.getTrinketComponent(serverPlayerEntity).get().getInventory().get("chest").get("backslot").getStack(0).copy();

        serverPlayerEntity.setStackInHand(Hand.MAIN_HAND, backStack);
        TrinketsApi.getTrinketComponent(serverPlayerEntity).get().getInventory().get("chest").get("backslot").setStack(0, handStack);
    }
}
