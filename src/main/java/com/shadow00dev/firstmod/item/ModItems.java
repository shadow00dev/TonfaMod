package com.shadow00dev.firstmod.item;

import com.shadow00dev.firstmod.FirstMod;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FirstMod.MODID);

    public static final DeferredItem<Item> FIRST_ITEM = ITEMS.registerItem(
            "first_item",
            props -> new Item(props.sword(ToolMaterial.DIAMOND, 10, -0.4f).durability(1)),
            new Item.Properties());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
