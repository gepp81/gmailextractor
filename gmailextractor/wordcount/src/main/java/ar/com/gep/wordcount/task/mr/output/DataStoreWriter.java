package ar.com.gep.wordcount.task.mr.output;

import static ar.com.gep.wordcount.ds.DataStoreFactory.ofy;

import java.io.IOException;

import com.google.appengine.tools.mapreduce.OutputWriter;

public class DataStoreWriter<O> extends OutputWriter<O> {
    private static final long serialVersionUID = 1L;

    /**
     * @see com.google.appengine.tools.mapreduce.OutputWriter#write(java.lang.Object)
     */
    @Override
    public void write(O o) throws IOException {
        doWrite(o);
    }

    /**
     * @param outputInstance
     *            output object to be persisted by the datastore.
     */
    protected void doWrite(O outputInstance) {
        ofy().save().entity(outputInstance).now();
    }

    /**
     * @see com.google.appengine.tools.mapreduce.OutputWriter#close()
     */
    @Override
    public void close() throws IOException {
    }
}