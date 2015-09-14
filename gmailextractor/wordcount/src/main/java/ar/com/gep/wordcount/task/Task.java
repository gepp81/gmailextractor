package ar.com.gep.wordcount.task;

import java.io.IOException;
import java.util.Map;

import ar.com.gep.wordcount.config.TaskService;

import com.google.inject.Inject;

public abstract class Task {

    @Inject
    private TaskService taskService;

    public static final String PARAM_ACTION = "action";
    public static final String PARAM_TODAY = "today";

    protected static final String TOKEN = "token";

    public abstract String getAction();

    public abstract void run(final Map<String, String> arguments) throws IOException;

    protected void star(final String action, final Map<String, String> parameters) {
        taskService.enqueueTask(action, parameters);
    }
}
