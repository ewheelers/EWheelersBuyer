package com.ewheelers.ewheelersbuyer.ModelClass;

public class StatementsModel {
    public static final int REWARD=0;
    public static final int CREDITS=1;
    public static final int COUPONS=2;
//reward
    int typeLayout;
    String points;
    String comment;
    String adddate;
    String expiredate;

//credits
    String txnid;
    String date;
    String credit;
    String debit;
    String balance;
    String status;
    String comments;

//coupons
    String offerimage;
    String offervalue;
    String offercomment;
    String offerexpires;
    String offerminorder;
    String offercode;

    public String getOfferimage() {
        return offerimage;
    }

    public void setOfferimage(String offerimage) {
        this.offerimage = offerimage;
    }

    public String getOffervalue() {
        return offervalue;
    }

    public void setOffervalue(String offervalue) {
        this.offervalue = offervalue;
    }

    public String getOffercomment() {
        return offercomment;
    }

    public void setOffercomment(String offercomment) {
        this.offercomment = offercomment;
    }

    public String getOfferexpires() {
        return offerexpires;
    }

    public void setOfferexpires(String offerexpires) {
        this.offerexpires = offerexpires;
    }

    public String getOfferminorder() {
        return offerminorder;
    }

    public void setOfferminorder(String offerminorder) {
        this.offerminorder = offerminorder;
    }

    public String getOffercode() {
        return offercode;
    }

    public void setOffercode(String offercode) {
        this.offercode = offercode;
    }

    public String getTxnid() {
        return txnid;
    }

    public void setTxnid(String txnid) {
        this.txnid = txnid;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getCredit() {
        return credit;
    }

    public void setCredit(String credit) {
        this.credit = credit;
    }

    public String getDebit() {
        return debit;
    }

    public void setDebit(String debit) {
        this.debit = debit;
    }

    public String getBalance() {
        return balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getComments() {
        return comments;
    }

    public void setComments(String comments) {
        this.comments = comments;
    }

    public int getTypeLayout() {
        return typeLayout;
    }

    public void setTypeLayout(int typeLayout) {
        this.typeLayout = typeLayout;
    }

    public String getPoints() {
        return points;
    }

    public void setPoints(String points) {
        this.points = points;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getAdddate() {
        return adddate;
    }

    public void setAdddate(String adddate) {
        this.adddate = adddate;
    }

    public String getExpiredate() {
        return expiredate;
    }

    public void setExpiredate(String expiredate) {
        this.expiredate = expiredate;
    }
}
