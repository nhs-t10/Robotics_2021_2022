package org.firstinspires.ftc.teamcode.unitTests.unitconversion;

import org.firstinspires.ftc.teamcode.auxilary.units.TimeUnit;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeUnitTest {
    @Test
    public void test() {
        assertEquals(8.0, TimeUnit.NS.convertTo(TimeUnit.NS, 8), 0.08);
        assertEquals(0.000003, TimeUnit.NS.convertTo(TimeUnit.MS, 3), 0.03);
        assertEquals(1.1e-8, TimeUnit.NS.convertTo(TimeUnit.S, 11), 0.11);
        assertEquals(3.166666666666666e-10, TimeUnit.NS.convertTo(TimeUnit.MIN, 19), 0.19);
        assertEquals(8.333333333333334e-13, TimeUnit.NS.convertTo(TimeUnit.HR, 3), 0.03);
        assertEquals(0.0, TimeUnit.NS.convertTo(TimeUnit.D, 0), 0);
        assertEquals(6.02486047691527e-16, TimeUnit.NS.convertTo(TimeUnit.YR, 19), 0.19);
        assertEquals(3e-7, TimeUnit.NS.convertTo(TimeUnit.JIF, 3), 0.03);

        assertEquals(20000000.0, TimeUnit.MS.convertTo(TimeUnit.NS, 20), 0.2);
        assertEquals(4.0, TimeUnit.MS.convertTo(TimeUnit.MS, 4), 0.04);
        assertEquals(0.02, TimeUnit.MS.convertTo(TimeUnit.S, 20), 0.2);
        assertEquals(0.0, TimeUnit.MS.convertTo(TimeUnit.MIN, 0), 0);
        assertEquals(0.0000030555555555555556, TimeUnit.MS.convertTo(TimeUnit.HR, 11), 0.11);
        assertEquals(1.6203703703703703e-7, TimeUnit.MS.convertTo(TimeUnit.D, 14), 0.14);
        assertEquals(6.024860476915271e-10, TimeUnit.MS.convertTo(TimeUnit.YR, 19), 0.19);
        assertEquals(1.4, TimeUnit.MS.convertTo(TimeUnit.JIF, 14), 0.14);

        assertEquals(11000000000.0, TimeUnit.S.convertTo(TimeUnit.NS, 11), 0.11);
        assertEquals(16000.0, TimeUnit.S.convertTo(TimeUnit.MS, 16), 0.16);
        assertEquals(9.0, TimeUnit.S.convertTo(TimeUnit.S, 9), 0.09);
        assertEquals(0.11666666666666667, TimeUnit.S.convertTo(TimeUnit.MIN, 7), 0.07);
        assertEquals(0.003611111111111111, TimeUnit.S.convertTo(TimeUnit.HR, 13), 0.13);
        assertEquals(0.0002314814814814815, TimeUnit.S.convertTo(TimeUnit.D, 20), 0.2);
        assertEquals(3.170979198376459e-7, TimeUnit.S.convertTo(TimeUnit.YR, 10), 0.1);
        assertEquals(300.0, TimeUnit.S.convertTo(TimeUnit.JIF, 3), 0.03);

        assertEquals(840000000000.0, TimeUnit.MIN.convertTo(TimeUnit.NS, 14), 0.14);
        assertEquals(300000.0, TimeUnit.MIN.convertTo(TimeUnit.MS, 5), 0.05);
        assertEquals(600.0, TimeUnit.MIN.convertTo(TimeUnit.S, 10), 0.1);
        assertEquals(1.0, TimeUnit.MIN.convertTo(TimeUnit.MIN, 1), 0.01);
        assertEquals(0.21666666666666667, TimeUnit.MIN.convertTo(TimeUnit.HR, 13), 0.13);
        assertEquals(0.003472222222222222, TimeUnit.MIN.convertTo(TimeUnit.D, 5), 0.05);
        assertEquals(0.00000380517503805175, TimeUnit.MIN.convertTo(TimeUnit.YR, 2), 0.02);
        assertEquals(0.0, TimeUnit.MIN.convertTo(TimeUnit.JIF, 0), 0);

        assertEquals(14400000000000.0, TimeUnit.HR.convertTo(TimeUnit.NS, 4), 0.04);
        assertEquals(50400000.0, TimeUnit.HR.convertTo(TimeUnit.MS, 14), 0.14);
        assertEquals(36000.0, TimeUnit.HR.convertTo(TimeUnit.S, 10), 0.1);
        assertEquals(420.0, TimeUnit.HR.convertTo(TimeUnit.MIN, 7), 0.07);
        assertEquals(1.0, TimeUnit.HR.convertTo(TimeUnit.HR, 1), 0.01);
        assertEquals(0.75, TimeUnit.HR.convertTo(TimeUnit.D, 18), 0.18);
        assertEquals(0.00045662100456621003, TimeUnit.HR.convertTo(TimeUnit.YR, 4), 0.04);
        assertEquals(6480000.0, TimeUnit.HR.convertTo(TimeUnit.JIF, 18), 0.18);

        assertEquals(86400000000000.0, TimeUnit.D.convertTo(TimeUnit.NS, 1), 0.01);
        assertEquals(777600000.0, TimeUnit.D.convertTo(TimeUnit.MS, 9), 0.09);
        assertEquals(1382400.0, TimeUnit.D.convertTo(TimeUnit.S, 16), 0.16);
        assertEquals(7200.0, TimeUnit.D.convertTo(TimeUnit.MIN, 5), 0.05);
        assertEquals(144.0, TimeUnit.D.convertTo(TimeUnit.HR, 6), 0.06);
        assertEquals(19.0, TimeUnit.D.convertTo(TimeUnit.D, 19), 0.19);
        assertEquals(0.0027397260273972603, TimeUnit.D.convertTo(TimeUnit.YR, 1), 0.01);
        assertEquals(112320000.0, TimeUnit.D.convertTo(TimeUnit.JIF, 13), 0.13);

        assertEquals(94608000000000000.0, TimeUnit.YR.convertTo(TimeUnit.NS, 3), 0.03);
        assertEquals(346896000000.0, TimeUnit.YR.convertTo(TimeUnit.MS, 11), 0.11);
        assertEquals(599184000.0, TimeUnit.YR.convertTo(TimeUnit.S, 19), 0.19);
        assertEquals(5781600.0, TimeUnit.YR.convertTo(TimeUnit.MIN, 11), 0.11);
        assertEquals(157680.0, TimeUnit.YR.convertTo(TimeUnit.HR, 18), 0.18);
        assertEquals(730.0, TimeUnit.YR.convertTo(TimeUnit.D, 2), 0.02);
        assertEquals(2.0, TimeUnit.YR.convertTo(TimeUnit.YR, 2), 0.02);
        assertEquals(63072000000.0, TimeUnit.YR.convertTo(TimeUnit.JIF, 20), 0.2);

        assertEquals(80000000.0, TimeUnit.JIF.convertTo(TimeUnit.NS, 8), 0.08);
        assertEquals(50.0, TimeUnit.JIF.convertTo(TimeUnit.MS, 5), 0.05);
        assertEquals(0.11, TimeUnit.JIF.convertTo(TimeUnit.S, 11), 0.11);
        assertEquals(0.002, TimeUnit.JIF.convertTo(TimeUnit.MIN, 12), 0.12);
        assertEquals(0.00003888888888888889, TimeUnit.JIF.convertTo(TimeUnit.HR, 14), 0.14);
        assertEquals(0.0000017361111111111112, TimeUnit.JIF.convertTo(TimeUnit.D, 15), 0.15);
        assertEquals(2.219685438863521e-9, TimeUnit.JIF.convertTo(TimeUnit.YR, 7), 0.07);
        assertEquals(7.0, TimeUnit.JIF.convertTo(TimeUnit.JIF, 7), 0.07);
    }
}
