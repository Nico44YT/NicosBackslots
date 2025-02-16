package nazario.nicos_backslots.networking;

import nazario.nicos_backslots.BackSlotMain;
import nazario.nicos_backslots.networking.packets.BackslotSwitchPacketC2S;
import nazario.liby.api.registry.auto.LibyAutoRegister;
import nazario.liby.api.registry.auto.LibyEntrypoints;
import net.fabricmc.fabric.api.networking.v1.ServerPlayNetworking;
import net.minecraft.util.Identifier;

@LibyAutoRegister(method = "registerC2SPackets", entrypoints = LibyEntrypoints.MAIN)
public class BackSlotPackets {
    public static final Identifier SWITCH_ID = new Identifier(BackSlotMain.MOD_ID, "switch");

    public static void registerC2SPackets() {
        ServerPlayNetworking.registerGlobalReceiver(SWITCH_ID, BackslotSwitchPacketC2S::receive);
    }
}
