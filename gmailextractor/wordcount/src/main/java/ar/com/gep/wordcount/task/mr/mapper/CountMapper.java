package ar.com.gep.wordcount.task.mr.mapper;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.KeyValue;
import com.google.appengine.tools.mapreduce.Mapper;

public class CountMapper extends Mapper<Entity, String, Integer> {

    private static final long serialVersionUID = 7325377330311811198L;
    private static final Integer ONE = 1;

    @Override
    public void map(Entity entry) {
        String[] words = ((String) entry.getProperty("data")).split(" ");
        if (words.length > 0) {
            for (String word : words) {
                getContext().emit(new KeyValue<String, Integer>(word, ONE));
            }
        }
    }

}
