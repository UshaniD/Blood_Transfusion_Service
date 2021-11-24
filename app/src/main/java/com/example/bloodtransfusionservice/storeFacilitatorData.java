package com.example.bloodtransfusionservice;

public class storeFacilitatorData {
    String fullname;
    String email;
    String nic;
    String empid;
    String district;
    String gender;
    String dob;
    String mobile;
    String password;
    String resetpass;
    String location;
    String address;

    public storeFacilitatorData(String mobile, String fullname, String email, String nic, String password, String empid, String district, String dob, String gender, String location, String address) {
        this.mobile = mobile;
        this.password = password;
        this.fullname = fullname;
        this.email = email;
        this.nic = nic;
        this.district = district;
        this.empid = empid;
        this.dob = dob;
        this.gender = gender;
        this.location = location;
        this.address = address;
    }

    public storeFacilitatorData() {
    }

    public storeFacilitatorData(String password) {
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

    public String getEmpid() {
        return empid;
    }

    public void setEmpid(String empid) {
        this.empid = empid;
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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
