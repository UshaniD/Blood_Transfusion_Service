package com.example.bloodtransfusionservice;

public class storeBloodIssueData {
    String issueId;
    String issuedBy;
    String issuedUnit;
    String bloodGroup;
    String reason;
    String comment;
    String date;
    String district;

    public storeBloodIssueData() {
    }

    public storeBloodIssueData(String issuedUnit, String bloodGroup) {
        this.issuedUnit = issuedUnit;
        this.bloodGroup = bloodGroup;
    }

    public storeBloodIssueData(String date, String district, String bloodGroup, String issuedUnit) {
        this.bloodGroup = bloodGroup;
        this.district = district;
        this.issuedUnit = issuedUnit;
        this.date = date;
    }

    public storeBloodIssueData(String issueId, String issuedBy, String issuedUnit, String bloodGroup, String reason, String comment, String date, String district) {
        this.issueId = issueId;
        this.issuedBy = issuedBy;
        this.issuedUnit = issuedUnit;
        this.bloodGroup = bloodGroup;
        this.comment = comment;
        this.date = date;
        this.district = district;
        this.reason = reason;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getIssueId() {
        return issueId;
    }

    public void setIssueId(String issueId) {
        this.issueId = issueId;
    }

    public String getIssuedBy() {
        return issuedBy;
    }

    public void setIssuedBy(String issuedBy) {
        this.issuedBy = issuedBy;
    }

    public String getIssuedUnit() {
        return issuedUnit;
    }

    public void setIssuedUnit(String issuedUnit) {
        this.issuedUnit = issuedUnit;
    }

    public String getBloodGroup() {
        return bloodGroup;
    }

    public void setBloodGroup(String bloodGroup) {
        this.bloodGroup = bloodGroup;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }


}
