package kamkeel.npcdbc.client.gui.dbc;

import JinRyuu.DragonBC.common.DBCConfig;
import JinRyuu.JRMCore.*;
import cpw.mods.fml.common.FMLCommonHandler;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;
import kamkeel.npcdbc.LocalizationHelper;
import kamkeel.npcdbc.client.ClientCache;
import kamkeel.npcdbc.client.ColorMode;
import kamkeel.npcdbc.client.gui.dbc.constants.GuiInfo;
import kamkeel.npcdbc.config.ConfigDBCClient;
import kamkeel.npcdbc.data.PlayerDBCInfo;
import kamkeel.npcdbc.data.dbcdata.DBCData;
import kamkeel.npcdbc.data.form.Form;
import kamkeel.npcdbc.mixin.IDBCGuiScreen;
import kamkeel.npcdbc.util.PlayerDataUtil;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiButton;
import net.minecraft.client.gui.GuiLabel;
import net.minecraft.util.ResourceLocation;
import org.lwjgl.opengl.GL11;

import java.util.HashMap;

import static JinRyuu.JRMCore.JRMCoreGuiScreen.kqGW3Z;

@SideOnly(Side.CLIENT)
public class StatSheetGui extends AbstractJRMCGui {

    private static final ResourceLocation icons = new ResourceLocation("jinryuumodscore:icons.png");
    private static final ResourceLocation icons3 = new ResourceLocation("jinryuumodscore:icons3.png");
    private int upgradeCounter;

    private final HashMap<String, GuiLabel> labelMap = new HashMap<>();

    private GuiButton[] buttons = new GuiButton[7];

    public StatSheetGui() {
        super(10);
    }

    protected StatSheetGui(int guiReplacementID){
        super(guiReplacementID);
    }

