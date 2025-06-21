// Полный путь: src/main/java/tech/vvp/vvp/VVP.java
package tech.vvp.vvp;

import com.mojang.logging.LogUtils;
import net.neoforged.fml.common.Mod;
import net.neoforged.fml.event.lifecycle.FMLCommonSetupEvent;
import net.neoforged.fml.javafmlmod.FMLJavaModLoadingContext;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.common.NeoForge;
import org.slf4j.Logger;
import net.minecraft.resources.ResourceLocation;
import tech.vvp.vvp.network.ModVariables;

@Mod(VVP.MOD_ID)
public class VVP {
    public static final String MOD_ID = "vvp";
    private static final Logger LOGGER = LogUtils.getLogger();

    public VVP() {
        IEventBus modEventBus = FMLJavaModLoadingContext.get().getModEventBus();

        // --- РЕГИСТРАЦИЯ ВСЕЙ ЛОГИКИ ИЗ MODVARIABLES ---
        // Регистрируем обработчики для загрузки мода (капы, пакеты)
        modEventBus.register(ModVariables.RegistrationEvents.class);
        // Регистрируем обработчики для игровых событий (вход игрока и т.д.)
        NeoForge.EVENT_BUS.register(ModVariables.GameEvents.class);
        // ----------------------------------------------------

        modEventBus.addListener(this::setup);
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