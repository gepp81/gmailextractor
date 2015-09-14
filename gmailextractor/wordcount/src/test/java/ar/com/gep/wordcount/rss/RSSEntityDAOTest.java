package ar.com.gep.wordcount.rss;

import static ar.com.gep.wordcount.ds.DataStoreFactory.ofy;

import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.JUnit4;

import com.google.appengine.api.datastore.QueryResultIterator;
import com.google.appengine.tools.development.testing.LocalDatastoreServiceTestConfig;
import com.google.appengine.tools.development.testing.LocalServiceTestHelper;

@RunWith(JUnit4.class)
public class RSSEntityDAOTest {

    LocalServiceTestHelper helper = new LocalServiceTestHelper(new LocalDatastoreServiceTestConfig());
    RSSEntityDAO dao = new RSSEntityDAO();

    @Before
    public void setUp() {
        helper.setUp();
        
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
    
    @Test
    public void testGetChannel() {
        QueryResultIterator<Channel> channels = RSSEntityDAO.getChannels(null);
        Assert.assertNotNull(channels.next());
    }

}
