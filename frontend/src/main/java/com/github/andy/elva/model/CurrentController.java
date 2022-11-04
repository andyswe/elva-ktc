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

public class CurrentController extends HttpServlet {

    private static final String DB_HOST = "database";

    private static final long serialVersionUID = 1L;
    private Jdbi jdbi;

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


        PrintWriter writer = response.getWriter();
        response.setContentType("text/plain");

        List<Measurement> data = jdbi.withExtension(MeasurementDao.class, (dao) -> dao.listLast());
        if (data.size() > 1) {
            throw new RuntimeException("Nope");
        }
        Measurement measurement = data.get(0);
        writer.write("Current power " + measurement.power);

        writer.flush();
    }
}
