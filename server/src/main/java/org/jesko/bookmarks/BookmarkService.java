package org.jesko.bookmarks;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
 
@Path("/bookmarks")
public class BookmarkService {

	private final BookmarksModel bookmarksModel = BookmarksModel.getInstance();
	
	@Produces({"application/json"})
	@GET
	public List<Bookmark> getBookmarks() {
		return bookmarksModel.getBookmarks();
	}
	
	@Produces({"application/json"})
	@PUT
	public Bookmark addBookmark(Bookmark bookmark) {
		bookmarksModel.add(bookmark);
		return bookmark;
	}
}
