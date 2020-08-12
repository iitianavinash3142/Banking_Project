package com.example.Practice_Project.OnBoarding;

import javax.persistence.*;

@Entity
public class Transaction {

    @Id
    @GeneratedValue(strategy= GenerationType.AUTO)
    private Integer id;

    private String description;
    private Integer amount;
    private String status;
    private Integer accountBalance;

    @ManyToOne
    private User user;


    public Transaction(){

    }

    public Transaction(String description, String status, Integer amount, Integer accountBalance, String mobileNo){
        super();
        this.description = description;
        this.status = status;
        this.amount = amount;
        this.accountBalance = accountBalance;
        this.user = new User(mobileNo,"","","",0);
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Integer getAmount() {
        return amount;
    }

    public void setAmount(Integer amount) {
        this.amount = amount;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Integer getAccountBalance() {
        return accountBalance;
    }

    public void setAccountBalance(Integer accountBalance) {
        this.accountBalance = accountBalance;
    }
}
