package kamkeel.npcdbc.data;

import kamkeel.npcdbc.api.IFormMastery;
import net.minecraft.nbt.NBTTagCompound;

public class FormMastery implements IFormMastery {

    public float maxLevel = 100, instantTransformationUnlockLevel = 50;
    public float attributeMultiFlat = 1.0f, attributeMultiPerLevel = 0.01f, attributeMultiMinOrMax = 1.5f;
    public float updateGain = 0.01f, updateMindBonusFlat = 1.0f, updateMindBonusPerMind = 0.001f, updateMindBonusMax = 1.5f;
    public float attackGain = 0.03f, attackMindBonusFlat = 1.0f, attackMindBonusPerMind = 0.001f, attackMindBonusMax = 1.5f;
    public float damagedGain = 0.03f, damagedMindBonusFlat = 1.0f, damagedMindBonusPerMind = 0.001f, damagedMindBonusMax = 1.5f;
    public float fireKiGain = 0.03f, fireKiMindBonusFlat = 1.0f, fireKiMindBonusPerMind = 0.001f, fireKiMindBonusMax = 1.5f;
    public float kiDrainMultiFlat = 1.0f, kiDrainMultiPerLevel = -0.01f, kiDrainMultiMinOrMax = 0.5f;
    public float strainMultiFlat = 1.0f, strainMultiPerLevel = -0.01f, strainMultiMinOrMax = 0.5f;
    public float healthRequirementMultiFlat = 1.0f, healthRequirementMultiPerLevel = 0.01f, healthRequirementMultiMinOrMax = 1.5f;
    private CustomForm parent;

    public FormMastery(CustomForm parent) {
        this.parent = parent;
    }


    public float getMulti(String type, String type1) {
        switch (type.toLowerCase()) {
            case "attribute":
                switch (type1.toLowerCase()) {
                    case "flat":
                        return attributeMultiFlat;
                    case "perlevel":
                        return attributeMultiPerLevel;
                    case "minormax":
                        return attributeMultiMinOrMax;
                }
            case "kidrain":
                switch (type1.toLowerCase()) {
                    case "flat":
                        return kiDrainMultiFlat;
                    case "perlevel":
                        return kiDrainMultiPerLevel;
                    case "minormax":
                        return kiDrainMultiMinOrMax;
                }
            case "strain":
                switch (type1.toLowerCase()) {
                    case "flat":
                        return strainMultiFlat;
                    case "perlevel":
                        return strainMultiPerLevel;
                    case "minormax":
                        return strainMultiMinOrMax;
                }
            case "healthrequirement":
                switch (type1.toLowerCase()) {
                    case "flat":
                        return healthRequirementMultiFlat;
                    case "perlevel":
                        return healthRequirementMultiPerLevel;
                    case "minormax":
                        return healthRequirementMultiMinOrMax;
                }

        }
        return 1.0f;
    }

    public void setMulti(String type, String type1, float value) {
        switch (type.toLowerCase()) {
            case "attribute":
                switch (type1.toLowerCase()) {
                    case "flat":
                        attributeMultiFlat = value;
                    case "perlevel":
                        attributeMultiPerLevel = value;
                    case "minormax":
                        attributeMultiMinOrMax = value;
                }
            case "kidrain":
                switch (type1.toLowerCase()) {
                    case "flat":
                        kiDrainMultiFlat = value;
                    case "perlevel":
                        kiDrainMultiPerLevel = value;
                    case "minormax":
                        kiDrainMultiMinOrMax = value;
                }
            case "strain":
                switch (type1.toLowerCase()) {
                    case "flat":
                        strainMultiFlat = value;
                    case "perlevel":
                        strainMultiPerLevel = value;
                    case "minormax":
                        strainMultiMinOrMax = value;
                }
            case "healthrequirement":
                switch (type1.toLowerCase()) {
                    case "flat":
                        healthRequirementMultiFlat = value;
                    case "perlevel":
                        healthRequirementMultiPerLevel = value;
                    case "minormax":
                        healthRequirementMultiMinOrMax = value;
                }

        }
    }

    public float getGain(String eventType, String type) {
        switch (eventType.toLowerCase()) {
            case "update":
                switch (type.toLowerCase()) {
                    case "gain":
                        return updateGain;
                    case "flat":
                        return updateMindBonusFlat;
                    case "permind":
                        return updateMindBonusPerMind;
                    case "max":
                        return updateMindBonusMax;
                }
            case "attack":
                switch (type.toLowerCase()) {
                    case "gain":
                        return attackGain;
                    case "flat":
                        return attackMindBonusFlat;
                    case "permind":
                        return attackMindBonusPerMind;
                    case "max":
                        return attackMindBonusMax;
                }
            case "damaged":
                switch (type.toLowerCase()) {
                    case "gain":
                        return damagedGain;
                    case "flat":
                        return damagedMindBonusFlat;
                    case "permind":
                        return damagedMindBonusPerMind;
                    case "max":
                        return damagedMindBonusMax;
                }
            case "fireki":
                switch (type.toLowerCase()) {
                    case "gain":
                        return fireKiGain;
                    case "flat":
                        return fireKiMindBonusFlat;
                    case "permind":
                        return fireKiMindBonusPerMind;
                    case "max":
                        return fireKiMindBonusMax;
                }

        }
        return 1.0f;
    }

