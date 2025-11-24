package io.github.shadow00dev.tonfa;

import io.github.shadow00dev.tonfa.component.ModDataComponents;
import io.github.shadow00dev.tonfa.item.ModItems;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.CreativeModeTabs;

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.fml.common.Mod;
import net.neoforged.neoforge.event.BuildCreativeModeTabContentsEvent;

import java.util.List;
import java.util.function.Supplier;

@Mod(TonfaMod.MODID)
public class TonfaMod {
    public static final String MODID = "shadowstonfa";
    private static final List<Supplier<ItemStack>> TONFAS_IN_ORDER = List.of(
            ModItems.WOOD_TONFA::toStack,
            ModItems.STONE_TONFA::toStack,
            ModItems.COPPER_TONFA::toStack,
            ModItems.IRON_TONFA::toStack,
            ModItems.GOLD_TONFA::toStack,
            ModItems.DIAMOND_TONFA::toStack,
            ModItems.NETHERITE_TONFA::toStack,
            ModItems.FLAME_TONFA::toStack,
            ModItems.WIND_TONFA::toStack
    );

    public TonfaMod(IEventBus modEventBus) {
        ModItems.register(modEventBus);
        ModDataComponents.register(modEventBus);

        modEventBus.addListener(this::addCreative);

    }

    private void addCreative(BuildCreativeModeTabContentsEvent event) {
        if (event.getTabKey() != CreativeModeTabs.COMBAT) return;

        ItemStack previous = Items.NETHERITE_AXE.getDefaultInstance();

        for (Supplier<ItemStack> stack : TONFAS_IN_ORDER) {
            ItemStack current = stack.get();
            event.insertAfter(previous, current, CreativeModeTab.TabVisibility.PARENT_AND_SEARCH_TABS);
            previous = current;
        }
    }
}