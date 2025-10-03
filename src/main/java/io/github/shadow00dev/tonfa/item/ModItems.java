package io.github.shadow00dev.tonfa.item;

import io.github.shadow00dev.tonfa.TonfaMod;
import io.github.shadow00dev.tonfa.item.custom.TonfaItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ToolMaterial;
import net.neoforged.bus.api.IEventBus;
import net.neoforged.neoforge.registries.DeferredItem;
import net.neoforged.neoforge.registries.DeferredRegister;



public class ModItems {
    public static final DeferredRegister.Items ITEMS = DeferredRegister.createItems(TonfaMod.MODID);

    public static final DeferredItem<Item> DIAMOND_TONFA = ITEMS.registerItem("diamond_tonfa", properties -> new TonfaItem(properties, ToolMaterial.DIAMOND));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
