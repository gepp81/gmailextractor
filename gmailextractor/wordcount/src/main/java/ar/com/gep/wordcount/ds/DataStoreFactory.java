package ar.com.gep.wordcount.ds;

import static com.googlecode.objectify.ObjectifyService.factory;
import ar.com.gep.wordcount.rss.ChannelEntity;
import ar.com.gep.wordcount.rss.ChannelError;
import ar.com.gep.wordcount.rss.EntryEntity;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class DataStoreFactory {

    static {
        factory().register(ChannelEntity.class);
        factory().register(EntryEntity.class);
        factory().register(ChannelError.class);
    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }
}
