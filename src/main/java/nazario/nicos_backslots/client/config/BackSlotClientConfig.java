package nazario.nicos_backslots.client.config;

import me.shedaniel.autoconfig.ConfigData;
import me.shedaniel.autoconfig.annotation.Config;
import nazario.nicos_backslots.BackSlotMain;

@Config(name = BackSlotMain.MOD_ID)
public class BackSlotClientConfig implements ConfigData {
    int xGuiOffset = 91;
    int yGuiOffset = 23;
}
