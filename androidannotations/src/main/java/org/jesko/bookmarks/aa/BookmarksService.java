package org.jesko.bookmarks.aa;

import com.googlecode.androidannotations.annotations.rest.Get;
import com.googlecode.androidannotations.annotations.rest.Put;
import com.googlecode.androidannotations.annotations.rest.Rest;

import org.springframework.http.converter.json.MappingJacksonHttpMessageConverter;

@Rest(rootUrl = "http://192.168.1.109:8080/server/", converters = {MappingJacksonHttpMessageConverter.class})
public interface BookmarksService {

    @Get("bookmarks")
    BookmarkList getBookmarks();

    @Put("bookmarks")
    void addBookmark(Bookmark bookmark);
}
