package top.mcmtr.mod;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.registry.EventRegistryClient;
import org.mtr.mapping.registry.RegistryClient;
import org.mtr.mod.client.CustomResourceLoader;
import org.mtr.mod.client.DynamicTextureCache;
import top.mcmtr.mod.client.ClientData;
import top.mcmtr.mod.items.ItemBlockClickingBase;
import top.mcmtr.mod.packet.PacketRequestData;

public class InitClient {
    private static long lastMillis = 0;
    private static long gameMillis = 0;
    private static BlockPos lastPosition;
    private static Runnable movePlayer;
    public static final RegistryClient REGISTRY_CLIENT = new RegistryClient(Init.REGISTRY);

    public static void init() {
        REGISTRY_CLIENT.registerBlockRenderType(RenderLayer.getCutout(), Blocks.CATENARY_NODE);

        REGISTRY_CLIENT.registerItemModelPredicate(Items.CATENARY_CONNECTOR, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());
        REGISTRY_CLIENT.registerItemModelPredicate(Items.CONNECTOR_REMOVER, new Identifier(Init.MOD_ID, "selected"), checkItemPredicateTag());

        REGISTRY_CLIENT.setupPackets(new Identifier(Init.MOD_ID, "packet"));
        EventRegistryClient.registerClientJoin(() -> {
            ClientData.reset();
            DynamicTextureCache.instance = new DynamicTextureCache();
            lastMillis = System.currentTimeMillis();
            gameMillis = 0;
            lastPosition = null;
            CustomResourceLoader.reload();
            DynamicTextureCache.instance.reload();
        });

        EventRegistryClient.registerStartClientTick(() -> {
            incrementGameMillis();
            final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
            if (clientPlayerEntity != null) {
                final BlockPos blockPos = clientPlayerEntity.getBlockPos();
                if (lastPosition == null || lastPosition.getManhattanDistance(new Vector3i(blockPos.data)) > 8) {
                    REGISTRY_CLIENT.sendPacketToServer(new PacketRequestData(false));
                    lastPosition = blockPos;
                }
            }
        });

        EventRegistryClient.registerEndClientTick(() -> {
            if (movePlayer != null) {
                movePlayer.run();
                movePlayer = null;
            }
        });

        REGISTRY_CLIENT.init();
    }

    public static void scheduleMovePlayer(Runnable movePlayer) {
        InitClient.movePlayer = movePlayer;
    }

    public static long serializeExit(String exit) {
        final char[] characters = exit.toCharArray();
        long code = 0;
        for (final char character : characters) {
            code = code << 8;
            code += character;
        }
        return code;
    }

    public static String deserializeExit(long code) {
        StringBuilder exit = new StringBuilder();
        long charCodes = code;
        while (charCodes > 0) {
            exit.insert(0, (char) (charCodes & 0xFF));
            charCodes = charCodes >> 8;
        }
        return exit.toString();
    }

    public static float getGameTick() {
        return gameMillis / 50F;
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