package ar.com.gep.wordcount.task.mr.output;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

import com.google.appengine.tools.mapreduce.Output;
import com.google.appengine.tools.mapreduce.OutputWriter;
import com.google.common.collect.ImmutableList;

public class DataStoreOutput<T> extends Output<T, Void> {
    private static final long serialVersionUID = 868276534742230776L;

    /**
     * Default constructor.
     *
     * @param shardCount
     *            Number of writers.
     */
    public DataStoreOutput() {
    }

    /**
     * @return a new instance of a DatastoreWriter
     */
    protected DataStoreWriter<T> createWriter() {
        return new DataStoreWriter<T>();
    }

    /**
     * @see com.google.appengine.tools.mapreduce.Output#finish(java.util.List)
     */
    @Override
    public Void finish(Collection<? extends OutputWriter<T>> outputWriters) throws IOException {
        return null;
    }

    @Override
    public List<? extends OutputWriter<T>> createWriters(int numShards) {
        ImmutableList.Builder<OutputWriter<T>> out = ImmutableList.builder();
        for (int i = 0; i < numShards; i++) {
            out.add(createWriter());
        }
        return out.build();
    }
}