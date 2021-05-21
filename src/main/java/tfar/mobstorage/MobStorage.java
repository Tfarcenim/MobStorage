package tfar.mobstorage;

import net.fabricmc.api.ModInitializer;
import net.minecraft.core.Registry;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Mob;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.SaddleItem;

public class MobStorage implements ModInitializer {

	public static final Item FLYING_SADDLE = new SaddleItem(new Item.Properties().stacksTo(1).tab(CreativeModeTab.TAB_TRANSPORTATION));

	public static final String MODID = "mobstorage";

	@Override
	public void onInitialize() {
		Registry.register(Registry.ITEM,new ResourceLocation(MODID,"flying_saddle"),FLYING_SADDLE);
	}

	public static boolean mobInteract(Player player, InteractionHand interactionHand, Mob mob, InteractionResult result) {
		if (!player.level.isClientSide) {
			ServerPlayer serverPlayer = (ServerPlayer)player;
			InventoryData data = InventoryData.get((ServerLevel) player.level);
			if (player.isCrouching()) {
				data.openInventory(player, mob);
			} else {
				mob.setAggressive(false);
				mob.setTarget(null);
				player.startRiding(mob);
				mob.setNoGravity(true);
			}
		}
		return true;
	}
}
