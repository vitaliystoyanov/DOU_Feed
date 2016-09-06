package com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.feed.view;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.bogdan_kolomiets_1996.bogdan.dou_feed.DouApp;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.HTTPUtils;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.R;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.di.module.FeedViewModule;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.model.entity.feed.FeedItem;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.article.view.ArticleFragment;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.comments.view.CommentsFragment;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.common.BaseFragment;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.common.EndlessOnScrollListener;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.feed.presenter.FeedPresenter;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Bogdan Kolomiets
 * @version 1
 * @date 21.06.16
 */
public class FeedFragment extends BaseFragment implements FeedView, FeedAdapter.OnFeedListener {
  @BindView(R.id.feedRecyclerView)
  RecyclerView feedRecyclerView;

  @Inject
  FeedPresenter presenter;

  @BindView(R.id.swipeContainer)
  SwipeRefreshLayout swipeLayout;

  @BindView(R.id.fabSendNews)
  FloatingActionButton fabAddArticle;

  private Button btnUp;
  private LinearLayoutManager mLayoutManager;
  private FeedAdapter mFeedAdapter;
  private Unbinder unbinder;

  @Override
  public void onCreate(@Nullable Bundle savedInstanceState) {
    super.onCreate(savedInstanceState);
    DouApp.get(getContext()).getAppComponent().plus(new FeedViewModule(this)).inject(this);
  }

  @Nullable
  @Override
  public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
    View view = inflater.inflate(R.layout.feed_layout, container, false);
    unbinder = ButterKnife.bind(this, view);
    presenter.updateView(this);
    return view;
  }

  @Override
  public void onActivityCreated(@Nullable Bundle savedInstanceState) {
    super.onActivityCreated(savedInstanceState);
    mLayoutManager = new LinearLayoutManager(getContext());
    feedRecyclerView.setLayoutManager(mLayoutManager);

    swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
      @Override
      public void onRefresh() {
        mFeedAdapter.clear();
        presenter.onRefresh();
      }
    });

    feedRecyclerView.addOnScrollListener(new EndlessOnScrollListener(mLayoutManager) {
      @Override
      public void onLoadMore() {
        presenter.loadFeed(false);
      }
    });

    mFeedAdapter = new FeedAdapter(getContext(), this);
    feedRecyclerView.setAdapter(mFeedAdapter);

    configureButtonSendNews();
    presenter.onActivityCreated();
  }

  @Override
  public void showFeed(List<FeedItem> feed) {
    mFeedAdapter.addFeed(feed);
  }

  @Override
  public void stopRefresh() {
    swipeLayout.setRefreshing(false);
  }

  @Override
  public void addNewArticle() {
    NewArticleDialog dialog = new NewArticleDialog(getContext());
    dialog.show();
  }

  @Override
  public void onClick(String url, FeedAdapter.Type type) {
    String rubric = HTTPUtils.getRubric(url);
    String urlPage = HTTPUtils.getPageUrl(url);
    switch (type) {
      case COMMENT:
        CommentsFragment cf = CommentsFragment.newInstance(rubric, urlPage);
        getActivity().getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(
                R.anim.right_in,
                R.anim.left_out,
                R.anim.left_in,
                R.anim.right_out)
            .replace(R.id.container, cf, null)
            .addToBackStack(null)
            .commit();
        break;
      case FEED_ITEM:
        ArticleFragment fragment = ArticleFragment.newInstance(rubric, urlPage);
        getActivity().getSupportFragmentManager().beginTransaction()
            .setCustomAnimations(
                R.anim.right_in,
                R.anim.left_out,
                R.anim.left_in,
                R.anim.right_out)
            .replace(R.id.container, fragment, null)
            .addToBackStack(null)
            .commit();
        break;
      case SHARE:
        Intent intent = new Intent();
        intent.setAction(Intent.ACTION_SEND);
        intent.putExtra(Intent.EXTRA_TEXT, url);
        intent.setType("text/plain");
        startActivity(intent);
        break;
    }
  }

  @Override
  public void onShowBtnUp() {
    btnUp = (Button) getActivity().findViewById(R.id.feedGoUpBtn);
    btnUp.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        feedRecyclerView.scrollToPosition(0);
      }
    });
    btnUp.setVisibility(View.VISIBLE);
  }

  @Override
  public void onHideBtnUp() {
    if (btnUp != null) {
      btnUp.setVisibility(View.INVISIBLE);
    }
  }

  @Override
  public void showLoading() {
    showProgressDial();
  }

  @Override
  public void hideLoading() {
    hideProgressDial();
  }

  @Override
  public void showError(String message) {
    onError(message);
  }

  @Override
  public Context getDouContext() {
    return getContext();
  }

  private void configureButtonSendNews() {
    fabAddArticle.setOnClickListener(new View.OnClickListener() {
      @Override
      public void onClick(View v) {
        presenter.onAddArticleClick();
      }
    });
  }

  @Override
  public void onDestroyView() {
    super.onDestroyView();
    unbinder.unbind();
  }
}
