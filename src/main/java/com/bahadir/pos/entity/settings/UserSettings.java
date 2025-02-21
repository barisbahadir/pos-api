package com.bahadir.pos.entity.settings;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserSettings {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private SystemLanguage systemLang;

    @Enumerated(EnumType.STRING)
    private ThemeMode themeMode;

    @Enumerated(EnumType.STRING)
    private ThemeLayout themeLayout;

    @Enumerated(EnumType.STRING)
    private ThemeColorPresets themeColorPresets;

    private boolean themeStretch;
    private boolean breadCrumb;
    private boolean multiTab;
    private boolean darkSidebar;
    private String fontFamily;
    private int fontSize;

//    User classi icin
//    @OneToOne(cascade = CascadeType.ALL, orphanRemoval = true)
//    @JoinColumn(name = "user_settings_id")
//    private UserSettings userSettings;
}
