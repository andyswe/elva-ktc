package com.github.andy.elva.model;


import com.github.andy.elva.model.renderer.DefaultRenderer;
import com.github.andy.elva.model.renderer.Renderer;
import io.github.andyswe.elva.data.reader.Application;
import io.github.andyswe.elva.data.reader.MeasurementDao;
import org.jdbi.v3.core.Jdbi;
import org.junit.Test;

import java.io.ByteArrayOutputStream;
import java.io.PrintWriter;
import java.sql.Date;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.temporal.ChronoUnit;
import java.util.List;

public class StatisticsTest {

    @Test
    public void shouldCalculateMovingAverageForAllData() {
        Application.db_host = "emma:5433";
        Jdbi jdbi = Application.getJdbi();

        long start= System.currentTimeMillis();
        List<Measurement> data = jdbi.withExtension(MeasurementDao.class, MeasurementDao::list);
        System.out.println("Get data in (s): " + ((double)System.currentTimeMillis() - start)/1000.0);

        Duration duration = Duration.ofDays(30);
        Renderer renderer = new DefaultRenderer(data, duration, true, true, true);
        ByteArrayOutputStream bos = new ByteArrayOutputStream();
        PrintWriter writer = new PrintWriter(bos);

        start = System.currentTimeMillis();
        renderer.accept(writer);
        writer.flush();
        System.out.println("Render data in (s): " + ((double)System.currentTimeMillis() - start)/1000.0);
        System.out.println("Datasize is: " + (double)bos.size()/1000000.0);
    }

    @Test
    public void shouldGetWithDate() {
        Application.db_host = "emma:5433";
        Jdbi jdbi = Application.getJdbi();

        long start = System.currentTimeMillis();
        java.sql.Date date = Date.valueOf(LocalDateTime.now().minusDays(1).toLocalDate());
        List<Measurement> data = jdbi.withExtension(MeasurementDao.class, dao -> dao.listAfter(date));
        data.forEach(System.out::println);
    }
}