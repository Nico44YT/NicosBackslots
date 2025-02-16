package nazario.nicos_backslots;

import nazario.nicos_backslots.data.BackslotDataLoader;
import nazario.nicos_backslots.networking.BackSlotPackets;
import net.fabricmc.api.ClientModInitializer;
import net.fabricmc.fabric.api.client.event.lifecycle.v1.ClientTickEvents;
import net.fabricmc.fabric.api.client.keybinding.v1.KeyBindingHelper;
import net.fabricmc.fabric.api.client.networking.v1.ClientPlayNetworking;
import net.fabricmc.fabric.api.networking.v1.PacketByteBufs;
import net.fabricmc.fabric.api.resource.ResourceManagerHelper;
import net.minecraft.client.option.KeyBinding;
import net.minecraft.client.util.InputUtil;
import net.minecraft.resource.ResourceType;
import org.lwjgl.glfw.GLFW;

public class BackSlotClient implements ClientModInitializer {

    public static KeyBinding SWITCH_BACKSLOT = new KeyBinding(
            "key.nicos_backslots.swap",
            InputUtil.Type.KEYSYM,
            GLFW.GLFW_KEY_G,
            "key.categories.inventory"
    );

    @Override
    public void onInitializeClient() {
        SWITCH_BACKSLOT = KeyBindingHelper.registerKeyBinding(SWITCH_BACKSLOT);

        ClientTickEvents.END_CLIENT_TICK.register(client -> {
            if(SWITCH_BACKSLOT.wasPressed()) {
                ClientPlayNetworking.send(BackSlotPackets.SWITCH_ID, PacketByteBufs.create());
            }
        });

        ResourceManagerHelper.get(ResourceType.CLIENT_RESOURCES).registerReloadListener(new BackslotDataLoader());
    }
}
