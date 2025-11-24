package io.github.shadow00dev.tonfa.item.custom;

import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.Fireball;
import net.minecraft.world.entity.projectile.SmallFireball;
import net.minecraft.world.entity.projectile.windcharge.WindCharge;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

public class WindTonfaItem extends TonfaItem {
    public WindTonfaItem(Properties properties, ToolMaterial material, String resourceName) {
        super(properties.fireResistant(), material, resourceName);
    }

    @Override
    public @NotNull InteractionResult use(@NotNull Level level, @NotNull Player player, @NotNull InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            if (!player.getCooldowns().isOnCooldown(player.getItemInHand(hand)) && !level.isClientSide()) {
                Vec3 vec = player.getEyePosition().add(player.getLookAngle().multiply(0.5, 0.5, 0.5));
                WindCharge windcharge = new WindCharge(player, level, vec.x, vec.y, vec.z);;
                windcharge.setDeltaMovement(player.getLookAngle().multiply(1,1,1));
                level.addFreshEntity(windcharge);
                ItemStack item = player.getItemInHand(hand);
                item.hurtAndBreak(10, player, hand);
                player.getCooldowns().addCooldown(item, 100);
                level.playSound(
                        null,
                        player.getX(),
                        player.getY(),
                        player.getZ(),
                        SoundEvents.WIND_CHARGE_THROW,
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
                if (player.getXRot() > 70) {
                    player.addEffect(new MobEffectInstance(
                            MobEffects.SLOW_FALLING,
                            10,
                            0,
                            false,
                            false,
                            false
                    ));
                }

            }
        }
        super.inventoryTick(stack, level, entity, slot);
    }

}
