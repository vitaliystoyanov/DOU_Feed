package com.example.bogdan.dou_feed.api;

import com.example.bogdan.dou_feed.model.entity.ArticleEntity;
import com.example.bogdan.dou_feed.model.entity.CommentItemEntity;
import com.example.bogdan.dou_feed.model.entity.feed.FeedItem;

import java.util.List;

import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;

/**
 * @author Bogdan Kolomiets
 * @version 1
 * @date 21.06.16
 */
public interface DouApi {

    @GET("{rubric}/page/{page-number}")
    Observable<List<FeedItem>> getFeedEntityByRubric(@Path("rubric")String rubric,
                                                     @Path("page-number") int pageNumber);

    @GET("page/{page-number}")
    Observable<List<FeedItem>> getFeedEntity(@Path("page-number")int pageNumber);

    @GET("{rubric}/{article-url}")
    Observable<ArticleEntity> getArticleEntity(@Path("rubric")String rubric,
                                               @Path("article-url")String articleUrl);

    @GET("{rubric}/{article-url}/#comments")
    Observable<List<CommentItemEntity>> getCommentListEntity(@Path("rubric")String rubric,
                                                             @Path("article-url") String articleUrl);

}
