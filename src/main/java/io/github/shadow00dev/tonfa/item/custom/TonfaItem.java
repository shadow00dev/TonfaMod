package io.github.shadow00dev.tonfa.item.custom;

import io.github.shadow00dev.tonfa.component.ModDataComponents;
import io.github.shadow00dev.tonfa.item.client.renderer.TonfaRenderer;
import net.minecraft.client.Minecraft;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.item.enchantment.Enchantment;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import org.jspecify.annotations.NonNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animation.AnimationController;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.animation.object.PlayState;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import static net.minecraft.core.component.DataComponents.BLOCKS_ATTACKS;

public class TonfaItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation FLIP_ANIM = RawAnimation.begin().thenPlay("tonfa.flipped");
    private static final RawAnimation UNFLIP_ANIM = RawAnimation.begin().thenPlay("tonfa.unflipped");

    private static final Set<ResourceKey<Enchantment>> ALLOWED_ENCHANTMENTS = Set.of(
            Enchantments.SHARPNESS,
            Enchantments.KNOCKBACK,
            Enchantments.LOOTING,
            Enchantments.SMITE,
            Enchantments.BANE_OF_ARTHROPODS
    );

    public String resource = "wood";
    public TonfaItem(Properties properties, ToolMaterial material, String resourceName) {
        super(applyBaseProperties(properties, material, 1));
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        resource = resourceName;
    }
    public TonfaItem(Properties properties, ToolMaterial material, String resourceName, int attackDamage) {
        super(applyBaseProperties(properties, material, attackDamage));
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        resource = resourceName;
    }

    public static Properties applyBaseProperties(Properties properties, ToolMaterial material, int attackDamage) {
        return properties.sword(material, attackDamage, -1f)
                .component(BLOCKS_ATTACKS,
                        new BlocksAttacks(
                                0.25f,
                                1f,
                                List.of(new BlocksAttacks.DamageReduction(90f, Optional.empty(), 0f, 1f)),
                                new BlocksAttacks.ItemDamageFunction(3f, 1f, 1f),
                                Optional.of(DamageTypeTags.BYPASSES_SHIELD),
                                Optional.of(SoundEvents.SHIELD_BLOCK),
                                Optional.of(SoundEvents.SHIELD_BREAK)
                        )
                ).equippableUnswappable(EquipmentSlot.OFFHAND).component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK).component(ModDataComponents.EXTENDED, false).component(ModDataComponents.LASTSWINGTICK, 0L);
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private TonfaRenderer renderer;
            @Override
            public GeoItemRenderer<TonfaItem> getGeoItemRenderer() {
                if (this.renderer == null) {
                    this.renderer = new TonfaRenderer(resource);
                }
                return this.renderer;
            }
        });
    }

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("flipped_controller", 0, animTest -> PlayState.STOP).triggerableAnim("flip_anim", FLIP_ANIM))
                .add(new AnimationController<>("unflipped_controller", 0, animTest -> PlayState.STOP).triggerableAnim("unflip_anim", UNFLIP_ANIM));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, @NonNull InteractionHand hand) {
        long currentTick = entity.level().getGameTime();
        long savedTick = stack.getComponents().getOrDefault(ModDataComponents.LASTSWINGTICK, 0L);
        boolean extended = Boolean.TRUE.equals(stack.getComponents().get(ModDataComponents.EXTENDED));

        if (!Minecraft.getInstance().options.keyUse.isDown() && (currentTick - savedTick > 10)) {
            stack.set(ModDataComponents.EXTENDED, !extended);
            stack.set(ModDataComponents.LASTSWINGTICK, currentTick);
        }

        if (extended != Boolean.TRUE.equals(stack.getComponents().get(ModDataComponents.EXTENDED)) && entity.level() instanceof ServerLevel serverLevel) {
            long item = GeoItem.getOrAssignId(entity.getItemInHand(hand), serverLevel);
            if (!extended) {
                stopTriggeredAnim(entity, item, "unflipped_controller", "unflip_anim");
                triggerAnim(entity, item, "flipped_controller", "flip_anim");
            } else {
                System.out.println("working");
                stopTriggeredAnim(entity, GeoItem.getOrAssignId(entity.getItemInHand(hand), serverLevel), "flipped_controller", "flip_anim");
                triggerAnim(entity, GeoItem.getOrAssignId(entity.getItemInHand(hand), serverLevel), "unflipped_controller", "unflip_anim");
            }
        }
        return super.onEntitySwing(stack, entity, hand);
    }

    @Override
    public @NonNull InteractionResult use(@NonNull Level level, @NonNull Player player, @NonNull InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            player.stopUsingItem();
            return InteractionResult.FAIL;
        }
        return super.use(level, player, hand);
    }

    @Override
    public boolean supportsEnchantment(@NonNull ItemStack stack, @NonNull Holder<Enchantment> enchantment) {
        return super.supportsEnchantment(stack, enchantment) || ALLOWED_ENCHANTMENTS.contains(enchantment.getKey());
    }
}
