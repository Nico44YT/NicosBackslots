package nazario.nicos_backslots.api;

import nazario.nicos_backslots.data.BackslotData;
import net.minecraft.item.ItemStack;

public interface BackslotItemOverride {
    default BackslotData overrideBackslotRendering(BackslotData backslotData, ItemStack stack) {
        return backslotData;
    }
}
