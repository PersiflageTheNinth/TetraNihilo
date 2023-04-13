package io.github.persiflagetheninth.tetra_nihilo.tool;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.tags.BlockTags;
import net.minecraft.tags.TagKey;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.common.ToolAction;
import se.mickelus.tetra.util.ToolActionHelper;

public class TetraNihiloToolActions {
    public static final ToolAction crook = ToolAction.get("crook_dig");
    public static final TagKey<Block> crookMineable = BlockTags.create(new ResourceLocation("mineable/crook"));

    public static void init() {
        ToolActionHelper.appropriateTools.put(crook, crookMineable);
    }
}
