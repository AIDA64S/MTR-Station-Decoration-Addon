package top.mcmtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.client.IDrawing;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.packet.IPacket;
import mtr.screen.WidgetBetterTextField;
import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import top.mcmtr.blocks.BlockCustomTextSignBase;
import top.mcmtr.packet.MSDPacketTrainDataGuiClient;

public class CustomTextSignScreen extends ScreenMapper implements IGui, IPacket {
    private final BlockPos pos1;
    private final String[] messages;
    private final WidgetBetterTextField[] textFieldMessages;
    private final Component messageText = Text.translatable("gui.msd.custom_text_sign_message");
    private static final int MAX_MESSAGE_LENGTH = 2048;

    public CustomTextSignScreen(BlockPos pos1, int maxArrivals) {
        super(Text.literal(""));
        this.pos1 = pos1;
        messages = new String[maxArrivals];
        for (int i = 0; i < maxArrivals; i++) {
            messages[i] = "";
        }
        textFieldMessages = new WidgetBetterTextField[maxArrivals];
        for (int i = 0; i < maxArrivals; i++) {
            textFieldMessages[i] = new WidgetBetterTextField("", MAX_MESSAGE_LENGTH);
        }
        final Level world = Minecraft.getInstance().level;
        if (world != null) {
            final BlockEntity entity = world.getBlockEntity(pos1);
            if (entity instanceof BlockCustomTextSignBase.TileEntityBlockCustomTextSignBase) {
                for (int i = 0; i < maxArrivals; i++) {
                    messages[i] = ((BlockCustomTextSignBase.TileEntityBlockCustomTextSignBase) entity).getMessage(i);
                }
            }
        }
    }

    @Override
    protected void init() {
        super.init();
        final int textWidth = SQUARE_SIZE + TEXT_PADDING * 2;
        for (int i = 0; i < textFieldMessages.length; i++) {
            final WidgetBetterTextField textFieldMessage = textFieldMessages[i];
            IDrawing.setPositionAndWidth(textFieldMessage, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2 + (SQUARE_SIZE + TEXT_FIELD_PADDING) * i, width - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING - textWidth);
            textFieldMessage.setValue(messages[i]);
            addDrawableChild(textFieldMessage);
        }
    }

    @Override
    public void tick() {
        for (final WidgetBetterTextField textFieldMessage : textFieldMessages) {
            textFieldMessage.tick();
        }
    }

    @Override
    public void onClose() {
        for (int i = 0; i < textFieldMessages.length; i++) {
            messages[i] = textFieldMessages[i].getValue();
        }
        MSDPacketTrainDataGuiClient.sendCustomTextSignConfigC2S(pos1, messages);
        super.onClose();
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        try {
            renderBackground(matrices);
            font.draw(matrices, messageText, SQUARE_SIZE + TEXT_PADDING, SQUARE_SIZE, ARGB_WHITE);
            super.render(matrices, mouseX, mouseY, delta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean isPauseScreen() {
        return false;
    }
}