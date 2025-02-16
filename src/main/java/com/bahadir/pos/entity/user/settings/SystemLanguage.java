package com.bahadir.pos.entity.user.settings;

public enum SystemLanguage {
    TR_TR("tr_TR"),
    EN_US("en_US"),
    ZH_CN("zh_CN");

    private final String value;

    SystemLanguage(String value) {
        this.value = value;
    }

    public String getValue() {
        return value;
    }
}
