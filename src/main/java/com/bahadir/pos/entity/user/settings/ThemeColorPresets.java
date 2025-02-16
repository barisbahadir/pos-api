package com.bahadir.pos.entity.user.settings;

public enum ThemeColorPresets {
    DEFAULT("default"),
    CYAN("cyan"),
    PURPLE("purple"),
    BLUE("blue"),
    ORANGE("orange"),
    RED("red");

    private final String value;

    ThemeColorPresets(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
