package nazario.nicos_backslots.mixin;

import dev.emi.trinkets.api.TrinketComponent;
import dev.emi.trinkets.api.TrinketsApi;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.item.ItemStack;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Optional;

@Mixin(PlayerInventory.class)
public abstract class PlayerInventoryMixin {
    @Shadow @Final public PlayerEntity player;

    @Inject(method = "updateItems", at = @At("TAIL"))
    public void backslot$updateItems(CallbackInfo ci) {
        Optional<TrinketComponent> comp = TrinketsApi.getTrinketComponent(player);

        if(comp.isPresent()) {
            ItemStack stack = comp.get().getInventory().get("chest").get("backslot").getStack(0);
            stack.inventoryTick(this.player.world, this.player, -1, false);
        }
    }
}
