package ar.com.gep.wordcount.config;

import ar.com.gep.wordcount.servlet.CronServlet;
import ar.com.gep.wordcount.servlet.TaskServlet;
import ar.com.gep.wordcount.task.CleanerTask;
import ar.com.gep.wordcount.task.RSSExtractTask;
import ar.com.gep.wordcount.task.mr.WordCountMR;

import com.google.inject.servlet.ServletModule;

public class MainModule extends ServletModule {
    private static final String TASKS = "/tasks/";
    private static final String CRONS = "/crons";

    @Override
    protected void configureServlets() {
        serve(CRONS).with(CronServlet.class);
        
        serve(TASKS.concat(RSSExtractTask.ACTION)).with(TaskServlet.class);
        serve(TASKS.concat(CleanerTask.ACTION)).with(TaskServlet.class);
        serve(TASKS.concat(WordCountMR.ACTION)).with(TaskServlet.class);
    }

}
