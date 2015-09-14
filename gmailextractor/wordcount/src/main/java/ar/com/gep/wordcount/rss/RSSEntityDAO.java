package ar.com.gep.wordcount.rss;

import static ar.com.gep.wordcount.ds.DataStoreFactory.ofy;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.googlecode.objectify.cmd.Query;

public final class RSSEntityDAO {

    public static QueryResultIterator<Channel> getChannels(String token) {
        Query<Channel> query = ofy().load().type(Channel.class).limit(1);
        if (token != null && !token.isEmpty()) {
            Cursor cursor = Cursor.fromWebSafeString(token);
            query.startAt(cursor);
        }
        return query.iterator();
    }

}
