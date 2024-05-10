package kamkeel.npcdbc.client.gui;

import kamkeel.npcdbc.data.form.Form;
import kamkeel.npcdbc.network.PacketHandler;
import kamkeel.npcdbc.network.packets.RequestForm;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiYesNo;
import net.minecraft.client.gui.GuiYesNoCallback;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraft.util.StatCollector;
import noppes.npcs.client.Client;
import noppes.npcs.client.NoppesUtil;
import noppes.npcs.client.gui.SubGuiColorSelector;
import noppes.npcs.client.gui.util.*;
import noppes.npcs.constants.EnumPacketServer;
import noppes.npcs.entity.EntityNPCInterface;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Vector;

public class GuiNPCManageCustomForms extends GuiNPCInterface2 implements IScrollData, ICustomScrollListener, ITextfieldListener, IGuiData, ISubGuiListener, GuiYesNoCallback {
    private GuiCustomScroll scrollForms;
    private HashMap<String, Integer> data = new HashMap<String, Integer>();
    private Form customForm = new Form();
    private String selected = null;
    private String search = "";

    public GuiNPCManageCustomForms(EntityNPCInterface npc) {
        super(npc);
        PacketHandler.Instance.sendToServer(new RequestForm(-1, false).generatePacket());
    }

    public void initGui() {
        super.initGui();

        this.addButton(new GuiNpcButton(0, guiLeft + 368, guiTop + 8, 45, 20, "gui.add"));
        this.addButton(new GuiNpcButton(1, guiLeft + 368, guiTop + 32, 45, 20, "gui.remove"));

        if (scrollForms == null) {
            scrollForms = new GuiCustomScroll(this, 0, 0);
            scrollForms.setSize(143, 185);
        }
        scrollForms.guiLeft = guiLeft + 220;
        scrollForms.guiTop = guiTop + 4;
        addScroll(scrollForms);

        addTextField(new GuiNpcTextField(55, this, fontRendererObj, guiLeft + 220, guiTop + 4 + 3 + 185, 143, 20, search));

        if (customForm.id == -1)
            return;



        this.addTextField(new GuiNpcTextField(0, this, guiLeft + 40, guiTop + 4, 136, 20, customForm.name));
        getTextField(0).setMaxStringLength(20);
        addLabel(new GuiNpcLabel(0, "gui.name", guiLeft + 8, guiTop + 9));

        addLabel(new GuiNpcLabel(10, "ID", guiLeft + 178, guiTop + 4));
        addLabel(new GuiNpcLabel(11, customForm.id + "", guiLeft + 178, guiTop + 14));

        this.addTextField(new GuiNpcTextField(1, this, guiLeft + 70, guiTop + 26, 106, 20, customForm.menuName.replaceAll("§", "&")));
        getTextField(1).setMaxStringLength(20);
        addLabel(new GuiNpcLabel(1, "Menu name", guiLeft + 8, guiTop + 31));

        scrollForms.setList(getSearchList());
    }

    @Override
    protected void actionPerformed(GuiButton guibutton) {
        GuiNpcButton button = (GuiNpcButton) guibutton;
        if (button.id == 0) {
            save();
            String name = "New";
            while (data.containsKey(name))
                name += "_";
            Form form = new Form(-1, name);

            NBTTagCompound compound = form.writeToNBT();
            Client.sendData(EnumPacketServer.CustomFormSave, compound);
        }
        if (button.id == 1) {
            if (data.containsKey(scrollForms.getSelected())) {
                GuiYesNo guiyesno = new GuiYesNo(this, scrollForms.getSelected(), StatCollector.translateToLocal("gui.delete"), 1);
                displayGuiScreen(guiyesno);
            }
        }
    }

    @Override
    public void setGuiData(NBTTagCompound compound) {
        this.customForm = new Form();
        customForm.readFromNBT(compound);

        setSelected(customForm.name);
        initGui();
    }

    @Override
    public void keyTyped(char c, int i) {
        super.keyTyped(c, i);
        if (getTextField(55) != null) {
            if (getTextField(55).isFocused()) {
                if (search.equals(getTextField(55).getText()))
                    return;
                search = getTextField(55).getText().toLowerCase();
                scrollForms.resetScroll();
                scrollForms.setList(getSearchList());
            }
        }
    }

    private List<String> getSearchList() {
        if (search.isEmpty()) {
            return new ArrayList<String>(this.data.keySet());
        }
        List<String> list = new ArrayList<String>();
        for (String name : this.data.keySet()) {
            if (name.toLowerCase().contains(search))
                list.add(name);
        }
        return list;
    }

    @Override
    public void setData(Vector<String> list, HashMap<String, Integer> data) {
        String name = scrollForms.getSelected();
        this.data = data;
        scrollForms.setList(getSearchList());

        if (name != null)
            scrollForms.setSelected(name);
    }

    @Override
    public void setSelected(String selected) {
        this.selected = selected;
        scrollForms.setSelected(selected);
    }

    @Override
    public void customScrollClicked(int i, int j, int k, GuiCustomScroll guiCustomScroll) {
        if (guiCustomScroll.id == 0) {
            save();
            selected = scrollForms.getSelected();
            Client.sendData(EnumPacketServer.CustomFormGet, data.get(selected));
        }
    }

    @Override
    public void save() {
        if (selected != null && data.containsKey(selected) && customForm != null) {
            NBTTagCompound compound = customForm.writeToNBT();
            Client.sendData(EnumPacketServer.CustomFormSave, compound);
        }
    }

    @Override
    public void unFocused(GuiNpcTextField guiNpcTextField) {
        if (customForm.id == -1)
            return;


        switch(guiNpcTextField.id){
            case 0:
                String name = guiNpcTextField.getText();
                if (!name.isEmpty() && !data.containsKey(name)) {
                    String old = customForm.name;
                    data.remove(customForm.name);
                    customForm.name = name;
                    data.put(customForm.name, customForm.id);
                    selected = name;
                    scrollForms.replace(old, customForm.name);
                }
                break;
            case 1:
                String menuName = guiNpcTextField.getText();
                if(!menuName.isEmpty() && !data.containsKey(menuName)){
                    customForm.menuName = menuName.replaceAll("&", "§");
                }
                break;
        }
    }

    @Override
    public void subGuiClosed(SubGuiInterface subgui) {
        if (subgui instanceof SubGuiColorSelector) {
//	    	customForm.color = ((SubGuiColorSelector)subgui).color;
            initGui();
        }
    }

    @Override
    public void confirmClicked(boolean result, int id) {
        NoppesUtil.openGUI(player, this);
        if (!result)
            return;
        if (id == 1) {
            if (data.containsKey(scrollForms.getSelected())) {
                Client.sendData(EnumPacketServer.CustomFormRemove, data.get(selected));
                scrollForms.clear();
                customForm = new Form();
                initGui();
            }
        }
    }
}
