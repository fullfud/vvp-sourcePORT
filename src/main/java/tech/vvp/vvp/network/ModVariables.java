package tech.vvp.vvp.network;

import net.minecraft.core.Direction;
import net.minecraft.core.HolderLookup;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.network.protocol.common.custom.CustomPacketPayload;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.common.capabilities.Capability;
import net.neoforged.neoforge.common.capabilities.CapabilityBuilder;
import net.neoforged.neoforge.common.capabilities.ICapabilityProvider;
import net.neoforged.neoforge.common.capabilities.RegisterCapabilitiesEvent;
import net.neoforged.neoforge.capabilities.events.AttachCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import net.neoforged.neoforge.network.handling.IPayloadContext;
import net.neoforged.neoforge.network.registration.IPayloadRegistrar;
import net.neoforged.neoforge.network.registration.RegisterPayloadHandlersEvent;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import tech.vvp.vvp.VVP;
import tech.vvp.vvp.tools.Ammo;

import java.util.Optional;

// Этот класс теперь будет просто контейнером для всего
public class ModVariables {

    // 1. РЕГИСТРАЦИЯ ВСЕГО (Capabilities и Packets)
    // Этот вложенный класс будет автоматически найден NeoForge, если ты зарегистрируешь его в главном классе
    @Mod.EventBusSubscriber(modid = VVP.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD)
    public static class RegistrationEvents {

        @SubscribeEvent
        public static void registerCapabilities(RegisterCapabilitiesEvent event) {
            // ЗАМЕНА: event.register(PlayerVariable.class) заменен на явную регистрацию для Entity
            event.registerEntity(PLAYER_VARIABLE, EntityType.PLAYER, (player, context) -> new PlayerVariablesProvider());
        }

        @SubscribeEvent
        public static void registerPayloads(RegisterPayloadHandlersEvent event) {
            final IPayloadRegistrar registrar = event.registrar(VVP.MOD_ID);
            // ЗАМЕНА: Старая система пакетов удалена. Регистрируем новые "Payloads".
            registrar.play(PlayerVariablesSyncPayload.ID, PlayerVariablesSyncPayload::new, handler -> handler.client(PayloadHandlers::handlePlayerVariablesSync));
            registrar.play(WorldVariablesSyncPayload.ID, WorldVariablesSyncPayload::new, handler -> handler.client(PayloadHandlers::handleWorldVariablesSync));
        }
    }

    // 2. ОБРАБОТЧИКИ ИГРОВЫХ СОБЫТИЙ
    // Этот класс тоже нужно будет зарегистрировать
    @Mod.EventBusSubscriber(modid = VVP.MOD_ID)
    public static class GameEvents {

