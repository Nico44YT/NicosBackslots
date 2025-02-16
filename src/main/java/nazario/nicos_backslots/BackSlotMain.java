package nazario.nicos_backslots;

import nazario.liby.api.registry.auto.LibyRegistryLoader;
import net.fabricmc.api.ModInitializer;
public class BackSlotMain implements ModInitializer {

    public static final String MOD_ID = "nicos_backslots";

    @Override
    public void onInitialize() {
        LibyRegistryLoader.load("nazario.nicos_backslots");
    }
}
