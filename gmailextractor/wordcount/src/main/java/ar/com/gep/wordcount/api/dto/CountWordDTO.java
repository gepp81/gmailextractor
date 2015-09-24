package ar.com.gep.wordcount.api.dto;

public class CountWordDTO implements Comparable<CountWordDTO> {

    private String word;

    private Integer total;

    public CountWordDTO(String word, Integer total) {
        this.word = word;
        this.total = total;
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

    @Override
    public boolean equals(Object obj) {
        if (obj == null) {
            return false;
        }
        CountWordDTO other = (CountWordDTO) obj;
        if (this.getWord().equals(other.getWord())) {
            return true;
        }
        return false;
    }

    @Override
    public int compareTo(CountWordDTO o) {
        if (o.getTotal().equals(getTotal())) {
            return 0;
        }
        return o.getTotal() > getTotal() ? 1 : -1;
    }

}
