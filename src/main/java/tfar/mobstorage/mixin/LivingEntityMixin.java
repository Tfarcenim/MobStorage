package tfar.mobstorage.mixin;

import net.minecraft.core.BlockPos;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.mobstorage.InventoryData;

@Mixin(LivingEntity.class)
public abstract class LivingEntityMixin extends Entity {

    public LivingEntityMixin(EntityType<?> entityType, Level level) {
        super(entityType, level);
    }

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
}
