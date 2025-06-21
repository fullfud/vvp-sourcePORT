package tech.vvp.vvp.init;

import net.neoforged.api.distmarker.Dist;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.neoforge.client.event.EntityRenderersEvent;
import net.neoforged.neoforge.fml.common.Mod;
import tech.vvp.vvp.VVP;
import tech.vvp.vvp.client.renderer.entity.*;

@Mod.EventBusSubscriber(modid = VVP.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ModEntityRenderers {
    @SubscribeEvent
    public static void registerRenderers(EntityRenderersEvent.RegisterRenderers event) {
        event.registerEntityRenderer(ModEntities.VAZIK.get(), vazikRenderer::new);
        event.registerEntityRenderer(ModEntities.BIKEGREEN.get(), bikegreenRenderer::new);
        event.registerEntityRenderer(ModEntities.BIKERED.get(), bikeredRenderer::new);
        event.registerEntityRenderer(ModEntities.MI24.get(), mi24Renderer::new);
        event.registerEntityRenderer(ModEntities.MI24POL.get(), mi24polRenderer::new);
        event.registerEntityRenderer(ModEntities.MI24UKR.get(), mi24ukrRenderer::new);
        event.registerEntityRenderer(ModEntities.M997.get(), m997Renderer::new);
        event.registerEntityRenderer(ModEntities.M997_GREEN.get(), m997_greenRenderer::new);
        event.registerEntityRenderer(ModEntities.COBRA.get(), cobraRenderer::new);
        event.registerEntityRenderer(ModEntities.COBRASHARK.get(), cobrasharkRenderer::new);
        event.registerEntityRenderer(ModEntities.BTR_80A.get(), btr80aRenderer::new);
        event.registerEntityRenderer(ModEntities.BTR_80A_1.get(), btr80a_1Renderer::new);
    }
}