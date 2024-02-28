package top.mcmtr.core.legacy.data;

import org.mtr.core.data.Position;
import org.mtr.core.simulation.FileLoader;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import org.mtr.libraries.it.unimi.dsi.fastutil.objects.ObjectArraySet;
import top.mcmtr.core.data.Catenary;
import top.mcmtr.core.data.CatenaryType;
import top.mcmtr.core.data.OffsetPosition;

import java.nio.file.Path;
import java.util.UUID;

public final class LegacyCatenaryLoader {
    private static final String KEY_CATENARIES = "catenaries";

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

    private static UUID getUuid(long value1, long value2) {
        return value1 > value2 ? new UUID(value1, value2) : new UUID(value2, value1);
    }
}