package com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.comments.presenter;

import android.os.Bundle;

import com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.comments.view.CommentsView;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.lib.Presenter;

/**
 * @author Bogdan Kolomiets
 * @version 1
 * @date 22.06.16
 */
public interface CommentsPresenter extends Presenter<CommentsView> {

    void onCreate(String rubric, String url);

    void onActivityCreated(Bundle savedInstanceState);

    void loadComments(String rubric, String pageUrl, boolean showLoading);

    void onRefresh();
}
