package nazario.backslot;

import nazario.liby.api.registry.auto.LibyRegistryLoader;
import net.fabricmc.api.ModInitializer;
public class BackSlotMain implements ModInitializer {

    @Override
    public void onInitialize() {
        LibyRegistryLoader.load("nazario.backslot");
    }
}
