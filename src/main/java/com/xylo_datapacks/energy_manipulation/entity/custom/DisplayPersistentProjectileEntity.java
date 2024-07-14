package com.xylo_datapacks.energy_manipulation.entity.custom;

import com.mojang.datafixers.util.Pair;
import com.mojang.logging.LogUtils;
import com.mojang.serialization.Codec;
import com.xylo_datapacks.energy_manipulation.mixin.EntityAccessor;
import it.unimi.dsi.fastutil.ints.IntSet;
import java.util.List;
import java.util.Optional;
import java.util.function.IntFunction;
import net.minecraft.block.BlockState;
import net.minecraft.block.Blocks;
import net.minecraft.block.piston.PistonBehavior;
import net.minecraft.client.render.model.json.ModelTransformationMode;
import net.minecraft.entity.Entity;
import net.minecraft.entity.EntityType;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.data.DataTracker;
import net.minecraft.entity.data.TrackedData;
import net.minecraft.entity.data.TrackedDataHandlerRegistry;
import net.minecraft.entity.decoration.Brightness;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.inventory.StackReference;
import net.minecraft.item.ItemStack;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.registry.RegistryKeys;
import net.minecraft.server.command.ServerCommandSource;
import net.minecraft.text.OrderedText;
import net.minecraft.text.Text;
import net.minecraft.text.Texts;
import net.minecraft.util.StringIdentifiable;
import net.minecraft.util.Util;
import net.minecraft.util.function.ValueLists;
import net.minecraft.util.math.AffineTransformation;
import net.minecraft.util.math.Box;
import net.minecraft.util.math.ColorHelper;
import net.minecraft.util.math.MathHelper;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;
import org.slf4j.Logger;

