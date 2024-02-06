package top.mcmtr.mod.render;

import com.logisticscraft.occlusionculling.DataProvider;
import com.logisticscraft.occlusionculling.OcclusionCullingInstance;
import com.logisticscraft.occlusionculling.util.Vec3d;
import org.mtr.core.data.Position;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.MinecraftClientHelper;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.mod.client.ClientData;

import java.util.concurrent.CopyOnWriteArrayList;

public class OcclusionCullingThread extends Thread {
    private boolean started;
    private int renderDistance;
    private OcclusionCullingInstance occlusionCullingInstance;
    public static final CopyOnWriteArrayList<CatenaryWrapper> CATENARIES = new CopyOnWriteArrayList<>();

    @Override
    public synchronized void start() {
        if (!started) {
            super.start();
        }
        started = true;
    }

    @Override
    public void run() {
        final MinecraftClient minecraftClient = MinecraftClient.getInstance();
        while (minecraftClient.isRunning()) {
            updateInstance();
            occlusionCullingInstance.resetCache();
            final Vector3d cameraPosition = minecraftClient.getGameRendererMapped().getCamera().getPos();
            final Vec3d camera = new Vec3d(cameraPosition.getXMapped(), cameraPosition.getYMapped(), cameraPosition.getZMapped());
            CATENARIES.forEach(catenaryWrapper -> ClientData.getInstance().catenaryCulling.put(catenaryWrapper.hexId, occlusionCullingInstance.isAABBVisible(catenaryWrapper.minPosition, catenaryWrapper.maxPosition, camera)));
        }
    }

    private void updateInstance() {
        final int newRenderDistance = MinecraftClientHelper.getRenderDistance();
        if (renderDistance != newRenderDistance) {
            renderDistance = newRenderDistance;
            occlusionCullingInstance = new OcclusionCullingInstance(renderDistance * 16, new CullingDataProvider());
        }
    }

    public static final class CatenaryWrapper {
        private final String hexId;
        private final Vec3d minPosition;
        private final Vec3d maxPosition;

        public CatenaryWrapper(Position startPosition, Position endPosition, Catenary catenary) {
            hexId = catenary.getHexId();
            minPosition = new Vec3d(
                    Math.min(startPosition.getX(), endPosition.getX()),
                    Math.min(startPosition.getY(), endPosition.getY()),
                    Math.min(startPosition.getZ(), endPosition.getZ())
            );
            maxPosition = new Vec3d(
                    Math.max(startPosition.getX(), endPosition.getX()),
                    Math.max(startPosition.getY(), endPosition.getY()),
                    Math.max(startPosition.getZ(), endPosition.getZ())
            );
        }
    }

    private static final class CullingDataProvider implements DataProvider {
        private final MinecraftClient minecraftClient = MinecraftClient.getInstance();
        private ClientWorld clientWorld = null;

        @Override
        public boolean prepareChunk(int i, int i1) {
            clientWorld = minecraftClient.getWorldMapped();
            return clientWorld != null;
        }

        @Override
        public boolean isOpaqueFullCube(int x, int y, int z) {
            final BlockPos blockPos = new BlockPos(x, y, z);
            return clientWorld != null && clientWorld.getBlockState(blockPos).isOpaqueFullCube(new BlockView(clientWorld.data), blockPos);
        }

        @Override
        public void cleanup() {
            clientWorld = null;
        }
    }
}