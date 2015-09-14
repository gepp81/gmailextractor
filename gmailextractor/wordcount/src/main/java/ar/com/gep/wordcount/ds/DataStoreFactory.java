package ar.com.gep.wordcount.ds;

import static com.googlecode.objectify.ObjectifyService.factory;
import ar.com.gep.wordcount.rss.Channel;
import ar.com.gep.wordcount.rss.Entry;

import com.googlecode.objectify.Objectify;
import com.googlecode.objectify.ObjectifyService;

public class DataStoreFactory {

    static {
        factory().register(Channel.class);
        factory().register(Entry.class);

        Channel entity = new Channel();
        entity.setId("http://www.clarin.com/rss/politica/");
        entity.setName("Clarin - Politica");
        ofy().save().entity(entity);

        entity = new Channel();
        entity.setId("http://contenidos.lanacion.com.ar/herramientas/rss/categoria_id=30");
        entity.setName("La Nacion - Politica");
        ofy().save().entity(entity);

        entity = new Channel();
        entity.setId("http://www.pagina12.com.ar/diario/rss/ultimas_noticias.xml");
        entity.setName("Pagina 12 - Ultimas Noticias");
        ofy().save().entity(entity);

    }

    public static Objectify ofy() {
        return ObjectifyService.ofy();
    }
}
