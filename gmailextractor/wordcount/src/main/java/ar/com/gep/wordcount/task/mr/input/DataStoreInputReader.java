package ar.com.gep.wordcount.task.mr.input;

import static ar.com.gep.wordcount.ds.DataStoreFactory.ofy;
import static com.google.appengine.api.datastore.Entity.KEY_RESERVED_PROPERTY;

import java.io.IOException;
import java.util.NoSuchElementException;

import ar.com.gep.wordcount.ds.Entity;

import com.google.appengine.api.datastore.DatastoreTimeoutException;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.tools.mapreduce.InputReader;
import com.googlecode.objectify.Key;
import com.googlecode.objectify.cmd.Query;

public class DataStoreInputReader<I extends Entity> extends InputReader<I> {

    private static final long serialVersionUID = 1L;
    private final Key<I> startKey;
    private final Key<I> endKey;
    private final Class<I> clazz;

    private static final int BATCH_SIZE = 50;
    /** Objectify QueryResultIterators are not serializable */
    private transient QueryResultIterator<I> iterator;
    /** A cursor to re-generate the iterator */
    private String currentKey = null;
    private Filter[] filter;

    public DataStoreInputReader(Class<I> clazz, String startKey, String endKey, Filter[] filter) {

        this.clazz = clazz;
        this.startKey = Key.create(clazz, startKey);
        this.endKey = endKey != null ? Key.create(clazz, endKey) : null;
        this.filter = filter;

    }

    @Override
    public void beginSlice() throws IOException {
        this.iterator = buildIterator();
    }

    /**
     * Performs a query to the specified table in the datastore using the cursor stored.
     * 
     * @return
     */
    private QueryResultIterator<I> buildIterator() {
        Query<I> query = currentKey == null ? ofy().load().type(clazz).filterKey(">=", startKey) : ofy().load()
                .type(clazz).filterKey(">", Key.create(clazz, currentKey));

        query = query.order(KEY_RESERVED_PROPERTY).chunk(BATCH_SIZE);

        if (endKey != null) {
            query = query.filterKey("<", endKey);
        }

        if (filter != null) {
            for (Filter filterItem : filter) {
                query = query.filter(filterItem);
            }
        }

        return query.iterator();
    }

    /**
     * @see com.google.appengine.tools.mapreduce.InputReader#next()
     */
    @Override
    public I next() throws IOException, NoSuchElementException {
        if (!iteratorHasNext()) {
            throw new NoSuchElementException("No more elements in iterator.");
        }
        I nextValue = safeNext();
        this.currentKey = nextValue.getId();
        return nextValue;
    }

    private I safeNext() {
        try {
            return iterator.next();
        } catch (IllegalArgumentException e) {

        } catch (DatastoreTimeoutException e) {

        }
        iterator = buildIterator();
        return safeNext();
    }

    private boolean iteratorHasNext() {
        try {
            return iterator.hasNext();
        } catch (IllegalArgumentException e) {

        } catch (DatastoreTimeoutException e) {

        }
        iterator = buildIterator();
        return iteratorHasNext();
    }

    /**
     * @see com.google.appengine.tools.mapreduce.InputReader#getProgress()
     */
    @Override
    public Double getProgress() {
        return null;
    }

}
