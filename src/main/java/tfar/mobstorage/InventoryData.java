package tfar.mobstorage;

import com.google.common.collect.Sets;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.protocol.game.ClientboundHorseScreenOpenPacket;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.SimpleContainer;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.horse.AbstractHorse;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.HorseInventoryMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.saveddata.SavedData;
import net.minecraft.world.level.saveddata.maps.MapItemSavedData;
import tfar.mobstorage.mixin.ServerPlayerAccess;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryData extends SavedData {

    public InventoryData(String string) {
        super(string);
    }

    private final Map<UUID, SimpleContainer> invMap = new HashMap<>();

    public static InventoryData get(ServerLevel level) {
        return level.getServer().getLevel(Level.OVERWORLD).getDataStorage().computeIfAbsent(() -> new InventoryData("default"), "default");
    }

    public void openInventory(Player player, Mob mob) {
        UUID uuid = mob.getUUID();
        if (!invMap.containsKey(uuid)) {
            invMap.put(uuid,new SimpleContainer(2) {
                @Override
                public void setChanged() {
                    super.setChanged();
                    InventoryData.this.setDirty();
                }
            });
        }
        ServerPlayer serverPlayer = (ServerPlayer)player;

        if (player.containerMenu != player.inventoryMenu) {
           serverPlayer.closeContainer();
        }

        SimpleContainer simpleContainer = invMap.get(uuid);

        ServerPlayerAccess serverPlayerAccess = (ServerPlayerAccess)serverPlayer;

        serverPlayerAccess.$nextContainerCounter();
        serverPlayer.connection.send(new ClientboundHorseScreenOpenPacket(serverPlayerAccess.getContainerCounter(), simpleContainer.getContainerSize(), mob.getId()));
        player.containerMenu = new MobInventoryMenu(serverPlayerAccess.getContainerCounter(), player.inventory, simpleContainer, mob);
        serverPlayer.containerMenu.addSlotListener(serverPlayer);
    }

    public boolean hasSaddle(Mob mob) {
        UUID uuid = mob.getUUID();
        SimpleContainer container = invMap.get(uuid);
        if (container == null) return false;
        return container.hasAnyOf(Sets.newHashSet(MobStorage.FLYING_SADDLE));
    }


    @Override
    public void load(CompoundTag compoundTag) {
        invMap.clear();
        ListTag listTag = compoundTag.getList("list",10);
        for (Tag tag : listTag) {
            CompoundTag compoundTag1 = (CompoundTag)tag;
            UUID uuid = compoundTag1.getUUID("uuid");
            SimpleContainer simpleContainer = new SimpleContainer(2) {
                @Override
                public void setChanged() {
                    super.setChanged();
                    InventoryData.this.setDirty();
                }
            };
            simpleContainer.fromTag(compoundTag1.getList("items",10));
            invMap.put(uuid,simpleContainer);
        }
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        ListTag listTag = new ListTag();

        for (Map.Entry<UUID,SimpleContainer> entry : invMap.entrySet()) {
            CompoundTag tag = new CompoundTag();
            tag.putUUID("uuid",entry.getKey());
            tag.put("items",entry.getValue().createTag());
            listTag.add(tag);
        }

        compoundTag.put("list",listTag);

        return compoundTag;
    }
}
