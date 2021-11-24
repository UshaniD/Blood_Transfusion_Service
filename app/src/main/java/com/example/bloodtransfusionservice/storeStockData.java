package com.example.bloodtransfusionservice;

public class storeStockData {
    String bloodGroup;
    String district;
    String unit;

    public storeStockData(String bloodGroup, String district, String unit) {
        this.bloodGroup = bloodGroup;
        this.district = district;
        this.unit = unit;
    }

    public storeStockData() {
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

}
