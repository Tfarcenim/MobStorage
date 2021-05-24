package tfar.mobstorage.mixin;

import net.minecraft.network.protocol.PacketUtils;
import net.minecraft.network.protocol.game.ServerboundPlayerCommandPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.server.network.ServerGamePacketListenerImpl;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.mobstorage.InventoryData;

@Mixin(ServerGamePacketListenerImpl.class)
public class ServerGamePacketListenerImplMixin {

    @Shadow public ServerPlayer player;

    @Inject(method = "handlePlayerCommand",at = @At("HEAD"),cancellable = true)
    private void openHorseInv(ServerboundPlayerCommandPacket serverboundPlayerCommandPacket, CallbackInfo ci) {
        PacketUtils.ensureRunningOnSameThread(serverboundPlayerCommandPacket, (ServerGamePacketListenerImpl)(Object)this, this.player.getLevel());
        if (serverboundPlayerCommandPacket.getAction() == ServerboundPlayerCommandPacket.Action.OPEN_INVENTORY && !(player.getVehicle() instanceof AbstractHorse)
                && player.getVehicle() instanceof Mob) {
            InventoryData data = InventoryData.get((ServerLevel) player.level);
                data.openInventory(player, (Mob)player.getVehicle());
        }
    }
}
