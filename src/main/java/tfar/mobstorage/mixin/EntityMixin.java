package tfar.mobstorage.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.boss.EnderDragonPart;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.mobstorage.MobStorage;

@Mixin(Entity.class)
public abstract class EntityMixin {

    @Shadow public abstract InteractionResult interact(Player player, InteractionHand interactionHand);

    @Inject(method = "interact",at = @At("HEAD"),cancellable = true)
    private void dragonHandle(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        Entity entity = (Entity)(Object)this;
        if (entity instanceof EnderDragonPart) {
            if (MobStorage.dragonInteract(player,interactionHand,(EnderDragonPart)(Object)this,InteractionResult.PASS)) {
                cir.setReturnValue(InteractionResult.CONSUME);
            }
        }
    }
}
