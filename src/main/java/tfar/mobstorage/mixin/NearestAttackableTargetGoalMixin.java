package tfar.mobstorage.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.target.NearestAttackableTargetGoal;
import net.minecraft.world.entity.ai.goal.target.TargetGoal;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(NearestAttackableTargetGoal.class)
public abstract class NearestAttackableTargetGoalMixin<T extends LivingEntity> extends TargetGoal {

    @Shadow @Final protected Class<T> targetType;

    public NearestAttackableTargetGoalMixin(Mob mob, boolean bl) {
        super(mob, bl);
    }

    @Inject(method = "canUse",at = @At("HEAD"),cancellable = true)
    private void hostileRiding(CallbackInfoReturnable<Boolean> cir) {
        if (targetType == Player.class && this.mob.getPassengers().stream().anyMatch(Player.class::isInstance)) {
            cir.setReturnValue(false);
        }
    }
}
