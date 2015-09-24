package ar.com.gep.wordcount.task.mr.mapper;

import ar.com.gep.wordcount.rss.EntryEntity;

import com.google.appengine.tools.mapreduce.KeyValue;
import com.google.appengine.tools.mapreduce.Mapper;

public class CountMapper extends Mapper<EntryEntity, String, Integer> {

    private static final String SPLIT = " ";
    private static final Integer ONE = 1;

    private static final long serialVersionUID = 7325377330311811198L;

    @Override
    public void map(EntryEntity entry) {
        String[] words = entry.getData().split(SPLIT);
        if (words.length > 0) {
            for (String word : words) {
                getContext().emit(new KeyValue<String, Integer>(word, ONE));
            }
        }
    }

}
