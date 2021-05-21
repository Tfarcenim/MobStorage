package tfar.mobstorage.mixin;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.HorseInventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(HorseInventoryMenu.class)
public class HorseInventoryMixin {

    @Inject(method = "stillValid",at = @At("RETURN"),cancellable = true)
    private void forceOpen(Player player, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(true);
    }

}
