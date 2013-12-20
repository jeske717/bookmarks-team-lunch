package org.jesko.bookmarks;

import java.util.ArrayList;
import java.util.List;

public class BookmarksModel {

	private static BookmarksModel INSTANCE = new BookmarksModel();
	
	private BookmarksModel() {}
	
	public static BookmarksModel getInstance() {
		return INSTANCE;
	}
	
	private List<Bookmark> bookmarks = new ArrayList<>();
	
	public List<Bookmark> getBookmarks() {
		return bookmarks;
	}

	public void add(Bookmark bookmark) {
		bookmarks.add(bookmark);
	}

}
