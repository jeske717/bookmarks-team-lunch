package org.jesko.bookmarks;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
 
@Path("/bookmarks")
public class BookmarkService {

	private List<Bookmark> bookmarks = new ArrayList<>();
	
	@Produces({"application/json"})
	@GET
	public List<Bookmark> getBookmarks() {
		return bookmarks;
	}
	
	@Produces({"application/json"})
	@PUT
	public Bookmark addBookmark(Bookmark bookmark) {
		bookmarks.add(bookmark);
		return bookmark;
	}
}
