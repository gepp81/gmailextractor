package ar.com.gep.wordcount.task.mr;

import java.io.IOException;
import java.util.Map;

import org.joda.time.DateTime;

import com.google.appengine.api.datastore.Query;
import com.google.appengine.api.datastore.Query.Filter;
import com.google.appengine.api.datastore.Query.FilterPredicate;
import com.google.appengine.api.datastore.Query.FilterOperator;

import ar.com.gep.wordcount.config.TaskService;
import ar.com.gep.wordcount.ds.Entity;
import ar.com.gep.wordcount.rss.EntryEntity;
import ar.com.gep.wordcount.task.Task;
import ar.com.gep.wordcount.task.mr.entity.CountWordEntity;

public class WordCountMR extends Task {

    private static final String ENTRY_ENTITY = "EntryEntity";
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
        Filter filter = new FilterPredicate(Entity.DATE, FilterOperator.EQUAL, DateTime.now().minusDays(1).toString("yyyyMMdd"));
        Query query = new Query(ENTRY_ENTITY).setFilter(filter);

        MRJobRunner<EntryEntity, String, Integer, CountWordEntity> jobRunner = new MRJobRunner<EntryEntity, String, Integer, CountWordEntity>(
                query);
        jobRunner.runJob();
    }

}
