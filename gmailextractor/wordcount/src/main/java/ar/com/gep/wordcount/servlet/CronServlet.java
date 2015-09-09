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
import ar.com.gep.wordcount.task.GMailExtractTask;
import ar.com.gep.wordcount.task.Task;

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
        if (action.equals(GMailExtractTask.ACTION_VALUE)) {
            LOGGER.info("Ejecutando extraccion de mails");

            parameters.put(Task.PARAM_TODAY, DateTime.now().toString("yyyyMMdd"));
            parameters.put(Task.PARAM_ACTION, action);
            taskService.enqueueTask(action, parameters);
        }
    }

}
