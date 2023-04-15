package org.example.order;

public enum Ingredients {
    BULKA_FLUORESCENT("61c0c5a71d1f82001bdaaa6d", "Флюоресцентная булка R2-D3"),
    MEAT_SHELLFISH("61c0c5a71d1f82001bdaaa6f", "Мясо бессмертных моллюсков Protostomia"),
    RINGS_MINERAL("61c0c5a71d1f82001bdaaa76", "Хрустящие минеральные кольца"),
    CHEESE_MOLD("61c0c5a71d1f82001bdaaa7a", "Сыр с астероидной плесенью"),
    SAUSE_SPICY("61c0c5a71d1f82001bdaaa72", "Соус Spicy-X");

    private final String heshId;
    private final String name;

    Ingredients(String heshId, String name) {
        this.heshId = heshId;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public String getHeshId() {
        return heshId;
    }
}