    public void setGain(String eventType, String type, float value) {
        switch (eventType.toLowerCase()) {
            case "update":
                switch (type.toLowerCase()) {
                    case "gain":
                        updateGain = value;
                        break;
                    case "flat":
                        updateMindBonusFlat = value;
                        break;
                    case "permind":
                        updateMindBonusPerMind = value;
                        break;
                    case "max":
                        updateMindBonusMax = value;
                        break;
                }
                break;
            case "attack":
                switch (type.toLowerCase()) {
                    case "gain":
                        attackGain = value;
                        break;
                    case "flat":
                        attackMindBonusFlat = value;
                        break;
                    case "permind":
                        attackMindBonusPerMind = value;
                        break;
                    case "max":
                        attackMindBonusMax = value;
                        break;
                }
                break;
            case "damaged":
                switch (type.toLowerCase()) {
                    case "gain":
                        damagedGain = value;
                        break;
                    case "flat":
                        damagedMindBonusFlat = value;
                        break;
                    case "permind":
                        damagedMindBonusPerMind = value;
                        break;
                    case "max":
                        damagedMindBonusMax = value;
                        break;
                }
                break;
            case "fireki":
                switch (type.toLowerCase()) {
                    case "gain":
                        fireKiGain = value;
                        break;
                    case "flat":
                        fireKiMindBonusFlat = value;
                        break;
                    case "permind":
                        fireKiMindBonusPerMind = value;
                        break;
                    case "max":
                        fireKiMindBonusMax = value;
                        break;
                }
                break;

        }

    }

    public float getMaxLevel() {
        return maxLevel;
    }

    public void setMaxLevel(float value) {
        maxLevel = value;
    }

    public float getInstantTransformationUnlockLevel() {
        return instantTransformationUnlockLevel;
    }

    public void setInstantTransformationUnlockLevel(float value) {
        this.instantTransformationUnlockLevel = value;
    }

    public float calculateMulti(String type, float playerLevel) {
        float flat = getMulti(type, "flat");
        float perLevel = getMulti(type, "perlevel");
        float minOrMax = getMulti(type, "minormax");
        float value = perLevel * playerLevel + flat;

        boolean downwards = perLevel < 0.0;
        if (downwards ? value < minOrMax : value > minOrMax)
            value = minOrMax;

        return value;
    }

    public double calculateGainMindMulti(String type, int playerMind) {
        double max = getGain(type, "max");
        if (max <= 0.0)
            return max;

        double flat = getGain(type, "flat");
        double perMind = getGain(type, "permind");
        double multi = playerMind * perMind + flat;

        return multi > max ? max : multi;
    }

    public void readFromNBT(NBTTagCompound compound) {
        NBTTagCompound formMastery = compound.getCompoundTag("formMastery");

        maxLevel = formMastery.getInteger("maxLevel");
        instantTransformationUnlockLevel = formMastery.getFloat("instantTransformationUnlockLevel");

        NBTTagCompound attributeMulti = formMastery.getCompoundTag("attributeMulti");
        attributeMultiFlat = attributeMulti.getFloat("flat");
        attributeMultiPerLevel = attributeMulti.getFloat("perLevel");
        attributeMultiMinOrMax = attributeMulti.getFloat("max");

        NBTTagCompound kiDrainMulti = formMastery.getCompoundTag("kiDrainMulti");
        kiDrainMultiFlat = kiDrainMulti.getFloat("flat");
        kiDrainMultiPerLevel = kiDrainMulti.getFloat("perLevel");
        kiDrainMultiMinOrMax = kiDrainMulti.getFloat("minOrMax");

        NBTTagCompound strainMulti = formMastery.getCompoundTag("strainMulti");
        strainMultiFlat = strainMulti.getFloat("flat");
        strainMultiPerLevel = strainMulti.getFloat("perLevel");
        strainMultiMinOrMax = strainMulti.getFloat("minOrMax");

        NBTTagCompound healthRequirementMulti = formMastery.getCompoundTag("healthRequirementMulti");
        healthRequirementMultiFlat = healthRequirementMulti.getFloat("flat");
        healthRequirementMultiPerLevel = healthRequirementMulti.getFloat("perLevel");
        healthRequirementMultiMinOrMax = healthRequirementMulti.getFloat("minOrMax");

        NBTTagCompound update = formMastery.getCompoundTag("update");
        updateGain = update.getFloat("gain");
        updateMindBonusFlat = update.getFloat("flat");
        updateMindBonusPerMind = update.getFloat("perMind");
        updateMindBonusMax = update.getFloat("max");

        NBTTagCompound attack = formMastery.getCompoundTag("attack");
        attackGain = attack.getFloat("gain");
        attackMindBonusFlat = attack.getFloat("flat");
        attackMindBonusPerMind = attack.getFloat("perMind");
        attackMindBonusMax = attack.getFloat("max");

        NBTTagCompound damaged = formMastery.getCompoundTag("damaged");
        damagedGain = damaged.getFloat("gain");
        damagedMindBonusFlat = damaged.getFloat("flat");
        damagedMindBonusPerMind = damaged.getFloat("perMind");
        damagedMindBonusMax = damaged.getFloat("max");

        NBTTagCompound fireKi = formMastery.getCompoundTag("fireKi");
        fireKiGain = fireKi.getFloat("gain");
        fireKiMindBonusFlat = fireKi.getFloat("flat");
        fireKiMindBonusPerMind = fireKi.getFloat("perMind");
        fireKiMindBonusMax = fireKi.getFloat("max");
    }

