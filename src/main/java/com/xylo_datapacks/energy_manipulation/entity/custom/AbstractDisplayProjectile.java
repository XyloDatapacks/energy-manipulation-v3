package com.xylo_datapacks.energy_manipulation.entity.custom;

import com.mojang.logging.LogUtils;
import it.unimi.dsi.fastutil.ints.IntSet;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.entity.decoration.DisplayEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.inventory.StackReference;
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
    
    protected AbstractDisplayProjectile(EntityType<? extends AbstractItemDisplayProjectile> entityType, World world) {
        super(entityType, world);
        this.ignoreCameraFrustum = true;
        this.visibilityBoundingBox = this.getBoundingBox();
    }

    protected AbstractDisplayProjectile(EntityType<? extends AbstractItemDisplayProjectile> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        super(type, x, y, z, world, stack, weapon);
        this.ignoreCameraFrustum = true;
        this.visibilityBoundingBox = this.getBoundingBox();
    }

    protected AbstractDisplayProjectile(EntityType<? extends AbstractItemDisplayProjectile> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(type, owner, world, stack, shotFrom);
        this.ignoreCameraFrustum = true;
        this.visibilityBoundingBox = this.getBoundingBox();
    }


    
    
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    //region Display Entity implementation

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
        super.tick();
        /*Entity entity = this.getVehicle();
        if (entity != null && entity.isRemoved()) {
            this.stopRiding();
        }*/

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
        super.initDataTracker(builder);
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
        if (nbt.contains(TRANSFORMATION_NBT_KEY)) {
            AffineTransformation.ANY_CODEC
                    .decode(NbtOps.INSTANCE, nbt.get(TRANSFORMATION_NBT_KEY))
                    .resultOrPartial(Util.addPrefix("Display entity", LOGGER::error))
                    .ifPresent(pair -> this.setTransformation((AffineTransformation)pair.getFirst()));
        }

        if (nbt.contains(INTERPOLATION_DURATION_KEY, NbtElement.NUMBER_TYPE)) {
            int i = nbt.getInt(INTERPOLATION_DURATION_KEY);
            this.setInterpolationDuration(i);
        }

        if (nbt.contains(START_INTERPOLATION_KEY, NbtElement.NUMBER_TYPE)) {
            int i = nbt.getInt(START_INTERPOLATION_KEY);
            this.setStartInterpolation(i);
        }

        if (nbt.contains(TELEPORT_DURATION_KEY, NbtElement.NUMBER_TYPE)) {
            int i = nbt.getInt(TELEPORT_DURATION_KEY);
            this.setTeleportDuration(MathHelper.clamp(i, 0, 59));
        }

        if (nbt.contains(BILLBOARD_NBT_KEY, NbtElement.STRING_TYPE)) {
            DisplayEntity.BillboardMode.CODEC
                    .decode(NbtOps.INSTANCE, nbt.get(BILLBOARD_NBT_KEY))
                    .resultOrPartial(Util.addPrefix("Display entity", LOGGER::error))
                    .ifPresent(pair -> this.setBillboardMode((DisplayEntity.BillboardMode)pair.getFirst()));
        }

        if (nbt.contains(VIEW_RANGE_NBT_KEY, NbtElement.NUMBER_TYPE)) {
            this.setViewRange(nbt.getFloat(VIEW_RANGE_NBT_KEY));
        }

        if (nbt.contains(SHADOW_RADIUS_NBT_KEY, NbtElement.NUMBER_TYPE)) {
            this.setShadowRadius(nbt.getFloat(SHADOW_RADIUS_NBT_KEY));
        }

        if (nbt.contains(SHADOW_STRENGTH_NBT_KEY, NbtElement.NUMBER_TYPE)) {
            this.setShadowStrength(nbt.getFloat(SHADOW_STRENGTH_NBT_KEY));
        }

        if (nbt.contains(WIDTH_NBT_KEY, NbtElement.NUMBER_TYPE)) {
            this.setDisplayWidth(nbt.getFloat(WIDTH_NBT_KEY));
        }

        if (nbt.contains(HEIGHT_NBT_KEY, NbtElement.NUMBER_TYPE)) {
            this.setDisplayHeight(nbt.getFloat(HEIGHT_NBT_KEY));
        }

        if (nbt.contains(GLOW_COLOR_OVERRIDE_NBT_KEY, NbtElement.NUMBER_TYPE)) {
            this.setGlowColorOverride(nbt.getInt(GLOW_COLOR_OVERRIDE_NBT_KEY));
        }

        if (nbt.contains(BRIGHTNESS_NBT_KEY, NbtElement.COMPOUND_TYPE)) {
            Brightness.CODEC
                    .decode(NbtOps.INSTANCE, nbt.get(BRIGHTNESS_NBT_KEY))
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
                .ifSuccess(transformations -> nbt.put(TRANSFORMATION_NBT_KEY, transformations));
        DisplayEntity.BillboardMode.CODEC.encodeStart(NbtOps.INSTANCE, this.getBillboardMode()).ifSuccess(billboard -> nbt.put(BILLBOARD_NBT_KEY, billboard));
        nbt.putInt(INTERPOLATION_DURATION_KEY, this.getInterpolationDuration());
        nbt.putInt(TELEPORT_DURATION_KEY, this.getTeleportDuration());
        nbt.putFloat(VIEW_RANGE_NBT_KEY, this.getViewRange());
        nbt.putFloat(SHADOW_RADIUS_NBT_KEY, this.getShadowRadius());
        nbt.putFloat(SHADOW_STRENGTH_NBT_KEY, this.getShadowStrength());
        nbt.putFloat(WIDTH_NBT_KEY, this.getDisplayWidth());
        nbt.putFloat(HEIGHT_NBT_KEY, this.getDisplayHeight());
        nbt.putInt(GLOW_COLOR_OVERRIDE_NBT_KEY, this.getGlowColorOverride());
        Brightness brightness = this.getBrightnessUnpacked();
        if (brightness != null) {
            Brightness.CODEC.encodeStart(NbtOps.INSTANCE, brightness).ifSuccess(brightnessx -> nbt.put(BRIGHTNESS_NBT_KEY, brightnessx));
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
    //region Item Display Entity implementation

    public abstract static class AbstractItemDisplayProjectile extends AbstractDisplayProjectile {
        private static final String ITEM_NBT_KEY = "item";
        private static final String ITEM_DISPLAY_NBT_KEY = "item_display";
        private static final TrackedData<ItemStack> ITEM = DataTracker.registerData(AbstractItemDisplayProjectile.class, TrackedDataHandlerRegistry.ITEM_STACK);
        private static final TrackedData<Byte> ITEM_DISPLAY = DataTracker.registerData(AbstractItemDisplayProjectile.class, TrackedDataHandlerRegistry.BYTE);
        private final StackReference stackReference = StackReference.of(this::getItemStack, this::setItemStack);
        @Nullable
        private DisplayEntity.ItemDisplayEntity.Data data;

        protected AbstractItemDisplayProjectile(EntityType<? extends AbstractItemDisplayProjectile> entityType, World world) {
            super(entityType, world);
        }

        protected AbstractItemDisplayProjectile(EntityType<? extends AbstractItemDisplayProjectile> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
            super(type, x, y, z, world, stack, weapon);
        }

        protected AbstractItemDisplayProjectile(EntityType<? extends AbstractItemDisplayProjectile> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
            super(type, owner, world, stack, shotFrom);
        }


        @Override
        protected void initDataTracker(DataTracker.Builder builder) {
            super.initDataTracker(builder);
            builder.add(ITEM, ItemStack.EMPTY);
            builder.add(ITEM_DISPLAY, ModelTransformationMode.NONE.getIndex());
        }

        @Override
        public void onTrackedDataSet(TrackedData<?> data) {
            super.onTrackedDataSet(data);
            if (ITEM.equals(data) || ITEM_DISPLAY.equals(data)) {
                this.renderingDataSet = true;
            }
        }

        public ItemStack getItemStack() {
            return this.dataTracker.get(ITEM);
        }

        private void setItemStack(ItemStack stack) {
            this.dataTracker.set(ITEM, stack);
        }

        private void setTransformationMode(ModelTransformationMode transformationMode) {
            this.dataTracker.set(ITEM_DISPLAY, transformationMode.getIndex());
        }

        private ModelTransformationMode getTransformationMode() {
            return (ModelTransformationMode)ModelTransformationMode.FROM_INDEX.apply(this.dataTracker.get(ITEM_DISPLAY));
        }

        @Override
        public void readCustomDataFromNbt(NbtCompound nbt) {
            super.readCustomDataFromNbt(nbt);
            if (nbt.contains(ITEM_NBT_KEY)) {
                this.setItemStack((ItemStack)ItemStack.fromNbt(this.getRegistryManager(), nbt.getCompound(ITEM_NBT_KEY)).orElse(ItemStack.EMPTY));
            } else {
                this.setItemStack(ItemStack.EMPTY);
            }

            if (nbt.contains(ITEM_DISPLAY_NBT_KEY, NbtElement.STRING_TYPE)) {
                ModelTransformationMode.CODEC
                        .decode(NbtOps.INSTANCE, nbt.get(ITEM_DISPLAY_NBT_KEY))
                        .resultOrPartial(Util.addPrefix("Display entity", DisplayEntity.LOGGER::error))
                        .ifPresent(mode -> this.setTransformationMode((ModelTransformationMode)mode.getFirst()));
            }
        }

        @Override
        public void writeCustomDataToNbt(NbtCompound nbt) {
            super.writeCustomDataToNbt(nbt);
            if (!this.getItemStack().isEmpty()) {
                nbt.put(ITEM_NBT_KEY, this.getItemStack().encode(this.getRegistryManager()));
            }

            ModelTransformationMode.CODEC.encodeStart(NbtOps.INSTANCE, this.getTransformationMode()).ifSuccess(nbtx -> nbt.put(ITEM_DISPLAY_NBT_KEY, nbtx));
        }

        @Override
        public StackReference getStackReference(int mappedIndex) {
            return mappedIndex == 0 ? this.stackReference : StackReference.EMPTY;
        }

        @Nullable
        public DisplayEntity.ItemDisplayEntity.Data getData() {
            return this.data;
        }

        @Override
        protected void refreshData(boolean shouldLerp, float lerpProgress) {
            ItemStack itemStack = this.getItemStack();
            itemStack.setHolder(this);
            this.data = new DisplayEntity.ItemDisplayEntity.Data(itemStack, this.getTransformationMode());
        }

        public static record Data(ItemStack itemStack, ModelTransformationMode itemTransform) {
        }
    }

    //endregion
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

    //endregion
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    
    
}
