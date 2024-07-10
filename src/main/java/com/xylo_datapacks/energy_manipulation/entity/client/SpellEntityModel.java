// Made with Blockbench 4.10.3
// Exported for Minecraft version 1.17+ for Yarn
// Paste this class into your mod and generate all required imports

package com.xylo_datapacks.energy_manipulation.entity.client;

import com.xylo_datapacks.energy_manipulation.entity.custom.SpellEntity;
import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.model.*;
import net.minecraft.client.render.RenderLayer;
import net.minecraft.client.render.VertexConsumer;
import net.minecraft.client.util.math.MatrixStack;

@Environment(value= EnvType.CLIENT)
public class SpellEntityModel extends Model {
	private final ModelPart spell;
	
	public SpellEntityModel(ModelPart root) {
		super(RenderLayer::getEntitySolid);
        this.spell = root.getChild("spell");
	}
	
	public static TexturedModelData getTexturedModelData() {
		ModelData modelData = new ModelData();
		ModelPartData modelPartData = modelData.getRoot();
		ModelPartData spell = modelPartData.addChild("spell", ModelPartBuilder.create(), ModelTransform.pivot(0.0F, 0.0F, 0.0F));

		ModelPartData body = spell.addChild("body", ModelPartBuilder.create().uv(0, 0).cuboid(0.0F, -4.0F, -4.0F, 8.0F, 8.0F, 8.0F, new Dilation(0.0F)), ModelTransform.pivot(0.0F, 0.0F, 0.0F));
		return TexturedModelData.of(modelData, 32, 32);
	}
	
	@Override
	public void render(MatrixStack matrices, VertexConsumer vertexConsumer, int light, int overlay, float red, float green, float blue, float alpha) {
		spell.render(matrices, vertexConsumer, light, overlay, red, green, blue, alpha);
	}
	
}