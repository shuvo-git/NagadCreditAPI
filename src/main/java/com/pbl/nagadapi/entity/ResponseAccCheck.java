package com.pbl.nagadapi.entity;

import org.springframework.context.annotation.Primary;

import javax.persistence.*;

@Entity
@Table(name = "response_to_nagad")
public class ResponseAccCheck {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private short responseCode;

    @Column
    private String responseMsg;

    @Column
    private  String accountHolderName;

    @Column
    private String accountStatus;



    public ResponseAccCheck(){}

    public ResponseAccCheck(short responseCode, String responseMsg, String accountHolderName, String accountStatus){
        super();
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
        this.accountHolderName = accountHolderName;
        this.accountStatus = accountStatus;

    }

    public ResponseAccCheck(Long id, short responseCode, String responseMsg, String accountHolderName, String accountStatus){
        super();
        this.id = id;
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;
        this.accountHolderName = accountHolderName;
        this.accountStatus = accountStatus;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public short getResponseCode() {
        return responseCode;
    }

    public void setResponseCode(short responseCode) {
        this.responseCode = responseCode;
    }

    public String getResponseMsg() {
        return responseMsg;
    }

    public void setResponseMsg(String responseMsg) {
        this.responseMsg = responseMsg;
    }

    public String getAccountStatus() {
        return accountStatus;
    }

    public String getAccountHolderName() {
        return accountHolderName;
    }

    public void setAccountHolderName(String accountHolderName) {
        this.accountHolderName = accountHolderName;
    }

    public void setAccountStatus(String accountStatus) {
        this.accountStatus = accountStatus;
    }
}
