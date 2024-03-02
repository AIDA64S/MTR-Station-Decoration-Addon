package top.mcmtr.mod.client;

import org.mtr.core.data.Position;
import org.mtr.libraries.com.logisticscraft.occlusionculling.util.Vec3d;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.*;
import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.EntityHelper;
import org.mtr.mapping.mapper.MinecraftClientHelper;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.data.MSDClientData;
import top.mcmtr.core.data.RigidCatenary;
import top.mcmtr.mod.Init;
import top.mcmtr.mod.blocks.BlockRigidCatenaryNode;

import javax.annotation.Nullable;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

public final class MSDMinecraftClientData extends MSDClientData {
    public final Object2ObjectArrayMap<String, MSDMinecraftClientData.CatenaryWrapper> catenaryWrapperList = new Object2ObjectArrayMap<>();
    public final Object2ObjectArrayMap<String, MSDMinecraftClientData.RigidCatenaryWrapper> rigidCatenaryWrapperList = new Object2ObjectArrayMap<>();
    private static MSDMinecraftClientData instance = new MSDMinecraftClientData();

    @Override
    public void sync() {
        super.sync();
        checkAndRemoveFromMap(catenaryWrapperList, catenaries, Catenary::getHexId);
        checkAndRemoveFromMap(rigidCatenaryWrapperList, rigidCatenaries, RigidCatenary::getHexId);
        positionsToCatenary.forEach((startPosition, catenaryMap) -> catenaryMap.forEach((endPosition, catenary) -> {
            final String hexId = catenary.getHexId();
            final MSDMinecraftClientData.CatenaryWrapper catenaryWrapper = catenaryWrapperList.get(hexId);
            if (catenaryWrapper == null) {
                catenaryWrapperList.put(hexId, new MSDMinecraftClientData.CatenaryWrapper(catenary, hexId, startPosition, endPosition));
            } else {
                catenaryWrapper.catenary = catenary;
            }
        }));
        positionsToRigidCatenary.forEach((startPosition, rigidCatenaryMap) -> rigidCatenaryMap.forEach((endPosition, rigidCatenary) -> {
            final String hexId = rigidCatenary.getHexId();
            final MSDMinecraftClientData.RigidCatenaryWrapper rigidCatenaryWrapper = rigidCatenaryWrapperList.get(hexId);
            if (rigidCatenaryWrapper == null) {
                rigidCatenaryWrapperList.put(hexId, new MSDMinecraftClientData.RigidCatenaryWrapper(rigidCatenary, hexId, startPosition, endPosition));
            } else {
                rigidCatenaryWrapper.rigidCatenary = rigidCatenary;
            }
        }));
    }

    public void clean() {
        final ClientPlayerEntity clientPlayerEntity = MinecraftClient.getInstance().getPlayerMapped();
        if (clientPlayerEntity != null) {
            final Position position = Init.blockPosToPosition(clientPlayerEntity.getBlockPos());
            final int requestRadius = MinecraftClientHelper.getRenderDistance() * 16;
            catenaries.removeIf(catenary -> !catenary.closeTo(position, requestRadius));
            rigidCatenaries.removeIf(rigidCatenary -> !rigidCatenary.closeTo(position, requestRadius));
            sync();
        }
    }

    @Nullable
    public RigidCatenary getFacingRigidCatenary() {
        final MinecraftClient minecraftClient = MinecraftClient.getInstance();
        final ClientWorld clientWorld = minecraftClient.getWorldMapped();
        if (clientWorld == null) {
            return null;
        }
        final ClientPlayerEntity clientPlayerEntity = minecraftClient.getPlayerMapped();
        if (clientPlayerEntity == null) {
            return null;
        }
        final HitResult hitResult = minecraftClient.getCrosshairTargetMapped();
        if (hitResult == null) {
            return null;
        }
        final Vector3d hitPos = hitResult.getPos();
        final BlockPos blockPos = Init.newBlockPos(hitPos.getXMapped(), hitPos.getYMapped(), hitPos.getZMapped());
        if (clientWorld.getBlockState(blockPos).getBlock().data instanceof BlockRigidCatenaryNode) {
            final float playerAngle = EntityHelper.getYaw(new Entity(clientPlayerEntity.data)) + 90;
            final RigidCatenary[] closestRigidCatenary = {null};
            final double[] closestAngle = {720};
            positionsToRigidCatenary.getOrDefault(Init.blockPosToPosition(blockPos), new Object2ObjectOpenHashMap<>()).forEach((endPosition, rigidCatenary) -> {
                final double angle = Math.abs(Math.toDegrees(Math.atan2(endPosition.getZ() - blockPos.getZ(), endPosition.getX() - blockPos.getX())) - playerAngle) % 360;
                final double clampedAngle = angle > 180 ? 360 - angle : angle;
                if (clampedAngle < closestAngle[0]) {
                    closestRigidCatenary[0] = rigidCatenary;
                    closestAngle[0] = clampedAngle;
                }
            });
            return closestRigidCatenary[0];
        }
        return null;
    }

    public static MSDMinecraftClientData getInstance() {
        return instance;
    }

    public static void reset() {
        MSDMinecraftClientData.instance = new MSDMinecraftClientData();
    }

    private static <T, U, V> void checkAndRemoveFromMap(Map<T, U> map, ObjectSet<V> dataSet, Function<V, T> getId) {
        final ObjectAVLTreeSet<T> idSet = dataSet.stream().map(getId).collect(Collectors.toCollection(ObjectAVLTreeSet::new));
        final ObjectArrayList<T> idsToRemove = new ObjectArrayList<>();
        map.keySet().forEach(id -> {
            if (!idSet.contains(id)) {
                idsToRemove.add(id);
            }
        });
        idsToRemove.forEach(map::remove);
    }

    public static class CatenaryWrapper {
        public boolean shouldRender;
        public final String hexId;
        public final Vec3d startVector;
        public final Vec3d endVector;
        private Catenary catenary;

        private CatenaryWrapper(Catenary catenary, String hexId, Position startPosition, Position endPosition) {
            this.catenary = catenary;
            this.hexId = hexId;
            startVector = new Vec3d(
                    Math.min(startPosition.getX(), endPosition.getX()),
                    Math.min(startPosition.getY(), endPosition.getY()),
                    Math.min(startPosition.getZ(), endPosition.getZ())
            );
            endVector = new Vec3d(
                    Math.max(startPosition.getX(), endPosition.getX()),
                    Math.max(startPosition.getY(), endPosition.getY()),
                    Math.max(startPosition.getZ(), endPosition.getZ())
            );
        }

        public Catenary getCatenary() {
            return this.catenary;
        }
    }

    public static class RigidCatenaryWrapper {
        public boolean shouldRender;
        public final String hexId;
        public final Vec3d startVector;
        public final Vec3d endVector;
        private RigidCatenary rigidCatenary;

        public RigidCatenaryWrapper(RigidCatenary rigidCatenary, String hexId, Position startPosition, Position endPosition) {
            this.rigidCatenary = rigidCatenary;
            this.hexId = hexId;
            this.startVector = new Vec3d(
                    Math.min(startPosition.getX(), endPosition.getX()),
                    Math.min(startPosition.getY(), endPosition.getY()),
                    Math.min(startPosition.getZ(), endPosition.getZ())
            );
            ;
            this.endVector = new Vec3d(
                    Math.max(startPosition.getX(), endPosition.getX()),
                    Math.max(startPosition.getY(), endPosition.getY()),
                    Math.max(startPosition.getZ(), endPosition.getZ())
            );
        }

        public RigidCatenary getRigidCatenary() {
            return rigidCatenary;
        }
    }
}