    @Override
    public void updateScreen(){


        DBCData dbcClient = DBCData.getClient();
        PlayerDBCInfo dataClient = PlayerDataUtil.getClientDBCInfo();


        if(dbcClient == null || dataClient == null) {
            return;
        }
        if(!ConfigDBCClient.EnhancedGui || dbcClient.Accept == 0){
            JRMCoreGuiScreen DBCScreen = new JRMCoreGuiScreen(0);
            ((IDBCGuiScreen) DBCScreen).setGuiIDPostInit(10);
            FMLCommonHandler.instance().showGuiScreen(DBCScreen);
            return;
        }

        String formColor = "";
        String formName;
        Form customForm = dbcClient.getForm();

        boolean isLegendary = dbcClient.containsSE(14);
        boolean isLegendaryEnabled = JRMCoreH.lgndb(dbcClient.Race, dbcClient.State);

        boolean isMajin = dbcClient.containsSE(12);

        if(isMajin && isLegendary && isLegendaryEnabled){
            formColor = "§5";
        }else if(isMajin){
            formColor = "§c";
        }else if(isLegendary && isLegendaryEnabled){
            formColor = "§2";
        }

        boolean isFused = dbcClient.containsSE(10) || dbcClient.containsSE(11);
        if(isFused){
            formColor = "§d";
        }

        if(customForm != null){
            formName = customForm.getMenuName();
            formColor = dataClient.getFormColorCode(customForm);
        }else {
            formName = JRMCoreH.trl("jrmc", JRMCoreH.TransNms[dbcClient.Race][dbcClient.State]);


            boolean ascendedAboveBase = (dbcClient.Race == 4 && dbcClient.State > 4) || dbcClient.State > 0;
            if (formColor.isEmpty() && ascendedAboveBase)
                formColor = "§6";
        }
        formName = formColor + formName;
        formColor = (formColor.equals("§4") ? "" : formColor); //Makes stats pop out when your form color is the same as the default stat color


        boolean isMaxLevel = JRMCoreH.getPlayerLevel(JRMCoreH.PlyrAttrbts) >= JRMCoreH.getPlayerLevel(kqGW3Z(false) * 6);
        dynamicLabels.get("level")
            .updateDisplay("\u00a78"+JRMCoreH.numSep(JRMCoreH.getPlayerLevel(JRMCoreH.PlyrAttrbts)))
            .updateTooltip(isMaxLevel ? JRMCoreH.trl("jrmc", "LevelMax") : JRMCoreH.trl("jrmc", "LevelNext", JRMCoreH.cllr + JRMCoreH.attrLvlNext(JRMCoreH.PlyrAttrbts) + JRMCoreH.cldgy));

        dynamicLabels.get("tp")
            .updateDisplay(JRMCoreH.numSep(dbcClient.TP))
            .updateTooltip(JRMCoreH.cllr + JRMCoreH.numSep(JRMCoreH.attrCst(JRMCoreH.PlyrAttrbts, 0)) + JRMCoreH.cldgy);

//
//        String raceText = String.format("%s: §8%s", JRMCoreH.trl("jrmc", "Race"), JRMCoreH.Races[JRMCoreH.Race]);
//        this.dynamicLabels.add(new JRMCoreLabel(
//            raceText,
//            null,
//            this.guiWidthOffset+6,
//            this.guiHeightOffset+5+index*10+1
//        ));
//
//        this.dynamicLabels.add(new GuiIcon(
//            icons3,
//            guiWidthOffset+5+Minecraft.getMinecraft().fontRenderer.getStringWidth(raceText),
//            guiHeightOffset+2+index*10+1,
//            0,
//            (JRMCoreH.dnsGender(JRMCoreH.dns) < 1 ? 128 : 112),
//            16,
//            16
//        ));
//
//        index++;
//
//
//
//        //@TODO Add proper hovers
//        //@TODO Add proper translation
//        //@TODO Add custom form support
//        this.dynamicLabels.add(new JRMCoreLabel(
//            String.format("%s: §8%s", JRMCoreH.trl("jrmc", "TRState"), formColor+formName),
//            null,
//            this.guiWidthOffset+6,
//            this.guiHeightOffset+5+index*10+1
//        ));
//        index++;
//
//        this.dynamicLabels.add(new JRMCoreLabel(
//            String.format("%s: §8%s", JRMCoreH.trl("jrmc", "Class"), JRMCoreH.trl("jrmc", JRMCoreH.ClassesDBC[JRMCoreH.Class])),
//            JRMCoreH.trl("jrmc", JRMCoreH.ClassesDBCDesc[JRMCoreH.Class]),
//            this.guiWidthOffset+6,
//            this.guiHeightOffset+5+index*10+1,
//            200
//        ));
//        index++;
//
//        this.dynamicLabels.add(new JRMCoreLabel(
//            String.format("%s: §4%s", JRMCoreH.trl("jrmc", "Alignment"), JRMCoreH.algnCur(JRMCoreH.align)),
//            JRMCoreH.trl("jrmc", "AlignmentDesc", JRMCoreH.align+"%"),
//            this.guiWidthOffset+6,
//            this.guiHeightOffset+5+index*10+1
//        ));
//        index++;
//
//        //Line break between race info and stats
//        index++;
//
//
//        this.dynamicLabels.add(new JRMCoreLabel(
//            JRMCoreH.trl("jrmc", "Attributes"),
//            null,
//            this.guiWidthOffset+6,
//            this.guiHeightOffset+5+index*10+1
//        ));
//        index++;
//
//        String[] attrNames = new String[]{
//            "STR", "DEX", "CON", "WIL", "MND", "SPI"
//        };
//
//        int[] statVals = new int[6];
//
//        int upgradeCost = JRMCoreH.attrCst(JRMCoreH.PlyrAttrbts, this.upgradeCounter);
//        boolean canAffordUpgrade = JRMCoreH.curTP >= upgradeCost;
//        for(int i = 0; i < 6; i++){
//            boolean isMaxed = !(JRMCoreGuiScreen.kqGW3Z(isFused) > JRMCoreH.PlyrAttrbts[i]);
//            boolean isButtonEnabled = !isFused && !isMaxed && canAffordUpgrade;
//
//            boolean isSTRDEXWIL = (i < 2 || i == 3);
//
//            String upgradeTooltip = null;
//
//            int yPos = guiHeightOffset+i*10+index*10+1;
//
//            if(isFused){
//                upgradeTooltip = JRMCoreH.trl("dbc", "cantupgradef");
//            }else if(isMaxed){
//                upgradeTooltip = JRMCoreH.trl("jrmc", "AttributeMaxed");
//            }else if(!canAffordUpgrade){
//                upgradeTooltip = JRMCoreH.trl("jrmc", "cantupgrade")+ "\n" + JRMCoreH.trl("jrmc", "RequiredTP", "§4"+JRMCoreH.numSep(upgradeCost));
//            }
//
//            GuiButton button = buttons[i];
//            if(button == null || !this.buttonList.contains(button)){
//                button = new JRMCoreGuiButtonsA3(i, guiWidthOffset + 4, yPos+3, 10, 2, isButtonEnabled);
//                buttons[i] = button;
//                this.buttonList.add(button);
//            }else{
//                button.enabled = isButtonEnabled;
//            }
//            if(upgradeTooltip != null){
//                this.dynamicLabels.add(new JRMCoreLabel(button, upgradeTooltip));
//            }
//
//            int originalStatVal = JRMCoreH.PlyrAttrbts[i];
//            int modifiedStatVal = originalStatVal; //@TODO Replace with multi
//
//            if(isSTRDEXWIL){
//                modifiedStatVal = JRMCoreH.getPlayerAttribute(JRMCoreClient.mc.thePlayer, JRMCoreH.PlyrAttrbts, i, JRMCoreH.State, JRMCoreH.State2, JRMCoreH.Race, JRMCoreH.PlyrSkillX, JRMCoreH.curRelease, JRMCoreH.getArcRsrv(), JRMCoreH.StusEfctsMe(14), JRMCoreH.StusEfctsMe(12), JRMCoreH.StusEfctsMe(5), JRMCoreH.StusEfctsMe(13), JRMCoreH.StusEfctsMe(19), JRMCoreH.StusEfctsMe(20), JRMCoreH.Pwrtyp, JRMCoreH.PlyrSkills, isFused, JRMCoreH.getMajinAbsorption());;
//            }
//            statVals[i] = modifiedStatVal;
//
//            String statDisplay = JRMCoreH.numSep(modifiedStatVal);
//
//            String attributeDesc = JRMCoreH.attrNms(1, i) + ", "+ JRMCoreH.trl("jrmc", JRMCoreH.attrDsc[1][i]);
//
//            if(originalStatVal != modifiedStatVal){
//                attributeDesc = JRMCoreH.trl("jrmc", "Modified") +": §4" + formColor+statDisplay+"\n§8"
//                    + JRMCoreH.trl("jrmc", "Original") +": §4" + JRMCoreH.numSep(originalStatVal)+"\n§8"
//                    +attributeDesc;
//                float multi = (float) modifiedStatVal / originalStatVal;
//                if((JRMCoreH.round(multi, 1) != 1))
//                    statDisplay += " §4x"+JRMCoreH.round(multi, 1);
//            }
//
//            this.dynamicLabels.add(new JRMCoreLabel(
//                String.format("§8%s: §4%s%s", attrNames[i], (isSTRDEXWIL ? formColor : ""), statDisplay),
//                attributeDesc,
//                guiWidthOffset+15,
//                yPos+5
//            ));
//        }
//        index+=6;
//
//        boolean allMaxed = JRMCoreH.acm(JRMCoreH.PlyrAttrbts);
//
//        GuiButton upgradeButton = buttons[6];
//        if(upgradeButton == null){
//            upgradeButton = new JRMCoreGuiButtonsA3(
//                6,
//                guiWidthOffset+7,
//                guiHeightOffset + index*10+1 + 3,
//                10,
//                2,
//                (!allMaxed && !isFused)
//            );
//            buttons[6] = upgradeButton;
//            this.buttonList.add(upgradeButton);
//        }else{
//            upgradeButton.enabled = (!allMaxed && !isFused);
//        }
//
//
//        String upgradeDescription = JRMCoreH.trl("jrmc", "UCnam");
//        int descriptionWidth = this.mc.fontRenderer.getStringWidth(upgradeDescription+" ");
//
//        if (allMaxed) {
//            upgradeDescription += "\n§c" + JRMCoreH.cct(JRMCoreH.trl("jrmc", "AttributeAllMaxed"));
//            descriptionWidth = 150;
//        } else if (upgradeCost == 0 || !canAffordUpgrade) {
//            upgradeDescription += "\n§c" + JRMCoreH.cct(JRMCoreH.trl("jrmc", "cantupgrade"));
//        } else if (isFused) {
//            upgradeDescription += "\n§c" + JRMCoreH.cct(JRMCoreH.trl("dbc", "cantupgradef"));
//        }else if(upgradeCounter > 0){
//            upgradeDescription += ", ";
//        }
//
//        this.dynamicLabels.add(new JRMCoreLabel(
//            " §8UC: " + JRMCoreH.cldb + (upgradeCost <= 0 ? JRMCoreH.trl("jrmc", "LimitReached") : (allMaxed ? JRMCoreH.trl("jrmc", "AttributeAllMaxed") : JRMCoreH.numSep(upgradeCost)+" TP "+(upgradeCounter > 0 ? "x"+JRMCoreH.attributeMultiplier(this.upgradeCounter) : ""))),
//            upgradeDescription,
//            guiWidthOffset+15,
//            guiHeightOffset+5+index*10+1,
//            descriptionWidth
//            )
//        );
//
//        index = 0;
//
//        this.dynamicLabels.add(new JRMCoreLabel(
//           String.format("%s: §8%s%%", JRMCoreH.trl("jrmc", "PowerRelease"), dbcClient.Release),
//           null,
//            guiWidthOffset+133,
//            guiHeightOffset+5+index*10+1
//        ));
//        index++;
//        index++;
//
//
//        this.dynamicLabels.add(new JRMCoreLabel(
//            JRMCoreH.trl("jrmc", "Stats"),
//            null,
//            guiWidthOffset+133,
//            guiHeightOffset+5+index*10+1
//        ));
//        index++;
//
//        //@TODO ADD KI FIST
//        int stat = JRMCoreH.stat(mc.thePlayer, 0, 1, 0, statVals[0], dbcClient.Race, dbcClient.Class, 0);
//        //float inc = JRMCoreH.statInc(JRMCoreH.Pwrtyp, 0, 1, JRMCoreH.Race, JRMCoreH.Class, 0.0F);
//        int curAtr = (int)((double)stat * 0.01D * (double)JRMCoreH.curRelease * (double)JRMCoreH.weightPerc(0));
//        long longValue = (long)curAtr + (int)((double)JRMCoreH.SklLvl(12) * DBCConfig.cnfKFd * (double)statVals[5] * (double)JRMCoreH.curRelease * 0.01D);
//        if (longValue > 2147483647L) {
//            longValue = 2147483647L;
//        }
//
//        this.dynamicLabels.add(new JRMCoreLabel(
//            String.format("§8%s: §4%s", JRMCoreH.trl("jrmc", "mleDB"), formColor+JRMCoreH.numSep(longValue)),
//            "description",
//            guiWidthOffset+133,
//            guiHeightOffset+5+index*10+1
//        ));
//        index++;
//
//        //@TODO ADD KI PROT
//        int kiProtectionVal = (int)((double)JRMCoreH.SklLvl(11) * DBCConfig.cnfKDd * (double)statVals[5] * (double)JRMCoreH.curRelease * 0.01D);
//
//        stat = JRMCoreH.stat(mc.thePlayer, 1, 1, 1, statVals[1], dbcClient.Race, dbcClient.Class, 0);
//        //float inc = JRMCoreH.statInc(JRMCoreH.Pwrtyp, 0, 1, JRMCoreH.Race, JRMCoreH.Class, 0.0F);
//        curAtr = (int)((double)stat * (dbcClient.Release / 100.0F) * (double)JRMCoreH.weightPerc(1));
//        longValue = (long)curAtr + (long)kiProtectionVal;
//        if (longValue > 2147483647L) {
//            longValue = 2147483647L;
//        }
//
//        this.dynamicLabels.add(new JRMCoreLabel(
//            String.format("§8%s: §4%s", JRMCoreH.trl("jrmc", "DefDB"), formColor+JRMCoreH.numSep(longValue)),
//            "description",
//            guiWidthOffset+133,
//            guiHeightOffset+5+index*10+1
//        ));
//        index++;
//
//        this.dynamicLabels.add(new JRMCoreLabel(
//            String.format("§8%s: §4%s", JRMCoreH.trl("jrmc", "Passive"), formColor+JRMCoreH.numSep(longValue*JRMCoreConfig.StatPasDef/100)),
//            "description",
//            guiWidthOffset+138,
//            guiHeightOffset+5+index*10+1
//        ));
//        index++;
//
//        if(ClientCache.hasChargingDex){
//            this.dynamicLabels.add(new JRMCoreLabel(
//                String.format("§8%s: §4%s", "Charging", formColor + JRMCoreH.numSep((long) (longValue * ClientCache.chargingDexValues.get((int) dbcClient.Class) / 100))),
//                "description",
//                guiWidthOffset + 138,
//                guiHeightOffset + 5 + index * 10
//            ));
//            index++;
//        }
//
//        stat = JRMCoreH.stat(mc.thePlayer, 2, 1, 2, statVals[2], dbcClient.Race, dbcClient.Class, 0);
//
//        double dmgReducScaling = JRMCoreH.getPlayerAttribute(JRMCoreClient.mc.thePlayer, JRMCoreH.PlyrAttrbts, 2, JRMCoreH.State, JRMCoreH.State2, JRMCoreH.Race, JRMCoreH.PlyrSkillX, JRMCoreH.curRelease, JRMCoreH.getArcRsrv(), JRMCoreH.StusEfctsMe(14), JRMCoreH.StusEfctsMe(12), JRMCoreH.StusEfctsMe(5), JRMCoreH.StusEfctsMe(13), JRMCoreH.StusEfctsMe(19), JRMCoreH.StusEfctsMe(20), JRMCoreH.Pwrtyp, JRMCoreH.PlyrSkills, isFused, JRMCoreH.getMajinAbsorption());
//        double percentile = (dmgReducScaling > statVals[2] ? dmgReducScaling : statVals[2]) / ((double) statVals[2]);
//        int dmgReduction = (int)((1.0D - 1.0D / percentile) * 100.0D);
//        this.dynamicLabels.add(new JRMCoreLabel(
//            String.format("§8%s: §4%s %s", JRMCoreH.trl("jrmc", "BdDB"), JRMCoreH.numSep(stat), (JRMCoreH.round(percentile, 1) != 1.0D ? "R" + dmgReduction + "%" : "")),
//            "description",
//            guiWidthOffset+133,
//            guiHeightOffset+5+index*10+1
//        ));
//        index++;
//
//        stat = JRMCoreH.stat(mc.thePlayer, 2, 1, 3, statVals[2], dbcClient.Race, dbcClient.Class, 0);
//
//        this.dynamicLabels.add(new JRMCoreLabel(
//            String.format("§8%s: §4%s", JRMCoreH.trl("jrmc", "StDB"), JRMCoreH.numSep(stat)),
//            "description",
//            guiWidthOffset+133,
//            guiHeightOffset+5+index*10+1
//        ));
//        index++;
//
//        stat = JRMCoreH.stat(mc.thePlayer, 3, 1, 4, statVals[3], dbcClient.Race, dbcClient.Class, 0);
//
//        this.dynamicLabels.add(new JRMCoreLabel(
//            String.format("§8%s: §4%s", JRMCoreH.trl("jrmc", "EnPwDB"), formColor+JRMCoreH.numSep((int)((double)stat * 0.01D * (double)JRMCoreH.curRelease))),
//            "description",
//            guiWidthOffset+133,
//            guiHeightOffset+5+index*10+1
//        ));
//        index++;
//
//        stat = JRMCoreH.stat(mc.thePlayer, 5, 1, 5, statVals[5], dbcClient.Race, dbcClient.Class, JRMCoreH.SklLvl_KiBs(1));
//        int statBonus = stat - JRMCoreH.stat(mc.thePlayer, 5, 1, 5, statVals[5], dbcClient.Race, dbcClient.Class, 0);
//
//        this.dynamicLabels.add(new JRMCoreLabel(
//            String.format("§8%s: §4%s", JRMCoreH.trl("jrmc", "EnPlDB"), JRMCoreH.numSep(stat)),
//            "description",
//            guiWidthOffset+133,
//            guiHeightOffset+5+index*10+1
//        ));
//
//        //@TODO ADD RUNNING/FLYING SPEEDS

    }


