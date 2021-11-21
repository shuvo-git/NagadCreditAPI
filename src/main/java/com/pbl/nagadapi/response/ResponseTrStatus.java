package com.pbl.nagadapi.response;

public class ResponseTrStatus
{
    public static final String REF_NOT_FOUND="REF_NOT_FOUND";
    public static final String FT_NOT_FOUND="NULL";

    private String ftId;
    private String refId;
    private String creditAccount;
    private double creditAmount;

    public ResponseTrStatus(){}
    public ResponseTrStatus(String ftId,String refId, String creditAccount, double creditAmount){
        this.ftId = ftId;
        this.refId = refId;
        this.creditAccount = creditAccount;
        this.creditAmount = creditAmount;
    }

    public String getFtId() {
        return ftId;
    }

    public void setFtId(String ftId) {
        this.ftId = ftId;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getCreditAccount() {
        return creditAccount;
    }

    public void setCreditAccount(String creditAccount) {
        this.creditAccount = creditAccount;
    }

    public double getCreditAmount() {
        return creditAmount;
    }

    public void setCreditAmount(double creditAmount) {
        this.creditAmount = creditAmount;
    }
}
