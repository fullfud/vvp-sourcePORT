// Полный путь: D:\Downloads\vvp-sourcePORT\src\main\java\tech\vvp\vvp\config\VehicleConfigVVP.java
package tech.vvp.vvp.config;

import net.neoforged.neoforge.common.ModConfigSpec; // Переименовано для ясности, но ForgeConfigSpec тоже работает

public class VehicleConfigVVP {
    // Builder и Spec остаются такими же
    public static final ModConfigSpec.Builder BUILDER = new ModConfigSpec.Builder();
    public static final ModConfigSpec SPEC;

    // Сделал все поля final - это лучшая практика
    public static final ModConfigSpec.IntValue TYPHOON_MAX_ENERGY;
    public static final ModConfigSpec.IntValue TYPHOON_HP;
    public static final ModConfigSpec.IntValue TYPHOON_ENERGY_COST;
    public static final ModConfigSpec.DoubleValue TYPHOON_ENERGY_MULTIPLIER;
    public static final ModConfigSpec.IntValue TYPHOON_SHOOT_COST;

    public static final ModConfigSpec.IntValue MI_24_MAX_ENERGY;
    public static final ModConfigSpec.IntValue MI_24_MAX_ENERGY_COST;
    public static final ModConfigSpec.IntValue MI_24_MIN_ENERGY_COST;
    public static final ModConfigSpec.IntValue MI_24_HP;

    public static final ModConfigSpec.IntValue MI_24_CANNON_DAMAGE;
    public static final ModConfigSpec.IntValue MI_24_CANNON_EXPLOSION_DAMAGE;
    public static final ModConfigSpec.DoubleValue MI_24_CANNON_EXPLOSION_RADIUS;
    public static final ModConfigSpec.IntValue MI_24_ROCKET_DAMAGE;
    public static final ModConfigSpec.IntValue MI_24_ROCKET_EXPLOSION_DAMAGE;
    public static final ModConfigSpec.IntValue MI_24_ROCKET_EXPLOSION_RADIUS;
    public static final ModConfigSpec.BooleanValue MI_24_CANNON_DESTROY;

    // JAGUAR конфиги тоже должны быть final
    public static final ModConfigSpec.IntValue JAGUAR_MAX_ENERGY;
    public static final ModConfigSpec.IntValue JAGUAR_HP;
    public static final ModConfigSpec.IntValue JAGUAR_MAX_ENERGY_COST;
    public static final ModConfigSpec.IntValue JAGUAR_MIN_ENERGY_COST;

    // Статический блок инициализации остается таким же
    static {
        BUILDER.push("Vehicle Configs for VVP");

        TYPHOON_MAX_ENERGY = BUILDER
                .comment("Maximum energy for Typhoon vehicle")
                .defineInRange("typhoon_max_energy", 6500, 1, 10000000);

        TYPHOON_HP = BUILDER
                .comment("Health points for Typhoon vehicle")
                .defineInRange("typhoon_hp", 250, 1, 10000000);

        TYPHOON_ENERGY_COST = BUILDER
                .comment("Energy cost for Typhoon vehicle")
                .defineInRange("typhoon_energy_cost", 64, 0, 2147483647);

        TYPHOON_ENERGY_MULTIPLIER = BUILDER
                .comment("Energy multiplier for Typhoon vehicle")
                .defineInRange("typhoon_energy_multiplier", 0.1, 0.01, 1.0);

        TYPHOON_SHOOT_COST = BUILDER
                .comment("Shooting cost for Typhoon vehicle")
                .defineInRange("typhoon_shoot_cost", 10, 1, 1000);

        BUILDER.push("Mi-24 Configs"); // Добавил под-категорию для порядка
        BUILDER.comment("Deprecated, use datapack to change this value instead");
        MI_24_HP = BUILDER.defineInRange("mi_24_hp", 250, 1, 10000000);
        MI_24_MAX_ENERGY_COST = BUILDER.comment("The max energy cost of MI-24 per tick").defineInRange("mi_24_max_energy_cost", 228, 0, 2147483647);
        MI_24_MAX_ENERGY = BUILDER.comment("Deprecated, use datapack to change this value instead").defineInRange("mi_24_max_energy", 5000000, 0, 2147483647);
        MI_24_MIN_ENERGY_COST = BUILDER.comment("The min energy cost of MI-24 per tick").defineInRange("mi_24_min_energy_cost", 64, 0, 2147483647);
        MI_24_CANNON_DAMAGE = BUILDER.comment("The cannon damage of MI-24").defineInRange("mi_24_cannon_damage", 25, 1, 10000000);
        MI_24_CANNON_EXPLOSION_DAMAGE = BUILDER.comment("The cannon explosion damage of MI-24").defineInRange("mi_24_cannon_explosion_damage", 13, 1, 10000000);
        // ИСПРАВЛЕНА ОПЕЧАТКА В КЛЮЧЕ! было "mi_24_cannon_explosion_damage"
        MI_24_CANNON_EXPLOSION_RADIUS = BUILDER.comment("The cannon explosion radius of MI-24").defineInRange("mi_24_cannon_explosion_radius", 4.0, 1.0, 1000.0);
        MI_24_ROCKET_DAMAGE = BUILDER.comment("The rocket damage of MI-24").defineInRange("mi_24_rocket_damage", 80, 1, 10000000);
        MI_24_ROCKET_EXPLOSION_DAMAGE = BUILDER.comment("The rocket explosion damage of MI-24").defineInRange("mi_24_rocket_explosion_damage", 40, 1, 10000000);
        MI_24_ROCKET_EXPLOSION_RADIUS = BUILDER.comment("The rocket explosion radius of MI-24").defineInRange("mi_24_rocket_explosion_radius", 5, 1, 10000000);
        MI_24_CANNON_DESTROY = BUILDER.comment("Whether to destroy the block when cannon of MI-24 hits a block").define("mi_24_cannon_destroy", true);
        BUILDER.pop();

        BUILDER.push("Jaguar Configs"); // Добавил под-категорию для порядка
        JAGUAR_MAX_ENERGY = BUILDER.defineInRange("jaguar_max_energy", 10000, 1, 100000000);
        JAGUAR_HP = BUILDER.defineInRange("jaguar_hp", 500, 1, 100000000);
        JAGUAR_MAX_ENERGY_COST = BUILDER.defineInRange("jaguar_max_energy_cost", 300, 0, 2147483647);
        JAGUAR_MIN_ENERGY_COST = BUILDER.defineInRange("jaguar_min_energy_cost", 80, 0, 2147483647);
        BUILDER.pop();

        BUILDER.pop(); // Закрываем главную категорию "Vehicle Configs for VVP"
        SPEC = BUILDER.build();
    }
}