    @Override
    public void drawScreen(int mouseX, int mouseY, float partialTicks){
        super.drawScreen(mouseX, mouseY, partialTicks);
        drawStatusEffects(this.width/4, guiHeightOffset - 35);
        drawAlignmentBar(guiWidthOffset + 8, guiHeightOffset - 6);
    }

    @Override
    public void initGui(){
        super.initGui();

        if(!ConfigDBCClient.EnhancedGui){
            return;
        }
        addServerButtons();

        //Button to adjust GUI
        String s = "Switch to "+(ConfigDBCClient.EnhancedGui ? "Normal" : "§aEnhanced") +" GUI";
        int i = this.fontRendererObj.getStringWidth(s)+10;
        this.buttonList.add(new JRMCoreGuiButtons00(303030303, (this.width -i)/2, guiHeightOffset - 30, i + 8, 20, s, 0));

        String dark = ConfigDBCClient.DarkMode ? "Light" : "Dark";
        int j = this.fontRendererObj.getStringWidth(dark)+10;
        this.buttonList.add(new JRMCoreGuiButtons00(404040404, guiWidthOffset + 260, guiHeightOffset+5, j + 8, 20, dark, 0));


        //Difficulty button
        GuiInfo.ReferenceIDs ref = GuiInfo.ReferenceIDs.DIFFICULTY;
        String translation = ref.getTranslation();
        int stringWidth = fontRendererObj.getStringWidth(translation)+8;
        this.buttonList.add(new JRMCoreGuiButtons00(ref.getButtonId(), width/2 + 90 - stringWidth / 2, height/2 + 55, stringWidth, 20, translation, 0));

        int index = 0;

        String jrmcID = "jinryuumodscore:";

        dynamicLabels.put("level", new JRMCoreLabel(
            JRMCoreH.trl("jrmc", "Level")+": %s",
            "%s",
            guiWidthOffset+6,
            guiHeightOffset+index*10+6
        ));
        index++;

        dynamicLabels.put("tp", new JRMCoreLabel(
            "%s: %s",
            JRMCoreH.trl("jrmc", "RequiredTP"),
            guiWidthOffset+6,
            guiHeightOffset+index*10+6
        ));
    }

