package com.example.dahlia.listener;

public interface LastSeenCallBack {
    void onLastSeenReceived(String formattedDateTime);
    void onError(String errorMessage);
}
