package kamkeel.npcdbc.client.render;

import JinRyuu.JBRA.RenderPlayerJBRA;
import JinRyuu.JRMCore.entity.EntityCusPar;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import kamkeel.npcdbc.client.shader.PostProcessing;
import kamkeel.npcdbc.config.ConfigDBCClient;
import kamkeel.npcdbc.data.dbcdata.DBCData;
import kamkeel.npcdbc.data.npc.DBCDisplay;
import kamkeel.npcdbc.entity.EntityAura;
import kamkeel.npcdbc.mixins.early.IEntityMC;
import kamkeel.npcdbc.mixins.late.INPCDisplay;
import kamkeel.npcdbc.mixins.late.IRenderCusPar;
import kamkeel.npcdbc.scripted.DBCPlayerEvent;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.entity.Render;
import net.minecraft.client.renderer.entity.RenderManager;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraftforge.client.event.RenderLivingEvent;
import net.minecraftforge.client.event.RenderPlayerEvent;
import noppes.npcs.client.renderer.RenderCustomNpc;
import noppes.npcs.entity.EntityNPCInterface;
import org.lwjgl.BufferUtils;

import java.nio.FloatBuffer;
import java.util.Iterator;

import static kamkeel.npcdbc.client.shader.PostProcessing.processBloom;
import static org.lwjgl.opengl.GL11.*;

public class RenderEventHandler {
    public static final int TAIL_STENCIL_ID = 2;
    public static FloatBuffer PRE_RENDER_MODELVIEW = BufferUtils.createFloatBuffer(16);

    @SubscribeEvent
    public void enablePlayerStencil(RenderPlayerEvent.Pre e) {
        glClear(GL_STENCIL_BUFFER_BIT); //TODO: needs to be put somewhere else i.e RenderWorldLastEvent, but for some reason doesn't work when put there
        glEnable(GL_STENCIL_TEST);
        enableStencilWriting(e.entity.getEntityId());
    }

    @SubscribeEvent
    public void enableHandStencil(DBCPlayerEvent.RenderArmEvent.Pre e) {
        glClear(GL_STENCIL_BUFFER_BIT); //TODO: needs to be put somewhere else i.e RenderWorldLastEvent, but for some reason doesn't work when put there
        glEnable(GL_STENCIL_TEST);
        enableStencilWriting(e.entity.getEntityId());
    }

    @SubscribeEvent
    public void enableEntityStencil(RenderLivingEvent.Pre e) {
        if ((e.entity instanceof EntityPlayer)) {
            //IMPORTANT, SAVES THE MODEL VIEW MATRIX PRE ENTITYLIVING TRANSFORMATIONS
            glGetFloat(GL_MODELVIEW_MATRIX, PRE_RENDER_MODELVIEW);
        } else if ((e.entity instanceof EntityNPCInterface)) {
            glGetFloat(GL_MODELVIEW_MATRIX, PRE_RENDER_MODELVIEW);
            glClear(GL_STENCIL_BUFFER_BIT); //TODO: needs to be put somewhere else i.e RenderWorldLastEvent, but for some reason doesn't work when put there
            glEnable(GL_STENCIL_TEST);
            enableStencilWriting(e.entity.getEntityId());
            Minecraft.getMinecraft().entityRenderer.disableLightmap(0);
        }
        glDepthMask(true); //fixes a native MC RP1 entity bug in which the depth test is disabled
    }

    @SubscribeEvent
    public void renderNPC(RenderLivingEvent.Post e) {
        if (!(e.entity instanceof EntityNPCInterface))
            return;

        EntityNPCInterface entity = (EntityNPCInterface) e.entity;
        RenderCustomNpc r = (RenderCustomNpc) e.renderer;
        float partialTicks = Minecraft.getMinecraft().timer.renderPartialTicks;

        disableStencilWriting(entity.getEntityId(), false);
        Minecraft.getMinecraft().entityRenderer.disableLightmap(0);
        DBCDisplay display = ((INPCDisplay) entity.display).getDBCDisplay();

        ////////////////////////////////////////
        ////////////////////////////////////////
        //Aura
        EntityAura aura = display.auraEntity;
        if (aura != null && aura.shouldRender()) {
            glPushMatrix();
            glLoadMatrix(PRE_RENDER_MODELVIEW); //RESETS TRANSFORMATIONS DONE TO CURRENT MATRIX TO PRE-ENTITY RENDERING STATE
            glStencilFunc(GL_GREATER, entity.getEntityId(), 0xFF);
            AuraRenderer.Instance.renderAura(aura, partialTicks);
            //  NewAura.renderAura(aura, partialTicks);
            glPopMatrix();
        }


        ////////////////////////////////////////
        ////////////////////////////////////////
        //Custom Particles
        glPushMatrix();
        glLoadMatrix(PRE_RENDER_MODELVIEW); //IMPORTANT, PARTICLES WONT ROTATE PROPERLY WITHOUT THIS
        IRenderCusPar particleRender = null;
        for (Iterator<EntityCusPar> iter = display.particleRenderQueue.iterator(); iter.hasNext(); ) {
            EntityCusPar particle = iter.next();
            if (particleRender == null)
                particleRender = (IRenderCusPar) RenderManager.instance.getEntityRenderObject(particle);

            particleRender.renderParticle(particle, partialTicks);
            if (particle.isDead)
                iter.remove();
        }
        glPopMatrix();


        ////////////////////////////////////////
        ////////////////////////////////////////
        //Outline

        ////////////////////////////////////////
        ////////////////////////////////////////
        Minecraft.getMinecraft().entityRenderer.enableLightmap(0);
        enableStencilWriting(e.entity.getEntityId());
        // postStencilRendering();//LETS YOU DRAW TO THE COLOR BUFFER AGAIN
        glClear(GL_STENCIL_BUFFER_BIT); //TODO: needs to be put somewhere else i.e RenderWorldLastEvent, but for some reason doesn't work when put there
        glDisable(GL_STENCIL_TEST);

    }


