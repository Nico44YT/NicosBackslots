package nazario.nicos_backslots.networking;

import nazario.nicos_backslots.BackSlotMain;
import nazario.nicos_backslots.networking.packets.BackslotSwitchPayload;
import nazario.liby.api.registry.auto.LibyAutoRegister;
import nazario.liby.api.registry.auto.LibyEntrypoints;
import net.fabricmc.fabric.api.networking.v1.PayloadTypeRegistry;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.util.Identifier;

@LibyAutoRegister(method = "registerC2SPackets", entrypoints = LibyEntrypoints.MAIN)
public class BackSlotPackets {
    public static final Identifier SWITCH_PACKET_ID = Identifier.of(BackSlotMain.MOD_ID, "switch");

    public static void registerC2SPackets() {
        PayloadTypeRegistry.playC2S().register(BackslotSwitchPayload.ID, BackslotSwitchPayload.CODEC);

        ServerPlayNetworking.registerGlobalReceiver(BackslotSwitchPayload.ID, BackslotSwitchPayload::handler);
    }
}
