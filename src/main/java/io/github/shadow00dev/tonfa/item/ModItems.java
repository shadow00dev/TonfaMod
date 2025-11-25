package io.github.shadow00dev.tonfa.item;

import io.github.shadow00dev.tonfa.TonfaMod;
import io.github.shadow00dev.tonfa.item.custom.FlameTonfaItem;
import io.github.shadow00dev.tonfa.item.custom.TonfaItem;
import io.github.shadow00dev.tonfa.item.custom.WindTonfaItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;



public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TonfaMod.MODID);

    public static DeferredItem<Item> WOOD_TONFA = ITEMS.registerItem("wood_tonfa", properties -> new TonfaItem(properties, ToolMaterial.WOOD, "wood"));
    public static DeferredItem<Item> STONE_TONFA = ITEMS.registerItem("stone_tonfa", properties -> new TonfaItem(properties, ToolMaterial.STONE, "stone"));
    public static DeferredItem<Item> COPPER_TONFA = ITEMS.registerItem("copper_tonfa", properties -> new TonfaItem(properties, ToolMaterial.COPPER, "copper"));
    public static final DeferredItem<Item> IRON_TONFA = ITEMS.registerItem("iron_tonfa", properties -> new TonfaItem(properties, ToolMaterial.IRON, "iron"));
    public static final DeferredItem<Item> DIAMOND_TONFA = ITEMS.registerItem("diamond_tonfa", properties -> new TonfaItem(properties, ToolMaterial.DIAMOND, "diamond"));
    public static final DeferredItem<Item> GOLD_TONFA = ITEMS.registerItem("gold_tonfa", properties -> new TonfaItem(properties, ToolMaterial.GOLD, "gold"));
    public static final DeferredItem<Item> NETHERITE_TONFA = ITEMS.registerItem("netherite_tonfa", properties -> new TonfaItem(properties.fireResistant(), ToolMaterial.NETHERITE, "netherite"));
    public static final DeferredItem<Item> FLAME_TONFA = ITEMS.registerItem("flame_tonfa", properties -> new FlameTonfaItem(properties, ToolMaterial.IRON, "flame"));
    public static final DeferredItem<Item> WIND_TONFA = ITEMS.registerItem("wind_tonfa", properties -> new WindTonfaItem(properties, ToolMaterial.IRON, "wind"));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
