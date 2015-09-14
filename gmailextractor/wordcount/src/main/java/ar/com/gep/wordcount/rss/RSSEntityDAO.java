package ar.com.gep.wordcount.rss;

import com.google.appengine.api.datastore.Cursor;
import com.google.appengine.api.datastore.DatastoreService;
import com.google.appengine.api.datastore.DatastoreServiceFactory;
import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.FetchOptions;
import com.google.appengine.api.datastore.PreparedQuery;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.QueryResultList;

public final class RSSEntityDAO {

    private static final String CHANNEL = "Channel";

    public static QueryResultList<Entity> getChannels(String token) {
        FetchOptions fetchOptions = FetchOptions.Builder.withLimit(1);

        if (token != null && !token.isEmpty()) {
            Cursor cursor = Cursor.fromWebSafeString(token);
            fetchOptions.startCursor(cursor);
        }

        Query q = new Query(CHANNEL);
        DatastoreService datastore = DatastoreServiceFactory.getDatastoreService();
        PreparedQuery pq = datastore.prepare(q);

        QueryResultList<Entity> results = pq.asQueryResultList(fetchOptions);
        return results;
    }

}
