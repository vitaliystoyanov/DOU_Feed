package com.example.bogdan.dou_feed.model.entity.page;

import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.bogdan.dou_feed.R;

import java.util.ArrayList;

/**
 * @author Bogdan Kolomiets
 * @version 1
 * @date 26.06.16
 */
public class List extends PageElement {
    private java.util.List<String> mList;

    public List() {
        mList = new ArrayList<>();
    }

    public void add(String listElement) {
        mList.add(listElement);
    }

    @Override
    void display(LayoutInflater inflater, ViewGroup container) {
        TextView listView = (TextView) inflater.inflate(R.layout.article_content, null);

        for (String listElement : mList) {
            listView.setText(listElement + '\n');
        }

        container.addView(listView);
    }
}
