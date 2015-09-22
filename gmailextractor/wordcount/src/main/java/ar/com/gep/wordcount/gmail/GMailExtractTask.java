package ar.com.gep.wordcount.gmail;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import ar.com.gep.wordcount.config.TaskService;
import ar.com.gep.wordcount.task.Task;

import com.google.api.services.gmail.Gmail;

public class GMailExtractTask extends Task {

    private static final String SUBJECT = "Subject";
    public static final String ACTION = "getemails";
    public static final String NEXT_TOKEN = "nextToken";
    public static final String USER = "piqui81@gmail.com";

    private static final Logger LOGGER = Logger.getLogger(GMailExtractTask.class.getSimpleName());
    private Gmail gmail;
    private GMailExtractor extractor;

    public GMailExtractTask(TaskService taskService) {
        super(taskService);
    }

    @Override
    public String getAction() {
        return ACTION;
    }

    // private void saveNew(Message msg) {
    // ar.com.gep.wordcount.entity.Message entity = new ar.com.gep.wordcount.entity.Message();
    // entity.setId(msg.getId());
    // entity.setUser(USER);
    // entity.setDate(msg.getInternalDate());
    // for (MessagePartHeader header : msg.getPayload().getHeaders()) {
    // if (header.getName().equals(SUBJECT)) {
    // entity.setTitle(header.getValue());
    // break;
    // }
    // }
    // ofy().save().entity(entity);
    // }
    //
    // private void saveNewMails(final List<Message> messages, Gmail gmail) throws IOException {
    // for (Message message : messages) {
    // int count = ofy().load().type(ar.com.gep.wordcount.entity.Message.class).filterKey("=", message.getId())
    // .count();
    // if (count == 0) {
    // Message msg = extractor.getMessage(gmail, USER, message.getId());
    // saveNew(msg);
    // }
    // }
    // }

    @Override
    public void run(Map<String, String> arguments) throws IOException {
        LOGGER.info(arguments.toString());

        // gmail = Utils.loadGmailClient();
        // extractor = new GMailExtractor(gmail);

        // ListMessagesResponse response = extractor.getMails(arguments);

        // if (response.getNextPageToken() != null) {
        // arguments.put(NEXT_TOKEN, response.getNextPageToken());
        // star(getAction(), arguments);
        // }
        // saveNewMails(response.getMessages(), gmail);

    }

}
