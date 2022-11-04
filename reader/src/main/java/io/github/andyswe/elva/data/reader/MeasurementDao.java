package io.github.andyswe.elva.data.reader;

import com.github.andy.elva.model.Measurement;
import org.jdbi.v3.sqlobject.config.RegisterBeanMapper;
import org.jdbi.v3.sqlobject.customizer.Bind;
import org.jdbi.v3.sqlobject.statement.SqlQuery;
import org.jdbi.v3.sqlobject.statement.SqlUpdate;

import java.util.Date;
import java.util.List;

public interface MeasurementDao {

    @SqlUpdate("CREATE TABLE IF NOT EXISTS ELVA (date TIMESTAMP PRIMARY KEY, power INT, el INT, va INT)")
    void createTable();

    @SqlUpdate("INSERT INTO ELVA(date, power, el, va) VALUES (:date, :power, :el, :va)")
    void add(@Bind("date") Date date, @Bind("power") int power, @Bind("el") int el, @Bind("va") int va);

    @SqlQuery("SELECT * FROM ELVA")
    @RegisterBeanMapper(Measurement.class)
    List<Measurement> list();

    @SqlQuery("SELECT * FROM ELVA ORDER BY date DESC LIMIT 1")
    @RegisterBeanMapper(Measurement.class)
    List<Measurement> listLast();

    @SqlQuery("SELECT * FROM ELVA WHERE DATE > :afterDate::timestamp")
    @RegisterBeanMapper(Measurement.class)
    List<Measurement> listAfter(@Bind("afterDate")Date after);

    @SqlQuery("SELECT COUNT(*) FROM ELVA")
    int getRowCount();

}
