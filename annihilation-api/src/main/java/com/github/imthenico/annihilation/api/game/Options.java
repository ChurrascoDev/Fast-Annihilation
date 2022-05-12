package com.github.imthenico.annihilation.api.game;

public class Options {

    private int nexusDamage;
    private boolean nexusInvulnerability;
    private int nexusDamageMultiplier;
    private boolean selectRandomMap;

    public Options(int nexusDamage, boolean nexusInvulnerability, int nexusDamageMultiplier, boolean selectRandomMap) {
        this.nexusDamage = nexusDamage;
        this.nexusInvulnerability = nexusInvulnerability;
        this.nexusDamageMultiplier = nexusDamageMultiplier;
        this.selectRandomMap = selectRandomMap;
    }

    public void setSelectRandomMap(boolean selectRandomMap) {
        this.selectRandomMap = selectRandomMap;
    }

    public boolean isSelectRandomMap() {
        return selectRandomMap;
    }

    public void setNexusDamage(int nexusDamage) {
        this.nexusDamage = nexusDamage;
    }

    public void setNexusInvulnerability(boolean nexusInvulnerability) {
        this.nexusInvulnerability = nexusInvulnerability;
    }

    public void setNexusDamageMultiplier(int nexusDamageMultiplier) {
        if (nexusDamageMultiplier > 0) {
           this.nexusDamageMultiplier = nexusDamageMultiplier;
        }
    }

    public int getNexusDamage() {
        return nexusDamage;
    }

    public boolean areNexusInvulnerable() {
        return nexusInvulnerability;
    }

    public int getNexusDamageMultiplier() {
        return nexusDamageMultiplier;
    }

    public Options copy() {
        return new Options(nexusDamage, nexusInvulnerability, nexusDamageMultiplier, selectRandomMap);
    }

    public static Options defaultOptions() {
        return new Options(1, false, 1, true);
    }
}