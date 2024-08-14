package kamkeel.npcdbc.client.gui.hud.formWheel;

import kamkeel.npcdbc.client.gui.hud.WheelSegment;
import kamkeel.npcdbc.client.gui.hud.formWheel.icon.FormIcon;
import kamkeel.npcdbc.config.ConfigDBCClient;
import kamkeel.npcdbc.constants.DBCForm;
import kamkeel.npcdbc.controllers.FormController;
import kamkeel.npcdbc.data.FormWheelData;
import kamkeel.npcdbc.data.dbcdata.DBCData;
import kamkeel.npcdbc.data.form.Form;
import kamkeel.npcdbc.network.PacketHandler;
import kamkeel.npcdbc.network.packets.form.DBCSaveFormWheel;
import kamkeel.npcdbc.network.packets.form.DBCSelectForm;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.FontRenderer;
import org.lwjgl.opengl.GL11;

import static org.lwjgl.opengl.GL11.*;

class FormWheelSegment extends WheelSegment {
    public HUDFormWheel parent;
    public Form form;
    public FormWheelData data = new FormWheelData();
    private FormIcon icon = null;

    FormWheelSegment(HUDFormWheel parent, int index) {
        this(parent, 0, 0, index);

    }

    FormWheelSegment(HUDFormWheel parent, int posX, int posY, int index) {
        super(index);
        this.parent = parent;
        this.posX = posX;
        this.posY = posY;
        this.index = data.slot = index;
    }

    public void selectForm() {
        PacketHandler.Instance.sendToServer(new DBCSelectForm(data.formID, data.isDBC).generatePacket());
    }

    public void setForm(int formID, boolean isDBC, boolean updateServer) {
        data.formID = formID;
        data.isDBC = isDBC;
        form = !data.isDBC ? (Form) FormController.getInstance().get(data.formID) : null;
        if (updateServer)
            PacketHandler.Instance.sendToServer(new DBCSaveFormWheel(index, data).generatePacket());
        icon = form != null ? new FormIcon(parent, form) : new FormIcon(parent, data.formID);
    }

    public void setForm(FormWheelData data, boolean updateServer) {
        this.data = data;
        form = !data.isDBC ? (Form) FormController.getInstance().get(data.formID) : null;
        if (updateServer)
            PacketHandler.Instance.sendToServer(new DBCSaveFormWheel(index, data).generatePacket());
        icon = form != null ? new FormIcon(parent, form) : new FormIcon(parent, data.formID);
    }

    public void removeForm() {
        data.reset();
        form = null;
        PacketHandler.Instance.sendToServer(new DBCSaveFormWheel(index, data).generatePacket());
        if (parent.hoveredSlot == index)
            parent.selectSlot(-1);

        parent.timeClosedSubGui = Minecraft.getSystemTime();
        icon = null;
    }


    @Override
    protected void drawWheelItem(FontRenderer fontRenderer) {
        if (data.formID != -1) {
            if (index == 1 || index == 5) {
                glTranslatef(0, 10, 0);
            } else if (index == 2 || index == 4) {
                glTranslatef(0, -10, 0);
            }
            if (ConfigDBCClient.AlteranteSelectionWheelTexture) {
                glScaled(0.7, 0.7, 1);
                switch(index){
                    case 0:
                        glTranslatef(0, -15f, 0);
                        break;
                    case 1:
                        glTranslatef(-12, -5, 0);
                        break;
                    case 2:
                        glTranslatef(-11, 3, 0);
                        break;
                    case 3:
                        glTranslatef(0, 12f, 0);
                        break;
                    case 4:
                        glTranslatef(10, 3, 0);
                        break;
                    default:
                        glTranslatef(13, -5, 0);
                }
            }
            if (data.isDBC) {
                if (!parent.dbcForms.containsKey(data.formID))
                    removeForm();
            } else {
                if (!parent.dbcInfo.hasFormUnlocked(data.formID))
                    removeForm();
            }
            if(icon != null){
                glTranslatef(0, (float) -icon.height /2, 0);
                icon.draw();
                glTranslatef(0, icon.height, 0);
            }
            drawCenteredString(fontRenderer, getFormName(), 0, 0, 0xFFFFFFFF);
        }
    }

    public String getFormName() {
        DBCData dbcData = DBCData.get(parent.dbcInfo.parent.player);

        if(!data.isDBC)
            return form != null ? form.menuName : "";

        return DBCForm.getMenuName(dbcData.Race, data.formID);
    }
}