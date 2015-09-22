package ar.com.gep.wordcount.task.mr.mapper;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.tools.mapreduce.KeyValue;
import com.google.appengine.tools.mapreduce.Mapper;

public class CountMapper extends Mapper<Entity, String, Integer> {

    private static final String REPLACEMENT = "";
    private static final String REMOVE_CHAR = "[-+.^:,]";
    private static final String SPLIT = " ";
    private static final String DATA = "data";
    private static final Integer ONE = 1;

    private static final long serialVersionUID = 7325377330311811198L;

    @Override
    public void map(Entity entry) {
        String data = ((String) entry.getProperty(DATA)).replaceAll(REMOVE_CHAR, REPLACEMENT);
        data = data.toLowerCase();
        String[] words = data.split(SPLIT);
        if (words.length > 0) {
            for (String word : words) {
                getContext().emit(new KeyValue<String, Integer>(word, ONE));
            }
        }
    }

}
