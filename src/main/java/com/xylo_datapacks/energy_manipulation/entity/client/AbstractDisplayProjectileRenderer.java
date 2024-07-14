package com.xylo_datapacks.energy_manipulation.entity.client;

import com.xylo_datapacks.energy_manipulation.entity.custom.AbstractDisplayProjectile;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.Camera;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderDispatcher;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.texture.SpriteAtlasTexture;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;

@Environment(EnvType.CLIENT)
public abstract class AbstractDisplayProjectileRenderer<T extends AbstractDisplayProjectile, S> extends EntityRenderer<T> {
    private final EntityRenderDispatcher renderDispatcher;

    protected AbstractDisplayProjectileRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.renderDispatcher = context.getRenderDispatcher();
    }

    public Identifier getTexture(T displayEntity) {
        return SpriteAtlasTexture.BLOCK_ATLAS_TEXTURE;
    }

    public void render(T displayEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        DisplayEntity.RenderState renderState = displayEntity.getRenderState();
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

    private Quaternionf getBillboardRotation(DisplayEntity.RenderState renderState, T entity, float yaw, Quaternionf rotation) {
        Camera camera = this.renderDispatcher.camera;

        return switch (renderState.billboardConstraints()) {
            case FIXED ->
                    rotation.rotationYXZ((float) (-Math.PI / 180.0) * lerpYaw(entity, yaw), (float) (Math.PI / 180.0) * lerpPitch(entity, yaw), 0.0F);
            case HORIZONTAL ->
                    rotation.rotationYXZ((float) (-Math.PI / 180.0) * lerpYaw(entity, yaw), (float) (Math.PI / 180.0) * getNegatedPitch(camera), 0.0F);
            case VERTICAL ->
                    rotation.rotationYXZ((float) (-Math.PI / 180.0) * getBackwardsYaw(camera), (float) (Math.PI / 180.0) * lerpPitch(entity, yaw), 0.0F);
            case CENTER ->
                    rotation.rotationYXZ((float) (-Math.PI / 180.0) * getBackwardsYaw(camera), (float) (Math.PI / 180.0) * getNegatedPitch(camera), 0.0F);
        };
    }

    private static float getBackwardsYaw(Camera camera) {
        return camera.getYaw() - 180.0F;
    }

    private static float getNegatedPitch(Camera camera) {
        return -camera.getPitch();
    }

    private static <T extends AbstractDisplayProjectile> float lerpYaw(T entity, float delta) {
        return MathHelper.lerpAngleDegrees(delta, entity.prevYaw, entity.getYaw());
    }

    private static <T extends AbstractDisplayProjectile> float lerpPitch(T entity, float delta) {
        return MathHelper.lerp(delta, entity.prevPitch, entity.getPitch());
    }

    @Nullable
    protected abstract S getData(T entity);

    protected abstract void render(T entity, S data, MatrixStack matrices, VertexConsumerProvider vertexConsumers, int brightness, float lerpProgress);


    @Environment(EnvType.CLIENT)
    public static class ItemDisplayProjectileRenderer extends AbstractDisplayProjectileRenderer<AbstractDisplayProjectile.AbstractItemDisplayProjectile, DisplayEntity.ItemDisplayEntity.Data> {
        private final ItemRenderer itemRenderer;

        protected ItemDisplayProjectileRenderer(EntityRendererFactory.Context context) {
            super(context);
            this.itemRenderer = context.getItemRenderer();
        }

        @Nullable
        protected DisplayEntity.ItemDisplayEntity.Data getData(AbstractDisplayProjectile.AbstractItemDisplayProjectile itemAbstractDisplayProjectile) {
            return itemAbstractDisplayProjectile.getData();
        }

        public void render(
                AbstractDisplayProjectile.AbstractItemDisplayProjectile itemAbstractDisplayProjectile,
                DisplayEntity.ItemDisplayEntity.Data data,
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
                            itemAbstractDisplayProjectile.getWorld(),
                            itemAbstractDisplayProjectile.getId()
                    );
        }
    }
}
