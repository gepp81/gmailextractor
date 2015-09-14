package ar.com.gep.wordcount.config;

import static com.google.appengine.api.taskqueue.TaskOptions.Builder.withUrl;

import java.util.Map;
import java.util.UUID;
import java.util.logging.Logger;

import ar.com.gep.wordcount.task.Task;

import com.google.appengine.api.taskqueue.Queue;
import com.google.appengine.api.taskqueue.QueueFactory;
import com.google.appengine.api.taskqueue.TaskOptions;
import com.google.common.collect.Maps;

public class TaskService {
    private static final String TASKS_URL = "/tasks/%s";
    private Map<String, Task> tasks;

    public TaskService() {
        tasks = Maps.newHashMap();
    }

    public void registerTask(Task task) {
        tasks.put(task.getAction(), task);
    }

    public void enqueueTask(String action, Map<String, String> parameters) {

        Logger.getAnonymousLogger().info(parameters.get("token"));

        TaskOptions options = withUrl(String.format(TASKS_URL, action.toLowerCase())).taskName(
                action + UUID.randomUUID().toString()).method(TaskOptions.Method.POST);

        Logger log = Logger.getAnonymousLogger();
        log.info(options.getUrl());

        if (parameters != null) {
            for (String parameterName : parameters.keySet()) {
                options.param(parameterName, parameters.get(parameterName));
            }
        }
        Queue gaeQueue = QueueFactory.getDefaultQueue();
        gaeQueue.add(options);
    }

    public Task retrieveTask(String action) {
        if (action != null) {
            return tasks.get(action);
        }
        return null;
    }
}
