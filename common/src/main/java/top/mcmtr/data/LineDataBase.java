package top.mcmtr.data;

import mtr.mappings.PersistentStateMapper;
import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.msgpack.value.Value;

import java.util.HashMap;
import java.util.Map;

public abstract class LineDataBase extends PersistentStateMapper {
    protected final Level world;
    protected final Map<Player, BlockPos> playerLastUpdatedPositions = new HashMap<>();

    public LineDataBase(String name, Level world) {
        super(name);
        this.world = world;
    }

    @Override
    public CompoundTag save(CompoundTag compoundTag) {
        return compoundTag;
    }

    /**
     * 区块检测加载
     */
    public static boolean chunkLoaded(Level world, BlockPos pos) {
        return world.getChunkSource().getChunkNow(pos.getX() / 16, pos.getZ() / 16) != null && world.hasChunk(pos.getX() / 16, pos.getZ() / 16);
    }

    /**
     * 此方法用于从MessagePack中的Value读取数据，然后转换为Map返回
     */
    public static Map<String, Value> castMessagePackValueToSKMap(Value value) {
        final Map<Value, Value> oldMap = value == null ? new HashMap<>() : value.asMapValue().map();
        final HashMap<String, Value> resultMap = new HashMap<>(oldMap.size());
        oldMap.forEach((key, newValue) -> resultMap.put(key.asStringValue().asString(), newValue));
        return resultMap;
    }

    /**
     * 此方法用于检测两个方块坐标是否相同
     */
    public static boolean checkPosEquals(BlockPos pos1, BlockPos pos2) {
        return (pos1.getX() == pos2.getX() && pos1.getY() == pos2.getY() && pos1.getZ() == pos2.getZ());
    }
}