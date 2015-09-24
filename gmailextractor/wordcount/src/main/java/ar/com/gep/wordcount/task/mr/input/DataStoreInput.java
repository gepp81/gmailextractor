package ar.com.gep.wordcount.task.mr.input;

import static com.google.appengine.api.datastore.DatastoreServiceFactory.getDatastoreService;
import static com.google.appengine.api.datastore.Entity.SCATTER_RESERVED_PROPERTY;
import static com.google.appengine.api.datastore.FetchOptions.Builder.withLimit;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.tools.mapreduce.Input;
import com.google.appengine.tools.mapreduce.InputReader;

public class DataStoreInput<I extends ar.com.gep.wordcount.ds.Entity> extends Input<I> {
    private static final long serialVersionUID = 1L;
    private static final int SCATTER_ENTITIES_PER_SHARD = 32;
    private final Class<I> clazz;
    private final int shardCount;
    private final String entityKind;
    private Filter[] filter;
    private static final Comparator<Entity> ENTITY_COMPARATOR = new Comparator<Entity>() {
        @Override
        public int compare(Entity o1, Entity o2) {
            return o1.getKey().compareTo(o2.getKey());
        }
    };

    /**
     * Default constructor.
     * 
     * @param clazz
     *            of the type that should be read from the datastore. Cannot be null.
     * @param shardCount
     *            number of mappers that the job will try to create, if necessary. Must be > 0.
     * 
     * @throws NullPointerException
     *             if clazz is null
     * @throws IllegalArgumentException
     *             if shardCount <= 0.
     */
    public DataStoreInput(Class<I> clazz, int shardCount) {
        this(clazz, shardCount, null);
    }

    public DataStoreInput(Class<I> clazz, int shardCount, Filter[] filter) {

        this.clazz = clazz;
        this.shardCount = shardCount;
        this.entityKind = this.clazz.getSimpleName();
        this.filter = filter;
    }

    /**
     * @see com.google.appengine.tools.mapreduce.Input#createReaders()
     */
    @Override
    public List<? extends InputReader<I>> createReaders() throws IOException {
        List<DataStoreInputReader<I>> readers = new ArrayList<DataStoreInputReader<I>>();

        String startKey = getStartKey();
        if (startKey == null) {
            return Collections.emptyList();
        }

        List<Entity> scatterEntities = retrieveScatterKeys();
        for (String currentKey : chooseSplitPoints(scatterEntities, shardCount)) {
            addInputReader(readers, startKey, currentKey);
            startKey = currentKey;
        }

        addInputReader(readers, startKey, null);

        return readers;
    }

    private void addInputReader(List<DataStoreInputReader<I>> readers, String startKey, String currentKey) {
        DataStoreInputReader<I> reader = new DataStoreInputReader<I>(clazz, startKey, currentKey, filter);
        readers.add(reader);
    }

    /**
     * A scatter property is added to 1 out of every X entities (X is currently 512), @see
     * http://code.google.com/p/appengine-mapreduce/wiki/ ScatterPropertyImplementation
     * <p/>
     * We need to determine #shards - 1 split points to divide entity space into equal shards. We oversample the
     * entities with scatter properties to get a better approximation. Note: there is a region of entities before and
     * after each scatter entity: |---*------*------*------*------*------*------*---| * = scatter entity, - = entity so
     * if each scatter entity represents the region following it, there is an extra region before the first scatter
     * entity. Thus we query for one less than the desired number of regions to account for the this extra region before
     * the first scatter entity
     */
    private List<Entity> retrieveScatterKeys() {
        int desiredNumScatterEntities = (shardCount * SCATTER_ENTITIES_PER_SHARD) - 1;
        Query scatter = new Query(entityKind).addSort(SCATTER_RESERVED_PROPERTY).setKeysOnly();
        List<Entity> scatterKeys = getDatastoreService().prepare(scatter).asList(withLimit(desiredNumScatterEntities));
        Collections.sort(scatterKeys, ENTITY_COMPARATOR);
        return scatterKeys;
    }

    /**
     * Determine the number of regions per shard based on the actual number of scatter entities found. The number of
     * regions is one more than the number of keys retrieved to account for the region before the first scatter entity.
     * We ensure a minimum of 1 region per shard, since this is the smallest granularity of entity space we can
     * partition on at this stage. Assuming each region contains the same number of entities (which is not true, but
     * does as the number of regions approaches infinity) assign each shard an equal number of regions (rounded to the
     * nearest scatter key).
     * 
     * @param scatterKeys
     *            list of scattered keys that will be used to distribute the input into the available number of shards.
     */
    protected static Iterable<String> chooseSplitPoints(List<Entity> scatterKeys, int numShards) {
        double scatterRegionsPerShard = Math.max(1.0, (double) (scatterKeys.size() + 1) / numShards);
        Collection<String> splitKeys = new ArrayList<String>(numShards - 1);
        for (int i = 1; i < numShards; i++) {
            int splitPoint = (int) Math.round(i * scatterRegionsPerShard) - 1;
            if (splitPoint >= scatterKeys.size()) {
                break;
            }
            splitKeys.add(scatterKeys.get(splitPoint).getKey().getName());
        }
        return splitKeys;
    }

    /**
     * @return the first key of the entity.
     */
    private String getStartKey() {
        Query ascending = new Query(entityKind).addSort(Entity.KEY_RESERVED_PROPERTY).setKeysOnly();
        Iterator<Entity> ascendingIt = getDatastoreService().prepare(ascending).asIterator(withLimit(1));
        if (!ascendingIt.hasNext()) {
            return null;
        }
        return ascendingIt.next().getKey().getName();
    }
}