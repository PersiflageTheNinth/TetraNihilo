package io.github.persiflagetheninth.tetra_nihilo;

import com.mojang.logging.LogUtils;
import io.github.persiflagetheninth.tetra_nihilo.registry.TetraNihiloLootModifiers;
import io.github.persiflagetheninth.tetra_nihilo.tool.TetraNihiloToolActions;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.javafmlmod.FMLJavaModLoadingContext;
import org.slf4j.Logger;

@Mod(TetraNihilo.MODID)
public class TetraNihilo {
    public static final String MODID = "tetra_nihilo";
    public static final Logger LOGGER = LogUtils.getLogger();

    public TetraNihilo() {
        MinecraftForge.EVENT_BUS.register(this);
        TetraNihiloToolActions.init();
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();
        TetraNihiloLootModifiers.LOOT_MODIFIERS.register(modEventBus);
    }
}
