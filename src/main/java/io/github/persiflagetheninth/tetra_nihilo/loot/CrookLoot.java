package io.github.persiflagetheninth.tetra_nihilo.loot;

import com.google.common.base.Suppliers;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;
import io.github.persiflagetheninth.tetra_nihilo.TetraNihilo;
import io.github.persiflagetheninth.tetra_nihilo.compat.NihiloToolCompat;
import io.github.persiflagetheninth.tetra_nihilo.tool.TetraNihiloToolActions;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootContext;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.common.loot.LootModifier;
import org.jetbrains.annotations.NotNull;
import se.mickelus.tetra.items.modular.ItemModularHandheld;

import java.util.function.Supplier;

public class CrookLoot extends LootModifier {
    public static final Supplier<Codec<CrookLoot>> CODEC = Suppliers.memoize(() ->
            RecordCodecBuilder.create(instance -> codecStart(instance).apply(instance, CrookLoot::new)));

    public CrookLoot(LootItemCondition[] conditionsIn) {
        super(conditionsIn);
    }

    @Override
    protected @NotNull ObjectArrayList<ItemStack> doApply(ObjectArrayList<ItemStack> generatedLoot, LootContext context) {
        TetraNihilo.LOGGER.debug("Fired Crook Modifier");
        ItemStack tool = context.getParamOrNull(LootContextParams.TOOL);
        Entity entity = context.getParamOrNull(LootContextParams.THIS_ENTITY);
        BlockState blockState = context.getParamOrNull(LootContextParams.BLOCK_STATE);
        if (tool != null && blockState != null && entity instanceof Player
                && tool.getItem() instanceof ItemModularHandheld
                && tool.canPerformAction(TetraNihiloToolActions.crook)
                && NihiloToolCompat.canCrook(blockState)) {
            ObjectArrayList<ItemStack> changedLoot = NihiloToolCompat.getCrookDrops(generatedLoot, entity.level, blockState, context.getRandom());
            if (!changedLoot.isEmpty()) {
                return changedLoot;
            }
        }
        return generatedLoot;
    }

    @Override
    public Codec<? extends IGlobalLootModifier> codec() {
        return null;
    }
}
