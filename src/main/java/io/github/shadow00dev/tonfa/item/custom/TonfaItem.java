package io.github.shadow00dev.tonfa.item.custom;

import io.github.shadow00dev.tonfa.component.ModDataComponents;
import net.minecraft.core.component.DataComponents;
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

import java.util.List;
import java.util.Optional;

public class TonfaItem extends Item {
    public TonfaItem(Properties properties, ToolMaterial material) {
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
    }

    @Override
    public boolean onEntitySwing(ItemStack stack, LivingEntity entity, InteractionHand hand) {
        stack.set(ModDataComponents.EXTENDED, Boolean.FALSE.equals(stack.getComponents().get(ModDataComponents.EXTENDED)));
        return super.onEntitySwing(stack, entity, hand);
    }

    @Override
    public InteractionResult use(Level level, Player player, InteractionHand hand) {
        if (hand == InteractionHand.MAIN_HAND) {
            player.stopUsingItem();
            return InteractionResult.FAIL;
        }
        return super.use(level, player, hand);
    }
}
