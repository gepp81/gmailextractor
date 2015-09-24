package ar.com.gep.wordcount.task;

import static ar.com.gep.wordcount.ds.DataStoreFactory.ofy;

import java.io.IOException;
import java.net.URL;
import java.net.URLConnection;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.commons.lang3.StringEscapeUtils;

import ar.com.gep.wordcount.config.TaskService;
import ar.com.gep.wordcount.rss.ChannelEntity;
import ar.com.gep.wordcount.rss.ChannelError;
import ar.com.gep.wordcount.rss.EntryEntity;
import ar.com.gep.wordcount.rss.RSSEntityDAO;

import com.google.appengine.api.datastore.Entity;
import com.google.appengine.api.datastore.QueryResultList;
import com.google.apphosting.api.ApiProxy;
import com.rometools.rome.feed.synd.SyndEntry;
import com.rometools.rome.feed.synd.SyndFeed;
import com.rometools.rome.io.SyndFeedInput;
import com.rometools.rome.io.XmlReader;

public class RSSExtractTask extends Task {

    private static final Integer DELTA_TIME = 3000;
    private static final Long MAX_TIME = 45000l;
    public static final String ACTION = "getrss";

    public RSSExtractTask(TaskService taskService) {
        super(taskService);
    }

    @Override
    public String getAction() {
        return ACTION;
    }

    @Override
    public void run(Map<String, String> arguments) throws IOException {
        QueryResultList<Entity> query = RSSEntityDAO.getChannels(arguments.get(TOKEN));

        ChannelEntity channel = getChannel(query);

        if (!query.getCursor().toWebSafeString().isEmpty()) {
            arguments.put(TOKEN, query.getCursor().toWebSafeString());
            this.start(getAction(), arguments);
        }

        Logger.getAnonymousLogger().info("Canal: ".concat(channel.getName()));
        try {
            SyndFeedInput input = new SyndFeedInput();

            URLConnection conn = new URL(channel.getUrl()).openConnection();

            Long timeout = ApiProxy.getCurrentEnvironment().getRemainingMillis();
            conn.setConnectTimeout(timeout.intValue());
            if (timeout > MAX_TIME - DELTA_TIME) {
                conn.setConnectTimeout(MAX_TIME.intValue() - DELTA_TIME.intValue());
            }

            SyndFeed feed = input.build(new XmlReader(conn));
            EntryEntity entity;
            String data;
            for (SyndEntry entry : feed.getEntries()) {
                data = entry.getDescription().getValue().replaceAll("<.*?>", "");
                data = StringEscapeUtils.unescapeHtml4(data);
                if (!data.isEmpty()) {
                    entity = new EntryEntity(arguments.get(PARAM_TODAY), channel.getName(), entry.getTitle(), data);
                    ofy().save().entity(entity);
                }
            }

        } catch (Exception e) {
            Logger.getAnonymousLogger().severe("Dont get data from: " + channel.getId());
            ChannelError error = new ChannelError(channel, arguments.get(PARAM_TODAY));
            ofy().save().entity(error).now();
        }
    }

    private ChannelEntity getChannel(QueryResultList<Entity> query) {
        Entity entity = query.get(0);
        ChannelEntity channel = new ChannelEntity();
        channel.setId((String) entity.getProperty("id"));
        channel.setName((String) entity.getProperty("name"));
        channel.setUrl((String) entity.getProperty("url"));
        return channel;
    }

}
