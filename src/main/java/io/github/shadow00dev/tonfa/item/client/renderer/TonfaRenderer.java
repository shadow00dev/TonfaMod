package io.github.shadow00dev.tonfa.item.client.renderer;

import static io.github.shadow00dev.tonfa.TonfaMod.MODID;
import io.github.shadow00dev.tonfa.item.custom.TonfaItem;
import net.minecraft.resources.Identifier;
import org.jspecify.annotations.NonNull;
import software.bernie.geckolib.model.DefaultedItemGeoModel;
import software.bernie.geckolib.renderer.GeoItemRenderer;
import software.bernie.geckolib.renderer.base.GeoRenderState;

public class TonfaRenderer extends GeoItemRenderer<TonfaItem> {
    public TonfaRenderer(String type) {
        super(new DefaultedItemGeoModel<>(Identifier.fromNamespaceAndPath(MODID, "tonfa")) {
            @Override
            public @NonNull Identifier getTextureResource(@NonNull GeoRenderState state) {
                return Identifier.fromNamespaceAndPath(MODID, "textures/item/3d/" + type + ".png");
            }
        });
    }
}