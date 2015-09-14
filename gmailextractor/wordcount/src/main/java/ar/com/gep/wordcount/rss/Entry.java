package ar.com.gep.wordcount.rss;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;
import com.googlecode.objectify.annotation.Index;

@Entity
public class Entry {

    @Id
    private String id;

    @Index
    private String channel;

    @Index
    private String date;

    private String title;

    private String data;

    public Entry(String date, String channel, String title, String data) {
        this.id = channel.concat("-").concat(date).concat("-").concat(title);
        this.channel = channel;
        this.date = date;
        this.data = data;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getChannel() {
        return channel;
    }

    public void setChannel(String channel) {
        this.channel = channel;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

}
