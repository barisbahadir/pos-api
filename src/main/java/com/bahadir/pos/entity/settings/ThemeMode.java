package com.bahadir.pos.entity.settings;

public enum ThemeMode {
    LIGHT("light"),
    DARK("dark");

    private final String value;

    ThemeMode(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
