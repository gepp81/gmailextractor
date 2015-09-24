package ar.com.gep.wordcount.task;

import static ar.com.gep.wordcount.ds.DataStoreFactory.ofy;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringEscapeUtils;

import ar.com.gep.wordcount.config.TaskService;
import ar.com.gep.wordcount.rss.EntryEntity;

public class CleanerTask extends Task {

    public static final String ACTION = "cleaner";

    private static final String NEW_LINE = "\\n";
    private static final String TABS = "\\t";
    private static final String REPLACEMENT = "";
    private static final String REMOVE_CHAR = "[-+.^:,]";

    public CleanerTask(TaskService taskService) {
        super(taskService);
    }

    @Override
    public String getAction() {
        return ACTION;
    }

    @Override
    public void run(Map<String, String> arguments) throws IOException {

        Logger logger = Logger.getAnonymousLogger();
        try {

            List<EntryEntity> entries = ofy().load().type(EntryEntity.class)
                    .filter("date =", arguments.get(Task.PARAM_TODAY)).list();
            for (EntryEntity entry : entries) {
                String data = entry.getData();
                data = data.toLowerCase().replaceAll(REMOVE_CHAR, REPLACEMENT);
                data = data.replaceAll(TABS, REPLACEMENT);
                data = data.replaceAll(NEW_LINE, REPLACEMENT);
                data = data.replace("“", REPLACEMENT);
                data = data.replace("”", REPLACEMENT);
                data = data.replace(";", REPLACEMENT);
                data = data.replace("(", REPLACEMENT);
                data = data.replace(")", REPLACEMENT);
                data = data.replace("?", REPLACEMENT);
                data = data.replace("¿", REPLACEMENT);
                data = data.replace("¡", REPLACEMENT);
                data = data.replace("!", REPLACEMENT);
                data = data.replace("@", REPLACEMENT);
                data = data.replace("$", REPLACEMENT);
                data = data.replace("\"", REPLACEMENT);
                data = data.replace("'", REPLACEMENT);
                data = data.replace("°", REPLACEMENT);
                data = data.replace("/", REPLACEMENT);
                data = data.replace("\\", REPLACEMENT);
                data = StringEscapeUtils.unescapeHtml4(data);
                entry.setData(data);
            }
            if (!entries.isEmpty()) {
                ofy().save().entities(entries);
            }

        } catch (Exception e) {
            logger.severe(e.getMessage());
            throw e;
        }
    }

}
