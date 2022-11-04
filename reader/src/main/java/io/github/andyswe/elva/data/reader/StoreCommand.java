package io.github.andyswe.elva.data.reader;

import com.github.andy.elva.model.Measurement;
import org.jdbi.v3.core.Jdbi;

public class StoreCommand implements Runnable {
    private final Jdbi jdbi;
    private final Reader reader = new Reader();

    public StoreCommand(Jdbi jdbi) {
        this.jdbi = jdbi;
    }

    @Override
    public void run() {
        Measurement measurement = null;
        try {
            measurement = reader.obtain();
            final Measurement finalMeasurement = measurement;
            jdbi.withExtension(MeasurementDao.class, dao -> {
                dao.add(
                        finalMeasurement.getDate(),
                        finalMeasurement.getPower(),
                        finalMeasurement.getEl(),
                        finalMeasurement.getVa());
                return finalMeasurement;
            });
        } catch (Throwable e) {
            e.printStackTrace();
            System.out.println("Failed to store " + measurement);
        }
    }
}
