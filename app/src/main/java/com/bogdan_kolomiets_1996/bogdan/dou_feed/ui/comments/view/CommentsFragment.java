package com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.comments.view;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.*;
import android.view.View;

import com.bogdan_kolomiets_1996.bogdan.dou_feed.DouApp;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.R;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.di.module.CommentViewModule;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.model.entity.CommentItem;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.comments.presenter.CommentsPresenter;
import com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.common.BaseFragment;

import java.util.List;

import javax.inject.Inject;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * @author Bogdan Kolomiets
 * @version 1
 * @date 22.06.16
 */
public class CommentsFragment extends BaseFragment implements CommentsView{
    private final static int LAYOUT = R.layout.comments_layout;

    @BindView(R.id.commentRecyclerView)
    RecyclerView commentRecyclerView;

    @BindView(R.id.commentSwipeContainer)
    SwipeRefreshLayout swipeLayout;

    @Inject
    CommentsPresenter presenter;

    private LinearLayoutManager mLayoutManager;
    private CommentsAdapter mAdapter;
    private Unbinder unbinder;

    public static CommentsFragment newInstance(String rubric, String pageUrl) {
        CommentsFragment fragment = new CommentsFragment();

        Bundle args = new Bundle();
        args.putString("rubric", rubric);
        args.putString("pageUrl", pageUrl);
        fragment.setArguments(args);

        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        DouApp.get(getContext()).getAppComponent().plus(new CommentViewModule(this)).inject(this);
        super.onCreate(savedInstanceState);
        String rubric = getArguments().getString("rubric");
        String url = getArguments().getString("pageUrl");
        presenter.onCreate(rubric, url);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(LAYOUT, container, false);
        unbinder = ButterKnife.bind(this, view);
        mLayoutManager = new LinearLayoutManager(getContext());
        commentRecyclerView.setLayoutManager(mLayoutManager);
        mAdapter = new CommentsAdapter(getContext());
        commentRecyclerView.setAdapter(mAdapter);

        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                mAdapter.clear();
                presenter.onRefresh();
            }
        });

        presenter.onCreateView(savedInstanceState);
        return view;
    }

    @Override
    public void showComments(List<CommentItem> comments) {
        mAdapter.addComments(comments);
    }

    @Override
    public void stopRefresh() {
        swipeLayout.setRefreshing(false);
    }

    @Override
    public void showEmptyList() {
        showError("Пока не коментариев к этой записи");
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

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
