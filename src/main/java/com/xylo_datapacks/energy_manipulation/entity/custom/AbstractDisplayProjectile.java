package com.xylo_datapacks.energy_manipulation.entity.custom;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtOps;
import net.minecraft.util.Util;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.slf4j.Logger;

public abstract class AbstractDisplayProjectile extends PersistentProjectileEntity {
    
    protected AbstractDisplayProjectile(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        //this.noClip = true;
        this.ignoreCameraFrustum = true;
        this.visibilityBoundingBox = this.getBoundingBox();
    }

    protected AbstractDisplayProjectile(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        super(type, x, y, z, world, stack, weapon);
        //this.noClip = true;
        this.ignoreCameraFrustum = true;
        this.visibilityBoundingBox = this.getBoundingBox();
    }

    protected AbstractDisplayProjectile(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(type, owner, world, stack, shotFrom);
        //this.noClip = true;
        this.ignoreCameraFrustum = true;
        this.visibilityBoundingBox = this.getBoundingBox();
    }

    
    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /* Item Display implementation */

    static final Logger LOGGER = LogUtils.getLogger();
    public static final int field_42384 = -1;
    private static final TrackedData<Integer> START_INTERPOLATION = DataTracker.registerData(AbstractDisplayProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> INTERPOLATION_DURATION = DataTracker.registerData(AbstractDisplayProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TELEPORT_DURATION = DataTracker.registerData(AbstractDisplayProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Vector3f> TRANSLATION = DataTracker.registerData(AbstractDisplayProjectile.class, TrackedDataHandlerRegistry.VECTOR3F);
    private static final TrackedData<Vector3f> SCALE = DataTracker.registerData(AbstractDisplayProjectile.class, TrackedDataHandlerRegistry.VECTOR3F);
    private static final TrackedData<Quaternionf> LEFT_ROTATION = DataTracker.registerData(AbstractDisplayProjectile.class, TrackedDataHandlerRegistry.QUATERNIONF);
    private static final TrackedData<Quaternionf> RIGHT_ROTATION = DataTracker.registerData(AbstractDisplayProjectile.class, TrackedDataHandlerRegistry.QUATERNIONF);
    private static final TrackedData<Byte> BILLBOARD = DataTracker.registerData(AbstractDisplayProjectile.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Integer> BRIGHTNESS = DataTracker.registerData(AbstractDisplayProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> VIEW_RANGE = DataTracker.registerData(AbstractDisplayProjectile.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> SHADOW_RADIUS = DataTracker.registerData(AbstractDisplayProjectile.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> SHADOW_STRENGTH = DataTracker.registerData(AbstractDisplayProjectile.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> WIDTH = DataTracker.registerData(AbstractDisplayProjectile.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> HEIGHT = DataTracker.registerData(AbstractDisplayProjectile.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Integer> GLOW_COLOR_OVERRIDE = DataTracker.registerData(AbstractDisplayProjectile.class, TrackedDataHandlerRegistry.INTEGER);
    private static final IntSet RENDERING_DATA_IDS = IntSet.of(
            TRANSLATION.id(), SCALE.id(), LEFT_ROTATION.id(), RIGHT_ROTATION.id(), BILLBOARD.id(), BRIGHTNESS.id(), SHADOW_RADIUS.id(), SHADOW_STRENGTH.id()
    );
    private static final float field_42376 = 0.0F;
    private static final float field_42377 = 1.0F;
    private static final int field_42378 = -1;
    public static final String TELEPORT_DURATION_KEY = "teleport_duration";
    public static final String INTERPOLATION_DURATION_KEY = "interpolation_duration";
    public static final String START_INTERPOLATION_KEY = "start_interpolation";
    public static final String TRANSFORMATION_NBT_KEY = "transformation";
    public static final String BILLBOARD_NBT_KEY = "billboard";
    public static final String BRIGHTNESS_NBT_KEY = "brightness";
    public static final String VIEW_RANGE_NBT_KEY = "view_range";
    public static final String SHADOW_RADIUS_NBT_KEY = "shadow_radius";
    public static final String SHADOW_STRENGTH_NBT_KEY = "shadow_strength";
    public static final String WIDTH_NBT_KEY = "width";
    public static final String HEIGHT_NBT_KEY = "height";
    public static final String GLOW_COLOR_OVERRIDE_NBT_KEY = "glow_color_override";
    private long interpolationStart = -2147483648L;
    private int interpolationDuration;
    private float lerpProgress;
    private Box visibilityBoundingBox;
    protected boolean renderingDataSet;
    private boolean startInterpolationSet;
    private boolean interpolationDurationSet;
    @Nullable
    private DisplayEntity.RenderState renderState;
    @Nullable
    private DisplayEntity.InterpolationTarget interpolationTarget;
    

    @Override
    public void onTrackedDataSet(TrackedData<?> data) {
        super.onTrackedDataSet(data);
        if (HEIGHT.equals(data) || WIDTH.equals(data)) {
            this.updateVisibilityBoundingBox();
        }

        if (START_INTERPOLATION.equals(data)) {
            this.startInterpolationSet = true;
        }

        if (INTERPOLATION_DURATION.equals(data)) {
            this.interpolationDurationSet = true;
        }

        if (RENDERING_DATA_IDS.contains(data.id())) {
            this.renderingDataSet = true;
        }
    }

    private static AffineTransformation getTransformation(DataTracker dataTracker) {
        Vector3f vector3f = dataTracker.get(TRANSLATION);
        Quaternionf quaternionf = dataTracker.get(LEFT_ROTATION);
        Vector3f vector3f2 = dataTracker.get(SCALE);
        Quaternionf quaternionf2 = dataTracker.get(RIGHT_ROTATION);
        return new AffineTransformation(vector3f, quaternionf, vector3f2, quaternionf2);
    }

    @Override
    public void tick() {
        Entity entity = this.getVehicle();
        if (entity != null && entity.isRemoved()) {
            this.stopRiding();
        }

        if (this.getWorld().isClient) {
            if (this.startInterpolationSet) {
                this.startInterpolationSet = false;
                int i = this.getStartInterpolation();
                this.interpolationStart = (long)(this.age + i);
            }

            if (this.interpolationDurationSet) {
                this.interpolationDurationSet = false;
                this.interpolationDuration = this.getInterpolationDuration();
            }

            if (this.renderingDataSet) {
                this.renderingDataSet = false;
                boolean bl = this.interpolationDuration != 0;
                if (bl && this.renderState != null) {
                    this.renderState = this.getLerpedRenderState(this.renderState, this.lerpProgress);
                } else {
                    this.renderState = this.copyRenderState();
                }

                this.refreshData(bl, this.lerpProgress);
            }

            if (this.interpolationTarget != null) {
                if (this.interpolationTarget.step == 0) {
                    this.interpolationTarget.apply(this);
                    this.resetPosition();
                    this.interpolationTarget = null;
                } else {
                    this.interpolationTarget.applyInterpolated(this);
                    this.interpolationTarget.step--;
                    if (this.interpolationTarget.step == 0) {
                        this.interpolationTarget = null;
                    }
                }
            }
        }
    }

    protected abstract void refreshData(boolean shouldLerp, float lerpProgress);

    @Override
    protected void initDataTracker(DataTracker.Builder builder) {
        builder.add(TELEPORT_DURATION, 0);
        builder.add(START_INTERPOLATION, 0);
        builder.add(INTERPOLATION_DURATION, 0);
        builder.add(TRANSLATION, new Vector3f());
        builder.add(SCALE, new Vector3f(1.0F, 1.0F, 1.0F));
        builder.add(RIGHT_ROTATION, new Quaternionf());
        builder.add(LEFT_ROTATION, new Quaternionf());
        builder.add(BILLBOARD, DisplayEntity.BillboardMode.FIXED.getIndex());
        builder.add(BRIGHTNESS, -1);
        builder.add(VIEW_RANGE, 1.0F);
        builder.add(SHADOW_RADIUS, 0.0F);
        builder.add(SHADOW_STRENGTH, 1.0F);
        builder.add(WIDTH, 0.0F);
        builder.add(HEIGHT, 0.0F);
        builder.add(GLOW_COLOR_OVERRIDE, -1);
    }

    @Override
    public void readCustomDataFromNbt(NbtCompound nbt) {
        super.readCustomDataFromNbt(nbt);
        if (nbt.contains("transformation")) {
            AffineTransformation.ANY_CODEC
                    .decode(NbtOps.INSTANCE, nbt.get("transformation"))
                    .resultOrPartial(Util.addPrefix("Display entity", LOGGER::error))
                    .ifPresent(pair -> this.setTransformation((AffineTransformation)pair.getFirst()));
        }

        if (nbt.contains("interpolation_duration", NbtElement.NUMBER_TYPE)) {
            int i = nbt.getInt("interpolation_duration");
            this.setInterpolationDuration(i);
        }

        if (nbt.contains("start_interpolation", NbtElement.NUMBER_TYPE)) {
            int i = nbt.getInt("start_interpolation");
            this.setStartInterpolation(i);
        }

        if (nbt.contains("teleport_duration", NbtElement.NUMBER_TYPE)) {
            int i = nbt.getInt("teleport_duration");
            this.setTeleportDuration(MathHelper.clamp(i, 0, 59));
        }

        if (nbt.contains("billboard", NbtElement.STRING_TYPE)) {
            DisplayEntity.BillboardMode.CODEC
                    .decode(NbtOps.INSTANCE, nbt.get("billboard"))
                    .resultOrPartial(Util.addPrefix("Display entity", LOGGER::error))
                    .ifPresent(pair -> this.setBillboardMode((DisplayEntity.BillboardMode)pair.getFirst()));
        }

        if (nbt.contains("view_range", NbtElement.NUMBER_TYPE)) {
            this.setViewRange(nbt.getFloat("view_range"));
        }

        if (nbt.contains("shadow_radius", NbtElement.NUMBER_TYPE)) {
            this.setShadowRadius(nbt.getFloat("shadow_radius"));
        }

        if (nbt.contains("shadow_strength", NbtElement.NUMBER_TYPE)) {
            this.setShadowStrength(nbt.getFloat("shadow_strength"));
        }

        if (nbt.contains("width", NbtElement.NUMBER_TYPE)) {
            this.setDisplayWidth(nbt.getFloat("width"));
        }

        if (nbt.contains("height", NbtElement.NUMBER_TYPE)) {
            this.setDisplayHeight(nbt.getFloat("height"));
        }

        if (nbt.contains("glow_color_override", NbtElement.NUMBER_TYPE)) {
            this.setGlowColorOverride(nbt.getInt("glow_color_override"));
        }

        if (nbt.contains("brightness", NbtElement.COMPOUND_TYPE)) {
            Brightness.CODEC
                    .decode(NbtOps.INSTANCE, nbt.get("brightness"))
                    .resultOrPartial(Util.addPrefix("Display entity", LOGGER::error))
                    .ifPresent(pair -> this.setBrightness((Brightness)pair.getFirst()));
        } else {
            this.setBrightness(null);
        }
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        AffineTransformation.ANY_CODEC
                .encodeStart(NbtOps.INSTANCE, getTransformation(this.dataTracker))
                .ifSuccess(transformations -> nbt.put("transformation", transformations));
        DisplayEntity.BillboardMode.CODEC.encodeStart(NbtOps.INSTANCE, this.getBillboardMode()).ifSuccess(billboard -> nbt.put("billboard", billboard));
        nbt.putInt("interpolation_duration", this.getInterpolationDuration());
        nbt.putInt("teleport_duration", this.getTeleportDuration());
        nbt.putFloat("view_range", this.getViewRange());
        nbt.putFloat("shadow_radius", this.getShadowRadius());
        nbt.putFloat("shadow_strength", this.getShadowStrength());
        nbt.putFloat("width", this.getDisplayWidth());
        nbt.putFloat("height", this.getDisplayHeight());
        nbt.putInt("glow_color_override", this.getGlowColorOverride());
        Brightness brightness = this.getBrightnessUnpacked();
        if (brightness != null) {
            Brightness.CODEC.encodeStart(NbtOps.INSTANCE, brightness).ifSuccess(brightnessx -> nbt.put("brightness", brightnessx));
        }
    }

    private void setTransformation(AffineTransformation transformation) {
        this.dataTracker.set(TRANSLATION, transformation.getTranslation());
        this.dataTracker.set(LEFT_ROTATION, transformation.getLeftRotation());
        this.dataTracker.set(SCALE, transformation.getScale());
        this.dataTracker.set(RIGHT_ROTATION, transformation.getRightRotation());
    }

    @Override
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps) {
        int i = this.getTeleportDuration();
        this.interpolationTarget = new DisplayEntity.InterpolationTarget(i, x, y, z, (double)yaw, (double)pitch);
    }

    @Override
    public double getLerpTargetX() {
        return this.interpolationTarget != null ? this.interpolationTarget.x : this.getX();
    }

    @Override
    public double getLerpTargetY() {
        return this.interpolationTarget != null ? this.interpolationTarget.y : this.getY();
    }

    @Override
    public double getLerpTargetZ() {
        return this.interpolationTarget != null ? this.interpolationTarget.z : this.getZ();
    }

    @Override
    public float getLerpTargetPitch() {
        return this.interpolationTarget != null ? (float)this.interpolationTarget.pitch : this.getPitch();
    }

    @Override
    public float getLerpTargetYaw() {
        return this.interpolationTarget != null ? (float)this.interpolationTarget.yaw : this.getYaw();
    }

    @Override
    public Box getVisibilityBoundingBox() {
        return this.visibilityBoundingBox;
    }

    @Override
    public PistonBehavior getPistonBehavior() {
        return PistonBehavior.IGNORE;
    }

    @Override
    public boolean canAvoidTraps() {
        return true;
    }

    @Nullable
    public DisplayEntity.RenderState getRenderState() {
        return this.renderState;
    }

    private void setInterpolationDuration(int interpolationDuration) {
        this.dataTracker.set(INTERPOLATION_DURATION, interpolationDuration);
    }

    private int getInterpolationDuration() {
        return this.dataTracker.get(INTERPOLATION_DURATION);
    }

    private void setStartInterpolation(int startInterpolation) {
        this.dataTracker.set(START_INTERPOLATION, startInterpolation, true);
    }

    private int getStartInterpolation() {
        return this.dataTracker.get(START_INTERPOLATION);
    }

    private void setTeleportDuration(int teleportDuration) {
        this.dataTracker.set(TELEPORT_DURATION, teleportDuration);
    }

    private int getTeleportDuration() {
        return this.dataTracker.get(TELEPORT_DURATION);
    }

    private void setBillboardMode(DisplayEntity.BillboardMode billboardMode) {
        this.dataTracker.set(BILLBOARD, billboardMode.getIndex());
    }

    private DisplayEntity.BillboardMode getBillboardMode() {
        return (DisplayEntity.BillboardMode)DisplayEntity.BillboardMode.FROM_INDEX.apply(this.dataTracker.get(BILLBOARD));
    }

    private void setBrightness(@Nullable Brightness brightness) {
        this.dataTracker.set(BRIGHTNESS, brightness != null ? brightness.pack() : -1);
    }

    @Nullable
    private Brightness getBrightnessUnpacked() {
        int i = this.dataTracker.get(BRIGHTNESS);
        return i != -1 ? Brightness.unpack(i) : null;
    }

    private int getBrightness() {
        return this.dataTracker.get(BRIGHTNESS);
    }

    private void setViewRange(float viewRange) {
        this.dataTracker.set(VIEW_RANGE, viewRange);
    }

    private float getViewRange() {
        return this.dataTracker.get(VIEW_RANGE);
    }

    private void setShadowRadius(float shadowRadius) {
        this.dataTracker.set(SHADOW_RADIUS, shadowRadius);
    }

    private float getShadowRadius() {
        return this.dataTracker.get(SHADOW_RADIUS);
    }

    private void setShadowStrength(float shadowStrength) {
        this.dataTracker.set(SHADOW_STRENGTH, shadowStrength);
    }

    private float getShadowStrength() {
        return this.dataTracker.get(SHADOW_STRENGTH);
    }

    private void setDisplayWidth(float width) {
        this.dataTracker.set(WIDTH, width);
    }

    private float getDisplayWidth() {
        return this.dataTracker.get(WIDTH);
    }

    private void setDisplayHeight(float height) {
        this.dataTracker.set(HEIGHT, height);
    }

    private int getGlowColorOverride() {
        return this.dataTracker.get(GLOW_COLOR_OVERRIDE);
    }

    private void setGlowColorOverride(int glowColorOverride) {
        this.dataTracker.set(GLOW_COLOR_OVERRIDE, glowColorOverride);
    }

    public float getLerpProgress(float delta) {
        int i = this.interpolationDuration;
        if (i <= 0) {
            return 1.0F;
        } else {
            float f = (float)((long)this.age - this.interpolationStart);
            float g = f + delta;
            float h = MathHelper.clamp(MathHelper.getLerpProgress(g, 0.0F, (float)i), 0.0F, 1.0F);
            this.lerpProgress = h;
            return h;
        }
    }

    private float getDisplayHeight() {
        return this.dataTracker.get(HEIGHT);
    }

    @Override
    public void setPosition(double x, double y, double z) {
        super.setPosition(x, y, z);
        this.updateVisibilityBoundingBox();
    }

    private void updateVisibilityBoundingBox() {
        float f = this.getDisplayWidth();
        float g = this.getDisplayHeight();
        if (f != 0.0F && g != 0.0F) {
            this.ignoreCameraFrustum = false;
            float h = f / 2.0F;
            double d = this.getX();
            double e = this.getY();
            double i = this.getZ();
            this.visibilityBoundingBox = new Box(d - (double)h, e, i - (double)h, d + (double)h, e + (double)g, i + (double)h);
        } else {
            this.ignoreCameraFrustum = true;
        }
    }

    @Override
    public boolean shouldRender(double distance) {
        return distance < MathHelper.square((double)this.getViewRange() * 64.0 * getRenderDistanceMultiplier());
    }

    @Override
    public int getTeamColorValue() {
        int i = this.getGlowColorOverride();
        return i != -1 ? i : super.getTeamColorValue();
    }

    private DisplayEntity.RenderState copyRenderState() {
        return new DisplayEntity.RenderState(
                DisplayEntity.AbstractInterpolator.constant(getTransformation(this.dataTracker)),
                this.getBillboardMode(),
                this.getBrightness(),
                DisplayEntity.FloatLerper.constant(this.getShadowRadius()),
                DisplayEntity.FloatLerper.constant(this.getShadowStrength()),
                this.getGlowColorOverride()
        );
    }

    private DisplayEntity.RenderState getLerpedRenderState(DisplayEntity.RenderState state, float lerpProgress) {
        AffineTransformation affineTransformation = state.transformation.interpolate(lerpProgress);
        float f = state.shadowRadius.lerp(lerpProgress);
        float g = state.shadowStrength.lerp(lerpProgress);
        return new DisplayEntity.RenderState(
                new DisplayEntity.AffineTransformationInterpolator(affineTransformation, getTransformation(this.dataTracker)),
                this.getBillboardMode(),
                this.getBrightness(),
                new DisplayEntity.FloatLerperImpl(f, this.getShadowRadius()),
                new DisplayEntity.FloatLerperImpl(g, this.getShadowStrength()),
                this.getGlowColorOverride()
        );
    }
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    
    
}
