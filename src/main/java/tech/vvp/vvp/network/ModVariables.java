// tech/vvp/vvp/network/ModVariables.java

package tech.vvp.vvp.network;

import com.atsuishio.superbwarfare.capability.entity.PlayerVariable;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import tech.vvp.vvp.VVP;

/**
 * Этот класс теперь отвечает ТОЛЬКО за обработку игровых событий,
 * чтобы гарантировать, что данные из SuperbWarfare копируются и синхронизируются правильно.
 * Вся логика переменных и пакетов находится в самом SuperbWarfare.
 */
@Mod.EventBusSubscriber(modid = VVP.MOD_ID)
public class ModVariables {

    // Событие для копирования данных при смерти и респавне игрока
    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        // Мы копируем данные только после смерти, чтобы сохранить переменные
        if (event.isWasDeath()) {
            Player original = event.getOriginal();
            Player newPlayer = event.getEntity();

            // Получаем "capability" из SuperbWarfare у старого и нового игрока
            // и вызываем метод копирования из самого SuperbWarfare.
            // Это самый правильный и безопасный способ.
            PlayerVariable.getCap(original).ifPresent(oldStore -> {
                PlayerVariable.getCap(newPlayer).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    // Событие для синхронизации данных при входе в мир
    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        // Вызываем стандартный метод синхронизации из SuperbWarfare
        PlayerVariable.sync(event.getEntity());
    }

    // Событие для синхронизации данных при смене измерения
    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        PlayerVariable.sync(event.getEntity());
    }
}