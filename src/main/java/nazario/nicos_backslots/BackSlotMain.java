package nazario.nicos_backslots;

import nazario.liby.api.registry.auto.LibyRegistryLoader;
import net.fabricmc.api.ModInitializer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class BackSlotMain implements ModInitializer {

    public static final String MOD_ID = "nicos_backslots";
    public static final Logger LOGGER = LoggerFactory.getLogger(MOD_ID);

    @Override
    public void onInitialize() {
        LibyRegistryLoader.load("nazario.nicos_backslots");
    }
}
