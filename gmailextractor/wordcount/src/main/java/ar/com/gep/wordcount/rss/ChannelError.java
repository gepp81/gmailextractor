package ar.com.gep.wordcount.rss;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class ChannelError implements ar.com.gep.wordcount.ds.Entity {

    @Id
    private String id;

    @Index
    private String name;

    private String date;

    public ChannelError(ChannelEntity channel, String date) {
        this.id = channel.getName().concat(date);
        this.name = channel.getName();
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

}
