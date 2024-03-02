package top.mcmtr.mod.screen;

import org.mtr.core.tool.Utilities;
import org.mtr.mapping.holder.ClickableWidget;
import org.mtr.mapping.holder.MutableText;
import org.mtr.mapping.holder.Text;
import org.mtr.mapping.mapper.*;
import org.mtr.mapping.tool.TextCase;
import org.mtr.mod.client.IDrawing;
import org.mtr.mod.data.IGui;
import top.mcmtr.core.data.RigidCatenary;
import top.mcmtr.core.operation.MSDUpdateDataRequest;
import top.mcmtr.mod.InitClient;
import top.mcmtr.mod.client.MSDMinecraftClientData;
import top.mcmtr.mod.packet.MSDPacketUpdateData;

public class RigidCatenaryShapeModifierScreen extends ScreenExtension implements IGui {
    private boolean needUpdate;
    private RigidCatenary.Shape shape;
    private double radius;
    private final RigidCatenary rigidCatenary;
    private final double maxRadius;
    private final ButtonWidgetExtension buttonShape;
    private final ButtonWidgetExtension buttonMinus0;
    private final ButtonWidgetExtension buttonMinus1;
    private final ButtonWidgetExtension buttonMinus2;
    private final ButtonWidgetExtension buttonPlus0;
    private final ButtonWidgetExtension buttonPlus1;
    private final ButtonWidgetExtension buttonPlus2;
    private final TextFieldWidgetExtension textFieldRadius;
    private final MutableText shapeText = TextHelper.translatable("gui.msd.rigid_catenary_shape");
    private final MutableText radiusText = TextHelper.translatable("gui.msd.rigid_catenary_radius");
    private final int xStart;
    private static final int BUTTON_WIDTH = SQUARE_SIZE * 16;