        @SubscribeEvent
        public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player) {
                event.addCapability(new ResourceLocation(VVP.MOD_ID, "player_variables"), new PlayerVariablesProvider());
            }
        }

        @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                syncAllData(player);
            }
        }

        @SubscribeEvent
        public static void onPlayerRespawn(PlayerEvent.PlayerRespawnEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                syncAllData(player);
            }
        }

        @SubscribeEvent
        public static void onPlayerDimChange(PlayerEvent.PlayerChangedDimensionEvent event) {
            if (event.getEntity() instanceof ServerPlayer player) {
                syncAllData(player);
            }
        }

        @SubscribeEvent
        public static void onPlayerClone(PlayerEvent.Clone event) {
            // Копируем данные только при смерти
            if (event.isWasDeath()) {
                event.getOriginal().getCapability(PLAYER_VARIABLE).ifPresent(oldStore -> {
                    event.getEntity().getCapability(PLAYER_VARIABLE).ifPresent(newStore -> {
                        // Предполагается, что в PlayerVariable есть метод для копирования данных
                        newStore.copyFrom(oldStore);
                    });
                });
            }
        }

        private static void syncAllData(ServerPlayer player) {
            // Синхронизируем переменные игрока
            player.getCapability(PLAYER_VARIABLE).ifPresent(cap -> {
                CompoundTag tag = new CompoundTag();
                // Предполагается, что в PlayerVariable есть метод для сохранения
                cap.saveNBTData(tag); 
                PacketDistributor.sendToPlayer(player, new PlayerVariablesSyncPayload(tag));
            });

            // Синхронизируем переменные мира
            WorldVariables worldData = WorldVariables.get(player.serverLevel());
            CompoundTag worldTag = worldData.save(new CompoundTag(), player.serverLevel().registryAccess());
            PacketDistributor.sendToPlayer(player, new WorldVariablesSyncPayload(worldTag));
        }
    }

    // --- СИСТЕМА CAPABILITIES ---
    public static final Capability<PlayerVariable> PLAYER_VARIABLE = CapabilityBuilder.builder(PlayerVariable.class).build();

    private static class PlayerVariablesProvider implements ICapabilityProvider<CompoundTag> {
        private final PlayerVariable playerVariable = new PlayerVariable();
        // ЗАМЕНА: LazyOptional -> Optional
        private final Optional<PlayerVariable> instance = Optional.of(playerVariable);

        @Override
        public @NotNull <T> Optional<T> getCapability(@NotNull Capability<T> cap, @Nullable Direction side) {
            return cap == PLAYER_VARIABLE ? instance.map(p -> (T) p) : Optional.empty();
        }
        
        // ЗАМЕНА: Добавлен HolderLookup.Provider
        @Override
        public CompoundTag serializeNBT(HolderLookup.Provider provider) {
            CompoundTag nbt = new CompoundTag();
            // Предполагается, что в PlayerVariable есть метод для сохранения
            this.playerVariable.saveNBTData(nbt);
            return nbt;
        }
        
        @Override
        public void deserializeNBT(HolderLookup.Provider provider, CompoundTag nbt) {
            // Предполагается, что в PlayerVariable есть метод для загрузки
            this.playerVariable.loadNBTData(nbt);
        }
    }

    // --- СИСТЕМА SAVED DATA МИРА ---
    public static class WorldVariables extends SavedData {
        public static final String DATA_NAME = VVP.MOD_ID + "_world_variables";

        // ЗАМЕНА: Сигнатура метода load изменилась
        public static WorldVariables load(CompoundTag tag, HolderLookup.Provider provider) {
            return new WorldVariables(); // Добавь здесь логику загрузки
        }

        // ЗАМЕНА: Сигнатура метода save изменилась
        @Override
        public @NotNull CompoundTag save(@NotNull CompoundTag nbt, @NotNull HolderLookup.Provider provider) {
            return nbt; // Добавь здесь логику сохранения
        }

        public static WorldVariables create() {
            return new WorldVariables();
        }

        private static WorldVariables clientSide = new WorldVariables();

        public static WorldVariables get(LevelAccessor world) {
            if (world instanceof ServerLevel level) {
                // ЗАМЕНА: Способ получения SavedData изменился
                return level.getDataStorage().computeIfAbsent(
                    new SavedData.Factory<>(WorldVariables::create, WorldVariables::load, DATA_NAME),
                    DATA_NAME
                );
            }
            return clientSide;
        }
    }

    // --- СИСТЕМА СЕТЕВЫХ ПАКЕТОВ (PAYLOADS) ---
    // ЗАМЕНА: Вместо старых классов сообщений, создаем "record" для каждого пакета
    public record PlayerVariablesSyncPayload(CompoundTag data) implements CustomPacketPayload {
        public static final ResourceLocation ID = new ResourceLocation(VVP.MOD_ID, "sync_player_variables");

        public PlayerVariablesSyncPayload(FriendlyByteBuf buf) {
            this(buf.readNbt());
        }

        @Override
        public void write(FriendlyByteBuf buf) {
            buf.writeNbt(data);
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }

    public record WorldVariablesSyncPayload(CompoundTag data) implements CustomPacketPayload {
        public static final ResourceLocation ID = new ResourceLocation(VVP.MOD_ID, "sync_world_variables");

        public WorldVariablesSyncPayload(FriendlyByteBuf buf) {
            this(buf.readNbt());
        }

        @Override
        public void write(FriendlyByteBuf buf) {
            buf.writeNbt(data);
        }

        @Override
        public ResourceLocation id() {
            return ID;
        }
    }
    
    // Обработчики для наших пакетов
    private static class PayloadHandlers {
        public static void handlePlayerVariablesSync(final PlayerVariablesSyncPayload payload, final IPayloadContext context) {
            context.enqueueWork(() -> {
                Player player = context.player();
                player.getCapability(PLAYER_VARIABLE).ifPresent(cap -> cap.loadNBTData(payload.data()));
            });
        }
        
        public static void handleWorldVariablesSync(final WorldVariablesSyncPayload payload, final IPayloadContext context) {
             context.enqueueWork(() -> {
                WorldVariables.clientSide.load(payload.data(), null); // Загружаем данные на клиенте
             });
        }
    }
}