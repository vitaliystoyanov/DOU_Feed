package com.example.bogdan.dou_feed.ui.common;

/**
 * @author Bogdan Kolomiets
 * @version 1
 * @date 21.06.16
 */
public interface View {

    void showLoading();

    void hideLoading();

    void showError(String message);
}