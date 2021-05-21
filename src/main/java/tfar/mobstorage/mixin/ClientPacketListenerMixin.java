package tfar.mobstorage.mixin;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.screens.inventory.HorseInventoryScreen;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.client.multiplayer.ClientPacketListener;
import net.minecraft.client.player.LocalPlayer;
import net.minecraft.network.protocol.game.ClientboundHorseScreenOpenPacket;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.inventory.HorseInventoryMenu;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import tfar.mobstorage.MobInventoryMenu;
import tfar.mobstorage.MobInventoryScreen;

@Mixin(ClientPacketListener.class)
public class ClientPacketListenerMixin {
    @Shadow private ClientLevel level;

    @Shadow private Minecraft minecraft;

    @Inject(method = "handleHorseScreenOpen",at = @At("TAIL"))
    private void customScreen(ClientboundHorseScreenOpenPacket clientboundHorseScreenOpenPacket, CallbackInfo ci) {
        Entity entity = this.level.getEntity(clientboundHorseScreenOpenPacket.getEntityId());
        if (!(entity instanceof AbstractHorse) && entity instanceof Mob) {
            Mob mob = (Mob)entity;
            LocalPlayer localPlayer = this.minecraft.player;
            SimpleContainer simpleContainer = new SimpleContainer(clientboundHorseScreenOpenPacket.getSize());
            MobInventoryMenu horseInventoryMenu = new MobInventoryMenu(clientboundHorseScreenOpenPacket.getContainerId(), localPlayer.inventory, simpleContainer, mob);
            localPlayer.containerMenu = horseInventoryMenu;
            this.minecraft.setScreen(new MobInventoryScreen(horseInventoryMenu, localPlayer.inventory, mob));
        }
    }
}
