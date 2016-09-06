package com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.article.presenter;

import android.content.Intent;
import android.os.Bundle;

import com.bogdan_kolomiets_1996.bogdan.dou_feed.Constants;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.DouApp;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.HTTPUtils;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.model.DouModel;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.model.entity.article.Article;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.common.BasePresenter;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.article.view.ArticleView;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.common.View;

import javax.inject.Inject;

import rx.Observer;

/**
 * @author Bogdan Kolomiets
 * @version 1
 * @date 22.06.16
 */
public class ArticlePresenterImpl extends BasePresenter implements ArticlePresenter {
  private ArticleView mView;
  private String mRubric;
  private String mUrl;
  private String mFullUrl;

  @Inject
  public ArticlePresenterImpl(DouModel model, ArticleView view) {
    super(model);
    mView = view;
  }


  @Override
  public void onCreate(String rubric, String url) {
    mRubric = rubric;
    mUrl = url;
    mFullUrl = Constants.HTTP.BASE_URL + mRubric + "/" + mUrl;
  }

  @Override
  public void onCreateView(Bundle savedInstanceState) {
    if (HTTPUtils.isNetworkAvailable(mView.getDouContext())) {
      mView.showLoading();
    }
    mModel.getArticle(mRubric, mUrl)
        .subscribe(new Observer<Article>() {
          @Override
          public void onCompleted() {
            mView.hideLoading();
          }

          @Override
          public void onError(Throwable e) {
            e.printStackTrace();
            mView.hideLoading();

            if (HTTPUtils.isNetworkException(e)) {
              mView.showError(Constants.HTTP.NET_ERROR_MSG);
            }
          }

          @Override
          public void onNext(Article articleEntity) {
            if (articleEntity != null) {
              showArticle(articleEntity);
            }
          }
        });
  }

  @Override
  public void onShareClick() {
    Intent intent = new Intent();
    intent.setAction(Intent.ACTION_SEND);
    intent.putExtra(Intent.EXTRA_TEXT, mFullUrl);
    intent.setType("text/plain");
    mView.getDouContext().startActivity(intent);
  }

  private void showArticle(Article articleEntity) {
    mView.showHead(articleEntity.getHeader().getAuthorName(),
        articleEntity.getHeader().getDate(),
        articleEntity.getHeader().getTitle());
    for (int i = 0; i < articleEntity.size(); i++) {
      mView.showPageElement(articleEntity.getPageElement(i));
    }
  }

  @Override
  public void updateView(ArticleView view) {
    mView = null;
    mView = view;
  }
}
