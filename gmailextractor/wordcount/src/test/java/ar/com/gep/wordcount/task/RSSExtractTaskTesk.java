package ar.com.gep.wordcount.task;

import static ar.com.gep.wordcount.ds.DataStoreFactory.ofy;

import java.io.IOException;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import ar.com.gep.wordcount.config.TaskService;
import ar.com.gep.wordcount.rss.ChannelEntity;
import ar.com.gep.wordcount.rss.RSSEntityDAO;

import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;
import com.google.common.collect.Maps;

@RunWith(JUnit4.class)
public class RSSExtractTaskTesk {

    LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    RSSEntityDAO dao = new RSSEntityDAO();

    @Before
    public void setUp() {
        helper.setUp();

        ChannelEntity entity = new ChannelEntity();
        entity.setUrl("http://www.clarin.com/diario/hoy/deportes.xml");
        entity.setName("Clarin-Deportes");
        entity.setId("Clarin-Deportes");
        ofy().save().entity(entity);

        entity = new ChannelEntity();
        entity.setUrl("http://contenidos.lanacion.com.ar/herramientas/rss/categoria_id=30");
        entity.setName("La Nacion - Politica");
        entity.setId("La Nacion - Politica");
        ofy().save().entity(entity);

        entity = new ChannelEntity();
        entity.setUrl("http://www.pagina12.com.ar/diario/rss/ultimas_noticias.xml");
        entity.setName("Pagina 12 - Ultimas Noticias");
        entity.setId("Pagina 12 - Ultimas Noticias");
        ofy().save().entity(entity);

    }

    @Test
    public void testGetChannel() throws IOException {
        RSSExtractTask task = new RSSExtractTask(new TaskService());
        // Map<String, String> arguments = Maps.newHashMap();
        // task.run(arguments);
    }

}
