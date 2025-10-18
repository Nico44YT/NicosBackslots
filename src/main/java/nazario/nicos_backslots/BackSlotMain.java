package nazario.nicos_backslots;

import nazario.nicos_backslots.networking.BackSlotPackets;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackSlotMain implements ModInitializer {

    public static final String MOD_ID = "nicos_backslots";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        BackSlotPackets.registerC2SPackets();
    }
}
