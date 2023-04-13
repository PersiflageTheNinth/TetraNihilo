package io.github.persiflagetheninth.tetra_nihilo.compat;

import dev.ftb.ftbsba.tools.recipies.ToolsRecipeCache;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import novamachina.exnihilosequentia.common.crafting.ItemStackWithChance;
import novamachina.exnihilosequentia.common.registries.ExNihiloRegistries;

import java.util.List;

public class NihiloToolCompat {
    public static boolean isLoaded(String mod) {
        return ModList.get().isLoaded(mod);
    }
    public static boolean canHammer(BlockState state) {
        if (isLoaded("exnihilosequentia")) {
            return NihiloToolCompat.NihiloHelper.hammerable(state);
        } else if (isLoaded("ftbsba")) {
            return NihiloToolCompat.FTBHelper.hammerable(state);
        }
        return false;
    }
    public static ObjectArrayList<ItemStack> getHammerDrops(Level level, BlockState state) {
        if (isLoaded("exnihilosequentia")) {
            return NihiloToolCompat.NihiloHelper.hammerDrops(level, state.getBlock());
        } else if (isLoaded("ftbsba")) {
            return NihiloToolCompat.FTBHelper.hammerDrops(level, new ItemStack(state.getBlock()));
        }
        return new ObjectArrayList<>();
    }
    public static boolean canCrook(BlockState state) {
        if (isLoaded("exnihilosequentia")) {
            return NihiloToolCompat.NihiloHelper.crookable(state);
        } else if (isLoaded("ftbsba")) {
            return NihiloToolCompat.FTBHelper.crookable(state);
        }
        return false;
    }
    public static ObjectArrayList<ItemStack> getCrookDrops(Level level, BlockState state) {
        return new ObjectArrayList<>();
    }
    public static class FTBHelper {
        public static boolean hammerable(BlockState state) {
            return ToolsRecipeCache.hammerable(state);
        }
        public static boolean crookable(BlockState state) {
            return ToolsRecipeCache.crookable(state);
        }
        public static ObjectArrayList<ItemStack> hammerDrops(Level level, ItemStack block) {
            ObjectArrayList<ItemStack> loot = new ObjectArrayList<>();
            List<ItemStack> ftbLoot = ToolsRecipeCache.getHammerDrops(level, block);
            ftbLoot.stream().map(ItemStack::copy).forEach(loot::add);
            return loot;
        }
    }

    public static class NihiloHelper {
        public static boolean hammerable(BlockState state) {
            return ExNihiloRegistries.HAMMER_REGISTRY.isHammerable(state.getBlock());
        }
        public static boolean crookable(BlockState state) {
            return ExNihiloRegistries.CROOK_REGISTRY.isCrookable(state.getBlock());
        }
        public static ObjectArrayList<ItemStack> hammerDrops(Level level, Block block) {
            ObjectArrayList<ItemStack> loot = new ObjectArrayList<>();
            List<ItemStackWithChance> list = ExNihiloRegistries.HAMMER_REGISTRY.getResult(block);
            for (ItemStackWithChance stack : list) {
                if(level.getRandom().nextFloat() <= stack.getChance() && stack.getStack() != ItemStack.EMPTY) {
                    loot.add(stack.getStack());
                }
            }
            return loot;
        }
    }
}
