package ar.com.gep.wordcount.task;

import java.util.Map;

public abstract class Task {

    public abstract String getAction();

    public abstract void run(final Map<String, String> arguments);
}
