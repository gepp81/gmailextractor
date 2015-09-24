package ar.com.gep.wordcount.task.mr.reducer;

import org.joda.time.DateTime;

import ar.com.gep.wordcount.task.Task;
import ar.com.gep.wordcount.task.mr.entity.CountWordEntity;

import com.google.appengine.tools.mapreduce.Reducer;
import com.google.appengine.tools.mapreduce.ReducerInput;

public class CountReducer extends Reducer<String, Integer, CountWordEntity> {

    private static final long serialVersionUID = 2557723051751329047L;

    @Override
    public void reduce(String key, ReducerInput<Integer> values) {
        Integer sum = 0;
        while (values.hasNext()) {
            values.next();
            sum++;
        }
        String yesterday = DateTime.now().minusDays(1).toString(Task.FORMAT_DATE);

        CountWordEntity count = new CountWordEntity(key, sum, yesterday);

        getContext().emit(count);
    }

}
