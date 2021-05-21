package tfar.mobstorage;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.gui.screens.inventory.InventoryScreen;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.animal.horse.AbstractChestedHorse;
import net.minecraft.world.entity.player.Inventory;

public class MobInventoryScreen extends AbstractContainerScreen<MobInventoryMenu> {
    private static final ResourceLocation HORSE_INVENTORY_LOCATION = new ResourceLocation("textures/gui/container/horse.png");
    private final Mob horse;
    private float xMouse;
    private float yMouse;

    public MobInventoryScreen(MobInventoryMenu horseInventoryMenu, Inventory inventory, Mob abstractHorse) {
        super(horseInventoryMenu, inventory, abstractHorse.getDisplayName());
        this.horse = abstractHorse;
        this.passEvents = false;
    }

    protected void renderBg(PoseStack poseStack, float f, int i, int j) {
        RenderSystem.color4f(1.0F, 1.0F, 1.0F, 1.0F);
        this.minecraft.getTextureManager().bind(HORSE_INVENTORY_LOCATION);
        int k = (this.width - this.imageWidth) / 2;
        int l = (this.height - this.imageHeight) / 2;
        this.blit(poseStack, k, l, 0, 0, this.imageWidth, this.imageHeight);
        if (this.horse instanceof AbstractChestedHorse) {
            AbstractChestedHorse abstractChestedHorse = (AbstractChestedHorse)this.horse;
            if (abstractChestedHorse.hasChest()) {
                this.blit(poseStack, k + 79, l + 17, 0, this.imageHeight, abstractChestedHorse.getInventoryColumns() * 18, 54);
            }
        }

        if (true) {
            this.blit(poseStack, k + 7, l + 35 - 18, 18, this.imageHeight + 54, 18, 18);
        }

        InventoryScreen.renderEntityInInventory(k + 51, l + 60, 17, (float)(k + 51) - this.xMouse, (float)(l + 75 - 50) - this.yMouse, this.horse);
    }

    public void render(PoseStack poseStack, int i, int j, float f) {
        this.renderBackground(poseStack);
        this.xMouse = (float)i;
        this.yMouse = (float)j;
        super.render(poseStack, i, j, f);
        this.renderTooltip(poseStack, i, j);
    }
}
