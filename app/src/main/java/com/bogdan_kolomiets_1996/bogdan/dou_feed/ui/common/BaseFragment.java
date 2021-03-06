package com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.common;

import android.app.ProgressDialog;
import android.support.v4.app.Fragment;
import android.widget.Toast;

/**
 * @author Bogdan Kolomiets
 * @version 1
 * @date 21.06.16
 */
public abstract class BaseFragment extends Fragment {

    private ProgressDialog mProgressDialog;

    protected void showProgressDial() {
        mProgressDialog = new ProgressDialog(getContext());
        mProgressDialog.setIndeterminate(true);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setTitle("Загрузка...");
        mProgressDialog.setMessage("Пожалуйста, подождите");
        mProgressDialog.show();
    }

    protected void hideProgressDial() {
        if (mProgressDialog != null) {
            mProgressDialog.dismiss();
        }
    }

    protected void onError(String message) {
        Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
    }
}
