package tech.vvp.vvp.network;

import tech.vvp.vvp.VVP;
import tech.vvp.vvp.tools.Ammo;
import com.atsuishio.superbwarfare.Mod;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.saveddata.SavedData;
import net.neoforged.neoforge.common.capabilities.*;
import net.neoforged.neoforge.common.util.FakePlayer;
import net.neoforged.neoforge.common.util.LazyOptional;
import net.neoforged.neoforge.event.AttachCapabilitiesEvent;
import net.neoforged.neoforge.event.entity.player.PlayerEvent;
import net.neoforged.neoforge.eventbus.api.SubscribeEvent;
import net.neoforged.neoforge.network.NetworkEvent;
import net.neoforged.neoforge.network.PacketDistributor;
import org.jetbrains.annotations.NotNull;

import java.util.function.Supplier;

@net.minecraftforge.fml.common.Mod.EventBusSubscriber(bus = net.minecraftforge.fml.common.Mod.EventBusSubscriber.Bus.MOD)
public class ModVariables {

    @SubscribeEvent
    public static void init(RegisterCapabilitiesEvent event) {
        event.register(PlayerVariable.class);
    }

    @net.minecraftforge.fml.common.Mod.EventBusSubscriber
    public static class EventBusVariableHandlers {
        @SubscribeEvent
        public static void onPlayerLoggedInSyncPlayerVariables(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity().level().isClientSide()) return;

            var player = event.getEntity();
            player.getCapability(PLAYER_VARIABLE, null).orElse(new PlayerVariable()).sync(player);
        }

        @SubscribeEvent
        public static void onPlayerRespawnedSyncPlayerVariables(PlayerEvent.PlayerRespawnEvent event) {
            if (event.getEntity().level().isClientSide()) return;

            var player = event.getEntity();
            player.getCapability(PLAYER_VARIABLE, null).orElse(new PlayerVariable()).sync(player);
        }

        @SubscribeEvent
        public static void onPlayerChangedDimensionSyncPlayerVariables(PlayerEvent.PlayerChangedDimensionEvent event) {
            if (event.getEntity().level().isClientSide()) return;

            var player = event.getEntity();
            player.getCapability(PLAYER_VARIABLE, null).orElse(new PlayerVariable()).sync(player);
        }

        @SubscribeEvent
        public static void clonePlayer(PlayerEvent.Clone event) {
            event.getOriginal().revive();
            PlayerVariable original = event.getOriginal().getCapability(PLAYER_VARIABLE, null).orElse(new PlayerVariable());
            PlayerVariable clone = event.getEntity().getCapability(PLAYER_VARIABLE, null).orElse(new PlayerVariable());

            for (var type : Ammo.values()) {
                type.set(clone, type.get(original));
            }

            clone.tacticalSprint = original.tacticalSprint;

            if (event.getEntity().level().isClientSide()) return;

            var player = event.getEntity();
            player.getCapability(PLAYER_VARIABLE, null).orElse(new PlayerVariable()).sync(player);
        }

        @SubscribeEvent
        public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
            if (event.getEntity().level().isClientSide()) return;
            SavedData worldData = WorldVariables.get(event.getEntity().level());
            // if (worldData != null) // Закомментировано для исправления ошибки Invalid message
            //    Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new SavedDataSyncMessage(1, worldData));
        }

        @SubscribeEvent
        public static void onPlayerChangedDimension(PlayerEvent.PlayerChangedDimensionEvent event) {
            if (event.getEntity().level().isClientSide()) return;
            SavedData worldData = WorldVariables.get(event.getEntity().level());
            // if (worldData != null) // Закомментировано для исправления ошибки Invalid message
            //    Mod.PACKET_HANDLER.send(PacketDistributor.PLAYER.with(() -> (ServerPlayer) event.getEntity()), new SavedDataSyncMessage(1, worldData));
        }
    }

    public static class WorldVariables extends SavedData {
        public static final String DATA_NAME = VVP.MOD_ID + "_world_variables";

        public static WorldVariables load(CompoundTag tag) {
            WorldVariables data = new WorldVariables();
            data.read();
            return data;
        }

        public void read() {
        }

        @Override
        public @NotNull CompoundTag save(@NotNull CompoundTag nbt) {
            return nbt;
        }

        static WorldVariables clientSide = new WorldVariables();

        public static WorldVariables get(LevelAccessor world) {
            if (world instanceof ServerLevel level)
                return level.getDataStorage().computeIfAbsent(WorldVariables::load, WorldVariables::new, DATA_NAME);
            return clientSide;
        }
    }

    public static class SavedDataSyncMessage {
        private final int type;
        private SavedData data;

        public SavedDataSyncMessage(FriendlyByteBuf buffer) {
            this.type = buffer.readInt();
            CompoundTag nbt = buffer.readNbt();
            if (nbt == null) return;

            new WorldVariables();
        }

        public SavedDataSyncMessage(int type, SavedData data) {
            this.type = type;
            this.data = data;
        }

        public static void buffer(SavedDataSyncMessage message, FriendlyByteBuf buffer) {
            buffer.writeInt(message.type);
            if (message.data != null)
                buffer.writeNbt(message.data.save(new CompoundTag()));
        }

        public static void handler(SavedDataSyncMessage message, Supplier<NetworkEvent.Context> contextSupplier) {
            NetworkEvent.Context context = contextSupplier.get();
            context.enqueueWork(() -> {
                if (!context.getDirection().getReceptionSide().isServer() && message.data != null) {
                    if (message.type != 0)
                        WorldVariables.clientSide = (WorldVariables) message.data;
                }
            });
            context.setPacketHandled(true);
        }
    }

    public static final Capability<PlayerVariable> PLAYER_VARIABLE = CapabilityManager.get(new CapabilityToken<>() {
    });

    @net.minecraftforge.fml.common.Mod.EventBusSubscriber
    private static class PlayerVariablesProvider implements ICapabilitySerializable<Tag> {
        /* // Закомментировано, чтобы избежать дублирования с SuperbWarfare
        @SubscribeEvent
        public static void onAttachCapabilities(AttachCapabilitiesEvent<Entity> event) {
            if (event.getObject() instanceof Player && !(event.getObject() instanceof FakePlayer))
                event.addCapability(Mod.loc("player_variables"), new PlayerVariablesProvider());
        }
        */

        private final PlayerVariable playerVariable = new PlayerVariable();
        private final LazyOptional<PlayerVariable> instance = LazyOptional.of(() -> playerVariable);

        @Override
        public <T> @NotNull LazyOptional<T> getCapability(@NotNull Capability<T> cap, Direction side) {
            return cap == PLAYER_VARIABLE ? instance.cast() : LazyOptional.empty();
        }

        @Override
        public Tag serializeNBT() {
            return playerVariable.writeNBT();
        }

        @Override
        public void deserializeNBT(Tag nbt) {
            playerVariable.readNBT(nbt);
        }
    }

}
