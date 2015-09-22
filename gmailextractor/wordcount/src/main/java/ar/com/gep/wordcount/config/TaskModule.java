package ar.com.gep.wordcount.config;

import ar.com.gep.wordcount.task.CleanerTask;
import ar.com.gep.wordcount.task.RSSExtractTask;
import ar.com.gep.wordcount.task.mr.WordCountMR;

import com.google.inject.AbstractModule;
import com.google.inject.Provides;
import com.google.inject.Singleton;

public class TaskModule extends AbstractModule {

    @Override
    protected void configure() {
    }

    @Provides
    @Singleton
    public TaskService provideTasksService() {
        TaskService taskService = new TaskService();
        taskService.registerTask(new RSSExtractTask(taskService));
        taskService.registerTask(new CleanerTask(taskService));
        taskService.registerTask(new WordCountMR(taskService));
        return taskService;
    }

}
