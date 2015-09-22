package ar.com.gep.wordcount.task.mr.entity;

import com.googlecode.objectify.annotation.Entity;
import com.googlecode.objectify.annotation.Id;

@Entity
public class CountWordEntity {

    private static final String SEPARATOR = "-";

    @Id
    private String id;

    private String word;

    private Integer total;

    private String date;

    public CountWordEntity() {
        // due objectify
    }

    public CountWordEntity(final String word, final Integer total, final String date) {
        this.id = date.concat(SEPARATOR).concat(word);
        this.word = word;
        this.total = total;
        this.date = date;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getWord() {
        return word;
    }

    public void setWord(String word) {
        this.word = word;
    }

    public Integer getTotal() {
        return total;
    }

    public void setTotal(Integer total) {
        this.total = total;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public static String generateId(String word, String date) {
        return date.concat(SEPARATOR).concat(word);
    }
}
