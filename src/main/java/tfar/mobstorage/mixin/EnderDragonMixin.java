package tfar.mobstorage.mixin;

import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderDragon.class)
public class EnderDragonMixin {

    @Inject(method = "canRide",at = @At("RETURN"),cancellable = true)
    private void ride(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(entity instanceof Player);
    }
}
