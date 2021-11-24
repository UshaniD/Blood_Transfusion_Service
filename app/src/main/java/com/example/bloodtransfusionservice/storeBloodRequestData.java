package com.example.bloodtransfusionservice;

public class storeBloodRequestData {
    String requestId;
    String requesterName;
    String patientName;
    String nic;
    String bloodType;
    String district;
    String location;
    String address;
    String phone;
    String status;
    String date;

    public storeBloodRequestData(String requestId, String requesterName, String patientName, String nic, String bloodType, String district, String location, String address, String phone, String status, String date) {
        this.requestId = requestId;
        this.requesterName = requesterName;
        this.patientName = patientName;
        this.nic = nic;
        this.bloodType = bloodType;
        this.district = district;
        this.location = location;
        this.phone = phone;
        this.status = status;
        this.date = date;
        this.address = address;
    }

    public storeBloodRequestData() {
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public String getRequesterName() {
        return requesterName;
    }

    public void setRequesterName(String requesterName) {
        this.requesterName = requesterName;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
    }

    public String getBloodType() {
        return bloodType;
    }

    public void setBloodType(String bloodType) {
        this.bloodType = bloodType;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
