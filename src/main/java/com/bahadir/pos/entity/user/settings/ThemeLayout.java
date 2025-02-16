package com.bahadir.pos.entity.user.settings;

public enum ThemeLayout {
    VERTICAL("vertical"),
    HORIZONTAL("horizontal"),
    MINI("mini");

    private final String value;

    ThemeLayout(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
