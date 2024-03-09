package top.mcmtr.mod.packet;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.ScreenExtension;
import top.mcmtr.core.data.OffsetPosition;
import top.mcmtr.mod.blocks.BlockCatenaryNode;
import top.mcmtr.mod.blocks.BlockCustomTextSignBase;
import top.mcmtr.mod.blocks.BlockYamanoteRailwaySign;
import top.mcmtr.mod.screen.CatenaryScreen;
import top.mcmtr.mod.screen.CustomTextScreen;
import top.mcmtr.mod.screen.RigidCatenaryShapeModifierScreen;
import top.mcmtr.mod.screen.YamanoteRailwaySignScreen;

import java.util.function.Consumer;
import java.util.function.Predicate;

public final class MSDClientPacketHelper {
    public static void openMSDBlockEntityScreen(BlockPos blockPos) {
        getBlockEntity(blockPos, blockEntity -> {
            if (blockEntity.data instanceof BlockYamanoteRailwaySign.BlockYamanoteRailwaySignEntity) {
                openScreen(new YamanoteRailwaySignScreen(blockPos), screenExtension -> screenExtension instanceof YamanoteRailwaySignScreen);
            }
        });
    }

    public static void openCustomTextConfigScreen(BlockPos blockPos, int maxArrivals) {
        getBlockEntity(blockPos, blockEntity -> {
            if (blockEntity.data instanceof BlockCustomTextSignBase.BlockCustomTextSignBaseEntity) {
                openScreen(new CustomTextScreen(blockPos, maxArrivals), screenExtension -> screenExtension instanceof CustomTextScreen);
            }
        });
    }

    public static void openCatenaryScreen(boolean isConnected, BlockPos blockPos, OffsetPosition offsetPosition) {
        getBlockEntity(blockPos, blockEntity -> {
            if (blockEntity.data instanceof BlockCatenaryNode.BlockCatenaryNodeEntity) {
                openScreen(new CatenaryScreen(isConnected, blockPos, offsetPosition), screenExtension -> screenExtension instanceof CatenaryScreen);
            }
        });
    }

    public static void openRigidCatenaryShapeModifierScreen(String rigidCatenaryId) {
        openScreen(new RigidCatenaryShapeModifierScreen(rigidCatenaryId), screenExtension -> screenExtension instanceof RigidCatenaryShapeModifierScreen);
    }

    private static void openScreen(ScreenExtension screenExtension, Predicate<ScreenExtension> isInstance) {
        final MinecraftClient minecraftClient = MinecraftClient.getInstance();
        final Screen screen = minecraftClient.getCurrentScreenMapped();
        if (screen == null || screen.data instanceof ScreenExtension && !isInstance.test((ScreenExtension) screen.data)) {
            minecraftClient.openScreen(new Screen(screenExtension));
        }
    }

    private static void getBlockEntity(BlockPos blockPos, Consumer<BlockEntity> consumer) {
        final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
        if (clientWorld != null) {
            final BlockEntity blockEntity = clientWorld.getBlockEntity(blockPos);
            if (blockEntity != null) {
                consumer.accept(blockEntity);
            }
        }
    }
}