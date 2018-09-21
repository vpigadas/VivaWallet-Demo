package com.vpigadas.vivawallet.models;

import java.util.Date;

public class Order {

    public long OrderCode;
    public int ErrorCode;
    public String ErrorText;
    public Date TimeStamp;

    public long getOrderCode() {
        return OrderCode;
    }

    public int getErrorCode() {
        return ErrorCode;
    }

    public String getErrorText() {
        return ErrorText;
    }

    public Date getTimeStamp() {
        return TimeStamp;
    }
}
