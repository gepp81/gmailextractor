package ar.com.gep.wordcount.config;

import ar.com.gep.wordcount.servlet.CronServlet;
import ar.com.gep.wordcount.servlet.TaskServlet;
import ar.com.gep.wordcount.task.GMailExtractTask;

import com.google.inject.servlet.ServletModule;

public class MainModule extends ServletModule {
    private static final String TASKS = "/tasks/";
    private static final String CRONS = "/crons";

    @Override
    protected void configureServlets() {
        serve(CRONS).with(CronServlet.class);

        serve(TASKS.concat(GMailExtractTask.ACTION_VALUE)).with(TaskServlet.class);

    }

}