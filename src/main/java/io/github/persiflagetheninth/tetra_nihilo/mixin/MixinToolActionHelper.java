package io.github.persiflagetheninth.tetra_nihilo.mixin;

import io.github.persiflagetheninth.tetra_nihilo.compat.NihiloToolCompat;
import io.github.persiflagetheninth.tetra_nihilo.tool.TetraNihiloToolActions;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.ToolAction;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import se.mickelus.tetra.TetraToolActions;
import se.mickelus.tetra.util.ToolActionHelper;

@Mixin(ToolActionHelper.class)
public abstract class MixinToolActionHelper {
    @Inject(method="isEffectiveOn(Lnet/minecraftforge/common/ToolAction;Lnet/minecraft/world/level/block/state/BlockState;)Z", at = @At("RETURN"), cancellable = true, remap = false)
    private static void onIsEffectiveOn(ToolAction action, BlockState state, CallbackInfoReturnable<Boolean> cir) {
        if (!cir.getReturnValue()) {
            if (action == TetraToolActions.hammer && NihiloToolCompat.canHammer(state)) {
                cir.setReturnValue(true);
            }
            if (action == TetraNihiloToolActions.crook && NihiloToolCompat.canCrook(state)) {
                cir.setReturnValue(true);
            }
        }
    }
}
