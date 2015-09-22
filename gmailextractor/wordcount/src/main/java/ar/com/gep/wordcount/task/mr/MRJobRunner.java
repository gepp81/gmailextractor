package ar.com.gep.wordcount.task.mr;

import java.io.Serializable;

import ar.com.gep.wordcount.task.mr.entity.CountWordEntity;
import ar.com.gep.wordcount.task.mr.mapper.CountMapper;
import ar.com.gep.wordcount.task.mr.reducer.CountReducer;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.tools.mapreduce.MapReduceJob;
import com.google.appengine.tools.mapreduce.MapReduceSettings;
import com.google.appengine.tools.mapreduce.MapReduceSpecification;
import com.google.appengine.tools.mapreduce.Marshaller;
import com.google.appengine.tools.mapreduce.Marshallers;
import com.google.appengine.tools.mapreduce.inputs.DatastoreInput;
import com.google.appengine.tools.mapreduce.outputs.DatastoreOutput;

public class MRJobRunner<Entity, KM extends Serializable, VM extends Serializable, R> {

    protected static final int MAP_SHARDS = 30;
    protected static final int REDUCE_SHARDS = 5;

    private Query query;

    public MRJobRunner(Query query) {
        this.query = query;
    }

    public void runJob() {
        MapReduceJob.start(getSpec(), getSettings());
    }

    @SuppressWarnings({ "unchecked", "rawtypes" })
    public MapReduceSpecification<Entity, String, Integer, CountWordEntity, Void> getSpec() {
        Marshaller<String> intermediateKeyMarshaller = Marshallers.getStringMarshaller();
        Marshaller<Integer> intermediateValueMarshaller = Marshallers.getIntegerMarshaller();

        DatastoreInput input = new DatastoreInput(query, MAP_SHARDS);
        MapReduceSpecification<Entity, String, Integer, CountWordEntity, Void> spec = new MapReduceSpecification.Builder(
                input, new CountMapper(), new CountReducer(), new DatastoreOutput())
                .setKeyMarshaller(intermediateKeyMarshaller).setValueMarshaller(intermediateValueMarshaller)
                .setNumReducers(REDUCE_SHARDS).build();

        return spec;
    }

    public static MapReduceSettings getSettings() {
        MapReduceSettings settings = new MapReduceSettings.Builder().setBucketName("dev-aileron-838.appspot.com")
                .setWorkerQueueName("mrqueue").build();
        return settings;
    }
}