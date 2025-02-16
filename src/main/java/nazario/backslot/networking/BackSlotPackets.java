package nazario.backslot.networking;

import nazario.backslot.networking.packets.BackslotSwitchPacketC2S;
import nazario.liby.api.registry.auto.LibyAutoRegister;
import nazario.liby.api.registry.auto.LibyEntrypoints;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

@LibyAutoRegister(method = "registerC2SPackets", entrypoints = LibyEntrypoints.MAIN)
public class BackSlotPackets {
    public static final Identifier SWITCH_ID = new Identifier("backslot", "switch");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(SWITCH_ID, BackslotSwitchPacketC2S::receive);
    }
}
