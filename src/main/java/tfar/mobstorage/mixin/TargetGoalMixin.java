package tfar.mobstorage.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(TargetGoal.class)
public abstract class TargetGoalMixin<T extends LivingEntity> extends Goal {

    @Shadow protected LivingEntity targetMob;

    @Shadow @Final protected Mob mob;

    @Inject(method = "canContinueToUse",at = @At("HEAD"),cancellable = true)
    private void hostileRiding(CallbackInfoReturnable<Boolean> cir) {
        if (this.targetMob instanceof Player && this.mob.getPassengers().stream().anyMatch(Player.class::isInstance)) {
            cir.setReturnValue(false);
        }
    }
}
