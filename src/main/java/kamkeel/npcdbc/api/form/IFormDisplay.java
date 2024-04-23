package kamkeel.npcdbc.api.form;

/**
 * This interface is heavily based on how DBC calculates its form masteries. Please check any race's form_mastery.cfg config to
 * get a better understanding on how this interface functions
 */
public interface IFormDisplay {

    String getHairCode();

    /**
     * @param hairCode The hair code to set transformation's hair to, usually gotten from the JinGames Hair Salon
     */
    void setHairCode(String hairCode);

    /**
     * @param type Legal types: aura, hair, eye, bodycm, body1, body2, body3
     * @return Decimal color of type
     */
    int getColor(String type);

    /**
     * @param type  Legal types: aura, hair, eye, bodycm, body1, body2, body3
     * @param color Decimal color to set type as
     */
    void setColor(String type, int color);

    /**
     * @param type Legal types: "base", "ssj", "ssj2", "ssj3", "ssj4", "oozaru", "" for no type
     */
    void setHairType(String type);

    int getNameColor();

    /**
     * @param type Legal types: "base", "ssj", "ssj2", "ssj3", "ssj4", "oozaru", "" for no type
     */
    String getHairType(String type);

    /**
     * @param type Legal types: aura, hair, eye, bodycm, body1, body2, body3
     * @return True if form has the "hasTypeColor" field to true,
     * useful for Namekians and Arcosians multi body color setting
     */
    boolean hasColor(String type);


    /**
     * @return form's size, default is 1.0f of player's current size
     */
    float getSize();

    /**
     * @param size size to set form to. 2.0f sets the player to 2x their normal size. Max: 50.0
     */
    void setSize(float size);

    boolean hasSize();

    /**
     * @param type Legal: firstform, secondform, thirdform, finalform, ultimatecooler
     */
    void setBodyType(String type);

    int getAuraColor();

    void setAuraColor(int auraColor);

    String getBodyType();

    public boolean hasMask();

    void hasMask(boolean hasMask);

    /**
     * Saves CustomForm with the New Form Display Modifications
     *
     * @return IFormDisplay self object
     */
    IFormDisplay save();
}