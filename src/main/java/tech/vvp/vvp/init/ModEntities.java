package tech.vvp.vvp.init;

import com.atsuishio.superbwarfare.entity.projectile.*;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.neoforged.neoforge.registries.ForgeRegistries;
import net.neoforged.neoforge.registries.RegistryObject;
import tech.vvp.vvp.VVP;
import tech.vvp.vvp.entity.vehicle.*;

public class ModEntities {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, VVP.MOD_ID);

    public static final RegistryObject<EntityType<vazikEntity>> VAZIK = ENTITY_TYPES.register("vazik",
            () -> EntityType.Builder.of(vazikEntity::new, MobCategory.MISC)
                    .setTrackingRange(64)
                    .setUpdateInterval(1)
                    .fireImmune()
                    .sized(2.7f, 2.3f)
                    .build("vazik"));

    public static final RegistryObject<EntityType<m997Entity>> M997 = ENTITY_TYPES.register("m997",
            () -> EntityType.Builder.of(m997Entity::new, MobCategory.MISC)
                    .setTrackingRange(64)
                    .setUpdateInterval(1)
                    .fireImmune()
                    .sized(4.2f, 3.2f)
                    .build("m997"));

    public static final RegistryObject<EntityType<m997_greenEntity>> M997_GREEN = ENTITY_TYPES.register("m997_green",
            () -> EntityType.Builder.of(m997_greenEntity::new, MobCategory.MISC)
                    .setTrackingRange(64)
                    .setUpdateInterval(1)
                    .fireImmune()
                    .sized(4.2f, 3.2f)
                    .build("m997_green"));

    public static final RegistryObject<EntityType<btr80aEntity>> BTR_80A = ENTITY_TYPES.register("btr_80a",
            () -> EntityType.Builder.of(btr80aEntity::new, MobCategory.MISC)
                    .setTrackingRange(64)
                    .setUpdateInterval(1)
                    .fireImmune()
                    .sized(3.9f, 3.2f)
                    .build("btr_80a"));

    public static final RegistryObject<EntityType<btr80a_1Entity>> BTR_80A_1 = ENTITY_TYPES.register("btr_80a_1",
            () -> EntityType.Builder.of(btr80a_1Entity::new, MobCategory.MISC)
                    .setTrackingRange(64)
                    .setUpdateInterval(1)
                    .fireImmune()
                    .sized(3.9f, 3.2f)
                    .build("btr_80a_1"));

    public static final RegistryObject<EntityType<bikegreenEntity>> BIKEGREEN = ENTITY_TYPES.register("bikegreen",
            () -> EntityType.Builder.of(bikegreenEntity::new, MobCategory.MISC)
                    .setTrackingRange(64)
                    .setUpdateInterval(1)
                    .fireImmune()
                    .sized(0.9f, 1.2f)
                    .build("bikegreen"));

    public static final RegistryObject<EntityType<bikeredEntity>> BIKERED = ENTITY_TYPES.register("bikered",
            () -> EntityType.Builder.of(bikeredEntity::new, MobCategory.MISC)
                    .setTrackingRange(64)
                    .setUpdateInterval(1)
                    .fireImmune()
                    .sized(0.9f, 1.2f)
                    .build("bikered"));

    public static final RegistryObject<EntityType<mi24Entity>> MI24 = ENTITY_TYPES.register("mi24",
            () -> EntityType.Builder.of(mi24Entity::new, MobCategory.MISC)
                    .setTrackingRange(64).setUpdateInterval(1).fireImmune().sized(4.5f, 4.8f)
                    .build("mi24"));

    public static final RegistryObject<EntityType<mi24ukrEntity>> MI24UKR = ENTITY_TYPES.register("mi24ukr",
            () -> EntityType.Builder.of(mi24ukrEntity::new, MobCategory.MISC)
                    .setTrackingRange(64).setUpdateInterval(1).fireImmune().sized(4.5f, 4.8f)
                    .build("mi24ukr"));

    public static final RegistryObject<EntityType<mi24polEntity>> MI24POL = ENTITY_TYPES.register("mi24polsha",
            () -> EntityType.Builder.of(mi24polEntity::new, MobCategory.MISC)
                    .setTrackingRange(64).setUpdateInterval(1).fireImmune().sized(4.5f, 4.8f)
                    .build("mi24polsha"));

    public static final RegistryObject<EntityType<cobraEntity>> COBRA = ENTITY_TYPES.register("cobra",
            () -> EntityType.Builder.of(cobraEntity::new, MobCategory.MISC)
                    .setTrackingRange(64).setUpdateInterval(1).fireImmune().sized(4.5f, 4.8f)
                    .build("cobra"));

    public static final RegistryObject<EntityType<cobrasharkEntity>> COBRASHARK = ENTITY_TYPES.register("cobrashark",
            () -> EntityType.Builder.of(cobrasharkEntity::new, MobCategory.MISC)
                    .setTrackingRange(64).setUpdateInterval(1).fireImmune().sized(4.5f, 4.8f)
                    .build("cobrashark"));
                    
    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}