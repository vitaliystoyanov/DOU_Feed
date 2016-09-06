package com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.lib;

import com.bogdan_kolomiets_1996.bogdan.dou_feed.ui.common.View;

/**
 * @author Bogdan Kolomiets
 * @version 1
 * @date 30.06.16
 */
public interface Presenter<V extends View> {

    void updateView(V view);

}
