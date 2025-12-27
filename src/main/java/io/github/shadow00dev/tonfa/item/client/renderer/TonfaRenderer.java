package io.github.shadow00dev.tonfa.item.client.renderer;
import io.github.shadow00dev.tonfa.TonfaMod;
import io.github.shadow00dev.tonfa.item.custom.TonfaItem;
import net.minecraft.resources.Identifier;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class TonfaRenderer extends GeoItemRenderer<TonfaItem> {
    public TonfaRenderer(String type) {
        super(new DefaultedItemGeoModel<>(Identifier.fromNamespaceAndPath(TonfaMod.MODID, "tonfa")) {
            @Override
            public Identifier getTextureResource(GeoRenderState state) {
                return Identifier.fromNamespaceAndPath(TonfaMod.MODID, "textures/item/3d/" + type + ".png");
            }
        });
    }
}