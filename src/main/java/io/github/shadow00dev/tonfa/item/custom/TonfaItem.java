package io.github.shadow00dev.tonfa.item.custom;

import io.github.shadow00dev.tonfa.component.ModDataComponents;
import io.github.shadow00dev.tonfa.item.client.renderer.TonfaRenderer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.tags.DamageTypeTags;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.item.component.BlocksAttacks;
import net.minecraft.world.level.Level;
import net.neoforged.fml.startup.Server;
import org.jetbrains.annotations.NotNull;
import software.bernie.geckolib.animatable.GeoItem;
import software.bernie.geckolib.animatable.SingletonGeoAnimatable;
import software.bernie.geckolib.animatable.client.GeoRenderProvider;
import software.bernie.geckolib.animatable.instance.AnimatableInstanceCache;
import software.bernie.geckolib.animatable.manager.AnimatableManager;
import software.bernie.geckolib.animatable.processing.AnimationController;
import software.bernie.geckolib.animation.PlayState;
import software.bernie.geckolib.animation.RawAnimation;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.util.GeckoLibUtil;

import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

public class TonfaItem extends Item implements GeoItem {
    private static final RawAnimation TEST_ANIM = RawAnimation.begin().thenPlay("animation.tonfa.new");
    private final AnimatableInstanceCache cache = GeckoLibUtil.createInstanceCache(this);
    public String resource = "wood";
    public TonfaItem(Properties properties, ToolMaterial material, String resourceName) {
        super(properties.sword(material, 1, -1f)
                .component(DataComponents.BLOCKS_ATTACKS,
                        new BlocksAttacks(
                            0.25f,
                            1f,
                            List.of(new BlocksAttacks.DamageReduction(90f, Optional.empty(), 0f, 1f)),
                            new BlocksAttacks.ItemDamageFunction(3f, 1f, 1f),
                            Optional.of(DamageTypeTags.BYPASSES_SHIELD),
                            Optional.of(SoundEvents.SHIELD_BLOCK),
                            Optional.of(SoundEvents.SHIELD_BREAK)
                            )
                ).component(DataComponents.BREAK_SOUND, SoundEvents.SHIELD_BREAK).component(ModDataComponents.EXTENDED, false));
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

    @Override
    public void registerControllers(AnimatableManager.ControllerRegistrar controllers) {
        controllers.add(new AnimationController<>("test_controller", 20, animTest -> PlayState.STOP)
                .triggerableAnim("test_anim", TEST_ANIM));
    }

    @Override
    public AnimatableInstanceCache getAnimatableInstanceCache() {
        return this.cache;
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, @NotNull LivingEntity entity, @NotNull InteractionHand hand) {
        if (entity.level() instanceof ServerLevel serverLevel) {
            triggerAnim(entity, GeoItem.getOrAssignId(entity.getItemInHand(hand), serverLevel), "test_controller", "test_anim");
        }
        stack.set(ModDataComponents.EXTENDED, Boolean.FALSE.equals(stack.getComponents().get(ModDataComponents.EXTENDED)));
        return super.onEntitySwing(stack, entity, hand);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (level instanceof ServerLevel serverLevel) {
            triggerAnim(player, GeoItem.getOrAssignId(player.getItemInHand(hand), serverLevel), "test_controller", "test_anim");
        }
        if (hand == InteractionHand.MAIN_HAND) {
            player.stopUsingItem();
            return InteractionResult.FAIL;
        }
        return super.use(level, player, hand);
    }
}
