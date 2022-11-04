import com.github.andy.elva.model.Measurement;
import io.github.andyswe.elva.data.reader.MeasurementDao;
import io.github.andyswe.elva.data.reader.StoreCommand;
import org.hamcrest.MatcherAssert;
import org.jdbi.v3.core.Jdbi;
import org.jdbi.v3.sqlobject.SqlObjectPlugin;
import org.junit.Test;

import java.util.List;

import static org.hamcrest.Matchers.greaterThan;

public class StoreCommandTest {

    @Test
    public void storeTest() {

        Jdbi jdbi = Jdbi.create("jdbc:h2:./build/test/test"+System.currentTimeMillis());
        jdbi.installPlugin(new SqlObjectPlugin());

        jdbi.withExtension(MeasurementDao.class, dao -> {
            dao.createTable();
            return null;
        });

        Runnable command = new StoreCommand(jdbi);
        command.run();

        List<Measurement> result = jdbi.withExtension(MeasurementDao.class, dao -> {
            return dao.list();
        });
        MatcherAssert.assertThat(result.size(), greaterThan(0));
    }
}
