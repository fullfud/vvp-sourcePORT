package tech.vvp.vvp.init;

import net.minecraft.world.item.Item;
import net.minecraft.world.item.Rarity;
import net.minecraft.world.item.RecordItem;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;
import tech.vvp.vvp.VVP;
import tech.vvp.vvp.item.armor.usahelmet;
import tech.vvp.vvp.item.armor.usachest;


public class ModItems {
    public static final DeferredRegister<Item> REGISTRY =
            DeferredRegister.create(ForgeRegistries.ITEMS, VVP.MOD_ID);

    public static final RegistryObject<Item> SHELL_30MM = REGISTRY.register("shell_30mm",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> RUFLAG = REGISTRY.register("ruflag",
            () -> new Item(new Item.Properties().stacksTo(64)));

    public static final RegistryObject<Item> ICON_SPAWN_ITEM = REGISTRY.register("icon_spawn_item",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ICON_CIVILIAN = REGISTRY.register("icon_civilian",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> ARMOR_ICON = REGISTRY.register("armor_icon",
            () -> new Item(new Item.Properties()));

    public static final RegistryObject<Item> USA_HELMET = REGISTRY.register("usahelmet",
            usahelmet::new);

    public static final RegistryObject<Item> USA_CHEST = REGISTRY.register("usachest",
            usachest::new);

    public static final RegistryObject<Item> RADIOHEAD = REGISTRY.register("music_disc_radiohead",
            () -> new RecordItem(15, ModSounds.RADIOHEAD,
                    new Item.Properties().stacksTo(1).rarity(Rarity.RARE), 4800));


    public static void register(IEventBus eventBus) {
        REGISTRY.register(eventBus);
    }
}