package com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.article.presenter;

import android.os.Bundle;

import com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.article.view.ArticleView;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.lib.Presenter;

/**
 * @author Bogdan Kolomiets
 * @version 1
 * @date 22.06.16
 */
public interface ArticlePresenter extends Presenter<ArticleView> {

    void onCreate(String rubric, String url);

    void onCreateView(Bundle savedInstanceState);

    void onShareClick();

}
