package top.mcmtr.mod.screen;

import org.mtr.mapping.holder.*;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mapping.mapper.TextFieldWidgetExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import top.mcmtr.mod.InitClient;
import top.mcmtr.mod.blocks.BlockCustomTextSignBase;
import top.mcmtr.mod.packet.MSDPacketUpdateCustomText;

public class CustomTextScreen extends ScreenExtension implements IGui {
    private final BlockPos blockPos;
    private final String[] messages;
    private final TextFieldWidgetExtension[] textFieldMessages;
    private final MutableText messageText = TextHelper.translatable("gui.msd.custom_message");
    private static final int MAX_MESSAGE_LENGTH = 2048;

    public CustomTextScreen(BlockPos blockPos, int maxArrivals) {
        super();
        this.blockPos = blockPos;
        this.messages = new String[maxArrivals];
        final ClientWorld clientWorld = MinecraftClient.getInstance().getWorldMapped();
        if (clientWorld != null) {
            final BlockEntity blockEntity = clientWorld.getBlockEntity(blockPos);
            if (blockEntity != null && blockEntity.data instanceof BlockCustomTextSignBase.BlockCustomTextSignBaseEntity) {
                for (int i = 0; i < maxArrivals; i++) {
                    messages[i] = ((BlockCustomTextSignBase.BlockCustomTextSignBaseEntity) blockEntity.data).getMessage(i);
                }
            }
        }
        textFieldMessages = new TextFieldWidgetExtension[maxArrivals];
        for (int i = 0; i < maxArrivals; i++) {
            textFieldMessages[i] = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, MAX_MESSAGE_LENGTH, TextCase.DEFAULT, null, "");
        }
    }

    @Override
    protected void init2() {
        super.init2();
        for (int i = 0; i < textFieldMessages.length; i++) {
            final TextFieldWidgetExtension textFieldMessage = textFieldMessages[i];
            IDrawing.setPositionAndWidth(textFieldMessage, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2 + (SQUARE_SIZE + TEXT_FIELD_PADDING) * i, width - SQUARE_SIZE * 2 - TEXT_FIELD_PADDING);
            textFieldMessage.setText2(messages[i]);
            addChild(new ClickableWidget(textFieldMessage));
        }
    }

    @Override
    public void onClose2() {
        super.onClose2();
        for (int i = 0; i < textFieldMessages.length; i++) {
            messages[i] = textFieldMessages[i].getText2();
        }
        InitClient.REGISTRY_CLIENT.sendPacketToServer(new MSDPacketUpdateCustomText(blockPos, messages));
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        renderBackground(graphicsHolder);
        graphicsHolder.drawText(messageText, SQUARE_SIZE + TEXT_PADDING, SQUARE_SIZE, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
        super.render(graphicsHolder, mouseX, mouseY, delta);
    }

    @Override
    public boolean isPauseScreen2() {
        return false;
    }
}