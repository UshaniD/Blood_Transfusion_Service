package com.example.bloodtransfusionservice;

public class storeData {
    String dateLastDonated;
    String fullname;
    String email;
    String nic;
    String bloodGroup;
    String district;
    String gender;
    String dob;
    String mobile;
    String password;
    String resetpass;
    String location;
    String address;
    String noOfDonations;
    String userStatus;
    String diseases;
    String diseasesList;

    public storeData(String mobile, String fullname, String email, String nic, String password, String bloodGroup, String district, String dateLastDonated, String dob, String gender, String location, String address) {
        this.mobile = mobile;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.nic = nic;
        this.bloodGroup = bloodGroup;
        this.district = district;
        this.dateLastDonated = dateLastDonated;
        this.dob = dob;
        this.gender = gender;
        this.location = location;
        this.address = address;
    }

    public storeData(String mobile, String fullname, String email, String nic, String password, String bloodGroup, String district, String dateLastDonated, String dob, String gender, String location, String address, String noOfDonations, String userStatus, String diseases, String diseasesList) {
        this.mobile = mobile;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.nic = nic;
        this.bloodGroup = bloodGroup;
        this.district = district;
        this.dateLastDonated = dateLastDonated;
        this.dob = dob;
        this.gender = gender;
        this.location = location;
        this.noOfDonations = noOfDonations;
        this.userStatus = userStatus;
        this.address = address;
        this.diseases = diseases;
        this.diseasesList = diseasesList;
    }

    public String getDiseases() {
        return diseases;
    }

    public void setDiseases(String diseases) {
        this.diseases = diseases;
    }

    public String getDiseasesList() {
        return diseasesList;
    }

    public void setDiseasesList(String diseasesList) {
        this.diseasesList = diseasesList;
    }

    public storeData(String fullname, String email, String nic, String password, String bloodGroup, String district, String dob, String gender, String location) {
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.nic = nic;
        this.bloodGroup = bloodGroup;
        this.district = district;
        this.dateLastDonated = dateLastDonated;
        this.dob = dob;
        this.gender = gender;
        this.location = location;

    }

    public storeData() {

    }

    public storeData(String password) {
        this.password = password;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getNic() {
        return nic;
    }

    public void setNic(String nic) {
        this.nic = nic;
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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getDob() {
        return dob;
    }

    public void setDob(String dob) {
        this.dob = dob;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getResetpass() {
        return resetpass;
    }

    public void setResetpass(String resetpass) {
        this.resetpass = resetpass;
    }

    public String getDateLastDonated() {
        return dateLastDonated;
    }

    public void setDateLastDonated(String dateLastDonated) {
        this.dateLastDonated = dateLastDonated;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getNoOfDonations() {
        return noOfDonations;
    }

    public void setNoOfDonations(String noOfDonations) {
        this.noOfDonations = noOfDonations;
    }

    public String getUserStatus() {
        return userStatus;
    }

    public void setUserStatus(String userStatus) {
        this.userStatus = userStatus;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
