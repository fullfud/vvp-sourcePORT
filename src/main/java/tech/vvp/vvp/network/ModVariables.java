package tech.vvp.vvp.network;

import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.capabilities.EntityCapability;
import net.neoforged.neoforge.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.common.util.INBTSerializable;
import net.neoforged.neoforge.event.AttachCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.event.RegisterPayloadHandlersEvent;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.vvp.vvp.VVP;

@Mod.EventBusSubscriber(modid = VVP.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
public class ModVariables {

    // --- Capabilities ---
    public static final EntityCapability<PlayerVariable, Void> PLAYER_VARIABLE =
            EntityCapability.createVoid(new ResourceLocation(VVP.MOD_ID, "player_variable"), PlayerVariable.class);

    @SubscribeEvent
    public static void registerCapabilities(RegisterCapabilitiesEvent event) {
        event.register(PLAYER_VARIABLE);
    }

    // --- Сетевые пакеты (Payloads) ---
    public record PlayerVariablesSyncPayload(CompoundTag data) implements CustomPacketPayload {
        public static final ResourceLocation ID = new ResourceLocation(VVP.MOD_ID, "player_variables_sync");

        public PlayerVariablesSyncPayload(FriendlyByteBuf buf) {
            this(buf.readNbt());
        }

        @Override
        public void write(FriendlyByteBuf buf) {
            buf.writeNbt(data);
        }

        @Override
        public @NotNull ResourceLocation id() {
            return ID;
        }
    }

    @SubscribeEvent
    public static void registerPayloads(RegisterPayloadHandlersEvent event) {
        event.registrar(VVP.MOD_ID)
                .play(PlayerVariablesSyncPayload.ID, PlayerVariablesSyncPayload::new, handler -> handler
                        .client(PayloadHandlers::handlePlayerVariablesSync));
    }

    // --- Игровые события для синхронизации и сохранения ---
    // Этот класс будет автоматически подхвачен, так как он находится внутри класса с аннотацией
    @Mod.EventBusSubscriber(modid = VVP.MOD_ID)
    private static class GameEvents {
        @SubscribeEvent
        public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
                // Прикрепляем наш провайдер к игроку
                event.registerCapability(PLAYER_VARIABLE, new PlayerVariablesProvider());
            }
        }

        @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                syncPlayerVariables(player);
            }
        }

        @SubscribeEvent
        public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                syncPlayerVariables(player);
            }
        }

        @SubscribeEvent
        public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                syncPlayerVariables(player);
            }
        }

        @SubscribeEvent
        public static void onPlayerClone(PlayerEvent.Clone event) {
            if (event.isWasDeath()) {
                Player original = event.getOriginal();
                Player newPlayer = event.getEntity();
                
                // Используем Provider для доступа к данным
                original.getCapabilityProvider().getCapability(PLAYER_VARIABLE).ifPresent(oldStore -> {
                    newPlayer.getCapabilityProvider().getCapability(PLAYER_VARIABLE).ifPresent(newStore -> {
                        newStore.copyFrom(oldStore);
                    });
                });
            }
        }
    }

    // --- Логика синхронизации ---
    public static void syncPlayerVariables(Player player) {
        if (player instanceof ServerPlayer serverPlayer) {
            serverPlayer.getCapabilityProvider().getCapability(PLAYER_VARIABLE).ifPresent(cap -> {
                PacketDistributor.sendToPlayer(serverPlayer, new PlayerVariablesSyncPayload(cap.serializeNBT(serverPlayer.registryAccess())));
            });
        }
    }

    // --- Провайдер для Capability ---
    private static class PlayerVariablesProvider implements ICapabilityProvider<Player, Void, PlayerVariable>, INBTSerializable<CompoundTag> {
        private final PlayerVariable playerVariable = new PlayerVariable();

        @Override
        public @NotNull PlayerVariable getCapability(@NotNull Player owner, @Nullable Void context) {
            return playerVariable;
        }

        @Override
        public CompoundTag serializeNBT(HolderLookup.Provider provider) {
            return playerVariable.saveNBTData(new CompoundTag());
        }

        @Override
        public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
            playerVariable.loadNBTData(nbt);
        }
    }

    // --- Обработчики пакетов ---
    private static class PayloadHandlers {
        public static void handlePlayerVariablesSync(final PlayerVariablesSyncPayload payload, final IPayloadContext context) {
            context.enqueueWork(() -> {
                Player player = context.player();
                if (player != null) {
                    player.getCapabilityProvider().getCapability(PLAYER_VARIABLE).ifPresent(cap -> {
                        cap.loadNBTData(payload.data());
                    });
                }
            });
        }
    }
}