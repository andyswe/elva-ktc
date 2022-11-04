package com.github.andy.elva.model;

import com.github.andy.elva.model.renderer.DefaultRenderer;
import com.github.andy.elva.model.renderer.Renderer;
import io.github.andyswe.elva.data.reader.Application;
import io.github.andyswe.elva.data.reader.MeasurementDao;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.jdbi.v3.core.Jdbi;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.Enumeration;
import java.util.List;

public class DataController extends HttpServlet {

    private static final String DB_HOST = "database";

    private static final long serialVersionUID = 1L;
    private Jdbi jdbi;
    List<Measurement> data = null;
    long dataFetchTimeStamp = 0;


    @Override
    public void init() throws ServletException {
        Application.db_host = DB_HOST;
        System.out.println("Started data controller servlet");
        super.init();
        if (this.jdbi == null) {
            this.jdbi = Application.getJdbi();
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String timeUnit = null;
        Integer quantity = 0;
        boolean power = false;
        boolean el = false;
        boolean va = false;
        Integer historyDays =1;

        Enumeration<String> parameterNames = request.getParameterNames();
        while (parameterNames.hasMoreElements()) {
            String paramName = parameterNames.nextElement();
            if (paramName.equals("quantity")) {
                quantity = Integer.valueOf(request.getParameterValues("quantity")[0]);
            }
            if (paramName.equals("historyDays")) {
                historyDays = Integer.valueOf(request.getParameterValues("historyDays")[0]);
            }
            if (paramName.equals("timeUnit")) {
                timeUnit = request.getParameterValues("timeUnit")[0].toUpperCase();
            }
            if (paramName.equals("power")) {
                power = Boolean.valueOf(request.getParameterValues("power")[0]);
            }
            if (paramName.equals("el")) {
                el = Boolean.valueOf(request.getParameterValues("el")[0]);
            }
            if (paramName.equals("va")) {
                va = Boolean.valueOf(request.getParameterValues("va")[0]);
            }
            //System.out.println("REQ: " + timeUnit + "," + +quantity + "," + power + "," + el + "," + va);
        }
        PrintWriter writer = response.getWriter();
        response.setContentType("text/csv");

        boolean expired = System.currentTimeMillis() - dataFetchTimeStamp > 60000;
        if (data == null || expired) {
            Date date = Date.valueOf(LocalDateTime.now().minusDays(historyDays).toLocalDate());
            data = jdbi.withExtension(MeasurementDao.class, (dao) -> dao.listAfter(date));
            //System.out.println("Fetched data");
        }

        Duration duration;
        if (timeUnit.equals("WEEKS")) {
            timeUnit = "DAYS";
            quantity = quantity * 7;
        } else if (timeUnit.equals("MONTHS")) {
            timeUnit = "DAYS";
            quantity = quantity * 30;
        }
        duration = Duration.of(quantity, ChronoUnit.valueOf(timeUnit));
        Renderer renderer = new DefaultRenderer(data, duration, power, el, va);
        renderer.accept(writer);
        writer.flush();
    }
}
