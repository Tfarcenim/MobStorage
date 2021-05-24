package tfar.mobstorage.mixin;

import com.mojang.math.Vector3d;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.phases.DragonPhaseInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(EnderDragon.class)
public abstract class EnderDragonMixin extends LivingEntity {

    protected EnderDragonMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "canRide",at = @At("RETURN"),cancellable = true)
    private void ride(Entity entity, CallbackInfoReturnable<Boolean> cir) {
        cir.setReturnValue(entity instanceof Player);
    }

    @Redirect(method = "aiStep",at = @At(value = "INVOKE",target = "Lnet/minecraft/world/entity/boss/enderdragon/phases/DragonPhaseInstance;getFlyTargetLocation()Lnet/minecraft/world/phys/Vec3;"))
    private Vec3 forcePath(DragonPhaseInstance dragonPhaseInstance) {
        ServerPlayer player = (ServerPlayer) this.getPassengers().stream().filter(ServerPlayer.class::isInstance).findFirst().orElse(null);
        if(player != null) {
            Vec3 newVec = position().add(0,6,0).add(player.getLookAngle().scale(10));
            return newVec;
        }
        return dragonPhaseInstance.getFlyTargetLocation();
    }
}
