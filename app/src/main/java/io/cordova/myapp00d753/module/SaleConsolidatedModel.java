package io.cordova.myapp00d753.module;

/* renamed from: io.cordova.myapp00d753.module.SaleConsolidatedModel */
public class SaleConsolidatedModel {
    String cyAchievement;
    String cySold;
    String cyTarget;
    String growth;

    /* renamed from: ly */
    String f423ly;
    String lyAchievement;
    String lySold;
    String lyTarget;
    String name;
    String year;

    public SaleConsolidatedModel(String name2, String cyTarget2, String cySold2, String cyAchievement2, String lyTarget2, String lySold2, String lyAchievement2, String growth2, String year2, String ly) {
        this.name = name2;
        this.cyTarget = cyTarget2;
        this.cySold = cySold2;
        this.cyAchievement = cyAchievement2;
        this.lyTarget = lyTarget2;
        this.lySold = lySold2;
        this.lyAchievement = lyAchievement2;
        this.growth = growth2;
        this.year = year2;
        this.f423ly = ly;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name2) {
        this.name = name2;
    }

    public String getCyTarget() {
        return this.cyTarget;
    }

    public void setCyTarget(String cyTarget2) {
        this.cyTarget = cyTarget2;
    }

    public String getCySold() {
        return this.cySold;
    }

    public void setCySold(String cySold2) {
        this.cySold = cySold2;
    }

    public String getCyAchievement() {
        return this.cyAchievement;
    }

    public void setCyAchievement(String cyAchievement2) {
        this.cyAchievement = cyAchievement2;
    }

    public String getLyTarget() {
        return this.lyTarget;
    }

    public void setLyTarget(String lyTarget2) {
        this.lyTarget = lyTarget2;
    }

    public String getLySold() {
        return this.lySold;
    }

    public void setLySold(String lySold2) {
        this.lySold = lySold2;
    }

    public String getLyAchievement() {
        return this.lyAchievement;
    }

    public void setLyAchievement(String lyAchievement2) {
        this.lyAchievement = lyAchievement2;
    }

    public String getGrowth() {
        return this.growth;
    }

    public void setGrowth(String growth2) {
        this.growth = growth2;
    }

    public String getYear() {
        return this.year;
    }

    public void setYear(String year2) {
        this.year = year2;
    }

    public String getLy() {
        return this.f423ly;
    }

    public void setLy(String ly) {
        this.f423ly = ly;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof SaleConsolidatedModel)) {
            return false;
        }
        return ((SaleConsolidatedModel) obj).name.equals(this.name);
    }

    public int hashCode() {
        return this.name.hashCode();
    }
}
