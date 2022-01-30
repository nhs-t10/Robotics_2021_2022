package org.firstinspires.ftc.teamcode.unitTests.opmodetesting;

import java.time.Clock;
import java.time.Instant;
import java.time.ZoneId;

public class TimeDilatedClock extends Clock {
    private final ZoneId zone = ZoneId.systemDefault();
    private long timeDilation = 0;

    private final long startTime;

    public TimeDilatedClock(long td) {
        this.timeDilation = td;
        this.startTime = System.currentTimeMillis();
    }

    @Override
    public ZoneId getZone() {
        return zone;
    }

    @Override
    public Clock withZone(ZoneId zoneId) {
        return this;
    }

    @Override
    public Instant instant() {
        long delta = System.currentTimeMillis() - startTime;
        return Instant.ofEpochMilli(startTime + delta * timeDilation);
    }

    protected void setTimeDilationFactor(long coef) {
        this.timeDilation = coef;
    }
}
