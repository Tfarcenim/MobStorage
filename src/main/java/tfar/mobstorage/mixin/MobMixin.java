package tfar.mobstorage.mixin;

import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.mobstorage.MobStorage;

@Mixin(Mob.class)
public class MobMixin {
    @Inject(method = "checkAndHandleImportantInteractions",at = @At("RETURN"),cancellable = true)
    private void handleInteract(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        Mob mob = (Mob)(Object)this;
        InteractionResult result = cir.getReturnValue();
        if (MobStorage.mobInteract(player,interactionHand,mob,result)) {
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }
}