    public void renderPlayer(EntityPlayer player, Render renderer, float partialTicks, boolean isArm) {
        RenderPlayerJBRA render = (RenderPlayerJBRA) renderer;
        DBCData data = DBCData.get(player);
        Minecraft.getMinecraft().entityRenderer.disableLightmap(0);
        EntityAura aura = data.auraEntity;

        ////////////////////////////////////////
        ////////////////////////////////////////
        //Outline
        if (ConfigDBCClient.EnableBloom) {
            PostProcessing.drawToBuffers(0, 2);
            processBloom = true;
        }

        data.outline = new PlayerOutline(0x00ffff, 0xffffff);
        //  data.outline = null;
        if (data.outline != null) {
            glStencilFunc(GL_NOTEQUAL, player.getEntityId(), 0xFF);  // Test stencil value
            PlayerOutline.renderOutline(render, player, partialTicks, isArm);
        } else if (aura == null && ((IEntityMC) player).getRenderPassTampered()) {
            ((IEntityMC) player).setRenderPass(0);
        }
        if (processBloom)
            PostProcessing.resetDrawBuffer();

        ////////////////////////////////////////
        ////////////////////////////////////////
        //Aura
        if (aura != null && aura.shouldRender()) {
            glPushMatrix();
            glLoadMatrix(PRE_RENDER_MODELVIEW); //RESETS TRANSFORMATIONS DONE TO CURRENT MATRIX TO PRE-ENTITY RENDERING STATE
            glStencilFunc(GL_GREATER, player.getEntityId(), 0xFF);
            glStencilMask(0x0);
            AuraRenderer.Instance.renderAura(aura, partialTicks);
            // NewAura.renderAura(aura, partialTicks);
            glPopMatrix();
        }

        ////////////////////////////////////////
        ////////////////////////////////////////
        //Custom Particles
        glPushMatrix();
        glLoadMatrix(PRE_RENDER_MODELVIEW); //IMPORTANT, PARTICLES WONT ROTATE PROPERLY WITHOUT THIS
         glStencilFunc(GL_GREATER, player.getEntityId(), 0xFF);
        IRenderCusPar particleRender = null;
        for (Iterator<EntityCusPar> iter = data.particleRenderQueue.iterator(); iter.hasNext(); ) {
            EntityCusPar particle = iter.next();
            if (particleRender == null)
                particleRender = (IRenderCusPar) RenderManager.instance.getEntityRenderObject(particle);

            particleRender.renderParticle(particle, partialTicks);
            if (particle.isDead)
                iter.remove();
        }
        glPopMatrix();

        ////////////////////////////////////////
        ////////////////////////////////////////
        Minecraft.getMinecraft().entityRenderer.enableLightmap(0);
        postStencilRendering();

    }

    @SubscribeEvent
    public void renderPlayer(DBCPlayerEvent.RenderEvent.Pre e) {
        EntityAura aura = DBCData.get(e.entityPlayer).auraEntity;
        if ((aura != null && aura.shouldRender()) || DBCData.get(e.entityPlayer).outline != null)
            Minecraft.getMinecraft().entityRenderer.disableLightmap(0);
    }

    @SubscribeEvent
    public void renderPlayer(DBCPlayerEvent.RenderEvent.Post e) {
        renderPlayer(e.entityPlayer, e.renderer, e.partialRenderTick, false);
    }

    @SubscribeEvent
    public void renderHand(DBCPlayerEvent.RenderArmEvent.Post e) {
        renderPlayer(e.entityPlayer, e.renderer, e.partialRenderTick, true);
    }

    public static void enableStencilWriting(int id) {
        glStencilFunc(GL_ALWAYS, id, 0xFF);  // Always draw to the color buffer & pass the stencil test
        glStencilMask(0xFF);  // Write to stencil buffer
        glStencilOp(GL_KEEP, GL_KEEP, GL_REPLACE);  // Keep stencil value
    }

    public static void disableStencilWriting(int id, boolean invert) {
        glStencilFunc(invert ? GL_EQUAL : GL_NOTEQUAL, id, 0xFF);  // Test stencil value
        glStencilMask(0x00);  // Do not write to stencil buffer
    }

    public static void postStencilRendering() {
        glStencilFunc(GL_ALWAYS, 0, 0xFF);
    }
}
