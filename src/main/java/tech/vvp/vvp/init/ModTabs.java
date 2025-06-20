package tech.vvp.vvp.init;

import com.atsuishio.superbwarfare.item.ContainerBlockItem;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.RegistryObject;
import tech.vvp.vvp.VVP;

@SuppressWarnings("unused")
public class ModTabs {
    public static final DeferredRegister<CreativeModeTab> TABS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, VVP.MOD_ID);

    // Общая вкладка со всеми предметами мода
    public static final RegistryObject<CreativeModeTab> VEHICLES = TABS.register("vvp", () -> CreativeModeTab.builder()
            .icon(() -> new ItemStack(ModItems.ICON_SPAWN_ITEM.get()))
            .title(Component.translatable("item_group.vvp.vvp"))
            .displayItems((parameters, output) -> {
                // Добавляем всю технику
                output.accept(ContainerBlockItem.createInstance(ModEntities.BTR_80A.get()));
                output.accept(ContainerBlockItem.createInstance(ModEntities.BTR_80A_1.get()));
                output.accept(ContainerBlockItem.createInstance(ModEntities.M997.get()));
                output.accept(ContainerBlockItem.createInstance(ModEntities.M997_GREEN.get()));
                output.accept(ContainerBlockItem.createInstance(ModEntities.MI24.get()));
                output.accept(ContainerBlockItem.createInstance(ModEntities.MI24POL.get()));
                output.accept(ContainerBlockItem.createInstance(ModEntities.MI24UKR.get()));
                output.accept(ContainerBlockItem.createInstance(ModEntities.COBRA.get()));
                output.accept(ContainerBlockItem.createInstance(ModEntities.COBRASHARK.get()));
            })
            .build());

    public static final RegistryObject<CreativeModeTab> CIVILIAN_VEHICLE = TABS.register("civilian_vehicle",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.vvp_civilian_vehicle_tab"))
                    .icon(() -> new ItemStack(ModItems.ICON_CIVILIAN.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ContainerBlockItem.createInstance(ModEntities.VAZIK.get()));
                        output.accept(ContainerBlockItem.createInstance(ModEntities.BIKEGREEN.get()));
                        output.accept(ContainerBlockItem.createInstance(ModEntities.BIKERED.get()));
                    })
                    .build());

    public static final RegistryObject<CreativeModeTab> ARMOR_TAB = TABS.register("armor_tab",
            () -> CreativeModeTab.builder()
                    .title(Component.translatable("itemGroup.vvp_armor_tab"))
                    .icon(() -> new ItemStack(ModItems.ARMOR_ICON.get()))
                    .displayItems((parameters, output) -> {
                        output.accept(ModItems.USA_HELMET.get());
                        output.accept(ModItems.USA_CHEST.get());
                    })
                    .build());
}