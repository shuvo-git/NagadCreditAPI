package com.pbl.nagadapi.entity;

import javax.persistence.*;

@Entity
@Table(name = "response_transaction")
public class ResponseTransaction {

    public static final String COMPLETE="TRAN_COMPLETE";
    public static final String INCOMPLETE="TRAN_INCOMPLETE";
    public static final String DUPLICATE_REF="DUPLICATE_REF_ID";
    public static final String NOT_FOUND="NULL";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true)
    private String refId;

    @Column
    private String ftId;

    @Column
    private short responseCode;

    @Column
    private String responseMsg;

    public ResponseTransaction(){}
    public ResponseTransaction(String refId,String ftId,short responseCode,String responseMsg){
        super();
        this.refId = refId;
        this.ftId = ftId;
        this.responseCode = responseCode;
        this.responseMsg = responseMsg;

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getRefId() {
        return refId;
    }

    public void setRefId(String refId) {
        this.refId = refId;
    }

    public String getFtId() {
        return ftId;
    }

    public void setFtId(String ftId) {
        this.ftId = ftId;
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
}