    @Override
    protected void actionPerformed(GuiButton button){
        super.actionPerformed(button);
        int id = button.id;
        if(id == 303030303){
            ConfigDBCClient.EnhancedGui = false;
            ConfigDBCClient.EnhancedGuiProperty.set(false);
        }
        if(id == 404040404){
            ConfigDBCClient.DarkMode = !ConfigDBCClient.DarkMode;
            ConfigDBCClient.DarkModeProperty.set(ConfigDBCClient.DarkMode);
            initGui();
        }
        if(id >= 0 && id <= 5){
            if(!JRMCoreH.isFused()){
                JRMCoreH.Upg((byte) (id+upgradeCounter*6));
            }
        }
        if(id == 6){
            this.upgradeCounter++;
            if(this.upgradeCounter >= 3)
                this.upgradeCounter = 0;
        }

    }

    protected void drawAlignmentBar(int x, int y){
        mc.getTextureManager().bindTexture(icons);
        GL11.glPushMatrix();
        int alignment1;
        int alignment2;

        if(JRMCoreH.Algnmnt_Good(JRMCoreH.align)){
            alignment1 = 654591;
            alignment2 = 6028287;
        } else if (JRMCoreH.Algnmnt_Neut(JRMCoreH.align)) {
            alignment1 = 9127101;
            alignment2 = 11042302;
        } else {
            alignment1 = 16726090;
            alignment2 = 16544131;
        }

        float h2 = (float)(alignment1 >> 16 & 255) / 255.0F;
        float h3 = (float)(alignment1 >> 8 & 255) / 255.0F;
        float h4 = (float)(alignment1 & 255) / 255.0F;
        float h1 = 1.0F;
        GL11.glColor4f(h1 * h2, h1 * h3, h1 * h4, 0.5F);
        this.drawTexturedModalRect(x, y, 8, 174, 241, 7);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
        GL11.glPopMatrix();
        this.drawTexturedModalRect(x-8, y+1, 0, 169, menuImageWidth, 5);
        int max = menuImageWidth - 20;
        if (max < 1) {
            max = 1;
        }

        double maxperc = (double)max * 0.01D;
        int alignmentAdjusted = (int)(maxperc * (double)JRMCoreH.align);
        if (alignmentAdjusted > menuImageWidth) {
            alignmentAdjusted = menuImageWidth;
        }

        h2 = (float)(alignment2 >> 16 & 255) / 255.0F;
        h3 = (float)(alignment2 >> 8 & 255) / 255.0F;
        h4 = (float)(alignment2 & 255) / 255.0F;
        GL11.glColor4f(h1 * h2, h1 * h3, h1 * h4, 1.0F);
        this.drawTexturedModalRect((this.width - 5) / 2 - max / 2 + alignmentAdjusted - 4, guiHeightOffset-9, 0, 182, 11, 13);
        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
    }

    protected void drawStatusEffects(int x, int y){
        JRMCoreClient.bars.showSE(x, y, 0, 0);
    }
}