    public RigidCatenaryShapeModifierScreen(String rigidCatenaryId) {
        super();
        needUpdate = false;
        rigidCatenary = MSDMinecraftClientData.getInstance().rigidCatenaryIdMap.get(rigidCatenaryId);
        shape = rigidCatenary == null ? RigidCatenary.Shape.QUADRATIC : rigidCatenary.rigidCatenaryMath.getShape();
        radius = rigidCatenary == null ? 0 : rigidCatenary.rigidCatenaryMath.getVerticalRadius();
        maxRadius = rigidCatenary == null ? 0 : rigidCatenary.rigidCatenaryMath.getMaxVerticalRadius();

        buttonShape = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal(""), button -> {
            shape = shape == RigidCatenary.Shape.QUADRATIC ? RigidCatenary.Shape.TWO_RADII : RigidCatenary.Shape.QUADRATIC;
            update(radius, true);
        });
        buttonMinus2 = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("-10"), button -> update(radius - 10, true));
        buttonMinus1 = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("-1"), button -> update(radius - 1, true));
        buttonMinus0 = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("-0.1"), button -> update(radius - 0.1, true));
        buttonPlus0 = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("+0.1"), button -> update(radius + 0.1, true));
        buttonPlus1 = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("+1"), button -> update(radius + 1, true));
        buttonPlus2 = new ButtonWidgetExtension(0, 0, 0, SQUARE_SIZE, TextHelper.literal("+10"), button -> update(radius + 10, true));
        textFieldRadius = new TextFieldWidgetExtension(0, 0, 0, SQUARE_SIZE, 256, TextCase.DEFAULT, "[^\\d\\.]", "0");

        xStart = Math.max(GraphicsHolder.getTextWidth(shapeText), GraphicsHolder.getTextWidth(radiusText)) + TEXT_PADDING * 2;
    }

    @Override
    protected void init2() {
        super.init2();
        IDrawing.setPositionAndWidth(buttonShape, xStart, 0, BUTTON_WIDTH);
        IDrawing.setPositionAndWidth(buttonMinus2, xStart, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2);
        IDrawing.setPositionAndWidth(buttonMinus1, xStart + SQUARE_SIZE * 2, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2);
        IDrawing.setPositionAndWidth(buttonMinus0, xStart + SQUARE_SIZE * 4, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2);
        IDrawing.setPositionAndWidth(buttonPlus0, xStart + BUTTON_WIDTH - SQUARE_SIZE * 6, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2);
        IDrawing.setPositionAndWidth(buttonPlus1, xStart + BUTTON_WIDTH - SQUARE_SIZE * 4, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2);
        IDrawing.setPositionAndWidth(buttonPlus2, xStart + BUTTON_WIDTH - SQUARE_SIZE * 2, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, SQUARE_SIZE * 2);
        IDrawing.setPositionAndWidth(textFieldRadius, (xStart + SQUARE_SIZE * 6) + TEXT_FIELD_PADDING / 2, SQUARE_SIZE + TEXT_FIELD_PADDING / 2, BUTTON_WIDTH - SQUARE_SIZE * 12 - TEXT_FIELD_PADDING);

        addChild(new ClickableWidget(buttonShape));
        addChild(new ClickableWidget(buttonMinus2));
        addChild(new ClickableWidget(buttonMinus1));
        addChild(new ClickableWidget(buttonMinus0));
        addChild(new ClickableWidget(buttonPlus0));
        addChild(new ClickableWidget(buttonPlus1));
        addChild(new ClickableWidget(buttonPlus2));
        addChild(new ClickableWidget(textFieldRadius));
        update(radius, false);
    }

    @Override
    public void render(GraphicsHolder graphicsHolder, int mouseX, int mouseY, float delta) {
        final GuiDrawing guiDrawing = new GuiDrawing(graphicsHolder);
        guiDrawing.beginDrawingRectangle();
        guiDrawing.drawRectangle(0, 0, width, shape == RigidCatenary.Shape.QUADRATIC ? SQUARE_SIZE : SQUARE_SIZE * 2 + TEXT_FIELD_PADDING, ARGB_BACKGROUND);
        guiDrawing.finishDrawingRectangle();
        super.render(graphicsHolder, mouseX, mouseY, delta);
        graphicsHolder.drawText(shapeText, TEXT_PADDING, TEXT_PADDING, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
        if (shape != RigidCatenary.Shape.QUADRATIC) {
            graphicsHolder.drawText(radiusText, TEXT_PADDING, SQUARE_SIZE + TEXT_PADDING + TEXT_FIELD_PADDING / 2, ARGB_WHITE, false, MAX_LIGHT_GLOWING);
        }
    }

    @Override
    public boolean isPauseScreen2() {
        return false;
    }

    @Override
    public void onClose2() {
        super.onClose2();
        if (needUpdate) {
            InitClient.REGISTRY_CLIENT.sendPacketToServer(new MSDPacketUpdateData(new MSDUpdateDataRequest(MSDMinecraftClientData.getInstance()).addRigidCatenary(RigidCatenary.copy(rigidCatenary, shape, radius))));
        }
    }

    private void update(double newRadius, boolean updateClient) {
        radius = Utilities.clamp(Utilities.round(newRadius, 2), 0, maxRadius);
        buttonShape.setMessage2(new Text(TextHelper.translatable(shape == RigidCatenary.Shape.QUADRATIC ? "gui.msd.rigid_catenary_shape_quadratic" : "gui.msd.rigid_catenary_shape_two_radii").data));
        buttonMinus2.setVisibleMapped(shape != RigidCatenary.Shape.QUADRATIC);
        buttonMinus1.setVisibleMapped(shape != RigidCatenary.Shape.QUADRATIC);
        buttonMinus0.setVisibleMapped(shape != RigidCatenary.Shape.QUADRATIC);
        buttonPlus0.setVisibleMapped(shape != RigidCatenary.Shape.QUADRATIC);
        buttonPlus1.setVisibleMapped(shape != RigidCatenary.Shape.QUADRATIC);
        buttonPlus2.setVisibleMapped(shape != RigidCatenary.Shape.QUADRATIC);
        buttonMinus2.setActiveMapped(radius > 0);
        buttonMinus1.setActiveMapped(radius > 0);
        buttonMinus0.setActiveMapped(radius > 0);
        buttonPlus0.setActiveMapped(radius < maxRadius);
        buttonPlus1.setActiveMapped(radius < maxRadius);
        buttonPlus2.setActiveMapped(radius < maxRadius);
        textFieldRadius.setVisibleMapped(shape != RigidCatenary.Shape.QUADRATIC);
        textFieldRadius.setText2(String.valueOf(radius));
        if (updateClient) {
            needUpdate = true;
            final MSDMinecraftClientData clientData = MSDMinecraftClientData.getInstance();
            clientData.rigidCatenaries.remove(rigidCatenary);
            clientData.rigidCatenaries.add(RigidCatenary.copy(rigidCatenary, shape, radius));
            clientData.sync();
        }
    }
}