package org.jesko.bookmarks.aa;

import android.content.Intent;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.rest.RestService;

import java.util.List;

import roboguice.activity.RoboListActivity;

@EActivity(R.layout.bookmark_list_layout)
public class BookmarksListActivity extends RoboListActivity {

    @ViewById(android.R.id.list)
    protected ListView bookmarksList;
    @RestService
    protected BookmarksService bookmarksService;

    private ArrayAdapter<Bookmark> bookmarkListAdapter;

    @AfterViews
    protected void initialize() {
        bookmarkListAdapter = new ArrayAdapter<Bookmark>(this, android.R.layout.simple_list_item_1);
        bookmarksList.setAdapter(bookmarkListAdapter);
        loadBookmarks();
    }

    @Background
    protected void loadBookmarks() {
        displayBookmarks(bookmarksService.getBookmarks());
    }

    @UiThread
    protected void displayBookmarks(List<Bookmark> bookmarks) {
        bookmarkListAdapter.addAll(bookmarks);
    }

    @ItemClick(android.R.id.list)
    protected void bookmarkPressed(Bookmark bookmark) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookmark.getLink()));
        startActivity(browserIntent);
    }
}
