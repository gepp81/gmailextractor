package ar.com.gep.wordcount.task;

import java.io.IOException;
import java.util.Map;

import ar.com.gep.wordcount.config.TaskService;

public abstract class Task {

    private TaskService taskService;

    public static final String FORMAT_DATE = "yyyyMMdd";

    public static final String PARAM_ACTION = "action";
    public static final String PARAM_TODAY = "today";

    protected static final String TOKEN = "token";

    public Task(TaskService taskService) {
        this.taskService = taskService;
    }

    public abstract String getAction();

    public abstract void run(final Map<String, String> arguments) throws IOException;

    protected void start(final String action, final Map<String, String> arguments) {
        taskService.enqueueTask(action, arguments);
    }
}
