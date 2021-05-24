package tfar.mobstorage.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.mobstorage.InventoryData;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    @Shadow public float yBodyRot;

    @Shadow public float yHeadRot;

    @Shadow public float flyingSpeed;

    @Shadow public abstract float getSpeed();

    @Shadow public abstract void setSpeed(float f);

    @Shadow public abstract void calculateEntityAnimation(LivingEntity livingEntity, boolean bl);

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

     //     ItemBasedSteering steering = new ItemBasedSteering(this.entityData, DATA_BOOST_TIME, DATA_SADDLE_ID);

    @Inject(method = "dropEquipment",at = @At("RETURN"))
    private void dropSaddle(CallbackInfo ci) {
        if (!this.level.isClientSide && (Object)this instanceof Mob) {
            InventoryData data = InventoryData.get((ServerLevel) this.level);
            data.dropItems((Mob)(Object)this);
        }
    }

    @Inject(method = "checkFallDamage",at = @At("HEAD"),cancellable = true)
    private void noFallDamage(double d, boolean bl, BlockState blockState, BlockPos blockPos, CallbackInfo ci) {
        if (this.getPassengers().stream().anyMatch(Player.class::isInstance)) {
            ci.cancel();
        }
    }

    @Inject(method = "travel",at = @At("RETURN"))
    private void revertGravity(Vec3 vec3, CallbackInfo ci) {
        if (this.getPassengers().stream().anyMatch(Player.class::isInstance) && (LivingEntity)(Object)this instanceof Mob) {
            travel(vec3);
        }
    }


    private boolean travel(Vec3 vec3) {
        if (!this.isAlive()) {
            return false;
        } else {
            Entity entity = this.getPassengers().isEmpty() ? null : this.getPassengers().get(0);
            if (this.isVehicle() && entity instanceof Player) {
                this.yRot = entity.yRot;
                this.yRotO = this.yRot;
                this.xRot = entity.xRot * 0.5F;
                this.setRot(this.yRot, this.xRot);
                this.yBodyRot = this.yRot;
                this.yHeadRot = this.yRot;
                this.maxUpStep = 1.0F;
                this.flyingSpeed = this.getSpeed() * 0.1F;

                if (this.isControlledByLocalInstance()) {
                    float f = 1;//this.getSteeringSpeed();

                    this.setSpeed(f);
                //    this.travelWithInput(new Vec3(0.0D, 0.0D, 1.0D));
                //    this.lerpSteps = 0;
                } else {
                    this.calculateEntityAnimation((Mob)(Object)this, false);
                    this.setDeltaMovement(Vec3.ZERO);
                }

                return true;
            } else {
                this.maxUpStep = 0.5F;
                this.flyingSpeed = 0.02F;
              //  this.travelWithInput(vec3);
                return false;
            }
        }
    }

}
