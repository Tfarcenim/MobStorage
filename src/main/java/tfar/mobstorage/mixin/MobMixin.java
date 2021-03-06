package tfar.mobstorage.mixin;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.MoverType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import tfar.mobstorage.InventoryData;
import tfar.mobstorage.MobStorage;

@Mixin(Mob.class)
abstract class MobMixin extends LivingEntity {
    protected MobMixin(EntityType<? extends LivingEntity> entityType, Level level) {
        super(entityType, level);
    }

    @Inject(method = "checkAndHandleImportantInteractions",at = @At("RETURN"),cancellable = true)
    private void handleInteract(Player player, InteractionHand interactionHand, CallbackInfoReturnable<InteractionResult> cir) {
        Mob mob = (Mob)(Object)this;
        InteractionResult result = cir.getReturnValue();
        if (MobStorage.mobInteract(player,interactionHand,mob,result)) {
            cir.setReturnValue(InteractionResult.CONSUME);
        }
    }

    @Inject(method = "baseTick",at = @At("RETURN"))
    private void entitytick(CallbackInfo ci) {
        ServerPlayer player = (ServerPlayer) this.getPassengers().stream().filter(entity -> entity instanceof ServerPlayer).findFirst().orElse(null);
        if (player!=null &&!level.isClientSide) {
            InventoryData data = InventoryData.get((ServerLevel) level);
            if (data.hasSaddle((Mob)(Object)this)) {
                setNoGravity(true);
                this.setDeltaMovement(Vec3.ZERO);
                this.move(MoverType.SELF, player.getLookAngle().scale(.75));
            }
        }
    }
}
