package com.github.andy.elva.model.renderer;

import com.github.andy.elva.model.Measurement;

import java.io.PrintWriter;
import java.time.Duration;
import java.time.ZonedDateTime;
import java.util.Date;
import java.util.List;

public class DefaultRenderer implements Renderer {

    private static final Duration RESOLUTION = Duration.ofMinutes(1);

    private Accumulative movingAvergageVa;
    private MovingAverage movingAvergagePower;
    private List<Measurement> data;
    private Duration duration;
    private boolean power;
    private boolean el;
    private boolean va;
    private int lastVa;


    public DefaultRenderer(List<Measurement> data, Duration movingAverageDuration, boolean power, boolean el, boolean va) {
        this.data = data;
        this.duration = movingAverageDuration;
        this.power = power;
        this.el = el;
        this.va = va;
        int frames = Math.toIntExact(movingAverageDuration.dividedBy(RESOLUTION));
        if (power) {
            this.movingAvergagePower = new MovingAverage(frames);
        }
        if (va) {
            this.movingAvergageVa = new Accumulative(data.get(0).getVa());
        }
    }

    @Override
    public void accept(PrintWriter out) {
        String header = "Date," + (power ? "Power," : "") + (el ? "El," : "") + (va ? "Va," : "") + "\n";
        System.out.println(header);
        out.write(header);
        Measurement firstMeasurement = data.get(0);
        int firstEl = firstMeasurement.getEl();
        lastVa = firstMeasurement.getVa();
        data.stream().sequential().forEach(m -> {
            String row = getDateWithAdjustedTimeZone(m.getDate()) + ",";
            double avg = 0;
            double avgVa = 0;
            if (movingAvergagePower != null) {
                avg = movingAvergagePower.next(m.getPower());
            }
            if (movingAvergageVa != null) {
                avgVa = movingAvergageVa.next(m.getVa()-lastVa);
                lastVa = m.getVa();
            }
            row = row + (power ? avg + "," : "") + (el ? (m.getEl() - firstEl) / 100 + "," : "") + (va ? avgVa + ",\n" : "\n");
            out.write(row);
        });
    }

    private String getDateWithAdjustedTimeZone(Date m) {
        return m.toString();
    }
}
