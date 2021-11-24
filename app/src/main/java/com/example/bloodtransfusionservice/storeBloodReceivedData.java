package com.example.bloodtransfusionservice;

public class storeBloodReceivedData {
    String id;
    String donorMobile;
    String donorDistrict;
    String bloodGroup;
    String receivedUnits;
    String date;
    String donatedDistrict;
    String updatedBy;

    public storeBloodReceivedData(String id, String donorMobile, String bloodGroup, String receivedUnits, String date, String donatedDistrict, String updatedBy) {
        this.id = id;
        this.donorMobile = donorMobile;
        this.bloodGroup = bloodGroup;
        this.receivedUnits = receivedUnits;
        this.date = date;
        this.donatedDistrict = donatedDistrict;
        this.updatedBy = updatedBy;
    }

    public storeBloodReceivedData() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDonorMobile() {
        return donorMobile;
    }

    public void setDonorMobile(String donorMobile) {
        this.donorMobile = donorMobile;
    }

    public String getDonorDistrict() {
        return donorDistrict;
    }

    public void setDonorDistrict(String donorDistrict) {
        this.donorDistrict = donorDistrict;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getReceivedUnits() {
        return receivedUnits;
    }

    public void setReceivedUnits(String receivedUnits) {
        this.receivedUnits = receivedUnits;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDonatedDistrict() {
        return donatedDistrict;
    }

    public void setDonatedDistrict(String donatedDistrict) {
        this.donatedDistrict = donatedDistrict;
    }

    public String getUpdatedBy() {
        return updatedBy;
    }

    public void setUpdatedBy(String updatedBy) {
        this.updatedBy = updatedBy;
    }
}
