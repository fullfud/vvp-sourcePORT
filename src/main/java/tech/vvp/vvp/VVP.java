package tech.vvp.vvp;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.bus.api.IEventBus;
import org.slf4j.Logger;
import net.minecraft.resources.ResourceLocation;

@Mod(VVP.MOD_ID)
public class VVP {
    public static final String MOD_ID = "vvp";
    private static final Logger LOGGER = LogUtils.getLogger();

    public VVP() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // Пример регистрации своих классов (если есть)
        // ModItems.register(modEventBus);
        // ModEntities.register(modEventBus);

        modEventBus.addListener(this::setup);

        // Глобальные ивенты (если нужны)
        // net.neoforged.neoforge.common.NeoForge.EVENT_BUS.register(this);
    }

    private void setup(final FMLCommonSetupEvent event) {
        event.enqueueWork(() -> {
            LOGGER.info("HELLO FROM COMMON SETUP");
            LOGGER.info("DIRT BLOCK >> {}", net.minecraft.world.level.block.Blocks.DIRT);
        });
    }

    public static ResourceLocation loc(String path) {
        return new ResourceLocation(MOD_ID, path);
    }
}