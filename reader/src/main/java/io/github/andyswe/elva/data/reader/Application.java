package io.github.andyswe.elva.data.reader;

import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.ScheduledFuture;

import static java.lang.Thread.sleep;
import static java.util.concurrent.TimeUnit.MINUTES;

public class Application {

    public static String db_host = "database";

    public static void main(String[] args) throws InterruptedException {

        System.out.println("Starting up...");
        ScheduledExecutorService executor = Executors.newScheduledThreadPool(5);
        sleep(4000);
        Jdbi jdbi = getJdbi();

        ScheduledFuture<?> scheduledFuture = executor.scheduleAtFixedRate(new StoreCommand(jdbi), 0, 1, MINUTES);
        System.out.println("Started! (will store readings at specified interval to database)");
        while (true) {
            try {
                sleep(500L);
            } catch (InterruptedException e) {
                System.out.println("Interrupted!");
                executor.shutdown();
            }
        }
    }

    public static Jdbi getJdbi() {
        try {
            String url = "jdbc:postgresql://" + db_host + "/elva";
            Jdbi jdbi = Jdbi.create(url, "postgres", "password").installPlugin(new SqlObjectPlugin());

            jdbi.withExtension(MeasurementDao.class, dao -> {
                dao.createTable();
                System.out.println("Verifying connection to database host '" + db_host + "' successful (rows in table: " + dao.getRowCount() + ")");
                return null;
            });
            return jdbi;
        } catch (Exception e) {
            throw new RuntimeException("Failed to connect to database: " + db_host, e);
        }
    }
}
