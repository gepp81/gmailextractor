package ar.com.gep.wordcount.task;

import static ar.com.gep.wordcount.ds.DataStoreFactory.ofy;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang3.StringEscapeUtils;

import ar.com.gep.wordcount.config.TaskService;
import ar.com.gep.wordcount.rss.EntryEntity;

public class CleanerTask extends Task {

    public static final String ACTION = "cleaner";
    
    public CleanerTask(TaskService taskService) {
        super(taskService);
    }

    @Override
    public String getAction() {
        return ACTION;
    }

    @Override
    public void run(Map<String, String> arguments) throws IOException {
        List<EntryEntity> entries = ofy().load().type(EntryEntity.class).list();
        for (EntryEntity entry : entries) {
            String newValue = entry.getData().replaceAll("<.*?>", "");
            newValue = StringEscapeUtils.unescapeHtml4(newValue);
            entry.setData(newValue);
        }
        if (!entries.isEmpty()) {
            ofy().save().entities(entries);
        }
    }

}
