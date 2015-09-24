package ar.com.gep.wordcount.api;

import static ar.com.gep.wordcount.ds.DataStoreFactory.ofy;

import java.util.Collections;
import java.util.List;
import java.util.Map;

import javax.inject.Named;

import org.joda.time.DateTime;
import org.joda.time.format.DateTimeFormat;
import org.joda.time.format.DateTimeFormatter;

import ar.com.gep.wordcount.api.dto.CountWordDTO;
import ar.com.gep.wordcount.task.Task;
import ar.com.gep.wordcount.task.mr.entity.CountWordEntity;

import com.google.api.server.spi.config.Api;
import com.google.api.server.spi.config.ApiMethod;
import com.google.api.server.spi.config.DefaultValue;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;
import com.googlecode.objectify.cmd.Query;

@Api(name = "countWord", version = "v1", description = "Get the most used word in a day, week or month. "
        + "Can set the number of results. Max = 10.")
public class CountWordEndpoint {

    private static final String MONTH = "month";
    private static final String DAY = "day";
    private static final String WORD = "word";
    private static final String FILTER_DATE = "date =";
    private static final String PARAM_DAY = DAY;
    private static final String PARAM_NAME = "name";
    private static final String PARAM_TYPE = "type";
    private static final String PARAM_LIMIT = "limit";

    @ApiMethod(name = "getCount")
    public CountWordEntity getCount(@Named(PARAM_NAME) String name, @Named(PARAM_DAY) String day) {

        CountWordEntity countWordEntity = ofy().load().type(CountWordEntity.class).filter(FILTER_DATE, day)
                .filter(WORD, name).list().get(0);
        return countWordEntity;
    }

    @ApiMethod(name = "tops")
    public List<CountWordDTO> tops(@Named(PARAM_TYPE) final String type, @Named(PARAM_DAY) final String day,
            @Named(PARAM_LIMIT) @DefaultValue("10") final Integer limit) throws Exception {

        if (limit <= 10) {
            Query<CountWordEntity> query;

            if (type.equals(DAY)) {
                query = ofy().load().type(CountWordEntity.class).filter(FILTER_DATE, day);
            } else {
                DateTimeFormatter formatter = DateTimeFormat.forPattern(Task.FORMAT_DATE);
                DateTime minDay = formatter.parseDateTime(day);
                if (type.equals(MONTH)) {
                    minDay = minDay.minusDays(30);
                } else {
                    minDay = minDay.minusDays(7);
                }
                System.out.println(minDay.toString(Task.FORMAT_DATE));
                query = ofy().load().type(CountWordEntity.class).filter("date <=", day)
                        .filter("date >", minDay.toString(Task.FORMAT_DATE));
            }

            List<CountWordEntity> items = query.list();

            Map<String, Integer> values = Maps.newHashMap();

            String wordKey;
            Integer total;
            for (CountWordEntity countWordEntity : items) {
                wordKey = countWordEntity.getWord();
                total = countWordEntity.getTotal();
                if (values.containsKey(wordKey)) {
                    values.put(wordKey, values.get(wordKey) + total);
                } else {
                    values.put(wordKey, total);
                }
            }

            List<CountWordDTO> dtos = Lists.newArrayList();
            CountWordDTO dto;

            for (String word : values.keySet()) {
                dto = new CountWordDTO(word, values.get(word));
                dtos.add(dto);
            }
            Collections.sort(dtos);
            if (dtos.size() <= limit) {
                return dtos;
            }
            return dtos.subList(0, limit);
        }
        throw new Exception("The max limit its 10");

    }
}
