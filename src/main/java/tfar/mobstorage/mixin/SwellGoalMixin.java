package tfar.mobstorage.mixin;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.ai.goal.Goal;
import net.minecraft.world.entity.ai.goal.SwellGoal;
import net.minecraft.world.entity.monster.Creeper;
import net.minecraft.world.entity.player.Player;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(SwellGoal.class)
public abstract class SwellGoalMixin extends Goal {

    @Shadow private LivingEntity target;

    @Shadow @Final private Creeper creeper;

    @Inject(method = "canUse",at = @At("HEAD"),cancellable = true)
    private void hostileRiding(CallbackInfoReturnable<Boolean> cir) {
        if (this.target instanceof Player && this.creeper.getPassengers().stream().anyMatch(Player.class::isInstance)) {
            cir.setReturnValue(false);
        }
    }
}
