// tech/vvp/vvp/network/ModVariables.java
package tech.vvp.vvp.network;

import com.atsuishio.superbwarfare.capability.entity.PlayerVariable;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import tech.vvp.vvp.VVP;

@Mod.EventBusSubscriber(modid = VVP.MOD_ID)
public class ModVariables {

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.isWasDeath()) {
            Player original = event.getOriginal();
            Player newPlayer = event.getEntity();
            PlayerVariable.getCap(original).ifPresent(oldStore -> {
                PlayerVariable.getCap(newPlayer).ifPresent(newStore -> {
                    newStore.copyFrom(oldStore);
                });
            });
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof Player player) {
            PlayerVariable.sync(player);
        }
    }

    @SubscribeEvent
    public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
        if (event.getEntity() instanceof Player player) {
            PlayerVariable.sync(player);
        }
    }
}