package io.github.shadow00dev.tonfa.item.client.renderer;
import io.github.shadow00dev.tonfa.TonfaMod;
import io.github.shadow00dev.tonfa.item.custom.TonfaItem;
import net.minecraft.resources.ResourceLocation;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class TonfaRenderer extends GeoItemRenderer<TonfaItem> {
    public TonfaRenderer(String type) {
        super(new DefaultedItemGeoModel<>(ResourceLocation.fromNamespaceAndPath(TonfaMod.MODID, "tonfa")) {
            @Override
            public ResourceLocation getTextureResource(GeoRenderState state) {
                return ResourceLocation.fromNamespaceAndPath(TonfaMod.MODID, "textures/item/" + type + ".png");
            }
        });
    }
}