package top.mcmtr.screen;

import com.mojang.blaze3d.vertex.PoseStack;
import mtr.data.IGui;
import mtr.mappings.ScreenMapper;
import mtr.mappings.Text;
import mtr.mappings.UtilitiesClient;
import mtr.screen.WidgetBetterTextField;
import mtr.screen.WidgetShorterSlider;
import net.minecraft.client.gui.components.AbstractWidget;
import net.minecraft.util.Mth;
import top.mcmtr.config.Config;

public class ConfigScreen extends ScreenMapper implements IGui {
    private final WidgetShorterSlider sliderRigidCatenarySegmentLength;
    private final WidgetShorterSlider sliderPIDSViewDistance;
    private final WidgetShorterSlider sliderRailwaySignViewDistance;
    private final WidgetShorterSlider sliderCustomTextViewDistance;
    private static final int BUTTON_WIDTH = 60;
    private static final int BUTTON_HEIGHT = TEXT_HEIGHT + TEXT_PADDING;

    public ConfigScreen() {
        super(Text.literal(""));
        this.sliderRigidCatenarySegmentLength = new WidgetShorterSlider(0, 0, 4, num -> String.format("%d", num < 1 ? 1 : num), null);
        this.sliderPIDSViewDistance = new WidgetShorterSlider(0, 0, Config.MAX_VIEW_DISTANCE, num -> String.format("%d", num < 4 ? 4 : num), null);
        this.sliderRailwaySignViewDistance = new WidgetShorterSlider(0, 0, Config.MAX_VIEW_DISTANCE, num -> String.format("%d", num < 4 ? 4 : num), null);
        this.sliderCustomTextViewDistance = new WidgetShorterSlider(0, 0, Config.MAX_VIEW_DISTANCE, num -> String.format("%d", num < 4 ? 4 : num), null);
    }

    @Override
    protected void init() {
        super.init();
        Config.refreshProperties();
        int i = 1;
        setPositionAndWidth(sliderRigidCatenarySegmentLength, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i++) + SQUARE_SIZE, BUTTON_WIDTH - TEXT_PADDING - font.width("256") + (SQUARE_SIZE * 9));
        setPositionAndWidth(sliderPIDSViewDistance, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i++) + SQUARE_SIZE, BUTTON_WIDTH - TEXT_PADDING - font.width("256") + (SQUARE_SIZE * 9));
        setPositionAndWidth(sliderRailwaySignViewDistance, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i++) + SQUARE_SIZE, BUTTON_WIDTH - TEXT_PADDING - font.width("256") + (SQUARE_SIZE * 9));
        setPositionAndWidth(sliderCustomTextViewDistance, width - (SQUARE_SIZE * 10) - BUTTON_WIDTH, (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i++) + SQUARE_SIZE, BUTTON_WIDTH - TEXT_PADDING - font.width("256") + (SQUARE_SIZE * 9));
        this.sliderRigidCatenarySegmentLength.setHeight(BUTTON_HEIGHT);
        this.sliderPIDSViewDistance.setHeight(BUTTON_HEIGHT);
        this.sliderRailwaySignViewDistance.setHeight(BUTTON_HEIGHT);
        this.sliderCustomTextViewDistance.setHeight(BUTTON_HEIGHT);
        this.sliderRigidCatenarySegmentLength.setValue(Config.getRigidCatenarySegmentLength());
        this.sliderPIDSViewDistance.setValue(Config.getYuuniPIDSMaxViewDistance());
        this.sliderRailwaySignViewDistance.setValue(Config.getYamanoteRailwaySignMaxViewDistance());
        this.sliderCustomTextViewDistance.setValue(Config.getCustomTextSignMaxViewDistance());
        addDrawableChild(sliderRigidCatenarySegmentLength);
        addDrawableChild(sliderPIDSViewDistance);
        addDrawableChild(sliderRailwaySignViewDistance);
        addDrawableChild(sliderCustomTextViewDistance);
    }

    @Override
    public void render(PoseStack matrices, int mouseX, int mouseY, float delta) {
        try {
            renderBackground(matrices);
            drawCenteredString(matrices, font, Text.translatable("gui.msd.options"), width / 2, TEXT_PADDING, ARGB_WHITE);
            int i = 1;
            drawString(matrices, font, Text.translatable("options.msd.rigid_catenary_segment_length"), SQUARE_SIZE, (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i++) + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
            drawString(matrices, font, Text.translatable("options.msd.pids_view_distance"), SQUARE_SIZE, (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i++) + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
            drawString(matrices, font, Text.translatable("options.msd.railway_sign_view_distance"), SQUARE_SIZE, (SQUARE_SIZE + TEXT_FIELD_PADDING) * (i++) + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
            drawString(matrices, font, Text.translatable("options.msd.custom_text_view_distance"), SQUARE_SIZE, (SQUARE_SIZE + TEXT_FIELD_PADDING) * i + SQUARE_SIZE + TEXT_PADDING, ARGB_WHITE);
            super.render(matrices, mouseX, mouseY, delta);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onClose() {
        super.onClose();
        Config.setRigidCatenarySegmentLength(Math.max(sliderRigidCatenarySegmentLength.getIntValue(), 1));
        Config.setYuuniPIDSMaxViewDistance(Math.max(sliderPIDSViewDistance.getIntValue(), 4));
        Config.setYamanoteRailwaySignMaxViewDistance(Math.max(sliderRailwaySignViewDistance.getIntValue(), 4));
        Config.setCustomTextSignMaxViewDistance(Math.max(sliderCustomTextViewDistance.getIntValue(), 4));
        Config.refreshProperties();
    }

    static void setPositionAndWidth(AbstractWidget widget, int x, int y, int widgetWidth) {
        UtilitiesClient.setWidgetX(widget, x);
        UtilitiesClient.setWidgetY(widget, y);
        widget.setWidth(Mth.clamp(widgetWidth, 0, 380 - (widget instanceof WidgetBetterTextField ? TEXT_FIELD_PADDING : 0)));
    }
}