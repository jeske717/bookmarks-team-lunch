package org.jesko.bookmarks.nolib;

import android.app.AlertDialog;
import android.app.ListActivity;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPut;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.DefaultHttpClient;
import org.codehaus.jackson.map.ObjectMapper;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

public class BookmarksListActivity extends ListActivity {

    private static final String BOOKMARKS_SERVICE_URL = "http://jeskeshouse.com/server/bookmarks";

    protected ListView bookmarksList;

    private ArrayAdapter<Bookmark> bookmarkListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.bookmark_list_layout);
        bookmarksList = (ListView) findViewById(android.R.id.list);
        bookmarkListAdapter = new ArrayAdapter<Bookmark>(this, android.R.layout.simple_list_item_1);
        bookmarksList.setAdapter(bookmarkListAdapter);

        bookmarksList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                bookmarkPressed(bookmarkListAdapter.getItem(i));
            }
        });

        loadBookmarks();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.bookmark_list_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        boolean handled = super.onOptionsItemSelected(item);
        if (handled) {
            return true;
        }
        if (item.getItemId() == R.id.add_bookmark) {
            showAddBookmarkDialog();
            return true;
        }
        return false;
    }

    protected void loadBookmarks() {
        new UpdateBookmarksTask().execute();
    }

    protected void displayBookmarks(List<Bookmark> bookmarks) {
        bookmarkListAdapter.clear();
        bookmarkListAdapter.addAll(bookmarks);
    }

    protected void bookmarkPressed(Bookmark bookmark) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(bookmark.getLink()));
        startActivity(browserIntent);
    }

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

    protected void saveBookmark(Bookmark bookmark) {
        new SaveBookmarksTask().execute(bookmark);
    }

    private class UpdateBookmarksTask extends AsyncTask<Void, Void, BookmarkList> {

        @Override
        protected BookmarkList doInBackground(Void... voids) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpGet getBookmarks = new HttpGet(BOOKMARKS_SERVICE_URL);
            InputStream is = null;
            try {
                HttpResponse response = httpClient.execute(getBookmarks);
                is = response.getEntity().getContent();
                return new ObjectMapper().readValue(is, BookmarkList.class);
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if(is != null) {
                    try {
                        is.close();
                    } catch (IOException ignored) {
                    }
                }
            }
            return null;
        }

        @Override
        protected void onPostExecute(BookmarkList bookmarks) {
            displayBookmarks(bookmarks);
        }
    }

    private class SaveBookmarksTask extends AsyncTask<Bookmark, Void, Void> {

        @Override
        protected Void doInBackground(Bookmark... bookmarks) {
            HttpClient httpClient = new DefaultHttpClient();
            HttpPut putBookmark = new HttpPut(BOOKMARKS_SERVICE_URL);
            try {
                String jsonPayload = new ObjectMapper().writeValueAsString(bookmarks[0]);
                putBookmark.setEntity(new StringEntity(jsonPayload));
                putBookmark.setHeader("Content-Type", "application/json");
                HttpResponse response = httpClient.execute(putBookmark);
                response.getEntity().consumeContent();
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            loadBookmarks();
        }
    }
}
