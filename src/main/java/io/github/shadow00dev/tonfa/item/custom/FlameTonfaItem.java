package io.github.shadow00dev.tonfa.item.custom;

import net.minecraft.core.component.DataComponents;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.registries.DeferredRegister;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class FlameTonfaItem extends TonfaItem {
    public FlameTonfaItem(Properties properties, ToolMaterial material, String resourceName) {
        super(properties.fireResistant(), material, resourceName);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            if (!player.getCooldowns().isOnCooldown(player.getItemInHand(hand)) && !level.isClientSide()) {
                Fireball fireball = new SmallFireball(level, player, player.getLookAngle().multiply(0.5, 0.5, 0.5));
                fireball.setPos(player.getEyePosition().add(player.getLookAngle().multiply(0.5, 0.5, 0.5)).subtract(0,0.2,0));
                level.addFreshEntity(fireball);
                ItemStack item = player.getItemInHand(hand);
                item.hurtAndBreak(25, player, hand);
                player.getCooldowns().addCooldown(item, 100);
                player.playSound(SoundEvents.FIRECHARGE_USE);
                level.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.FIRECHARGE_USE,
                        SoundSource.PLAYERS
                );

            }
            player.stopUsingItem();
        }
        return super.use(level, player, hand);
    }

    @Override
    public void inventoryTick(ItemStack stack, ServerLevel level, Entity entity, @Nullable EquipmentSlot slot) {
        if (!level.isClientSide() && entity instanceof Player player) {
            if (player.isHolding(stack.getItem())) {
                if (player.isOnFire() && player.getRemainingFireTicks() > 20) {
                    player.setRemainingFireTicks(20);
                }
                player.addEffect(new MobEffectInstance(
                        MobEffects.FIRE_RESISTANCE,
                        10,
                        0,
                        false,
                        false,
                        false
                ));
            }
        }
        super.inventoryTick(stack, level, entity, slot);
    }

}
