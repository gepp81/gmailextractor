package ar.com.gep.wordcount.task;

import static ar.com.gep.wordcount.ds.DataStoreFactory.ofy;

import java.io.IOException;
import java.net.URL;
import java.util.Map;
import java.util.logging.Logger;

import ar.com.gep.wordcount.rss.Channel;
import ar.com.gep.wordcount.rss.Entry;
import ar.com.gep.wordcount.rss.RSSEntityDAO;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.FeedException;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

public class RSSExtractTask extends Task {

    public static final String ACTION = "getrss";

    @Override
    public String getAction() {
        return ACTION;
    }

    @Override
    public void run(Map<String, String> arguments) throws IOException {
        QueryResultIterator<Channel> query = RSSEntityDAO.getChannels(arguments.get(TOKEN));

        Logger.getAnonymousLogger().severe("Next token: " + query.getCursor().toWebSafeString());

        if (!query.getCursor().toWebSafeString().isEmpty()) {
            arguments.put(TOKEN, query.getCursor().toWebSafeString());
        }

        while (query.hasNext()) {
            Channel channel = query.next();
            Logger.getAnonymousLogger().severe("Canal: ".concat(channel.getName()));
            try {
                SyndFeedInput input = new SyndFeedInput();

                SyndFeed feed = input.build(new XmlReader(new URL(channel.getId())));
                for (SyndEntry entry : feed.getEntries()) {

                    Entry entity = new Entry(arguments.get(PARAM_TODAY), channel.getName(), entry.getTitle(), entry
                            .getDescription().getValue());

                    ofy().save().entity(entity);

                }

            } catch (IllegalArgumentException | FeedException e) {
                Logger.getAnonymousLogger().severe("Dont get data from: " + channel.getId());
            }

        }
    }
}
