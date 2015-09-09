package ar.com.gep.wordcount.task;

import java.util.Map;
import java.util.logging.Logger;

public class GMailExtractTask extends Task {

    public static final String ACTION_VALUE = "getemails";

    private static final Logger LOGGER = Logger.getLogger(GMailExtractTask.class.getSimpleName());

    @Override
    public String getAction() {
        return ACTION_VALUE;
    }

    @Override
    public void run(Map<String, String> arguments) {
        LOGGER.info(arguments.toString());
    }

}
