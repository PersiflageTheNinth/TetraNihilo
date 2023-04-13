package io.github.persiflagetheninth.tetra_nihilo.compat;

import dev.ftb.ftbsba.tools.recipies.CrookDropsResult;
import dev.ftb.ftbsba.tools.recipies.ItemWithChance;
import dev.ftb.ftbsba.tools.recipies.ToolsRecipeCache;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fml.ModList;
import novamachina.exnihilosequentia.common.block.InfestedLeavesBlock;
import novamachina.exnihilosequentia.common.crafting.ItemStackWithChance;
import novamachina.exnihilosequentia.common.crafting.crook.CrookRecipe;
import novamachina.exnihilosequentia.common.init.ExNihiloItems;
import novamachina.exnihilosequentia.common.registries.ExNihiloRegistries;
import novamachina.exnihilosequentia.common.utility.Config;

import java.util.Collections;
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

    public static ObjectArrayList<ItemStack> getHammerDrops(Level level, BlockState state, RandomSource random) {
        if (isLoaded("exnihilosequentia")) {
            return NihiloToolCompat.NihiloHelper.hammerDrops(level, state.getBlock(), random);
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

    public static ObjectArrayList<ItemStack> getCrookDrops(ObjectArrayList<ItemStack> generatedLoot, Level level, BlockState state, RandomSource random) {
        if (isLoaded("exnihilosequentia")) {
            ObjectArrayList<ItemStack> newLoot = NihiloToolCompat.NihiloHelper.crookDrops(level, state.getBlock(), random);
            generatedLoot.addAll(newLoot);
            return generatedLoot;
        } else if (isLoaded("ftbsba")) {
            return NihiloToolCompat.FTBHelper.crookDrops(level, new ItemStack(state.getBlock()), random);
        }
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

        public static ObjectArrayList<ItemStack> crookDrops(Level level, ItemStack block, RandomSource random) {
            ObjectArrayList<ItemStack> loot = new ObjectArrayList<>();
            CrookDropsResult ftbLoot = ToolsRecipeCache.getCrookDrops(level, block);
            for (ItemWithChance stack : ftbLoot.items()) {
                if (random.nextFloat() <= stack.chance() && stack.item() != ItemStack.EMPTY) {
                    loot.add(stack.item());
                }
            }
            if (loot.size() > ftbLoot.max()) {
                Collections.shuffle(loot);
                loot.size(ftbLoot.max());
            }
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

        public static ObjectArrayList<ItemStack> hammerDrops(Level level, Block block, RandomSource random) {
            ObjectArrayList<ItemStack> loot = new ObjectArrayList<>();
            List<ItemStackWithChance> list = ExNihiloRegistries.HAMMER_REGISTRY.getResult(block);
            for (ItemStackWithChance stack : list) {
                if (random.nextFloat() <= stack.getChance() && stack.getStack() != ItemStack.EMPTY) {
                    loot.add(stack.getStack());
                }
            }
            return loot;
        }

        public static ObjectArrayList<ItemStack> crookDrops(Level level, Block block, RandomSource random) {
            ObjectArrayList<ItemStack> loot = new ObjectArrayList<>();
            List<CrookRecipe> list = ExNihiloRegistries.CROOK_REGISTRY.getDrops(block);
            for (CrookRecipe recipe : list) {
                for (ItemStackWithChance stack : recipe.getOutput()) {
                    if (random.nextFloat() <= stack.getChance() && stack.getStack() != ItemStack.EMPTY) {
                        loot.add(stack.getStack());
                    }
                }
            }
            if (block instanceof InfestedLeavesBlock) {
                loot.add(new ItemStack(Items.STRING, random.nextInt(Config.getMaxBonusStringCount()) + Config.getMinStringCount()));
                if (random.nextDouble() <= 0.8) {
                    loot.add(new ItemStack(ExNihiloItems.SILKWORM.get()));
                }
            }
            return loot;
        }
    }
}
