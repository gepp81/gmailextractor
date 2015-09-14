package ar.com.gep.wordcount.gmail;

import java.io.IOException;
import java.util.Map;

import com.google.api.services.gmail.Gmail;
import com.google.api.services.gmail.Gmail.Users.Messages.List;
import com.google.api.services.gmail.model.ListMessagesResponse;
import com.google.api.services.gmail.model.Message;

public class GMailExtractor {

    private static final String QUERY = "";

    private Gmail gmail;

    public GMailExtractor(final Gmail gmail) {
        this.gmail = gmail;
    }

    private ListMessagesResponse listMessagesMatchingQuery(Gmail service, String userId, String query,
            Map<String, String> arguments) throws IOException {
        List setQ = service.users().messages().list(userId).setQ(query);
        setQ.setMaxResults(100l);
        if (arguments.containsKey(GMailExtractTask.NEXT_TOKEN)) {
            setQ.setPageToken(arguments.get(GMailExtractTask.NEXT_TOKEN));
        }
        ListMessagesResponse response = setQ.execute();
        return response;
    }

    public ListMessagesResponse getMails(Map<String, String> arguments) throws IOException {
        return listMessagesMatchingQuery(gmail, GMailExtractTask.USER, QUERY, arguments);
    }

    public Message getMessage(Gmail service, String userId, String messageId) throws IOException {
        return service.users().messages().get(userId, messageId).execute();
    }
}
