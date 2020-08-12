package com.example.Practice_Project.OnBoarding;

import javax.persistence.Entity;
import javax.persistence.Id;

@Entity
public class User {

    @Id
    private String mobileNo;
    private String name;
    private String passCode;
    private String accountNo;

    public String getAccountNo() {
        return accountNo;
    }

    public void setAccountNo(String accountNo) {
        this.accountNo = accountNo;
    }

    public Integer getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Integer accountBalance) {
        this.accountBalance = accountBalance;
    }

    private Integer accountBalance;

    public User(){

    }
    public User(String mobileNo, String name, String passCode, String accountNo, Integer accountBalance) {
        this.mobileNo = mobileNo;
        this.name = name;
        this.passCode = passCode;
        this.accountNo = accountNo;
        this.accountBalance = accountBalance;
    }

    public String getMobileNo() {
        return mobileNo;
    }

    public void setMobileNo(String mobileNo) {
        this.mobileNo = mobileNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPassCode() {
        return passCode;
    }

    public void setPassCode(String passCode) {
        this.passCode = passCode;
    }
}
