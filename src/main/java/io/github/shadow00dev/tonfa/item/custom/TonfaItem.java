package io.github.shadow00dev.tonfa.item.custom;

import io.github.shadow00dev.tonfa.component.ModDataComponents;
import io.github.shadow00dev.tonfa.item.client.renderer.TonfaRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemDisplayContext;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.Level;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoAnimatable;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animatable.processing.AnimationTest;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.constant.DataTickets;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import static net.minecraft.core.component.DataComponents.BLOCKS_ATTACKS;

public class TonfaItem extends Item implements GeoItem {
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);

    private static final RawAnimation FLIP_ANIM = RawAnimation.begin().thenPlay("tonfa.flipped");
    private static final RawAnimation UNFLIP_ANIM = RawAnimation.begin().thenPlay("tonfa.unflipped");
    private final AnimationController<GeoAnimatable> flipped_anim = new AnimationController<>("flipped_controller", 0, this::setPlayState).triggerableAnim("flip_anim", FLIP_ANIM).receiveTriggeredAnimations();
    private final AnimationController<GeoAnimatable> unflipped_anim = new AnimationController<>("unflipped_controller", 0, this::setPlayState).triggerableAnim("unflip_anim", UNFLIP_ANIM).receiveTriggeredAnimations();

    public String resource = "wood";
    public TonfaItem(Properties properties, ToolMaterial material, String resourceName) {
        super(properties.sword(material, 1, -1f)
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
                ).equippableUnswappable(EquipmentSlot.OFFHAND).component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK).component(ModDataComponents.EXTENDED, false));
        SingletonGeoAnimatable.registerSyncedAnimatable(this);
        resource = resourceName;
    }

    @Override
    public void createGeoRenderer(Consumer<GeoRenderProvider> consumer) {
        consumer.accept(new GeoRenderProvider() {
            private TonfaRenderer renderer;
            @Override
            public @NotNull GeoItemRenderer<TonfaItem> getGeoItemRenderer() {
                if (this.renderer == null) {
                    this.renderer = new TonfaRenderer(resource);
                }
                return this.renderer;
            }
        });
    }

    private PlayState setPlayState(AnimationTest<GeoAnimatable> animTest) {
        if (animTest.getData(DataTickets.ITEM_RENDER_PERSPECTIVE) == ItemDisplayContext.GUI) {
            return PlayState.STOP;
        }
        return PlayState.CONTINUE;
    }
    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(flipped_anim).add(unflipped_anim);
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, @NotNull LivingEntity entity, @NotNull InteractionHand hand) {
        boolean extended = Boolean.TRUE.equals(stack.getComponents().get(ModDataComponents.EXTENDED));
        if (entity.level() instanceof ServerLevel serverLevel) {
            if (!extended) {
                stopTriggeredAnim(entity, GeoItem.getOrAssignId(entity.getItemInHand(hand), serverLevel), "unflipped_controller", "unflip_anim");
                triggerAnim(entity, GeoItem.getOrAssignId(entity.getItemInHand(hand), serverLevel), "flipped_controller", "flip_anim");
            } else {
                stopTriggeredAnim(entity, GeoItem.getOrAssignId(entity.getItemInHand(hand), serverLevel), "flipped_controller", "flip_anim");
                triggerAnim(entity, GeoItem.getOrAssignId(entity.getItemInHand(hand), serverLevel), "unflipped_controller", "unflip_anim");
            }
        }
        if (entity.swingTime == 0 || true) {
            stack.set(ModDataComponents.EXTENDED, !extended);
        }
        return super.onEntitySwing(stack, entity, hand);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            player.stopUsingItem();
            return InteractionResult.FAIL;
        }
        return super.use(level, player, hand);
    }
}
