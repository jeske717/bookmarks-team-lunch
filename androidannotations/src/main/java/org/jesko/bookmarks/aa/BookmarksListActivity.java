package org.jesko.bookmarks.aa;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import com.googlecode.androidannotations.annotations.AfterViews;
import com.googlecode.androidannotations.annotations.Background;
import com.googlecode.androidannotations.annotations.EActivity;
import com.googlecode.androidannotations.annotations.ItemClick;
import com.googlecode.androidannotations.annotations.OptionsItem;
import com.googlecode.androidannotations.annotations.OptionsMenu;
import com.googlecode.androidannotations.annotations.UiThread;
import com.googlecode.androidannotations.annotations.ViewById;
import com.googlecode.androidannotations.annotations.rest.RestService;

import java.util.List;

import roboguice.activity.RoboListActivity;

@EActivity(R.layout.bookmark_list_layout)
@OptionsMenu(R.menu.bookmark_list_menu)
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
        bookmarkListAdapter.clear();
        bookmarkListAdapter.addAll(bookmarks);
    }

    @ItemClick(android.R.id.list)
    protected void bookmarkPressed(Bookmark bookmark) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookmark.getLink()));
        startActivity(browserIntent);
    }

    @OptionsItem(R.id.add_bookmark)
    protected void showAddBookmarkDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Add Bookmark").setMessage("Enter a valid URL for the new bookmark:");

        final EditText input = new EditText(this);
        builder.setView(input);

        builder.setPositiveButton("Save", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Bookmark bookmark = new Bookmark();
                bookmark.setLink(input.getText().toString());
                saveBookmark(bookmark);
            }
        });

        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
            }
        });

        builder.show();
    }

    @Background
    protected void saveBookmark(Bookmark bookmark) {
        bookmarksService.addBookmark(bookmark);
        loadBookmarks();
    }
}
