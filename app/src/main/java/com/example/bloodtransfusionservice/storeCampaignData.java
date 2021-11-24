package com.example.bloodtransfusionservice;

import java.util.Comparator;

public class storeCampaignData {
    String campaignId;
    String organizerName;
    String contactPersonName;
    String nic;
    String dateOfCamp;
    String district;
    String location;
    String address;
    String phone;
    String status;
    String latitude;
    String longitude;

    public storeCampaignData(String campaignId, String organizerName, String nic, String dateOfCamp, String phone, String district, String location, String address, String status, String latitude, String longitude) {
        this.campaignId = campaignId;
        this.organizerName = organizerName;
        this.dateOfCamp = dateOfCamp;
        this.nic = nic;
        this.phone = phone;
        this.district = district;
        this.location = location;
        this.status = status;
        this.latitude = latitude;
        this.longitude = longitude;
        this.address = address;
    }

    public storeCampaignData() {
    }

    public String getCampaignId() {
        return campaignId;
    }

    public void setCampaignId(String campaignId) {
        this.campaignId = campaignId;
    }

    public String getOrganizerName() {
        return organizerName;
    }

    public void setOrganizerName(String organizerName) {
        this.organizerName = organizerName;
    }

    public String getContactPersonName() {
        return contactPersonName;
    }

    public void setContactPersonName(String contactPersonName) {
        this.contactPersonName = contactPersonName;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getDateOfCamp() {
        return dateOfCamp;
    }

    public void setDateOfCamp(String dateOfCamp) {
        this.dateOfCamp = dateOfCamp;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public static Comparator<storeCampaignData> dateComparator = new Comparator<storeCampaignData>() {
        @Override
        public int compare(storeCampaignData o1, storeCampaignData o2) {
            return o1.getDateOfCamp().compareToIgnoreCase(o2.getDateOfCamp());
        }
    };
}
