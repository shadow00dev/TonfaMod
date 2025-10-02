package com.shadow00dev.firstmod.item;

import com.shadow00dev.firstmod.FirstMod;
import net.minecraft.world.item.Item;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;

public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(FirstMod.MODID);

    public static final DeferredItem<Item> FIRST_ITEM = ITEMS.registerSimpleItem(
            "first_item",
            new Item.Properties());

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
