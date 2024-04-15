package kamkeel.npcdbc.data.SyncedData;

import kamkeel.npcdbc.api.ICustomForm;
import kamkeel.npcdbc.controllers.FormController;
import kamkeel.npcdbc.data.CustomForm;
import kamkeel.npcdbc.network.PacketRegistry;
import kamkeel.npcdbc.util.Utility;
import net.minecraft.entity.Entity;
import net.minecraft.entity.player.EntityPlayer;
import net.minecraft.nbt.NBTTagCompound;
import net.minecraftforge.common.IExtendedEntityProperties;

import java.util.ArrayList;
import java.util.List;

/**
 * Store all player CustomForms Data here
 */
public class CustomFormData extends PerfectSync<CustomFormData> implements IExtendedEntityProperties {
    public static String NAME = "CustomFormData";

    // id of form player is currently in, 0 if non CNPC custom form,
    public int currentForm = 0;
    // name of form to be entered upon transformation
    public String selectedForm = "";


    //names of forms given to player/player has unlocked
    public List<String> accessibleForms;
    //accessibleForms as objects
    public List<CustomForm> forms = new ArrayList<>();


    public CustomFormData(Entity player) {
        super(player);
        this.DATA_NAME = NAME;
        accessibleForms = new ArrayList<>();

        this.player = (EntityPlayer) player;
        if (!Utility.isServer())  //request accessibleForms objects from server
            PacketRegistry.syncData(this.player, "loadForms", null);

    }

    public static CustomFormData get(Entity player) {
        return get(player, NAME);

    }

    public static CustomFormData getClient() {
        return getClient(NAME);
    }

    //add conditions here
    public static boolean eligibleForCustomForms(Entity p) {
        if (p instanceof EntityPlayer)
            return true;
        return false;
    }

    public static boolean has(Entity p) {
        return get(p, NAME) != null;
    }

    public void addForm(String name) { //add form to player
        if (!accessibleForms.contains(name)) {
            accessibleForms.add(name);
            this.saveToNBT(true);
        }

    }

    public void removeForm(String name) { //removes form from player
        if (accessibleForms.contains(name)) {
            accessibleForms.remove(name);
            this.saveToNBT(true);
        }
    }

    public boolean hasForm(String name) {
        return accessibleForms.contains(name);
    }

    public boolean isInCustomForm() {
        return currentForm > 0;
    }

    public boolean isInForm(String formName) {
        return getCurrentForm().getName().equals(formName);
    }

    public CustomForm getCurrentForm() {
        if (currentForm > 0)
            return (CustomForm) (Utility.isServer() ? FormController.Instance.get(currentForm) : getFormClient(currentForm));

        return null;
    }

    public CustomForm getSelectedForm() {
        return (CustomForm) (Utility.isServer() ? FormController.Instance.get(selectedForm) : getFormClient(selectedForm));
    }

    @Override
    public void saveNBTData(NBTTagCompound compound) {
        NBTTagCompound c = compound(entity, NAME);

        c.setInteger("currentForm", currentForm);
        c.setString("selectedForm", selectedForm);


        if (!accessibleForms.isEmpty()) {

            StringBuilder sb = new StringBuilder();
            for (String s : accessibleForms)
                sb.append(s + ",");
            String st = sb.toString().substring(0, sb.length() - 1);
            c.setString("accessibleForms", st); //removes the last ,
        } else {
            c.setString("accessibleForms", "");
        }


    }

    public void loadNBTData(NBTTagCompound compound) {
        NBTTagCompound c = Utility.isServer() ? compound(entity, NAME) : compound;

        currentForm = c.getInteger("currentForm");
        selectedForm = c.getString("selectedForm");


        String s = c.getString("accessibleForms");
        List<String> newForms = new ArrayList<>();
        if (s.contains(",")) {
            String[] forms = s.split(",");
            for (String str : forms)
                newForms.add(str);
        } else if (s.isEmpty())
            newForms.clear();
        else
            newForms.add(s);
        accessibleForms = newForms;


        nbt = c;

    }

    ////////////////////////////////////////////////
    ////////////////////////////////////////////////
    // Client side CustomForm object loading

    public void loadAllAccessibleForms() { //sends all accessibleForm objects from server to client
        for (String e : accessibleForms) {
            FormController.Instance.loadToClient(player, (CustomForm) FormController.Instance.get(e));
        }
    }

    public void loadForm(NBTTagCompound data) {
        CustomForm f = new CustomForm();
        f.readFromNBT(data);
        for (CustomForm v : forms)
            if (v.name.equals(f.name))
                v.readFromNBT(data); //update form if exists

        forms.add(f);
    }

    public void unloadForm(NBTTagCompound data) {
        CustomForm f = new CustomForm();
        f.readFromNBT(data);
        for (CustomForm v : forms)
            if (v.name.equals(f.name))
                forms.remove(v);

    }

    public ICustomForm getFormClient(String name) {
        for (CustomForm f : forms) {
            if (f.name.equals(name))
                return f;
        }
        return null;
    }

    public ICustomForm getFormClient(int id) {
        for (CustomForm f : forms) {
            if (f.id == id)
                return f;
        }
        return null;
    }


}