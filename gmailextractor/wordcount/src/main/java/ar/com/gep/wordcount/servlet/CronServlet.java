package ar.com.gep.wordcount.servlet;

import java.io.IOException;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.joda.time.DateTime;

import ar.com.gep.wordcount.config.TaskService;
import ar.com.gep.wordcount.task.CleanerTask;
import ar.com.gep.wordcount.task.RSSExtractTask;
import ar.com.gep.wordcount.task.Task;
import ar.com.gep.wordcount.task.mr.WordCountMR;

import com.google.common.collect.Maps;
import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class CronServlet extends HttpServlet {

    private static final long serialVersionUID = -1848456503615196530L;

    private static final Logger LOGGER = Logger.getLogger(CronServlet.class.getSimpleName());

    @Inject
    private TaskService taskService;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        String action = req.getParameter(Task.PARAM_ACTION);

        Map<String, String> parameters = Maps.newHashMap();
        parameters.put(Task.PARAM_TODAY, DateTime.now().toString("yyyyMMdd"));
        parameters.put(Task.PARAM_ACTION, action);

        taskService.enqueueTask(action, parameters);

        if (action.equals(RSSExtractTask.ACTION)) {
            LOGGER.info("Ejecutando extraccion de rss");
        } else if (action.equals(CleanerTask.ACTION)) {
            LOGGER.info("Ejecutando limpieza de HTML");
        } else if (action.equals(WordCountMR.ACTION)) {
            LOGGER.info("Contando palabras");
        }
    }

}
