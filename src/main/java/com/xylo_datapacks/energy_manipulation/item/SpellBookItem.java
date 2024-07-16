package com.xylo_datapacks.energy_manipulation.item;

import com.google.common.collect.Lists;
import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import java.util.*;
import java.util.function.Predicate;

import com.xylo_datapacks.energy_manipulation.EnergyManipulation;
import com.xylo_datapacks.energy_manipulation.component.CatalystComponent;
import com.xylo_datapacks.energy_manipulation.component.ModDataComponentTypes;
import com.xylo_datapacks.energy_manipulation.config.SpellBookInfo;
import com.xylo_datapacks.energy_manipulation.datagen.ModItemTagsProvider;
import com.xylo_datapacks.energy_manipulation.entity.custom.ProjectileShapeEntity;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.base_class.GenericNode;
import com.xylo_datapacks.energy_manipulation.item.spell_book.node.spell.SpellNode;
import com.xylo_datapacks.energy_manipulation.network.ModPackets;
import com.xylo_datapacks.energy_manipulation.screen.spell_book.SpellBookScreenHandler;
import net.fabricmc.fabric.api.screenhandler.v1.ExtendedScreenHandlerFactory;
import net.minecraft.component.DataComponentTypes;
import net.minecraft.component.type.ChargedProjectilesComponent;
import net.minecraft.component.type.FoodComponent;
import net.minecraft.component.type.NbtComponent;
import net.minecraft.enchantment.EnchantmentHelper;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.player.PlayerInventory;
import net.minecraft.entity.projectile.FireworkRocketEntity;
import net.minecraft.entity.projectile.PersistentProjectileEntity;
import net.minecraft.entity.projectile.ProjectileEntity;
import net.minecraft.inventory.SimpleInventory;
import net.minecraft.item.*;
import net.minecraft.item.tooltip.TooltipType;
import net.minecraft.nbt.NbtCompound;
import net.minecraft.nbt.NbtElement;
import net.minecraft.nbt.NbtList;
import net.minecraft.network.RegistryByteBuf;
import net.minecraft.network.codec.PacketCodec;
import net.minecraft.network.packet.CustomPayload;
import net.minecraft.registry.RegistryWrapper;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.screen.ScreenHandler;
import net.minecraft.screen.ScreenTexts;
import net.minecraft.screen.slot.Slot;
import net.minecraft.server.network.ServerPlayerEntity;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.sound.SoundCategory;
import net.minecraft.sound.SoundEvent;
import net.minecraft.sound.SoundEvents;
import net.minecraft.stat.Stats;
import net.minecraft.text.Text;
import net.minecraft.util.*;
import net.minecraft.util.math.MathHelper;
import net.minecraft.util.math.Vec3d;
import net.minecraft.util.math.random.Random;
import net.minecraft.world.World;
import org.jetbrains.annotations.Nullable;
import org.joml.Quaternionf;
import org.joml.Vector3f;

