package com.xylo_datapacks.energy_manipulation.entity.client;

import java.util.ArrayList;
import java.util.List;

import com.xylo_datapacks.energy_manipulation.entity.custom.DisplayPersistentProjectileEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.MinecraftClient;
import net.minecraft.client.font.TextRenderer;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.block.BlockRenderManager;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.joml.Matrix4f;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public abstract class DisplayPersistentProjectileEntityRenderer<T extends DisplayPersistentProjectileEntity, S> extends EntityRenderer<T> {
    private final EntityRenderDispatcher renderDispatcher;

    protected DisplayPersistentProjectileEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.renderDispatcher = context.getRenderDispatcher();
    }

    public Identifier getTexture(T displayEntity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }

    public void render(T displayEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        DisplayPersistentProjectileEntity.RenderState renderState = displayEntity.getRenderState();
        if (renderState != null) {
            S object = this.getData(displayEntity);
            if (object != null) {
                float h = displayEntity.getLerpProgress(g);
                this.shadowRadius = renderState.shadowRadius().lerp(h);
                this.shadowOpacity = renderState.shadowStrength().lerp(h);
                int j = renderState.brightnessOverride();
                int k = j != -1 ? j : i;
                super.render(displayEntity, f, g, matrixStack, vertexConsumerProvider, k);
                matrixStack.push();
                matrixStack.multiply(this.getBillboardRotation(renderState, displayEntity, g, new Quaternionf()));
                AffineTransformation affineTransformation = renderState.transformation().interpolate(h);
                matrixStack.multiplyPositionMatrix(affineTransformation.getMatrix());
                this.render(displayEntity, object, matrixStack, vertexConsumerProvider, k, h);
                matrixStack.pop();
            }
        }
    }

    private Quaternionf getBillboardRotation(DisplayPersistentProjectileEntity.RenderState renderState, T entity, float yaw, Quaternionf rotation) {
        Camera camera = this.renderDispatcher.camera;

        return switch (renderState.billboardConstraints()) {
            case FIXED -> rotation.rotationYXZ((float) (-Math.PI / 180.0) * lerpYaw(entity, yaw), (float) (Math.PI / 180.0) * lerpPitch(entity, yaw), 0.0F);
            case HORIZONTAL -> rotation.rotationYXZ((float) (-Math.PI / 180.0) * lerpYaw(entity, yaw), (float) (Math.PI / 180.0) * getNegatedPitch(camera), 0.0F);
            case VERTICAL -> rotation.rotationYXZ((float) (-Math.PI / 180.0) * getBackwardsYaw(camera), (float) (Math.PI / 180.0) * lerpPitch(entity, yaw), 0.0F);
            case CENTER -> rotation.rotationYXZ((float) (-Math.PI / 180.0) * getBackwardsYaw(camera), (float) (Math.PI / 180.0) * getNegatedPitch(camera), 0.0F);
        };
    }

    private static float getBackwardsYaw(Camera camera) {
        return camera.getYaw() - 180.0F;
    }

    private static float getNegatedPitch(Camera camera) {
        return -camera.getPitch();
    }

    private static <T extends DisplayPersistentProjectileEntity> float lerpYaw(T entity, float delta) {
        return MathHelper.lerpAngleDegrees(delta, entity.prevYaw, entity.getYaw());
    }

    private static <T extends DisplayPersistentProjectileEntity> float lerpPitch(T entity, float delta) {
        return MathHelper.lerp(delta, entity.prevPitch, entity.getPitch());
    }

    @Nullable
    protected abstract S getData(T entity);

    protected abstract void render(T entity, S data, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int brightness, float lerpProgress);

    @Environment(EnvType.CLIENT)
    public static class BlockDisplayPersistentProjectileEntityRenderer extends DisplayPersistentProjectileEntityRenderer<DisplayPersistentProjectileEntity.BlockDisplayPersistentProjectileEntity, DisplayPersistentProjectileEntity.BlockDisplayPersistentProjectileEntity.Data> {
        private final BlockRenderManager blockRenderManager;

        protected BlockDisplayPersistentProjectileEntityRenderer(EntityRendererFactory.Context context) {
            super(context);
            this.blockRenderManager = context.getBlockRenderManager();
        }

        @Nullable
        protected DisplayPersistentProjectileEntity.BlockDisplayPersistentProjectileEntity.Data getData(DisplayPersistentProjectileEntity.BlockDisplayPersistentProjectileEntity blockDisplayPersistentProjectileEntity) {
            return blockDisplayPersistentProjectileEntity.getData();
        }

        public void render(
                DisplayPersistentProjectileEntity.BlockDisplayPersistentProjectileEntity blockDisplayPersistentProjectileEntity,
                DisplayPersistentProjectileEntity.BlockDisplayPersistentProjectileEntity.Data data,
                MatrixStack matrixStack,
                VertexConsumerProvider vertexConsumerProvider,
                int i,
                float f
        ) {
            this.blockRenderManager.renderBlockAsEntity(data.blockState(), matrixStack, vertexConsumerProvider, i, OverlayTexture.DEFAULT_UV);
        }
    }

    @Environment(EnvType.CLIENT)
    public static class ItemDisplayPersistentProjectileEntityRenderer extends DisplayPersistentProjectileEntityRenderer<DisplayPersistentProjectileEntity.ItemDisplayPersistentProjectileEntity, DisplayPersistentProjectileEntity.ItemDisplayPersistentProjectileEntity.Data> {
        private final ItemRenderer itemRenderer;

        protected ItemDisplayPersistentProjectileEntityRenderer(EntityRendererFactory.Context context) {
            super(context);
            this.itemRenderer = context.getItemRenderer();
        }

        @Nullable
        protected DisplayPersistentProjectileEntity.ItemDisplayPersistentProjectileEntity.Data getData(DisplayPersistentProjectileEntity.ItemDisplayPersistentProjectileEntity itemDisplayPersistentProjectileEntity) {
            return itemDisplayPersistentProjectileEntity.getData();
        }

        public void render(
                DisplayPersistentProjectileEntity.ItemDisplayPersistentProjectileEntity itemDisplayPersistentProjectileEntity,
                DisplayPersistentProjectileEntity.ItemDisplayPersistentProjectileEntity.Data data,
                MatrixStack matrixStack,
                VertexConsumerProvider vertexConsumerProvider,
                int i,
                float f
        ) {
            matrixStack.multiply(RotationAxis.POSITIVE_Y.rotation((float) Math.PI));
            this.itemRenderer
                    .renderItem(
                            data.itemStack(),
                            data.itemTransform(),
                            i,
                            OverlayTexture.DEFAULT_UV,
                            matrixStack,
                            vertexConsumerProvider,
                            itemDisplayPersistentProjectileEntity.getWorld(),
                            itemDisplayPersistentProjectileEntity.getId()
                    );
        }
    }

    @Environment(EnvType.CLIENT)
    public static class TextDisplayPersistentProjectileEntityRenderer extends DisplayPersistentProjectileEntityRenderer<DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity, DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.Data> {
        private final TextRenderer displayTextRenderer;

        protected TextDisplayPersistentProjectileEntityRenderer(EntityRendererFactory.Context context) {
            super(context);
            this.displayTextRenderer = context.getTextRenderer();
        }

        private DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextLines getLines(Text text, int width) {
            List<OrderedText> list = this.displayTextRenderer.wrapLines(text, width);
            List<DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextLine> list2 = new ArrayList(list.size());
            int i = 0;

            for (OrderedText orderedText : list) {
                int j = this.displayTextRenderer.getWidth(orderedText);
                i = Math.max(i, j);
                list2.add(new DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextLine(orderedText, j));
            }

            return new DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextLines(list2, i);
        }

        @Nullable
        protected DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.Data getData(DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity textDisplayPersistentProjectileEntity) {
            return textDisplayPersistentProjectileEntity.getData();
        }

        public void render(
                DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity textDisplayPersistentProjectileEntity,
                DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.Data data,
                MatrixStack matrixStack,
                VertexConsumerProvider vertexConsumerProvider,
                int i,
                float f
        ) {
            byte b = data.flags();
            boolean bl = (b & DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.SEE_THROUGH_FLAG) != 0;
            boolean bl2 = (b & DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.DEFAULT_BACKGROUND_FLAG) != 0;
            boolean bl3 = (b & DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.SHADOW_FLAG) != 0;
            DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextAlignment textAlignment = DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.getAlignment(b);
            byte c = (byte)data.textOpacity().lerp(f);
            int j;
            if (bl2) {
                float g = MinecraftClient.getInstance().options.getTextBackgroundOpacity(0.25F);
                j = (int)(g * 255.0F) << 24;
            } else {
                j = data.backgroundColor().lerp(f);
            }

            float g = 0.0F;
            Matrix4f matrix4f = matrixStack.peek().getPositionMatrix();
            matrix4f.rotate((float) Math.PI, 0.0F, 1.0F, 0.0F);
            matrix4f.scale(-0.025F, -0.025F, -0.025F);
            DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextLines textLines = textDisplayPersistentProjectileEntity.splitLines(this::getLines);
            int k = 9 + 1;
            int l = textLines.width();
            int m = textLines.lines().size() * k;
            matrix4f.translate(1.0F - (float)l / 2.0F, (float)(-m), 0.0F);
            if (j != 0) {
                VertexConsumer vertexConsumer = vertexConsumerProvider.getBuffer(bl ? RenderLayer.getTextBackgroundSeeThrough() : RenderLayer.getTextBackground());
                vertexConsumer.vertex(matrix4f, -1.0F, -1.0F, 0.0F).color(j).light(i);
                vertexConsumer.vertex(matrix4f, -1.0F, (float)m, 0.0F).color(j).light(i);
                vertexConsumer.vertex(matrix4f, (float)l, (float)m, 0.0F).color(j).light(i);
                vertexConsumer.vertex(matrix4f, (float)l, -1.0F, 0.0F).color(j).light(i);
            }

            for (DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextLine textLine : textLines.lines()) {
                float h = switch (textAlignment) {
                    case LEFT -> 0.0F;
                    case RIGHT -> (float)(l - textLine.width());
                    case CENTER -> (float)l / 2.0F - (float)textLine.width() / 2.0F;
                };
                this.displayTextRenderer
                        .draw(
                                textLine.contents(),
                                h,
                                g,
                                c << 24 | 16777215,
                                bl3,
                                matrix4f,
                                vertexConsumerProvider,
                                bl ? TextRenderer.TextLayerType.SEE_THROUGH : TextRenderer.TextLayerType.POLYGON_OFFSET,
                                0,
                                i
                        );
                g += (float)k;
            }
        }
    }
}
