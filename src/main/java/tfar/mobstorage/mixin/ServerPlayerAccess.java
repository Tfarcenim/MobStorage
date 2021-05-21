package tfar.mobstorage.mixin;

import net.minecraft.server.level.ServerPlayer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

@Mixin(ServerPlayer.class)
public interface ServerPlayerAccess {
    @Accessor int getContainerCounter();

    @Invoker("nextContainerCounter") void $nextContainerCounter();
}
