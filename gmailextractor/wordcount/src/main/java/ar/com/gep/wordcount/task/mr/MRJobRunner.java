package ar.com.gep.wordcount.task.mr;

import java.io.Serializable;

import ar.com.gep.wordcount.ds.Entity;
import ar.com.gep.wordcount.rss.EntryEntity;
import ar.com.gep.wordcount.task.mr.entity.CountWordEntity;
import ar.com.gep.wordcount.task.mr.input.DataStoreInput;
import ar.com.gep.wordcount.task.mr.mapper.CountMapper;
import ar.com.gep.wordcount.task.mr.output.DataStoreOutput;
import ar.com.gep.wordcount.task.mr.reducer.CountReducer;

import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.tools.mapreduce.MapReduceJob;
import com.google.appengine.tools.mapreduce.MapReduceSettings;
import com.google.appengine.tools.mapreduce.MapReduceSpecification;
import com.google.appengine.tools.mapreduce.Marshaller;
import com.google.appengine.tools.mapreduce.Marshallers;

public class MRJobRunner<D extends Entity, KM extends Serializable, VM extends Serializable, R> {

    protected static final int MAP_SHARDS = 30;
    protected static final int REDUCE_SHARDS = 5;

    private Filter[] filters;

    public MRJobRunner(Filter[] filters) {
        this.filters = filters;
    }

    public void runJob() {
        MapReduceJob.start(getSpec(), getSettings());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public MapReduceSpecification<D, KM, VM, R, Void> getSpec() {
        Marshaller<String> intermediateKeyMarshaller = Marshallers.getStringMarshaller();
        Marshaller<Integer> intermediateValueMarshaller = Marshallers.getIntegerMarshaller();

        DataStoreInput<EntryEntity> input = new DataStoreInput(EntryEntity.class, MAP_SHARDS, filters);
        DataStoreOutput<CountWordEntity> output = new DataStoreOutput<CountWordEntity>();
        MapReduceSpecification<D, KM, VM, R, Void> spec = new MapReduceSpecification.Builder(input, new CountMapper(),
                new CountReducer(), output).setKeyMarshaller(intermediateKeyMarshaller)
                .setValueMarshaller(intermediateValueMarshaller).setNumReducers(REDUCE_SHARDS).build();

        return spec;
    }

    public static MapReduceSettings getSettings() {
        MapReduceSettings settings = new MapReduceSettings.Builder().setBucketName("dev-aileron-838.appspot.com")
                .setWorkerQueueName("mrqueue").build();
        return settings;
    }
}