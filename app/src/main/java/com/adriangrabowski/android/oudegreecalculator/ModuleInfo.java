package com.adriangrabowski.android.oudegreecalculator;

public class ModuleInfo {
    private String code;
    private String name;
    private int level;
    private int credits;

    public ModuleInfo(String code, String name, int level, int credits) {
        this.code = code;
        this.name = name;
        this.level = level;
        this.credits = credits;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getCredits() {
        return credits;
    }

    public void setCredits(int credits) {
        this.credits = credits;
    }
}
