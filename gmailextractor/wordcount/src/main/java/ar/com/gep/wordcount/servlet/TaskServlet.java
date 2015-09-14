package ar.com.gep.wordcount.servlet;

import java.io.IOException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;
import java.util.logging.Logger;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import ar.com.gep.wordcount.config.TaskService;
import ar.com.gep.wordcount.task.Task;

import com.google.inject.Inject;
import com.google.inject.Singleton;

@Singleton
public class TaskServlet extends HttpServlet {

    private static final long serialVersionUID = -7077954908062065619L;

    private static final Logger LOGGER = Logger.getAnonymousLogger();

    @Inject
    private TaskService taskService;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        LOGGER.info(req.getParameter(Task.PARAM_ACTION));
        Task task = taskService.retrieveTask(req.getParameter(Task.PARAM_ACTION));
        try {
            if (task != null) {
                task.run(buildParameters(req));
                resp.setStatus(HttpServletResponse.SC_OK);
            } else {
                resp.sendError(HttpServletResponse.SC_NOT_FOUND, "Could not find and execute a task");
            }
        } catch (IllegalArgumentException e) {
            resp.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unknown route to task with entiy");
        }
    }

    @SuppressWarnings("rawtypes")
    private Map<String, String> buildParameters(final HttpServletRequest req) {
        Map<String, String> parameters = new HashMap<String, String>();
        Enumeration parameterNames = req.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String name = (String) parameterNames.nextElement();
            parameters.put(name, req.getParameter(name));
        }
        return parameters;
    }

}
