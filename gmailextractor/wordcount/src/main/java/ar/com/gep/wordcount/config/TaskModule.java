package ar.com.gep.wordcount.config;

import ar.com.gep.wordcount.task.GMailExtractTask;

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
        taskService.registerTask(new GMailExtractTask());
        return taskService;
    }

}
