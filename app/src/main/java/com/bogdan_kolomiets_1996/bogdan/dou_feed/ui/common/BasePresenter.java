package com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.common;

import com.bogdan_kolomiets_1996.bogdan.dou_feed.model.DouModel;

import rx.Subscription;
import rx.subscriptions.CompositeSubscription;

/**
 * @author Bogdan Kolomiets
 * @version 1
 * @date 21.06.16
 */
public abstract class BasePresenter {
  private CompositeSubscription mCompositeSubscription = new CompositeSubscription();
  protected final DouModel mModel;

  public BasePresenter(DouModel model) {
    mModel = model;
  }

  protected void addSubstription(Subscription subscription) {
    mCompositeSubscription.add(subscription);
  }

  public void onStop() {
    mCompositeSubscription.unsubscribe();
    mCompositeSubscription.clear();
  }
}
