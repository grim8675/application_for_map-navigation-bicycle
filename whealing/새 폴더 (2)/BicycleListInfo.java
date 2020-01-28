package com.example.whealingservice.bicyclePage;

import android.os.Parcel;
import android.os.Parcelable;

import java.io.Serializable;

public class BicycleListInfo implements Serializable {

    private String bcyclLendNm;
    private String bcyclLendSe;    // 무인, 유인
    private String rdnmadr;
    private String latitude;
    private String hardness;     //longitude
    private String operOpenHm;  //오픈시간
    private String operCloseHm;
    private String rstde;    // 휴무일
    private String chrgeSe;    //요금구분
    private String bcyclUseCharge;  //요금
    private String bcyclHoldCharge;    //자전거 보유 대수
    private String holderCo;  //거치대수
    private String repairStandYn;    //수리대설치여부
    private String phoneNumber;
    private String insttNm;

    public BicycleListInfo(String bcyclLendNm,
                           String bcyclLendSe,
                           String rdnmadr,
                           String latitude,
                           String hardness,
                           String operOpenHm,
                           String operCloseHm,
                           String rstde,
                           String chrgeSe,
                           String bcyclUseCharge,
                           String bcyclHoldCharge,
                           String holderCo,
                           String repairStandYn,
                           String phoneNumber,
                           String instnm) {
        this.bcyclLendNm = bcyclLendNm;
        this.bcyclLendSe = bcyclLendSe;
        this.rdnmadr = rdnmadr;
        this.latitude = latitude;
        this.hardness = hardness;
        this.operOpenHm = operOpenHm;
        this.operCloseHm = operCloseHm;
        this.rstde = rstde;
        this.chrgeSe = chrgeSe;
        this.bcyclUseCharge = bcyclUseCharge;
        this.bcyclHoldCharge = bcyclHoldCharge;
        this.holderCo = holderCo;
        this.repairStandYn = repairStandYn;
        this.phoneNumber = phoneNumber;
        this.insttNm = instnm;
    }

    public String getBcyclLendNm() {
        return bcyclLendNm;
    }

    public String getinsttNm() {
        return insttNm;
    }

    public void setBcyclLendNm(String bcyclLendNm) {
        this.bcyclLendNm = bcyclLendNm;
    }

    public String getBcyclLendSe() {
        return bcyclLendSe;
    }

    public void setBcyclLendSe(String bcyclLendSe) {
        this.bcyclLendSe = bcyclLendSe;
    }

    public String getRdnmadr() {
        return rdnmadr;
    }

    public void setRdnmadr(String rdnmadr) {
        this.rdnmadr = rdnmadr;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getHardness() {
        return hardness;
    }

    public void setHardness(String hardness) {
        this.hardness = hardness;
    }

    public String getOperOpenHm() {
        return operOpenHm;
    }

    public void setOperOpenHm(String operOpenHm) {
        this.operOpenHm = operOpenHm;
    }

    public String getOperCloseHm() {
        return operCloseHm;
    }

    public void setOperCloseHm(String operCloseHm) {
        this.operCloseHm = operCloseHm;
    }

    public String getRstde() {
        return rstde;
    }

    public void setRstde(String rstde) {
        this.rstde = rstde;
    }

    public String getChrgeSe() {
        return chrgeSe;
    }

    public void setChrgeSe(String chrgeSe) {
        this.chrgeSe = chrgeSe;
    }

    public String getBcyclUseCharge() {
        return bcyclUseCharge;
    }

    public void setBcyclUseCharge(String bcyclUseCharge) {
        this.bcyclUseCharge = bcyclUseCharge;
    }

    public String getBcyclHoldCharge() {
        return bcyclHoldCharge;
    }

    public void setBcyclHoldCharge(String bcyclHoldCharge) {
        this.bcyclHoldCharge = bcyclHoldCharge;
    }

    public String getHolderCo() {
        return holderCo;
    }

    public void setHolderCo(String holderCo) {
        this.holderCo = holderCo;
    }

    public String getRepairStandYn() {
        return repairStandYn;
    }

    public void setRepairStandYn(String repairStandYn) {
        this.repairStandYn = repairStandYn;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

}
