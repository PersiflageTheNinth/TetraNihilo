package io.github.persiflagetheninth.tetra_nihilo.registry;

import com.mojang.serialization.Codec;
import io.github.persiflagetheninth.tetra_nihilo.TetraNihilo;
import io.github.persiflagetheninth.tetra_nihilo.loot.HammerLoot;
import net.minecraftforge.common.loot.IGlobalLootModifier;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;

public class TetraNihiloLootModifiers {
    public static final DeferredRegister<Codec<?extends IGlobalLootModifier>> LOOT_MODIFIERS =
            DeferredRegister.create(ForgeRegistries.Keys.GLOBAL_LOOT_MODIFIER_SERIALIZERS, TetraNihilo.MODID);

    public static final RegistryObject<Codec<HammerLoot>> HAMMER = LOOT_MODIFIERS.register("from_hammer", HammerLoot.CODEC);
}
