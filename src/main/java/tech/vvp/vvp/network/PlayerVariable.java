package tech.vvp.vvp.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import tech.vvp.vvp.tools.Ammo;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

public class PlayerVariable {

    public Map<Ammo, Integer> ammo = new HashMap<>();
    public boolean tacticalSprint = false;

    /**
     * Получает Capability, изменяет его и синхронизирует с клиентом.
     */
    public static void modify(Player player, Consumer<PlayerVariable> consumer) {
        PlayerVariable cap = player.getCapability(ModVariables.PLAYER_VARIABLE);
        if (cap != null) {
            consumer.accept(cap);
            ModVariables.syncPlayerVariables(player);
        }
    }

    /**
     * Копирует данные из одного capability в другой. Используется при респавне.
     */
    public void copyFrom(PlayerVariable source) {
        this.ammo = new HashMap<>(source.ammo);
        this.tacticalSprint = source.tacticalSprint;
    }

    /**
     * Сохраняет данные этого capability в NBT.
     */
    public CompoundTag saveNBTData(CompoundTag nbt) {
        CompoundTag ammoTag = new CompoundTag();
        for (Map.Entry<Ammo, Integer> entry : ammo.entrySet()) {
            ammoTag.putInt(entry.getKey().name(), entry.getValue());
        }
        nbt.put("ammo", ammoTag);
        nbt.putBoolean("tacticalSprint", tacticalSprint);
        return nbt;
    }

    /**
     * Загружает данные в этот capability из NBT.
     */
    public void loadNBTData(CompoundTag nbt) {
        if (nbt.contains("ammo", CompoundTag.TAG_COMPOUND)) {
            CompoundTag ammoTag = nbt.getCompound("ammo");
            this.ammo.clear();
            for (String key : ammoTag.getAllKeys()) {
                try {
                    Ammo ammoType = Ammo.valueOf(key);
                    this.ammo.put(ammoType, ammoTag.getInt(key));
                } catch (IllegalArgumentException e) {
                    // Игнорируем неверные или устаревшие типы патронов
                }
            }
        }
        this.tacticalSprint = nbt.getBoolean("tacticalSprint");
    }
}