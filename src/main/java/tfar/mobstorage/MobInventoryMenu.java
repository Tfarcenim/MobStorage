package tfar.mobstorage;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.Container;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;

public class MobInventoryMenu extends AbstractContainerMenu {
    private final Container horseContainer;
    private final Mob horse;

    public MobInventoryMenu(int i, Inventory inventory, Container container, final Mob abstractHorse) {
        super(null, i);
        this.horseContainer = container;
        this.horse = abstractHorse;
        container.startOpen(inventory.player);
        this.addSlot(new Slot(container, 0, 8, 18) {
            public boolean mayPlace(ItemStack itemStack) {
                return itemStack.getItem() == MobStorage.FLYING_SADDLE && !this.hasItem();
            }

            @Environment(EnvType.CLIENT)
            public boolean isActive() {
                return true;
            }
        });
        int p;
        int o;

        for (p = 0; p < 3; ++p) {
            for (o = 0; o < 9; ++o) {
                this.addSlot(new Slot(inventory, o + p * 9 + 9, 8 + o * 18, 102 + p * 18 + -18));
            }
        }

        for (p = 0; p < 9; ++p) {
            this.addSlot(new Slot(inventory, p, 8 + p * 18, 142));
        }

    }

    public boolean stillValid(Player player) {
        return this.horseContainer.stillValid(player) && this.horse.isAlive() && this.horse.distanceTo(player) < 8.0F;
    }

    public ItemStack quickMoveStack(Player player, int i) {
        ItemStack itemStack = ItemStack.EMPTY;
        Slot slot = this.slots.get(i);
        if (slot != null && slot.hasItem()) {
            ItemStack itemStack2 = slot.getItem();
            itemStack = itemStack2.copy();
            int j = this.horseContainer.getContainerSize();
            if (i < j) {
                if (!this.moveItemStackTo(itemStack2, j, this.slots.size(), true)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(1).mayPlace(itemStack2) && !this.getSlot(1).hasItem()) {
                if (!this.moveItemStackTo(itemStack2, 1, 2, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (this.getSlot(0).mayPlace(itemStack2)) {
                if (!this.moveItemStackTo(itemStack2, 0, 1, false)) {
                    return ItemStack.EMPTY;
                }
            } else if (j <= 2 || !this.moveItemStackTo(itemStack2, 2, j, false)) {
                int l = j + 27;
                int n = l + 9;
                if (i >= l && i < n) {
                    if (!this.moveItemStackTo(itemStack2, j, l, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (i >= j && i < l) {
                    if (!this.moveItemStackTo(itemStack2, l, n, false)) {
                        return ItemStack.EMPTY;
                    }
                } else if (!this.moveItemStackTo(itemStack2, l, l, false)) {
                    return ItemStack.EMPTY;
                }

                return ItemStack.EMPTY;
            }

            if (itemStack2.isEmpty()) {
                slot.set(ItemStack.EMPTY);
            } else {
                slot.setChanged();
            }
        }

        return itemStack;
    }

    public void removed(Player player) {
        super.removed(player);
        if (!player.level.isClientSide) {
            InventoryData data = InventoryData.get((ServerLevel)player.level);
            data.setDirty();
        }
        this.horseContainer.stopOpen(player);
    }
}
