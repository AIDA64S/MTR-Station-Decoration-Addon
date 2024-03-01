package top.mcmtr.core.legacy.data;

import org.mtr.core.data.Position;
import org.mtr.core.simulation.FileLoader;
import org.mtr.core.tool.Angle;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.data.CatenaryType;
import top.mcmtr.core.data.OffsetPosition;
import top.mcmtr.core.data.RigidCatenary;

import java.nio.file.Path;
import java.util.UUID;

public final class LegacyCatenaryLoader {
    private static final String KEY_CATENARIES = "catenaries";
    private static final String KEY_RIGID_CATENARIES = "rigid_catenaries";

    public static void loadCatenary(Path savePath, ObjectArraySet<Catenary> catenaries) {
        final ObjectArraySet<LegacyCatenaryNode> legacyCatenaryNodes = new ObjectArraySet<>();
        new FileLoader<>(legacyCatenaryNodes, LegacyCatenaryNode::new, savePath, KEY_CATENARIES);

        final Object2ObjectOpenHashMap<UUID, CatenaryNodeConnection.CatenaryType> catenaryCache = new Object2ObjectOpenHashMap<>();
        legacyCatenaryNodes.forEach(legacyCatenaryNode -> {
            final Position startPosition = legacyCatenaryNode.getStartPosition();
            final long startPositionLong = legacyCatenaryNode.getStartPositionLong();
            legacyCatenaryNode.iterateConnections(catenaryNodeConnection -> {
                final CatenaryNodeConnection.CatenaryType catenaryType = catenaryNodeConnection.getCatenaryType();
                final Position endPosition = catenaryNodeConnection.getEndPoint();
                final long endPositionLong = catenaryNodeConnection.getEndPointLong();
                final UUID uuid = getUuid(startPositionLong, endPositionLong);
                final CatenaryNodeConnection.CatenaryType oldCatenaryType = catenaryCache.get(uuid);
                if (oldCatenaryType != null) {
                    final Catenary catenary;
                    switch (catenaryType) {
                        case CATENARY:
                            catenary = new Catenary(startPosition, endPosition, new OffsetPosition(0, 0, 0), new OffsetPosition(0, 0, 0), CatenaryType.CATENARY);
                            break;
                        case ELECTRIC:
                            catenary = new Catenary(startPosition, endPosition, new OffsetPosition(0, 0, 0), new OffsetPosition(0, 0, 0), CatenaryType.ELECTRIC);
                            break;
                        default:
                            catenary = new Catenary(startPosition, endPosition, new OffsetPosition(0, 0, 0), new OffsetPosition(0, 0, 0), CatenaryType.RIGID_SOFT_CATENARY);
                            break;
                    }
                    catenaries.add(catenary);
                } else {
                    catenaryCache.put(uuid, catenaryType);
                }
            });
        });
    }

    public static void loadRigidCatenary(Path savePath, ObjectArraySet<RigidCatenary> rigidCatenaries) {
        final ObjectArraySet<LegacyRigidCatenaryNode> legacyRigidCatenaryNodes = new ObjectArraySet<>();
        new FileLoader<>(legacyRigidCatenaryNodes, LegacyRigidCatenaryNode::new, savePath, KEY_RIGID_CATENARIES);

        final Object2ObjectOpenHashMap<UUID, RigidCatenaryNodeConnection> rigidCatenaryCache = new Object2ObjectOpenHashMap<>();
        legacyRigidCatenaryNodes.forEach(legacyRigidCatenaryNode -> {
            final Position startPosition = legacyRigidCatenaryNode.getStartPosition();
            final long startPositionLong = legacyRigidCatenaryNode.getStartPositionLong();
            legacyRigidCatenaryNode.iterateConnections(rigidCatenaryNodeConnection -> {
                final Position endPosition = rigidCatenaryNodeConnection.getEndPosition();
                final long endPositionLong = rigidCatenaryNodeConnection.getEndPositionLong();
                final Angle startAngle = rigidCatenaryNodeConnection.getStartAngle();
                final Angle endAngle = rigidCatenaryNodeConnection.getEndAngle();
                final UUID uuid = getUuid(startPositionLong, endPositionLong);
                final RigidCatenaryNodeConnection oldRigidCatenaryConnection = rigidCatenaryCache.get(uuid);
                if (oldRigidCatenaryConnection != null) {
                    final RigidCatenary rigidCatenary = new RigidCatenary(startPosition, startAngle, endPosition, endAngle, RigidCatenary.Shape.QUADRATIC, 0);
                    rigidCatenaries.add(rigidCatenary);
                } else {
                    rigidCatenaryCache.put(uuid, rigidCatenaryNodeConnection);
                }
            });
        });
    }

    private static UUID getUuid(long value1, long value2) {
        return value1 > value2 ? new UUID(value1, value2) : new UUID(value2, value1);
    }
}