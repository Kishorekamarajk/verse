package com.tecverse.app.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

import java.util.List;

public class TicketRegistrationRequest {
    @NotBlank @Size(max=150)
    private String officialName;
    @NotBlank @Email @Size(max=150)
    private String email;
    @NotBlank @Pattern(regexp="^\\+?[0-9]{10,15}$")
    private String phone;
    @NotBlank
    private String category;
    private String academiaType;
    private String collegeName;
    private String collegeDistrict;
    private String collegeState;
    private String universityName;
    private String academiaRole;
    private String registerNumber;

    private String centralMinistry;
    private String stateName;
    private String stateDepartment;

    private String organizationName;
    private String organizationLocation;
    private String designation;
    private String industryOrStartup;

    @NotBlank
    private String citizenshipStatus;
    private String passportNumber;
    private String passportValidUntil;
    private String passportName;

    private List<String> attendanceDays;

    public String getOfficialName(){return officialName;} public void setOfficialName(String v){this.officialName=v;}
    public String getEmail(){return email;} public void setEmail(String v){this.email=v;}
    public String getPhone(){return phone;} public void setPhone(String v){this.phone=v;}
    public String getCategory(){return category;} public void setCategory(String v){this.category=v;}
    public String getAcademiaType(){return academiaType;} public void setAcademiaType(String v){this.academiaType=v;}
    public String getCollegeName(){return collegeName;} public void setCollegeName(String v){this.collegeName=v;}
    public String getCollegeDistrict(){return collegeDistrict;} public void setCollegeDistrict(String v){this.collegeDistrict=v;}
    public String getCollegeState(){return collegeState;} public void setCollegeState(String v){this.collegeState=v;}
    public String getUniversityName(){return universityName;} public void setUniversityName(String v){this.universityName=v;}
    public String getAcademiaRole(){return academiaRole;} public void setAcademiaRole(String v){this.academiaRole=v;}
    public String getRegisterNumber(){return registerNumber;} public void setRegisterNumber(String v){this.registerNumber=v;}
    public String getCentralMinistry(){return centralMinistry;} public void setCentralMinistry(String v){this.centralMinistry=v;}
    public String getStateName(){return stateName;} public void setStateName(String v){this.stateName=v;}
    public String getStateDepartment(){return stateDepartment;} public void setStateDepartment(String v){this.stateDepartment=v;}
    public String getOrganizationName(){return organizationName;} public void setOrganizationName(String v){this.organizationName=v;}
    public String getOrganizationLocation(){return organizationLocation;} public void setOrganizationLocation(String v){this.organizationLocation=v;}
    public String getDesignation(){return designation;} public void setDesignation(String v){this.designation=v;}
    public String getIndustryOrStartup(){return industryOrStartup;} public void setIndustryOrStartup(String v){this.industryOrStartup=v;}
    public String getCitizenshipStatus(){return citizenshipStatus;} public void setCitizenshipStatus(String v){this.citizenshipStatus=v;}
    public String getPassportNumber(){return passportNumber;} public void setPassportNumber(String v){this.passportNumber=v;}
    public String getPassportValidUntil(){return passportValidUntil;} public void setPassportValidUntil(String v){this.passportValidUntil=v;}
    public String getPassportName(){return passportName;} public void setPassportName(String v){this.passportName=v;}
    public List<String> getAttendanceDays(){return attendanceDays;} public void setAttendanceDays(List<String> v){this.attendanceDays=v;}
}
