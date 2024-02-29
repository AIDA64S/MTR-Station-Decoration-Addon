package top.mcmtr.mod;

import org.mtr.mapping.holder.ClientPlayerEntity;
import org.mtr.mapping.holder.Identifier;
import org.mtr.mapping.holder.MinecraftClient;
import org.mtr.mapping.holder.RenderLayer;
import org.mtr.mapping.mapper.MinecraftClientHelper;
import org.mtr.mapping.registry.EventRegistryClient;
import org.mtr.mapping.registry.RegistryClient;
import top.mcmtr.core.operation.MSDDataRequest;
import top.mcmtr.mod.client.MSDMinecraftClientData;
import top.mcmtr.mod.items.ItemBlockClickingBase;
import top.mcmtr.mod.packet.MSDPacketRequestData;

public class InitClient {
    private static long lastMillis = 0;
    private static long gameMillis = 0;
    private static long lastUpdatePacketMillis = 0;
    private static long lastDataCleanMillis = 0;
    public static final RegistryClient REGISTRY_CLIENT = new RegistryClient(Init.REGISTRY);

    public static void init() {
        REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.CATENARY_NODE);

        REGISTRY_CLIENT.registerItemModelPredicate(Items.CATENARY_CONNECTOR, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
        REGISTRY_CLIENT.registerItemModelPredicate(Items.ELECTRIC_CONNECTOR, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
        REGISTRY_CLIENT.registerItemModelPredicate(Items.RIGID_SOFT_CATENARY_CONNECTOR, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
        REGISTRY_CLIENT.registerItemModelPredicate(Items.RIGID_CATENARY_CONNECTOR, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
        REGISTRY_CLIENT.registerItemModelPredicate(Items.CATENARY_REMOVER, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
        REGISTRY_CLIENT.registerItemModelPredicate(Items.RIGID_CATENARY_REMOVER, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());

        REGISTRY_CLIENT.setupPackets(new Identifier(Init.MOD_ID, "packet"));

        EventRegistryClient.registerClientJoin(() -> {
            MSDMinecraftClientData.reset();
            lastMillis = System.currentTimeMillis();
            gameMillis = 0;
        });

        EventRegistryClient.registerStartClientTick(() -> {
            incrementGameMillis();
            final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();

            if (clientPlayerEntity != null && lastUpdatePacketMillis >= 0 && getGameMillis() - lastUpdatePacketMillis > 500) {
                final MSDDataRequest dataRequest = new MSDDataRequest(clientPlayerEntity.getUuidAsString(), Init.blockPosToPosition(clientPlayerEntity.getBlockPos()), MinecraftClientHelper.getRenderDistance() * 16L);
                dataRequest.writeExistingIds(MSDMinecraftClientData.getInstance());
                InitClient.REGISTRY_CLIENT.sendPacketToServer(new MSDPacketRequestData(dataRequest));
                lastUpdatePacketMillis = -1;
                lastDataCleanMillis = getGameMillis();
            }

            // If player hasn't moved in 2 seconds, clean any out of range data
            if (lastDataCleanMillis >= 0 && getGameMillis() - lastDataCleanMillis > 2000) {
                MSDMinecraftClientData.getInstance().clean();
                lastDataCleanMillis = -1;
            }
        });

        EventRegistryClient.registerChunkLoad((clientWorld, worldChunk) -> {
            if (lastUpdatePacketMillis < 0) {
                lastUpdatePacketMillis = getGameMillis();
            }
        });


        REGISTRY_CLIENT.init();
    }

    public static long getGameMillis() {
        return gameMillis;
    }

    public static void incrementGameMillis() {
        final long currentMillis = System.currentTimeMillis();
        final long millisElapsed = currentMillis - lastMillis;
        lastMillis = currentMillis;
        gameMillis += millisElapsed;
    }

    private static RegistryClient.ModelPredicateProvider checkItemPredicateTag() {
        return ((itemStack, clientWorld, livingEntity) -> itemStack.getOrCreateTag().contains(ItemBlockClickingBase.TAG_POS) ? 1 : 0);
    }
}