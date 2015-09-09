package ar.com.gep.wordcount.task;

import java.util.Map;

public abstract class Task {

    public static final String PARAM_ACTION = "action";
    public static final String PARAM_TODAY = "today";

    public abstract String getAction();

    public abstract void run(final Map<String, String> arguments);
}
