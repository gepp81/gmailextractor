package ar.com.gep.wordcount.task.mr;

import java.io.IOException;
import java.util.Map;

import org.joda.time.DateTime;

import ar.com.gep.wordcount.config.TaskService;
import ar.com.gep.wordcount.rss.EntryEntity;
import ar.com.gep.wordcount.task.Task;
import ar.com.gep.wordcount.task.mr.entity.CountWordEntity;

import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterOperator;
import com.google.appengine.api.datastore.Query.FilterPredicate;

public class WordCountMR extends Task {

    public static String ACTION = "countwords";

    public WordCountMR(TaskService taskService) {
        super(taskService);
    }

    @Override
    public String getAction() {
        return ACTION;
    }

    @Override
    public void run(Map<String, String> arguments) throws IOException {
        Filter filter = new FilterPredicate(ar.com.gep.wordcount.ds.Entity.DATE, FilterOperator.EQUAL, DateTime.now()
                .minusDays(1).toString(Task.FORMAT_DATE));

        Filter[] filters = { filter };

        MRJobRunner<EntryEntity, String, Integer, CountWordEntity> jobRunner = new MRJobRunner<EntryEntity, String, Integer, CountWordEntity>(
                filters);
        jobRunner.runJob();
    }

}