public class SpellBookItem extends RangedWeaponItem {
    private static final float DEFAULT_PULL_TIME = 1.25F;
    public static final int RANGE = 8;
    private boolean charged = false;
    private boolean loaded = false;
    private boolean completed = false;
    private static final float CHARGE_PROGRESS = 0.2F;
    private static final float LOAD_PROGRESS = 0.5F;
    private static final float COMPLETE_PROGRESS = 0.75F;
    private static final SpellBookItem.LoadingSounds DEFAULT_LOADING_SOUNDS = new SpellBookItem.LoadingSounds(
            Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_START),
            Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_MIDDLE),
            Optional.of(SoundEvents.ITEM_CROSSBOW_LOADING_END)
    );
    public static final Predicate<ItemStack> CATALYST = stack -> stack.isIn(ModItemTagsProvider.CATALYSTS);
    public static final Predicate<ItemStack> ADVANCED_CATALYST = stack -> stack.isIn(ModItemTagsProvider.ADVANCED_CATALYSTS);
    public static final Predicate<ItemStack> CATALYST_HELD = CATALYST.or(ADVANCED_CATALYST);
    private static final String CHARGE_KEY = "Charge";

    public SpellBookItem(Item.Settings settings) {
        super(settings);
    }



    /*----------------------------------------------------------------------------------------------------------------*/
    /* region Casting Logic */

    public static SpellNode getSpellNode(LivingEntity user, ItemStack itemStack) {
        // get inventory items
        Map<Integer, ItemStack> spellBookContent = getBackpackContents(user.getRegistryManager(), itemStack);
        // check if there is slot zero
        if (spellBookContent.containsKey(0)) {
            // check if slot zero is spell book page
            if (spellBookContent.get(0).getItem() instanceof SpellBookPageItem) {
                // fetch spell node
                GenericNode node = SpellBookPageItem.getSpell(spellBookContent.get(0));
                if (node instanceof SpellNode spellNode) {
                    return spellNode;
                }
            }
        }
        return null;
    }
    

    /** custom version of CreateArrowEntity in RangedWeaponItem */
    protected ProjectileEntity createSpellEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        CatalystItem catalystItem = projectileStack.getItem() instanceof CatalystItem catalyst ? catalyst : (CatalystItem) ModItems.BASIC_CATALYST;
        PersistentProjectileEntity spell = catalystItem.createSpell(world, projectileStack, shooter, weaponStack);
        if (critical) {
            spell.setCritical(true);
        }

        return spell;
    }

    /* endregion */
    /*----------------------------------------------------------------------------------------------------------------*/
    

    /*----------------------------------------------------------------------------------------------------------------*/
    /* region Added Item Functionality */

    public static float getCharge(ItemStack stack) {
        return stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt().getFloat(CHARGE_KEY);
    }

    public static void setCharge(ItemStack stack, float charge) {
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
            currentNbt.putFloat(CHARGE_KEY, charge);
        }));
    }

    /** works like the one in player entity, but allows default even if not creative mode */
    public static ItemStack getProjectileType(LivingEntity user, ItemStack stack) {
        if (user instanceof PlayerEntity player) {
            if (!(stack.getItem() instanceof RangedWeaponItem)) {
                return ItemStack.EMPTY;
            } else {
                Predicate<ItemStack> predicate = ((RangedWeaponItem)stack.getItem()).getHeldProjectiles();
                ItemStack itemStack = RangedWeaponItem.getHeldProjectile(player, predicate);
                if (!itemStack.isEmpty()) {
                    return itemStack;
                } else {
                    predicate = ((RangedWeaponItem)stack.getItem()).getProjectiles();
        
                    for (int i = 0; i < player.getInventory().size(); i++) {
                        ItemStack itemStack2 = player.getInventory().getStack(i);
                        if (predicate.test(itemStack2)) {
                            return itemStack2;
                        }
                    }
        
                    return new ItemStack(ModItems.BASIC_CATALYST);
                }
            }
        }
        return ItemStack.EMPTY;
    }

    /* endregion */
    /*----------------------------------------------------------------------------------------------------------------*/


    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    /*----------------------------------------------------------------------------------------------------------------*/
    /* region Crossbow Like Functionality */

    /*----------------------------------------------------------------------------------------------------------------*/
    /* region Use */

    @Override
    public UseAction getUseAction(ItemStack stack) {
        return UseAction.CROSSBOW;
    }
    
    @Override
    public boolean isUsedOnRelease(ItemStack stack) {
        return stack.isOf(this);
    }
    
    @Override
    public TypedActionResult<ItemStack> use(World world, PlayerEntity user, Hand hand) {
        ItemStack itemStack = user.getStackInHand(hand);
        ChargedProjectilesComponent chargedProjectilesComponent = itemStack.get(DataComponentTypes.CHARGED_PROJECTILES);
        if (chargedProjectilesComponent != null && !chargedProjectilesComponent.isEmpty()) {
            this.shootAll(world, user, hand, itemStack, getSpeed(chargedProjectilesComponent), 1.0F, null);
            return TypedActionResult.consume(itemStack);
        } else if (!getProjectileType(user, itemStack).isEmpty()) {
            this.charged = false;
            this.loaded = false;
            this.completed = false;
            user.setCurrentHand(hand);
            return TypedActionResult.consume(itemStack);
        } else {
            return TypedActionResult.fail(itemStack);
        }
    }

    @Override
    public void usageTick(World world, LivingEntity user, ItemStack stack, int remainingUseTicks) {
        if (!world.isClient) {
            SpellBookItem.LoadingSounds loadingSounds = this.getLoadingSounds(stack);
            float f = (float)(stack.getMaxUseTime(user) - remainingUseTicks) / (float)getPullTime(stack, user);
            if (f < CHARGE_PROGRESS) {
                this.charged = false;
                this.loaded = false;
                this.completed = false;
            }

            if (f >= CHARGE_PROGRESS && !this.charged) {
                this.charged = true;
                loadingSounds.start().ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), (SoundEvent)sound.value(), SoundCategory.PLAYERS, LOAD_PROGRESS, 1.0F));
            }

            if (f >= LOAD_PROGRESS && !this.loaded) {
                this.loaded = true;
                loadingSounds.mid().ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), (SoundEvent)sound.value(), SoundCategory.PLAYERS, LOAD_PROGRESS, 1.0F));
            }

            if (f >= COMPLETE_PROGRESS && !this.completed) {
                this.completed = true;
                //loadingSounds.mid().ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), (SoundEvent)sound.value(), SoundCategory.PLAYERS, LOAD_PROGRESS, 1.0F));
            }
        }
    }

    @Override
    public void onStoppedUsing(ItemStack stack, World world, LivingEntity user, int remainingUseTicks) {
        int i = this.getMaxUseTime(stack, user) - remainingUseTicks;
        float f = getPullProgress(i, stack, user);
        if (!isCharged(stack) && loadProjectiles(user, stack)) {
            SpellBookItem.setCharge(stack, f);
            SpellBookItem.LoadingSounds loadingSounds = this.getLoadingSounds(stack);
            loadingSounds.end().ifPresent(sound -> world.playSound(null, user.getX(), user.getY(), user.getZ(), (SoundEvent)sound.value(), user.getSoundCategory(), 1.0F, 1.0F / (world.getRandom().nextFloat() * LOAD_PROGRESS + 1.0F) + CHARGE_PROGRESS));
        }
    }

    /* endregion */
    /*----------------------------------------------------------------------------------------------------------------*/

    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* region Projectile */
    
    @Override
    public Predicate<ItemStack> getHeldProjectiles() {
        return CATALYST_HELD;
    }

    @Override
    public Predicate<ItemStack> getProjectiles() {
        return CATALYST;
    }

    /** shadowing the one in CrossbowItem because we want to use out custom load.
     * </p> the method is unchanged */
    private static boolean loadProjectiles(LivingEntity shooter, ItemStack spellBookStack) {
        List<ItemStack> list = load(spellBookStack, getProjectileType(shooter, spellBookStack), shooter);
        if (!list.isEmpty()) {
            spellBookStack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.of(list));
            return true;
        } else {
            return false;
        }
    }
    
    /** shadowing the one in RangedWeaponItem because we want to use out custom getProjectile. 
     * </p> the method is unchanged at a logical level. just made it more readable */
    protected static List<ItemStack> load(ItemStack spellBookStack, ItemStack projectileStack, LivingEntity shooter) {
        if (projectileStack.isEmpty()) {
            return List.of();
        } else {
            int numberOfProjectilesDesired = shooter.getWorld() instanceof ServerWorld serverWorld ? EnchantmentHelper.getProjectileCount(serverWorld, spellBookStack, shooter, 1) : 1;
            List<ItemStack> list = new ArrayList(numberOfProjectilesDesired);
            ItemStack copiedProjectileStack = projectileStack.copy();

            for (int currentProjectileIndex = 0; currentProjectileIndex < numberOfProjectilesDesired; currentProjectileIndex++) {
                boolean isFirstProjectile = currentProjectileIndex == 0;
                ItemStack itemStack2 = getProjectile(spellBookStack, isFirstProjectile ? projectileStack : copiedProjectileStack, shooter, !isFirstProjectile);
                if (!itemStack2.isEmpty()) {
                    list.add(itemStack2);
                }
            }

            return list;
        }
    }

    /** shadowing the one in RangedWeaponItem 
     * </p> made more readable and changed stack removal to account for catalyst convert on consume */
    protected static ItemStack getProjectile(ItemStack spellBookStack, ItemStack projectileStack, LivingEntity shooter, boolean isMultishotProjectile) {
        /* numberOfAmmoToUse is different from zero only for the first projectile 
        * in this case im actually passing in the real projectile stack */
        int numberOfAmmoToUse = !isMultishotProjectile && !shooter.isInCreativeMode() && shooter.getWorld() instanceof ServerWorld serverWorld
                ? EnchantmentHelper.getAmmoUse(serverWorld, spellBookStack, projectileStack, 1)
                : 0;
        // not enough projectiles (first proj + !creative + server)
        if (numberOfAmmoToUse > projectileStack.getCount()) {
            return ItemStack.EMPTY;
        } 
        // no projectile required (multishot proj / creative / client)
        else if (numberOfAmmoToUse == 0) {
            ItemStack projectileStackToLoad = projectileStack.copyWithCount(1);
            projectileStackToLoad.set(DataComponentTypes.INTANGIBLE_PROJECTILE, Unit.INSTANCE);
            return projectileStackToLoad;
        } 
        // one or more projectiles (first proj + !creative + server)
        else {
            // get catalyst before possible empty stack
            CatalystComponent catalystComponent = projectileStack.get(ModDataComponentTypes.CATALYST);

            ItemStack projectileStackToLoad = projectileStack.split(numberOfAmmoToUse);
            if (projectileStack.isEmpty() && shooter instanceof PlayerEntity playerEntity) {
                playerEntity.getInventory().removeOne(projectileStack);
            }
            
            // changed system instead of just removing stack if empty, i try to convert the catalyst
            if (catalystComponent != null && shooter instanceof PlayerEntity playerEntity) {
                CatalystComponent.giveConversionItem(playerEntity, catalystComponent);
            }
            
            return projectileStackToLoad;
        }
    }

    public static boolean isCharged(ItemStack stack) {
        ChargedProjectilesComponent chargedProjectilesComponent = stack.getOrDefault(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
        return !chargedProjectilesComponent.isEmpty();
    }
    
    /** similar to crossbow version but calls createSpellEntity instead of super */
    @Override
    protected ProjectileEntity createArrowEntity(World world, LivingEntity shooter, ItemStack weaponStack, ItemStack projectileStack, boolean critical) {
        ProjectileEntity projectileEntity = createSpellEntity(world, shooter, weaponStack, projectileStack, critical);
        if (projectileEntity instanceof PersistentProjectileEntity persistentProjectileEntity) {
            persistentProjectileEntity.setSound(SoundEvents.ITEM_CROSSBOW_HIT);
        }

        return projectileEntity;
    }

    /* endregion */
    /*----------------------------------------------------------------------------------------------------------------*/
    
    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* region Shoot */
    
    public void shootAll(World world, LivingEntity shooter, Hand hand, ItemStack stack, float speed, float divergence, @Nullable LivingEntity target) {
        if (world instanceof ServerWorld serverWorld) {
            ChargedProjectilesComponent chargedProjectilesComponent = stack.set(DataComponentTypes.CHARGED_PROJECTILES, ChargedProjectilesComponent.DEFAULT);
            if (chargedProjectilesComponent != null && !chargedProjectilesComponent.isEmpty()) {
                this.shootAll(serverWorld, shooter, hand, stack, chargedProjectilesComponent.getProjectiles(), speed, divergence, getCharge(stack) >= COMPLETE_PROGRESS, target);
                if (shooter instanceof ServerPlayerEntity serverPlayerEntity) {
                    //Criteria.SHOT_CROSSBOW.trigger(serverPlayerEntity, stack);
                    serverPlayerEntity.incrementStat(Stats.USED.getOrCreateStat(stack.getItem()));
                }
            }
            SpellBookItem.setCharge(stack, 0.0F);
        }
    }

    @Override
    protected void shootAll(ServerWorld world, LivingEntity shooter, Hand hand, ItemStack stack, List<ItemStack> projectiles, float speed, float divergence, boolean critical, @Nullable LivingEntity target) {
        float f = EnchantmentHelper.getProjectileSpread(world, stack, shooter, 0.0F);
        float g = projectiles.size() == 1 ? 0.0F : 2.0F * f / (float)(projectiles.size() - 1);
        float h = (float)((projectiles.size() - 1) % 2) * g / 2.0F;
        float i = 1.0F;

        for (int j = 0; j < projectiles.size(); j++) {
            ItemStack itemStack = (ItemStack)projectiles.get(j);
            if (!itemStack.isEmpty()) {
                float k = h + i * (float)((j + 1) / 2) * g;
                i = -i;
                ProjectileEntity projectileEntity = this.createArrowEntity(world, shooter, stack, itemStack, critical);
                this.shoot(shooter, projectileEntity, j, speed, divergence, k, target);
                world.spawnEntity(projectileEntity);
                stack.damage(this.getWeaponStackDamage(itemStack), shooter, LivingEntity.getSlotForHand(hand));
                if (stack.isEmpty()) {
                    break;
                }
            }
        }
    }

    @Override
    protected void shoot(LivingEntity shooter, ProjectileEntity projectile, int index, float speed, float divergence, float yaw, @Nullable LivingEntity target) {
        Vector3f vector3f;
        if (target != null) {
            double d = target.getX() - shooter.getX();
            double e = target.getZ() - shooter.getZ();
            double f = Math.sqrt(d * d + e * e);
            double g = target.getBodyY(0.3333333333333333) - projectile.getY() + f * CHARGE_PROGRESS;
            vector3f = calcVelocity(shooter, new Vec3d(d, g, e), yaw);
        } else {
            Vec3d vec3d = shooter.getOppositeRotationVector(1.0F);
            Quaternionf quaternionf = new Quaternionf().setAngleAxis((double)(yaw * (float) (Math.PI / 180.0)), vec3d.x, vec3d.y, vec3d.z);
            Vec3d vec3d2 = shooter.getRotationVec(1.0F);
            vector3f = vec3d2.toVector3f().rotate(quaternionf);
        }

        projectile.setVelocity((double)vector3f.x(), (double)vector3f.y(), (double)vector3f.z(), speed, divergence);
        float h = getSoundPitch(shooter.getRandom(), index);
        shooter.getWorld().playSound(null, shooter.getX(), shooter.getY(), shooter.getZ(), SoundEvents.ITEM_CROSSBOW_SHOOT, shooter.getSoundCategory(), 1.0F, h);
    }

    private static Vector3f calcVelocity(LivingEntity shooter, Vec3d direction, float yaw) {
        Vector3f vector3f = direction.toVector3f().normalize();
        Vector3f vector3f2 = new Vector3f(vector3f).cross(new Vector3f(0.0F, 1.0F, 0.0F));
        if ((double)vector3f2.lengthSquared() <= 1.0E-7) {
            Vec3d vec3d = shooter.getOppositeRotationVector(1.0F);
            vector3f2 = new Vector3f(vector3f).cross(vec3d.toVector3f());
        }

        Vector3f vector3f3 = new Vector3f(vector3f).rotateAxis((float) (Math.PI / 2), vector3f2.x, vector3f2.y, vector3f2.z);
        return new Vector3f(vector3f).rotateAxis(yaw * (float) (Math.PI / 180.0), vector3f3.x, vector3f3.y, vector3f3.z);
    }

    private static float getSpeed(ChargedProjectilesComponent stack) {
        List<ItemStack> projectiles = stack.getProjectiles();
        float chargeSeconds = 0.0F;
        for (ItemStack projectile : projectiles) {
            CatalystComponent catalystComponent = projectile.get(ModDataComponentTypes.CATALYST);
            if (catalystComponent != null) {
                chargeSeconds += catalystComponent.chargeSeconds();
            }
        }
        return chargeSeconds / projectiles.size();
    }
    
    @Override
    public int getRange() {
        return RANGE;
    }

    /* endregion */
    /*----------------------------------------------------------------------------------------------------------------*/

    
    /*----------------------------------------------------------------------------------------------------------------*/
    /* region UseTime */
    
    @Override
    public int getMaxUseTime(ItemStack stack, LivingEntity user) {
        return 72000;
    }
    
    public static int getPullTime(ItemStack stack, LivingEntity user) {
        float f = EnchantmentHelper.getCrossbowChargeTime(stack, user, DEFAULT_PULL_TIME);
        return MathHelper.floor(f * 20.0F);
    }
    
    private static float getPullProgress(int useTicks, ItemStack stack, LivingEntity user) {
        float f = (float)useTicks / (float)getPullTime(stack, user);
        if (f > 1.0F) {
            f = 1.0F;
        }

        return f;
    }

    /* endregion */
    /*----------------------------------------------------------------------------------------------------------------*/



    @Override
    protected int getWeaponStackDamage(ItemStack projectile) {
        return ADVANCED_CATALYST.test(projectile) ? 3 : 1;
    }

    @Override
    public void appendTooltip(ItemStack stack, Item.TooltipContext context, List<Text> tooltip, TooltipType type) {
        ChargedProjectilesComponent chargedProjectilesComponent = stack.get(DataComponentTypes.CHARGED_PROJECTILES);
        if (chargedProjectilesComponent != null && !chargedProjectilesComponent.isEmpty()) {
            ItemStack itemStack = (ItemStack)chargedProjectilesComponent.getProjectiles().get(0);
            tooltip.add(EnergyManipulation.itemTranslation("spell_book.catalyst").append(ScreenTexts.SPACE).append(itemStack.toHoverableText()));
            if (type.isAdvanced() && ADVANCED_CATALYST.test(itemStack)) {
                List<Text> list = Lists.<Text>newArrayList();
                itemStack.getItem().appendTooltip(itemStack, context, list, type);
                if (!list.isEmpty()) {
                    for (int i = 0; i < list.size(); i++) {
                        list.set(i, Text.literal("  ").append((Text)list.get(i)).formatted(Formatting.GRAY));
                    }

                    tooltip.addAll(list);
                }
            }
        }
    }

    /*----------------------------------------------------------------------------------------------------------------*/
    /* region Sound */

    private static float getSoundPitch(Random random, int index) {
        return index == 0 ? 1.0F : getSoundPitch((index & 1) == 1, random);
    }

    private static float getSoundPitch(boolean flag, Random random) {
        float f = flag ? 0.63F : 0.43F;
        return 1.0F / (random.nextFloat() * LOAD_PROGRESS + 1.8F) + f;
    }
    
    SpellBookItem.LoadingSounds getLoadingSounds(ItemStack stack) {
        return DEFAULT_LOADING_SOUNDS; //(SpellBookItem.LoadingSounds)EnchantmentHelper.getEffect(stack, EnchantmentEffectComponentTypes.CROSSBOW_CHARGING_SOUNDS).orElse(DEFAULT_LOADING_SOUNDS);
    }
    
    public static record LoadingSounds(Optional<RegistryEntry<SoundEvent>> start, Optional<RegistryEntry<SoundEvent>> mid, Optional<RegistryEntry<SoundEvent>> end) {
        public static final Codec<SpellBookItem.LoadingSounds> CODEC = RecordCodecBuilder.create(
                instance -> instance.group(
                                SoundEvent.ENTRY_CODEC.optionalFieldOf("start").forGetter(SpellBookItem.LoadingSounds::start),
                                SoundEvent.ENTRY_CODEC.optionalFieldOf("mid").forGetter(SpellBookItem.LoadingSounds::mid),
                                SoundEvent.ENTRY_CODEC.optionalFieldOf("end").forGetter(SpellBookItem.LoadingSounds::end)
                        )
                        .apply(instance, SpellBookItem.LoadingSounds::new)
        );
    }

    /* endregion */
    /*----------------------------------------------------------------------------------------------------------------*/

    /* endregion */
    /*----------------------------------------------------------------------------------------------------------------*/
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    ////////////////////////////////////////////////////////////////////////////////////////////////////////////////////
    
    

    /*----------------------------------------------------------------------------------------------------------------*/
    /* region FabricItem Interface */

    @Override
    public boolean allowComponentsUpdateAnimation(PlayerEntity player, Hand hand, ItemStack oldStack, ItemStack newStack) {
        return SpellBookItem.getCharge(oldStack) == SpellBookItem.getCharge(new ItemStack(this));
    }

    /* endregion */
    /*----------------------------------------------------------------------------------------------------------------*/







    /*----------------------------------------------------------------------------------------------------------------*/
    /* region GUI */

    public static void openScreen(PlayerEntity player, ItemStack spellBookStack) {
        if (player.getWorld() != null && !player.getWorld().isClient()) {
            player.openHandledScreen(new ExtendedScreenHandlerFactory<SpellBookItem.SpellBookMenuData>() {
                @Override
                public SpellBookItem.SpellBookMenuData getScreenOpeningData(ServerPlayerEntity player) {
                    return new SpellBookItem.SpellBookMenuData(spellBookStack);
                }

                @Override
                public Text getDisplayName() {
                    return Text.translatable(spellBookStack.getItem().getTranslationKey());
                }

                @Override
                public @Nullable ScreenHandler createMenu(int syncId, PlayerInventory inv, PlayerEntity player) {
                    return new SpellBookScreenHandler(syncId, inv, spellBookStack);
                }
            });
        }
    }

    public record SpellBookMenuData(ItemStack spellBookStack) implements CustomPayload {
        public static final CustomPayload.Id<SpellBookItem.SpellBookMenuData> ID = new CustomPayload.Id<>(ModPackets.INIT_SPELL_BOOK_MENU_PACKET_ID);
        public static final PacketCodec<RegistryByteBuf, SpellBookItem.SpellBookMenuData> PACKET_CODEC = PacketCodec.tuple(ItemStack.PACKET_CODEC, SpellBookItem.SpellBookMenuData::spellBookStack, SpellBookItem.SpellBookMenuData::new);

        @Override
        public CustomPayload.Id<? extends CustomPayload> getId() {
            return ID;
        }
    }

    /* endregion */
    /*----------------------------------------------------------------------------------------------------------------*/


    /*----------------------------------------------------------------------------------------------------------------*/
    /* region Inventory */

    public SpellBookInfo getTier() {
        return new SpellBookInfo("spell book", 9, 1, false, "");
    }

    public static boolean isBackpackEmpty(ItemStack stack) {
        NbtCompound nbtCompound = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        NbtList inventoryNbt = nbtCompound.getList("Inventory", NbtElement.COMPOUND_TYPE);
        return inventoryNbt.isEmpty();
    }

    public static Map<Integer, ItemStack> getBackpackContents(RegistryWrapper.WrapperLookup registries, ItemStack stack) {
        Map<Integer, ItemStack> stacks = new LinkedHashMap<>();
        NbtCompound nbtCompound = stack.getOrDefault(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT).copyNbt();
        NbtList inventoryNbt = nbtCompound.getList("Inventory", NbtElement.COMPOUND_TYPE);

        for (NbtElement element : inventoryNbt) {
            NbtCompound stackTag = (NbtCompound) element;
            ItemStack.fromNbt(registries, stackTag).ifPresent(backpackStack -> stacks.put(stackTag.getInt("Slot"), backpackStack));
        }

        return stacks;
    }

    public static void setBackpackContent(ItemStack stack, SimpleInventory inventory, RegistryWrapper.WrapperLookup registries)  {
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
            currentNbt.put("Inventory", inventory.toNbtList(registries));
        }));
    }

    public static void wipeBackpack(ItemStack stack) {
        stack.apply(DataComponentTypes.CUSTOM_DATA, NbtComponent.DEFAULT, comp -> comp.apply(currentNbt -> {
            currentNbt.remove("Inventory");
        }));
    }

    /* endregion */
    /*----------------------------------------------------------------------------------------------------------------*/

}
