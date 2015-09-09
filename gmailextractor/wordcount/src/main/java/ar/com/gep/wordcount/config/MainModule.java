package ar.com.gep.wordcount.config;

import ar.com.gep.wordcount.servlet.CronServlet;
import ar.com.gep.wordcount.servlet.TaskServlet;
import ar.com.gep.wordcount.task.GMailExtractTask;

import com.google.inject.servlet.ServletModule;

public class MainModule extends ServletModule {
    @Override
    protected void configureServlets() {
        serve("/crons").with(CronServlet.class);

        serve("/tasks/".concat(GMailExtractTask.ACTION)).with(TaskServlet.class);

    }

}