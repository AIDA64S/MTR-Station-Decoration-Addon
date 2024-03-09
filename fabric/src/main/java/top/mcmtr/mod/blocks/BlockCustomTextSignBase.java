package top.mcmtr.mod.blocks;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.BlockEntityExtension;
import org.mtr.mapping.mapper.BlockExtension;
import org.mtr.mapping.mapper.BlockWithEntity;
import org.mtr.mod.block.IBlock;
import top.mcmtr.mod.Init;
import top.mcmtr.mod.packet.MSDPacketOpenCustomTextConfig;

public abstract class BlockCustomTextSignBase extends BlockChangeModelBase implements BlockWithEntity {
    protected final int maxArrivals;

    public BlockCustomTextSignBase(BlockSettings blockSettings, int maxArrivals, int count) {
        super(blockSettings, count);
        this.maxArrivals = maxArrivals;
    }

    @Override
    public ActionResult onUse2(BlockState state, World world, BlockPos pos, PlayerEntity player, Hand hand, BlockHitResult hit) {
        return IBlock.checkHoldingBrush(world, player, () -> {
            final BlockEntity entity = world.getBlockEntity(pos);
            if (entity != null && entity.data instanceof BlockCustomTextSignBase.BlockCustomTextSignBaseEntity) {
                ((BlockCustomTextSignBaseEntity) entity.data).markDirty2();
                Init.REGISTRY.sendPacketToClient(ServerPlayerEntity.cast(player), new MSDPacketOpenCustomTextConfig(pos, maxArrivals));
            }
        });
    }

    public static abstract class BlockCustomTextSignBaseEntity extends BlockEntityExtension {
        public final int maxArrivals;
        private final String[] messages;
        private static final String KEY_MESSAGE = "msd_custom_message";

        public BlockCustomTextSignBaseEntity(BlockEntityType<?> type, BlockPos blockPos, BlockState blockState, int maxArrivals) {
            super(type, blockPos, blockState);
            this.maxArrivals = maxArrivals;
            messages = new String[maxArrivals];
            for (int i = 0; i < maxArrivals; i++) {
                messages[i] = defaultFormat(i);
            }
        }

        @Override
        public void readCompoundTag(CompoundTag compoundTag) {
            for (int i = 0; i < maxArrivals; i++) {
                messages[i] = compoundTag.getString(KEY_MESSAGE + i);
            }
        }

        @Override
        public void writeCompoundTag(CompoundTag compoundTag) {
            for (int i = 0; i < maxArrivals; i++) {
                compoundTag.putString(KEY_MESSAGE + i, messages[i] == null ? "" : messages[i]);
            }
        }

        public void setData(String[] messages) {
            System.arraycopy(messages, 0, this.messages, 0, Math.min(messages.length, this.messages.length));
            markDirty2();
        }

        public String getMessage(int index) {
            if (index >= 0 && index < maxArrivals) {
                if (messages[index] == null) {
                    messages[index] = "";
                }
                return messages[index];
            } else {
                return "";
            }
        }

        public abstract String defaultFormat(int line);
    }
}