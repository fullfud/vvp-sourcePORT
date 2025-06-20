package tech.vvp.vvp.config;

import net.neoforged.neoforge.common.ForgeConfigSpec;
import net.neoforged.neoforge.fml.ModLoadingContext;

public class ModConfig {
    public static void register() {
        ForgeConfigSpec.Builder builder = new ForgeConfigSpec.Builder();
        ModLoadingContext.get().registerConfig(net.minecraftforge.fml.config.ModConfig.Type.COMMON, builder.build());
    }
} 