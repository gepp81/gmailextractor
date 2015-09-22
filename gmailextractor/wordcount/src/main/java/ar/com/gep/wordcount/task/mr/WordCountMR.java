package ar.com.gep.wordcount.task.mr;

import java.io.IOException;
import java.util.Map;

import org.joda.time.DateTime;

import ar.com.gep.wordcount.config.TaskService;
import ar.com.gep.wordcount.task.Task;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.Query;
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
        Query query = new Query(ar.com.gep.wordcount.ds.Entity.ENTRY_ENTITY).setFilter(filter);

        MRJobRunner<Entity, String, Integer, Entity> jobRunner = new MRJobRunner<Entity, String, Integer, Entity>(query);
        jobRunner.runJob();
    }

}
