package ar.com.gep.wordcount.config;

import static ar.com.gep.wordcount.ds.DataStoreFactory.ofy;
import ar.com.gep.wordcount.servlet.CronServlet;
import ar.com.gep.wordcount.servlet.TaskServlet;
import ar.com.gep.wordcount.task.CleanerTask;
import ar.com.gep.wordcount.task.RSSExtractTask;
import ar.com.gep.wordcount.task.mr.WordCountMR;
import ar.com.gep.wordcount.task.mr.entity.CountWordEntity;

import com.google.inject.servlet.ServletModule;

public class MainModule extends ServletModule {
    private static final String TASKS = "/tasks/";
    private static final String CRONS = "/crons";

    @Override
    protected void configureServlets() {
        serve(CRONS).with(CronServlet.class);
        
        CountWordEntity w = new CountWordEntity("guille", 5, "20150921");
        ofy().save().entity(w);
        w = new CountWordEntity("guille", 1, "20150922");
        ofy().save().entity(w);
        w = new CountWordEntity("naty", 1, "20150922");
        ofy().save().entity(w);

        serve(TASKS.concat(RSSExtractTask.ACTION)).with(TaskServlet.class);
        serve(TASKS.concat(CleanerTask.ACTION)).with(TaskServlet.class);
        serve(TASKS.concat(WordCountMR.ACTION)).with(TaskServlet.class);
    }

}
