package ar.com.gep.wordcount.task;

import java.util.Map;
import java.util.logging.Logger;

public class GMailExtractTask extends Task {

    public static final String ACTION = "getemails";

    private static final Logger LOGGER = Logger.getLogger(GMailExtractTask.class.getSimpleName());

    @Override
    public String getAction() {
        return ACTION;
    }

    @Override
    public void run(Map<String, String> arguments) {
        LOGGER.info(arguments.toString());
    }

}
