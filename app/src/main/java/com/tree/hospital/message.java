package com.tree.hospital;

import java.util.Date;

/**
 * Created by thy on 29/02/2016.
 */
public class message {
    String mText;
    String mSender;
    Date mDate;
    String mPrescription;

    public void setmPrescription(String mPrescription) {
        this.mPrescription = mPrescription;
    }

    public String getmPrescription() {
        return mPrescription;
    }

    public String getmSender() {
        return mSender;
    }

    public String getmText() {
        return mText;
    }

    public Date getmDate() {
        return mDate;
    }

    public void setmText(String mText) {
        this.mText = mText;
    }


    public void setmSender(String mSender) {
        this.mSender = mSender;
    }

    public void setmDate(Date mDate) {
        this.mDate = mDate;
    }
}
