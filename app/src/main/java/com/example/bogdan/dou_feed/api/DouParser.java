package com.example.bogdan.dou_feed.api;

import com.example.bogdan.dou_feed.model.entity.ArticleEntity.Type;
import com.example.bogdan.dou_feed.model.entity.ArticleEntity;
import com.example.bogdan.dou_feed.model.entity.CommentItemEntity;
import com.example.bogdan.dou_feed.model.entity.feed.FeedItem;
import com.example.bogdan.dou_feed.model.entity.TableEntity;
import com.example.bogdan.dou_feed.model.entity.feed.FeedItemContent;
import com.example.bogdan.dou_feed.model.entity.feed.FeedItemFooter;
import com.example.bogdan.dou_feed.model.entity.feed.Header;

import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;

import java.util.ArrayList;
import java.util.List;
import java.util.regex.Pattern;

/**
 * @author Bogdan Kolomiets
 * @version 1
 * @date 21.06.16
 */
public class DouParser {

    public static List<FeedItem> parseFeed(Document document) {
        List<FeedItem> feed = new ArrayList<>();

        Elements items = document.select(".b-lenta article");

        for (Element feedItem : items) {

            String url = feedItem.select("h2 a").first().attr("href");

            String imageUrl = feedItem.select("h2 a img").first().attr("src");
            String authorName = feedItem.select(".author").first().html();
            String date = feedItem.select(".date").first().text();
            Header header = new Header(imageUrl, authorName, date);

            String title = feedItem.select("h2 a").first().text().replace("&nbsp;", " ");
            String description = feedItem.select(".b-typo").first().text();
            description = deleteCommentCount(description);
            FeedItemContent content = new FeedItemContent(title, description);

            int watchCount;
            try {
                watchCount = Integer.parseInt(feedItem.select(".pageviews").first().text());
            } catch (NullPointerException e) {
                watchCount = 0;
            }
            int commentCount;
            try {
                commentCount = Integer.parseInt(feedItem.select(".b-typo a").first().html());
            } catch (NullPointerException e) {
                commentCount = 0;
            }
            String commentUrl;
            try {
               commentUrl = feedItem.select(".b-typo a").first().attr("href");
            } catch (NullPointerException e) {
                commentUrl = null;
            }
            FeedItemFooter footer = new FeedItemFooter(watchCount, commentCount, commentUrl);

            FeedItem feedItemEntity = new FeedItem.Builder()
                    .url(url)
                    .header(header)
                    .content(content)
                    .footer(footer)
                    .build();

            feed.add(feedItemEntity);
        }

        return feed;
    }

    public static ArticleEntity parseArticle(Document document) {
        Pattern pattern = Pattern.compile("h\\d");
        ArticleEntity articlePage = new ArticleEntity();

        String title = document.select("article.b-typo h1").first().text().replace("&nbsp;", " ");
        articlePage.setTitle(title);

        String date = document.select(".b-post-info .date").first().text();
        articlePage.setDate(date);

        String author = document.select(".b-post-info .author .name a").first().html();
        articlePage.setAuthor(author);

        Elements elements = document.select("article.b-typo div").first().children();
        for (Element element : elements) {
            switch (element.tagName()) {
                case "p":
                    if (element.hasText()) {
                        for (Element children : element.children()) {
                            if (children.tagName().equals("src")) {
                                articlePage.addElement(Type.IMAGE, children.attr("src"));
                            } else if (children.tagName().equals("a") &&
                                    children.children().hasAttr("src")) {
                                articlePage.addElement(Type.IMAGE, children.children().attr("src"));
                            }
                        }
                        articlePage.addElement(Type.CONTENT, element.text());
                    }
                    break;
                case "h1":
                case "h2":
                case "h3":
                case "h4":
                case "h5":
                case "h6":
                    articlePage.addElement(Type.CONTENT_HEADING, element.text());
                    break;
                case "pre":
                    articlePage.addElement(Type.CONTENT_CODE, element.text());
                    break;
                case "blockquote":
                    articlePage.addElement(Type.BLOCKQUOTE, element.text());
                    break;
                case "table":
                    TableEntity table = new TableEntity();
                    for (Element tableElements : element.children()) {
                        if (tableElements.tagName().equals("thead")) {
                            table.addTableRow();
                            for (Element head : tableElements.children().first().children()) {
                                table.addRowCell(head.text());
                            }
                        } else if (tableElements.tagName().equals("tbody")) {
                            for (Element row : tableElements.children()) {
                                table.addTableRow();
                                for (Element column : row.children()) {
                                    table.addRowCell(column.text());
                                }
                            }
                        }
                    }
                    articlePage.addTable(table);
            }
            if (element.children().hasAttr("src")) {
                articlePage.addElement(Type.IMAGE, element.children().attr("src"));
            }
        }

        return articlePage;
    }

    public static List<CommentItemEntity> parseComments(Document document) {
        List<CommentItemEntity> commentsList = new ArrayList<>();

        Element commentBlock = document.getElementById("commentsList");
        for (Element commentItem : commentBlock.children()) {
            String imageUrl = commentItem.select("img.g-avatar").first().attr("src");
            String authorName;
            String date;
            try {
                authorName = commentItem.select("a").first().text();
            } catch (NullPointerException e) {
                authorName = "Unknown";
            }
            try {
                date = commentItem.select(".comment-link").first().text();
            } catch (NullPointerException e) {
                date = "";
            }
            Header header = new Header(imageUrl, authorName, date);

            String content;
            try {
                content = commentItem.select(".text.b-typo").first().text();
            } catch (NullPointerException e) {
                content = "";
            }

            CommentItemEntity comment = new CommentItemEntity(header, content);
            commentsList.add(comment);
        }

        return commentsList;
    }

    private static String deleteCommentCount(String text) {
        char[] charText = text.toCharArray();
        int end = charText.length - 1;

        for (int i = end; i >= 0; i--) {
            try {
                Integer.parseInt(String.valueOf(charText[i]));
                end--;
            } catch (NumberFormatException e) {
                break;
            }
        }

        return String.valueOf(charText).substring(0, end + 1);
    }
}