    public NBTTagCompound writeToNBT(NBTTagCompound compound) {
        NBTTagCompound formMastery = new NBTTagCompound();
        compound.setTag("formMastery", formMastery);

        formMastery.setFloat("maxLevel", maxLevel);
        formMastery.setFloat("instantTransformationUnlockLevel", instantTransformationUnlockLevel);

        NBTTagCompound attributeMulti = new NBTTagCompound();
        formMastery.setTag("attributeMulti", attributeMulti);
        attributeMulti.setFloat("flat", attributeMultiFlat);
        attributeMulti.setFloat("perLevel", attributeMultiPerLevel);
        attributeMulti.setFloat("max", attributeMultiMinOrMax);

        NBTTagCompound kiDrainMulti = new NBTTagCompound();
        formMastery.setTag("kiDrainMulti", kiDrainMulti);
        kiDrainMulti.setFloat("flat", kiDrainMultiFlat);
        kiDrainMulti.setFloat("perLevel", kiDrainMultiPerLevel);
        kiDrainMulti.setFloat("minOrMax", kiDrainMultiMinOrMax);

        NBTTagCompound strainMulti = new NBTTagCompound();
        formMastery.setTag("strainMulti", strainMulti);
        strainMulti.setFloat("flat", strainMultiFlat);
        strainMulti.setFloat("perLevel", strainMultiPerLevel);
        strainMulti.setFloat("minOrMax", strainMultiMinOrMax);

        NBTTagCompound healthRequirementMulti = new NBTTagCompound();
        formMastery.setTag("healthRequirementMulti", healthRequirementMulti);
        healthRequirementMulti.setFloat("flat", healthRequirementMultiFlat);
        healthRequirementMulti.setFloat("perLevel", healthRequirementMultiPerLevel);
        healthRequirementMulti.setFloat("minOrMax", healthRequirementMultiMinOrMax);

        NBTTagCompound update = new NBTTagCompound();
        formMastery.setTag("update", update);
        update.setFloat("gain", updateGain);
        update.setFloat("flat", updateMindBonusFlat);
        update.setFloat("perMind", updateMindBonusPerMind);
        update.setFloat("max", updateMindBonusMax);

        NBTTagCompound attack = new NBTTagCompound();
        formMastery.setTag("attack", attack);
        attack.setFloat("gain", attackGain);
        attack.setFloat("flat", attackMindBonusFlat);
        attack.setFloat("perMind", attackMindBonusPerMind);
        attack.setFloat("max", attackMindBonusMax);

        NBTTagCompound damaged = new NBTTagCompound();
        formMastery.setTag("damaged", damaged);
        damaged.setFloat("gain", damagedGain);
        damaged.setFloat("flat", damagedMindBonusFlat);
        damaged.setFloat("perMind", damagedMindBonusPerMind);
        damaged.setFloat("max", damagedMindBonusMax);

        NBTTagCompound fireKi = new NBTTagCompound();
        formMastery.setTag("fireKi", fireKi);
        fireKi.setFloat("gain", fireKiGain);
        fireKi.setFloat("flat", fireKiMindBonusFlat);
        fireKi.setFloat("perMind", fireKiMindBonusPerMind);
        fireKi.setFloat("max", fireKiMindBonusMax);
        return formMastery;
    }

    public IFormMastery save() {
        if (parent != null)
            parent.save();
        return this;
    }
}
