package top.mcmtr.mod.screen;

import org.mtr.core.data.Position;
import org.mtr.mapping.holder.BlockPos;
import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.mapper.GraphicsHolder;
import org.mtr.mapping.mapper.ScreenExtension;
import org.mtr.mapping.mapper.TextHelper;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import org.mtr.mod.screen.WidgetShorterSlider;
import top.mcmtr.core.data.OffsetPosition;
import top.mcmtr.core.operation.MSDResetDataRequest;
import top.mcmtr.mod.Init;
import top.mcmtr.mod.InitClient;
import top.mcmtr.mod.client.MSDMinecraftClientData;
import top.mcmtr.mod.packet.MSDPacketResetData;
import top.mcmtr.mod.packet.MSDPacketUpdateCatenary;

public class CatenaryScreen extends ScreenExtension implements IGui {
    private final boolean isConnected;
    private final BlockPos blockPos;
    private final OffsetPosition offsetPosition;
    private final WidgetShorterSlider sliderPositionX;
    private final WidgetShorterSlider sliderPositionY;
    private final WidgetShorterSlider sliderPositionZ;
    private static final int BUTTON_WIDTH = 60;
    private static final int BUTTON_HEIGHT = TEXT_HEIGHT + TEXT_PADDING;

    public CatenaryScreen(boolean isConnected, BlockPos blockPos, OffsetPosition offsetPosition) {
        super();
        this.isConnected = isConnected;
        this.blockPos = blockPos;
        this.offsetPosition = offsetPosition;
        this.sliderPositionX = new WidgetShorterSlider(0, 0, 16, num -> String.format("%d", num - 8), null);
        this.sliderPositionY = new WidgetShorterSlider(0, 0, 16, num -> String.format("%d", num - 8), null);
        this.sliderPositionZ = new WidgetShorterSlider(0, 0, 16, num -> String.format("%d", num - 8), null);
    }

    @Override
    protected void init2() {
        super.init2();
        int i = 1;
        IDrawing.setPositionAndWidth(sliderPositionX, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i++) + SQUARE_SIZE, BUTTON_WIDTH - TEXT_PADDING - GraphicsHolder.getTextWidth("-8") + (SQUARE_SIZE * 9));
        IDrawing.setPositionAndWidth(sliderPositionY, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i++) + SQUARE_SIZE, BUTTON_WIDTH - TEXT_PADDING - GraphicsHolder.getTextWidth("-8") + (SQUARE_SIZE * 9));
        IDrawing.setPositionAndWidth(sliderPositionZ, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, (SQUARE_SIZE + TEXT_FIELD_PADDING) * i + SQUARE_SIZE, BUTTON_WIDTH - TEXT_PADDING - GraphicsHolder.getTextWidth("-8") + (SQUARE_SIZE * 9));
        sliderPositionX.setHeight2(BUTTON_HEIGHT);
        sliderPositionY.setHeight2(BUTTON_HEIGHT);
        sliderPositionZ.setHeight2(BUTTON_HEIGHT);
        sliderPositionX.setValue((int) (offsetPosition.getX() * 16 + 8));
        sliderPositionY.setValue((int) (offsetPosition.getY() * 16 + 8));
        sliderPositionZ.setValue((int) (offsetPosition.getZ() * 16 + 8));
        addChild(new ClickableWidget(sliderPositionX));
        addChild(new ClickableWidget(sliderPositionY));
        addChild(new ClickableWidget(sliderPositionZ));
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        renderBackground(graphicsHolder);
        graphicsHolder.drawCenteredText(TextHelper.translatable("gui.msd.catenary_offset"), width / 2, TEXT_PADDING, ARGB_WHITE);
        int i = 1;
        graphicsHolder.drawText(TextHelper.translatable("options.msd.catenary_offset_x"), SQUARE_SIZE, (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i++) + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
        graphicsHolder.drawText(TextHelper.translatable("options.msd.catenary_offset_y"), SQUARE_SIZE, (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i++) + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
        graphicsHolder.drawText(TextHelper.translatable("options.msd.catenary_offset_z"), SQUARE_SIZE, (SQUARE_SIZE + TEXT_FIELD_PADDING) * i + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
        super.render(graphicsHolder, mouseX, mouseY, delta);
    }

    @Override
    public void onClose2() {
        super.onClose2();
        offsetPosition.setX((sliderPositionX.getIntValue() - 8) / 16D);
        offsetPosition.setY((sliderPositionY.getIntValue() - 8) / 16D);
        offsetPosition.setZ((sliderPositionZ.getIntValue() - 8) / 16D);
        InitClient.REGISTRY_CLIENT.sendPacketToServer(new MSDPacketUpdateCatenary(blockPos, offsetPosition));
        if (isConnected) {
            final Position position = Init.blockPosToPosition(blockPos);
            InitClient.REGISTRY_CLIENT.sendPacketToServer(new MSDPacketResetData(new MSDResetDataRequest(MSDMinecraftClientData.getInstance()).addCatenaryNodePosition(position).addOffsetPosition(offsetPosition)));
        }
    }
}