public abstract class DisplayPersistentProjectileEntity extends PersistentProjectileEntity {
    static final Logger LOGGER = LogUtils.getLogger();
    public static final int field_42384 = -1;
    private static final TrackedData<Integer> START_INTERPOLATION = DataTracker.registerData(DisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> INTERPOLATION_DURATION = DataTracker.registerData(DisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Integer> TELEPORT_DURATION = DataTracker.registerData(DisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Vector3f> TRANSLATION = DataTracker.registerData(DisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.VECTOR3F);
    private static final TrackedData<Vector3f> SCALE = DataTracker.registerData(DisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.VECTOR3F);
    private static final TrackedData<Quaternionf> LEFT_ROTATION = DataTracker.registerData(DisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.QUATERNIONF);
    private static final TrackedData<Quaternionf> RIGHT_ROTATION = DataTracker.registerData(DisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.QUATERNIONF);
    private static final TrackedData<Byte> BILLBOARD = DataTracker.registerData(DisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.BYTE);
    private static final TrackedData<Integer> BRIGHTNESS = DataTracker.registerData(DisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.INTEGER);
    private static final TrackedData<Float> VIEW_RANGE = DataTracker.registerData(DisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> SHADOW_RADIUS = DataTracker.registerData(DisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> SHADOW_STRENGTH = DataTracker.registerData(DisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> WIDTH = DataTracker.registerData(DisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Float> HEIGHT = DataTracker.registerData(DisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.FLOAT);
    private static final TrackedData<Integer> GLOW_COLOR_OVERRIDE = DataTracker.registerData(DisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.INTEGER);
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
    private DisplayPersistentProjectileEntity.RenderState renderState;
    @Nullable
    private DisplayPersistentProjectileEntity.InterpolationTarget interpolationTarget;

    public DisplayPersistentProjectileEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
        super(entityType, world);
        this.noClip = true;
        this.ignoreCameraFrustum = true;
        this.visibilityBoundingBox = this.getBoundingBox();
    }

    protected DisplayPersistentProjectileEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
        super(type, x, y, z, world, stack, weapon);
        this.noClip = true;
        this.ignoreCameraFrustum = true;
        this.visibilityBoundingBox = this.getBoundingBox();
    }

    protected DisplayPersistentProjectileEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
        super(type, owner, world, stack, shotFrom);
        this.noClip = true;
        this.ignoreCameraFrustum = true;
        this.visibilityBoundingBox = this.getBoundingBox();
    }


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
        super.initDataTracker(builder);
        builder.add(TELEPORT_DURATION, 0);
        builder.add(START_INTERPOLATION, 0);
        builder.add(INTERPOLATION_DURATION, 0);
        builder.add(TRANSLATION, new Vector3f());
        builder.add(SCALE, new Vector3f(1.0F, 1.0F, 1.0F));
        builder.add(RIGHT_ROTATION, new Quaternionf());
        builder.add(LEFT_ROTATION, new Quaternionf());
        builder.add(BILLBOARD, DisplayPersistentProjectileEntity.BillboardMode.FIXED.getIndex());
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
            DisplayPersistentProjectileEntity.BillboardMode.CODEC
                    .decode(NbtOps.INSTANCE, nbt.get("billboard"))
                    .resultOrPartial(Util.addPrefix("Display entity", LOGGER::error))
                    .ifPresent(pair -> this.setBillboardMode((DisplayPersistentProjectileEntity.BillboardMode)pair.getFirst()));
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

    private void setTransformation(AffineTransformation transformation) {
        this.dataTracker.set(TRANSLATION, transformation.getTranslation());
        this.dataTracker.set(LEFT_ROTATION, transformation.getLeftRotation());
        this.dataTracker.set(SCALE, transformation.getScale());
        this.dataTracker.set(RIGHT_ROTATION, transformation.getRightRotation());
    }

    @Override
    public void writeCustomDataToNbt(NbtCompound nbt) {
        super.writeCustomDataToNbt(nbt);
        AffineTransformation.ANY_CODEC
                .encodeStart(NbtOps.INSTANCE, getTransformation(this.dataTracker))
                .ifSuccess(transformations -> nbt.put("transformation", transformations));
        DisplayPersistentProjectileEntity.BillboardMode.CODEC.encodeStart(NbtOps.INSTANCE, this.getBillboardMode()).ifSuccess(billboard -> nbt.put("billboard", billboard));
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

    @Override
    public void updateTrackedPositionAndAngles(double x, double y, double z, float yaw, float pitch, int interpolationSteps) {
        int i = this.getTeleportDuration();
        this.interpolationTarget = new DisplayPersistentProjectileEntity.InterpolationTarget(i, x, y, z, (double)yaw, (double)pitch);
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
    public DisplayPersistentProjectileEntity.RenderState getRenderState() {
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

    protected void setTeleportDuration(int teleportDuration) {
        this.dataTracker.set(TELEPORT_DURATION, teleportDuration);
    }

    private int getTeleportDuration() {
        return this.dataTracker.get(TELEPORT_DURATION);
    }

    private void setBillboardMode(DisplayPersistentProjectileEntity.BillboardMode billboardMode) {
        this.dataTracker.set(BILLBOARD, billboardMode.getIndex());
    }

    private DisplayPersistentProjectileEntity.BillboardMode getBillboardMode() {
        return (DisplayPersistentProjectileEntity.BillboardMode)DisplayPersistentProjectileEntity.BillboardMode.FROM_INDEX.apply(this.dataTracker.get(BILLBOARD));
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

    private DisplayPersistentProjectileEntity.RenderState copyRenderState() {
        return new DisplayPersistentProjectileEntity.RenderState(
                DisplayPersistentProjectileEntity.AbstractInterpolator.constant(getTransformation(this.dataTracker)),
                this.getBillboardMode(),
                this.getBrightness(),
                DisplayPersistentProjectileEntity.FloatLerper.constant(this.getShadowRadius()),
                DisplayPersistentProjectileEntity.FloatLerper.constant(this.getShadowStrength()),
                this.getGlowColorOverride()
        );
    }

    private DisplayPersistentProjectileEntity.RenderState getLerpedRenderState(DisplayPersistentProjectileEntity.RenderState state, float lerpProgress) {
        AffineTransformation affineTransformation = state.transformation.interpolate(lerpProgress);
        float f = state.shadowRadius.lerp(lerpProgress);
        float g = state.shadowStrength.lerp(lerpProgress);
        return new DisplayPersistentProjectileEntity.RenderState(
                new DisplayPersistentProjectileEntity.AffineTransformationInterpolator(affineTransformation, getTransformation(this.dataTracker)),
                this.getBillboardMode(),
                this.getBrightness(),
                new DisplayPersistentProjectileEntity.FloatLerperImpl(f, this.getShadowRadius()),
                new DisplayPersistentProjectileEntity.FloatLerperImpl(g, this.getShadowStrength()),
                this.getGlowColorOverride()
        );
    }

    @FunctionalInterface
    public interface AbstractInterpolator<T> {
        static <T> DisplayPersistentProjectileEntity.AbstractInterpolator<T> constant(T value) {
            return delta -> value;
        }

        T interpolate(float delta);
    }

    static record AffineTransformationInterpolator(AffineTransformation previous, AffineTransformation current)
            implements DisplayPersistentProjectileEntity.AbstractInterpolator<AffineTransformation> {
        public AffineTransformation interpolate(float f) {
            return (double)f >= 1.0 ? this.current : this.previous.interpolate(this.current, f);
        }
    }

    static record ArgbLerper(int previous, int current) implements DisplayPersistentProjectileEntity.IntLerper {
        @Override
        public int lerp(float delta) {
            return ColorHelper.Argb.lerp(delta, this.previous, this.current);
        }
    }

    public static enum BillboardMode implements StringIdentifiable {
        FIXED((byte)0, "fixed"),
        VERTICAL((byte)1, "vertical"),
        HORIZONTAL((byte)2, "horizontal"),
        CENTER((byte)3, "center");

        public static final Codec<DisplayPersistentProjectileEntity.BillboardMode> CODEC = StringIdentifiable.createCodec(DisplayPersistentProjectileEntity.BillboardMode::values);
        public static final IntFunction<DisplayPersistentProjectileEntity.BillboardMode> FROM_INDEX = ValueLists.createIdToValueFunction(
                DisplayPersistentProjectileEntity.BillboardMode::getIndex, values(), ValueLists.OutOfBoundsHandling.ZERO
        );
        private final byte index;
        private final String name;

        private BillboardMode(final byte index, final String name) {
            this.name = name;
            this.index = index;
        }

        @Override
        public String asString() {
            return this.name;
        }

        byte getIndex() {
            return this.index;
        }
    }

    public abstract static class BlockDisplayPersistentProjectileEntity extends DisplayPersistentProjectileEntity {
        public static final String BLOCK_STATE_NBT_KEY = "block_state";
        private static final TrackedData<BlockState> BLOCK_STATE = DataTracker.registerData(
                DisplayPersistentProjectileEntity.BlockDisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.BLOCK_STATE
        );
        @Nullable
        private DisplayPersistentProjectileEntity.BlockDisplayPersistentProjectileEntity.Data data;

        public BlockDisplayPersistentProjectileEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
            super(entityType, world);
        }

        protected BlockDisplayPersistentProjectileEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
            super(type, x, y, z, world, stack, weapon);
        }

        protected BlockDisplayPersistentProjectileEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
            super(type, owner, world, stack, shotFrom);
        }


        @Override
        protected void initDataTracker(DataTracker.Builder builder) {
            super.initDataTracker(builder);
            builder.add(BLOCK_STATE, Blocks.AIR.getDefaultState());
        }

        @Override
        public void onTrackedDataSet(TrackedData<?> data) {
            super.onTrackedDataSet(data);
            if (data.equals(BLOCK_STATE)) {
                this.renderingDataSet = true;
            }
        }

        private BlockState getBlockState() {
            return this.dataTracker.get(BLOCK_STATE);
        }

        private void setBlockState(BlockState state) {
            this.dataTracker.set(BLOCK_STATE, state);
        }

        @Override
        public void readCustomDataFromNbt(NbtCompound nbt) {
            super.readCustomDataFromNbt(nbt);
            this.setBlockState(NbtHelper.toBlockState(this.getWorld().createCommandRegistryWrapper(RegistryKeys.BLOCK), nbt.getCompound("block_state")));
        }

        @Override
        public void writeCustomDataToNbt(NbtCompound nbt) {
            super.writeCustomDataToNbt(nbt);
            nbt.put("block_state", NbtHelper.fromBlockState(this.getBlockState()));
        }

        @Nullable
        public DisplayPersistentProjectileEntity.BlockDisplayPersistentProjectileEntity.Data getData() {
            return this.data;
        }

        @Override
        protected void refreshData(boolean shouldLerp, float lerpProgress) {
            this.data = new DisplayPersistentProjectileEntity.BlockDisplayPersistentProjectileEntity.Data(this.getBlockState());
        }

        public static record Data(BlockState blockState) {
        }
    }

    @FunctionalInterface
    public interface FloatLerper {
        static DisplayPersistentProjectileEntity.FloatLerper constant(float value) {
            return delta -> value;
        }

        float lerp(float delta);
    }

    static record FloatLerperImpl(float previous, float current) implements DisplayPersistentProjectileEntity.FloatLerper {
        @Override
        public float lerp(float delta) {
            return MathHelper.lerp(delta, this.previous, this.current);
        }
    }

    @FunctionalInterface
    public interface IntLerper {
        static DisplayPersistentProjectileEntity.IntLerper constant(int value) {
            return delta -> value;
        }

        int lerp(float delta);
    }

    static record IntLerperImpl(int previous, int current) implements DisplayPersistentProjectileEntity.IntLerper {
        @Override
        public int lerp(float delta) {
            return MathHelper.lerp(delta, this.previous, this.current);
        }
    }

    static class InterpolationTarget {
        int step;
        final double x;
        final double y;
        final double z;
        final double yaw;
        final double pitch;

        InterpolationTarget(int step, double x, double y, double z, double yaw, double pitch) {
            this.step = step;
            this.x = x;
            this.y = y;
            this.z = z;
            this.yaw = yaw;
            this.pitch = pitch;
        }

        void apply(Entity entity) {
            entity.setPosition(this.x, this.y, this.z);
            ((EntityAccessor) entity).invokeSetRotation((float)this.yaw, (float)this.pitch);
        }

        void applyInterpolated(Entity entity) {
            ((EntityAccessor) entity).invokeLerpPosAndRotation(this.step, this.x, this.y, this.z, this.yaw, this.pitch);
        }
    }

    public abstract static class ItemDisplayPersistentProjectileEntity extends DisplayPersistentProjectileEntity {
        private static final String ITEM_NBT_KEY = "item";
        private static final String ITEM_DISPLAY_NBT_KEY = "item_display";
        private static final TrackedData<ItemStack> ITEM = DataTracker.registerData(DisplayPersistentProjectileEntity.ItemDisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.ITEM_STACK);
        private static final TrackedData<Byte> ITEM_DISPLAY = DataTracker.registerData(DisplayPersistentProjectileEntity.ItemDisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.BYTE);
        private final StackReference stackReference = StackReference.of(this::getItemStack, this::setItemStack);
        @Nullable
        private DisplayPersistentProjectileEntity.ItemDisplayPersistentProjectileEntity.Data data;

        public ItemDisplayPersistentProjectileEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
            super(entityType, world);
        }

        protected ItemDisplayPersistentProjectileEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
            super(type, x, y, z, world, stack, weapon);
        }

        protected ItemDisplayPersistentProjectileEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
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
            if (nbt.contains("item")) {
                this.setItemStack((ItemStack)ItemStack.fromNbt(this.getRegistryManager(), nbt.getCompound("item")).orElse(ItemStack.EMPTY));
            } else {
                this.setItemStack(ItemStack.EMPTY);
            }

            if (nbt.contains("item_display", NbtElement.STRING_TYPE)) {
                ModelTransformationMode.CODEC
                        .decode(NbtOps.INSTANCE, nbt.get("item_display"))
                        .resultOrPartial(Util.addPrefix("Display entity", DisplayPersistentProjectileEntity.LOGGER::error))
                        .ifPresent(mode -> this.setTransformationMode((ModelTransformationMode)mode.getFirst()));
            }
        }

        @Override
        public void writeCustomDataToNbt(NbtCompound nbt) {
            super.writeCustomDataToNbt(nbt);
            if (!this.getItemStack().isEmpty()) {
                nbt.put("item", this.getItemStack().encode(this.getRegistryManager()));
            }

            ModelTransformationMode.CODEC.encodeStart(NbtOps.INSTANCE, this.getTransformationMode()).ifSuccess(nbtx -> nbt.put("item_display", nbtx));
        }

        @Override
        public StackReference getStackReference(int mappedIndex) {
            return mappedIndex == 0 ? this.stackReference : StackReference.EMPTY;
        }

        @Nullable
        public DisplayPersistentProjectileEntity.ItemDisplayPersistentProjectileEntity.Data getData() {
            return this.data;
        }

        @Override
        protected void refreshData(boolean shouldLerp, float lerpProgress) {
            ItemStack itemStack = this.getItemStack();
            itemStack.setHolder(this);
            this.data = new DisplayPersistentProjectileEntity.ItemDisplayPersistentProjectileEntity.Data(itemStack, this.getTransformationMode());
        }

        public static record Data(ItemStack itemStack, ModelTransformationMode itemTransform) {
        }
    }

    public static record RenderState(
            DisplayPersistentProjectileEntity.AbstractInterpolator<AffineTransformation> transformation,
            DisplayPersistentProjectileEntity.BillboardMode billboardConstraints,
            int brightnessOverride,
            DisplayPersistentProjectileEntity.FloatLerper shadowRadius,
            DisplayPersistentProjectileEntity.FloatLerper shadowStrength,
            int glowColorOverride
    ) {
    }

    public abstract static class TextDisplayPersistentProjectileEntity extends DisplayPersistentProjectileEntity {
        public static final String TEXT_NBT_KEY = "text";
        private static final String LINE_WIDTH_NBT_KEY = "line_width";
        private static final String TEXT_OPACITY_NBT_KEY = "text_opacity";
        private static final String BACKGROUND_NBT_KEY = "background";
        private static final String SHADOW_NBT_KEY = "shadow";
        private static final String SEE_THROUGH_NBT_KEY = "see_through";
        private static final String DEFAULT_BACKGROUND_NBT_KEY = "default_background";
        private static final String ALIGNMENT_NBT_KEY = "alignment";
        public static final byte SHADOW_FLAG = 1;
        public static final byte SEE_THROUGH_FLAG = 2;
        public static final byte DEFAULT_BACKGROUND_FLAG = 4;
        public static final byte LEFT_ALIGNMENT_FLAG = 8;
        public static final byte RIGHT_ALIGNMENT_FLAG = 16;
        private static final byte INITIAL_TEXT_OPACITY = -1;
        public static final int INITIAL_BACKGROUND = 1073741824;
        private static final TrackedData<Text> TEXT = DataTracker.registerData(DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.TEXT_COMPONENT);
        private static final TrackedData<Integer> LINE_WIDTH = DataTracker.registerData(DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.INTEGER);
        private static final TrackedData<Integer> BACKGROUND = DataTracker.registerData(DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.INTEGER);
        private static final TrackedData<Byte> TEXT_OPACITY = DataTracker.registerData(DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.BYTE);
        private static final TrackedData<Byte> TEXT_DISPLAY_FLAGS = DataTracker.registerData(DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.class, TrackedDataHandlerRegistry.BYTE);
        private static final IntSet TEXT_RENDERING_DATA_IDS = IntSet.of(TEXT.id(), LINE_WIDTH.id(), BACKGROUND.id(), TEXT_OPACITY.id(), TEXT_DISPLAY_FLAGS.id());
        @Nullable
        private DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextLines textLines;
        @Nullable
        private DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.Data data;

        public TextDisplayPersistentProjectileEntity(EntityType<? extends PersistentProjectileEntity> entityType, World world) {
            super(entityType, world);
        }

        protected TextDisplayPersistentProjectileEntity(EntityType<? extends PersistentProjectileEntity> type, double x, double y, double z, World world, ItemStack stack, @Nullable ItemStack weapon) {
            super(type, x, y, z, world, stack, weapon);
        }

        protected TextDisplayPersistentProjectileEntity(EntityType<? extends PersistentProjectileEntity> type, LivingEntity owner, World world, ItemStack stack, @Nullable ItemStack shotFrom) {
            super(type, owner, world, stack, shotFrom);
        }


        @Override
        protected void initDataTracker(DataTracker.Builder builder) {
            super.initDataTracker(builder);
            builder.add(TEXT, Text.empty());
            builder.add(LINE_WIDTH, 200);
            builder.add(BACKGROUND, 1073741824);
            builder.add(TEXT_OPACITY, (byte)-1);
            builder.add(TEXT_DISPLAY_FLAGS, (byte)0);
        }

        @Override
        public void onTrackedDataSet(TrackedData<?> data) {
            super.onTrackedDataSet(data);
            if (TEXT_RENDERING_DATA_IDS.contains(data.id())) {
                this.renderingDataSet = true;
            }
        }

        private Text getText() {
            return this.dataTracker.get(TEXT);
        }

        private void setText(Text text) {
            this.dataTracker.set(TEXT, text);
        }

        private int getLineWidth() {
            return this.dataTracker.get(LINE_WIDTH);
        }

        private void setLineWidth(int lineWidth) {
            this.dataTracker.set(LINE_WIDTH, lineWidth);
        }

        private byte getTextOpacity() {
            return this.dataTracker.get(TEXT_OPACITY);
        }

        private void setTextOpacity(byte textOpacity) {
            this.dataTracker.set(TEXT_OPACITY, textOpacity);
        }

        private int getBackground() {
            return this.dataTracker.get(BACKGROUND);
        }

        private void setBackground(int background) {
            this.dataTracker.set(BACKGROUND, background);
        }

        private byte getDisplayFlags() {
            return this.dataTracker.get(TEXT_DISPLAY_FLAGS);
        }

        private void setDisplayFlags(byte flags) {
            this.dataTracker.set(TEXT_DISPLAY_FLAGS, flags);
        }

        private static byte readFlag(byte flags, NbtCompound nbt, String nbtKey, byte flag) {
            return nbt.getBoolean(nbtKey) ? (byte)(flags | flag) : flags;
        }

        @Override
        public void readCustomDataFromNbt(NbtCompound nbt) {
            super.readCustomDataFromNbt(nbt);
            if (nbt.contains("line_width", NbtElement.NUMBER_TYPE)) {
                this.setLineWidth(nbt.getInt("line_width"));
            }

            if (nbt.contains("text_opacity", NbtElement.NUMBER_TYPE)) {
                this.setTextOpacity(nbt.getByte("text_opacity"));
            }

            if (nbt.contains("background", NbtElement.NUMBER_TYPE)) {
                this.setBackground(nbt.getInt("background"));
            }

            byte b = readFlag((byte)0, nbt, "shadow", SHADOW_FLAG);
            b = readFlag(b, nbt, "see_through", SEE_THROUGH_FLAG);
            b = readFlag(b, nbt, "default_background", DEFAULT_BACKGROUND_FLAG);
            Optional<DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextAlignment> optional = DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextAlignment.CODEC
                    .decode(NbtOps.INSTANCE, nbt.get("alignment"))
                    .resultOrPartial(Util.addPrefix("Display entity", DisplayPersistentProjectileEntity.LOGGER::error))
                    .map(Pair::getFirst);
            if (optional.isPresent()) {
                b = switch ((DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextAlignment)optional.get()) {
                    case CENTER -> b;
                    case LEFT -> (byte)(b | 8);
                    case RIGHT -> (byte)(b | 16);
                };
            }

            this.setDisplayFlags(b);
            if (nbt.contains("text", NbtElement.STRING_TYPE)) {
                String string = nbt.getString("text");

                try {
                    Text text = Text.Serialization.fromJson(string, this.getRegistryManager());
                    if (text != null) {
                        ServerCommandSource serverCommandSource = this.getCommandSource().withLevel(2);
                        Text text2 = Texts.parse(serverCommandSource, text, this, 0);
                        this.setText(text2);
                    } else {
                        this.setText(Text.empty());
                    }
                } catch (Exception var8) {
                    DisplayPersistentProjectileEntity.LOGGER.warn("Failed to parse display entity text {}", string, var8);
                }
            }
        }

        private static void writeFlag(byte flags, NbtCompound nbt, String nbtKey, byte flag) {
            nbt.putBoolean(nbtKey, (flags & flag) != 0);
        }

        @Override
        public void writeCustomDataToNbt(NbtCompound nbt) {
            super.writeCustomDataToNbt(nbt);
            nbt.putString("text", Text.Serialization.toJsonString(this.getText(), this.getRegistryManager()));
            nbt.putInt("line_width", this.getLineWidth());
            nbt.putInt("background", this.getBackground());
            nbt.putByte("text_opacity", this.getTextOpacity());
            byte b = this.getDisplayFlags();
            writeFlag(b, nbt, "shadow", SHADOW_FLAG);
            writeFlag(b, nbt, "see_through", SEE_THROUGH_FLAG);
            writeFlag(b, nbt, "default_background", DEFAULT_BACKGROUND_FLAG);
            DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextAlignment.CODEC.encodeStart(NbtOps.INSTANCE, getAlignment(b)).ifSuccess(nbtElement -> nbt.put("alignment", nbtElement));
        }

        @Override
        protected void refreshData(boolean shouldLerp, float lerpProgress) {
            if (shouldLerp && this.data != null) {
                this.data = this.getLerpedRenderState(this.data, lerpProgress);
            } else {
                this.data = this.copyData();
            }

            this.textLines = null;
        }

        @Nullable
        public DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.Data getData() {
            return this.data;
        }

        private DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.Data copyData() {
            return new DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.Data(
                    this.getText(),
                    this.getLineWidth(),
                    DisplayPersistentProjectileEntity.IntLerper.constant(this.getTextOpacity()),
                    DisplayPersistentProjectileEntity.IntLerper.constant(this.getBackground()),
                    this.getDisplayFlags()
            );
        }

        private DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.Data getLerpedRenderState(DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.Data data, float lerpProgress) {
            int i = data.backgroundColor.lerp(lerpProgress);
            int j = data.textOpacity.lerp(lerpProgress);
            return new DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.Data(
                    this.getText(),
                    this.getLineWidth(),
                    new DisplayPersistentProjectileEntity.IntLerperImpl(j, this.getTextOpacity()),
                    new DisplayPersistentProjectileEntity.ArgbLerper(i, this.getBackground()),
                    this.getDisplayFlags()
            );
        }

        public DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextLines splitLines(DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.LineSplitter splitter) {
            if (this.textLines == null) {
                if (this.data != null) {
                    this.textLines = splitter.split(this.data.text(), this.data.lineWidth());
                } else {
                    this.textLines = new DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextLines(List.of(), 0);
                }
            }

            return this.textLines;
        }

        public static DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextAlignment getAlignment(byte flags) {
            if ((flags & LEFT_ALIGNMENT_FLAG) != 0) {
                return DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextAlignment.LEFT;
            } else {
                return (flags & RIGHT_ALIGNMENT_FLAG) != 0 ? DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextAlignment.RIGHT : DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextAlignment.CENTER;
            }
        }

        public static record Data(Text text, int lineWidth, DisplayPersistentProjectileEntity.IntLerper textOpacity, DisplayPersistentProjectileEntity.IntLerper backgroundColor, byte flags) {
        }

        @FunctionalInterface
        public interface LineSplitter {
            DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextLines split(Text text, int lineWidth);
        }

        public static enum TextAlignment implements StringIdentifiable {
            CENTER("center"),
            LEFT("left"),
            RIGHT("right");

            public static final Codec<DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextAlignment> CODEC = StringIdentifiable.createCodec(
                    DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextAlignment::values
            );
            private final String name;

            private TextAlignment(final String name) {
                this.name = name;
            }

            @Override
            public String asString() {
                return this.name;
            }
        }

        public static record TextLine(OrderedText contents, int width) {
        }

        public static record TextLines(List<DisplayPersistentProjectileEntity.TextDisplayPersistentProjectileEntity.TextLine> lines, int width) {
        }
    }
}
