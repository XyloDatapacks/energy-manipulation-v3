package com.xylo_datapacks.energy_manipulation.entity.client;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import com.xylo_datapacks.energy_manipulation.entity.custom.SpellEntity;
import com.xylo_datapacks.energy_manipulation.registry.ModEntityModelLayerRegistry;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.render.OverlayTexture;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.render.VertexConsumerProvider;
import net.minecraft.client.render.entity.EntityRenderer;
import net.minecraft.client.render.entity.EntityRendererFactory;
import net.minecraft.client.render.item.ItemRenderer;
import net.minecraft.client.util.math.MatrixStack;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.RotationAxis;

@Environment(value= EnvType.CLIENT)
public class SpellEntityRenderer extends EntityRenderer<SpellEntity> {
    private static final Identifier TEXTURE = EnergyManipulation.id("textures/entity/spell.png");
    private final SpellEntityModel model;
    
    public SpellEntityRenderer(EntityRendererFactory.Context context) {
        super(context);
        this.model = new SpellEntityModel(context.getPart(ModEntityModelLayerRegistry.SPELL));
    }

    @Override
    public void render(SpellEntity spellEntity, float f, float g, MatrixStack matrixStack, VertexConsumerProvider vertexConsumerProvider, int i) {
        matrixStack.push();
        matrixStack.multiply(RotationAxis.POSITIVE_Y.rotationDegrees(MathHelper.lerp(g, spellEntity.prevYaw, spellEntity.getYaw()) - 90.0f));
        matrixStack.multiply(RotationAxis.POSITIVE_Z.rotationDegrees(MathHelper.lerp(g, spellEntity.prevPitch, spellEntity.getPitch()) + 90.0f));
        VertexConsumer vertexConsumer = ItemRenderer.getDirectItemGlintConsumer(vertexConsumerProvider, this.model.getLayer(this.getTexture(spellEntity)), false, true);
        this.model.render(matrixStack, vertexConsumer, i, OverlayTexture.DEFAULT_UV, 0xFFFFFFFF);
        matrixStack.pop();
        super.render(spellEntity, f, g, matrixStack, vertexConsumerProvider, i);
    }

    @Override
    public Identifier getTexture(SpellEntity entity) {
        return TEXTURE;
    }
}
