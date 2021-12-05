package org.firstinspires.ftc.teamcode.unitTests.unitconversion;

import org.firstinspires.ftc.teamcode.auxilary.units.TimeUnit;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class TimeUnitTest {
    @Test
    public void test() {
        assertEquals(6.8, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("ms"), 6800000.0), 0.0068);
        assertEquals(11.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("ms"), 11200000.0), 0.0112);
        assertEquals(17.6, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("ms"), 17600000.000000004), 0.0176);

        assertEquals(13.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("s"), 13200000000.0), 0.0132);
        assertEquals(1.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("s"), 1200000000.0), 0.0012);
        assertEquals(12.6, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("s"), 12600000000.0), 0.0126);

        assertEquals(18.7, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("mn"), 1122000000000.0), 0.018699999999999998);
        assertEquals(11.8, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("min"), 708000000000.0), 0.011800000000000001);
        assertEquals(6.0, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("min"), 360000000000.0), 0.006);

        assertEquals(3.9, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("hr"), 14040000000000.0), 0.0039);
        assertEquals(16.1, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("hr"), 57960000000000.01), 0.0161);
        assertEquals(16.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("h"), 58680000000000.0), 0.016300000000000002);

        assertEquals(19.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("d"), 1684800000000000.0), 0.0195);
        assertEquals(17.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("d"), 1512000000000000.0), 0.0175);
        assertEquals(19.0, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("d"), 1641600000000000.0), 0.019);

        assertEquals(15.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("y"), 488808000000000000.0), 0.0155);
        assertEquals(16.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("yr"), 514036800000000000.0), 0.016300000000000002);
        assertEquals(14.0, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("yr"), 441504000000000000.0), 0.014);

        assertEquals(2.0, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("jif"), 20000000.0), 0.002);
        assertEquals(7.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("jiffie"), 73000000.0), 0.0073);
        assertEquals(13.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ns"), TimeUnit.forAbbreviation("jif"), 134000000.0), 0.0134);

        assertEquals(15.1, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("ns"), 0.0000151), 0.015099999999999999);
        assertEquals(12.7, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("ns"), 0.000012699999999999999), 0.0127);
        assertEquals(4.9, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("ns"), 0.0000049000000000000005), 0.004900000000000001);

        assertEquals(8.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("s"), 8300.0), 0.0083);
        assertEquals(4.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("s"), 4500.0), 0.0045);
        assertEquals(6.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("s"), 6500.0), 0.0065);

        assertEquals(1.8, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("min"), 108000.0), 0.0018);
        assertEquals(7.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("min"), 444000.0), 0.0074);
        assertEquals(17.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("mn"), 1050000.0), 0.0175);

        assertEquals(11.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("h"), 41400000.0), 0.0115);
        assertEquals(2.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("hr"), 7920000.000000001), 0.0022);
        assertEquals(2.9, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("hr"), 10440000.0), 0.0029);

        assertEquals(9.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("d"), 803520000.0000001), 0.009300000000000001);
        assertEquals(7.1, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("d"), 613440000.0), 0.0070999999999999995);
        assertEquals(11.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("d"), 976320000.0000001), 0.011300000000000001);

        assertEquals(15.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("y"), 479347200000.0), 0.0152);
        assertEquals(19.1, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("yr"), 602337600000.0), 0.019100000000000002);
        assertEquals(0.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("yr"), 12614400000.0), 0.0004);

        assertEquals(5.8, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("jif"), 58.0), 0.0058);
        assertEquals(5.1, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("jif"), 51.0), 0.0050999999999999995);
        assertEquals(1.0, TimeUnit.convertBetween(TimeUnit.forAbbreviation("ms"), TimeUnit.forAbbreviation("jiffy"), 10.0), 0.001);

        assertEquals(19.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("ns"), 1.94e-8), 0.019399999999999997);
        assertEquals(9.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("ns"), 9.3e-9), 0.009300000000000001);
        assertEquals(14.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("ns"), 1.43e-8), 0.0143);

        assertEquals(15.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("ms"), 0.0154), 0.0154);
        assertEquals(13.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("ms"), 0.013300000000000001), 0.013300000000000001);
        assertEquals(1.9, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("ms"), 0.0019), 0.0019);

        assertEquals(11.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("min"), 690.0), 0.0115);
        assertEquals(13.9, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("mn"), 834.0), 0.013900000000000001);
        assertEquals(8.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("mn"), 491.99999999999994), 0.008199999999999999);

        assertEquals(7.9, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("h"), 28440.0), 0.0079);
        assertEquals(10.8, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("h"), 38880.0), 0.0108);
        assertEquals(3.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("h"), 11880.0), 0.0033);

        assertEquals(5.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("d"), 466560.00000000006), 0.0054);
        assertEquals(16.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("d"), 1408320.0), 0.016300000000000002);
        assertEquals(14.9, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("d"), 1287360.0), 0.0149);

        assertEquals(15.9, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("y"), 501422400.0), 0.0159);
        assertEquals(1.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("y"), 40996800.0), 0.0013);
        assertEquals(9.6, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("yr"), 302745600.0), 0.0096);

        assertEquals(15.8, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("jif"), 0.158), 0.0158);
        assertEquals(7.1, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("jiffies"), 0.071), 0.0070999999999999995);
        assertEquals(19.0, TimeUnit.convertBetween(TimeUnit.forAbbreviation("s"), TimeUnit.forAbbreviation("jif"), 0.19), 0.019);

        assertEquals(9.9, TimeUnit.convertBetween(TimeUnit.forAbbreviation("min"), TimeUnit.forAbbreviation("ns"), 1.65e-10), 0.0099);
        assertEquals(2.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("mn"), TimeUnit.forAbbreviation("ns"), 4.1666666666666665e-11), 0.0025);
        assertEquals(14.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("min"), TimeUnit.forAbbreviation("ns"), 2.4166666666666666e-10), 0.0145);

        assertEquals(17.1, TimeUnit.convertBetween(TimeUnit.forAbbreviation("min"), TimeUnit.forAbbreviation("ms"), 0.00028500000000000004), 0.0171);
        assertEquals(12.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("mn"), TimeUnit.forAbbreviation("ms"), 0.00020333333333333333), 0.012199999999999999);
        assertEquals(13.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("mn"), TimeUnit.forAbbreviation("ms"), 0.00021999999999999998), 0.0132);

        assertEquals(0.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("min"), TimeUnit.forAbbreviation("s"), 0.006666666666666667), 0.0004);
        assertEquals(1.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("min"), TimeUnit.forAbbreviation("s"), 0.021666666666666667), 0.0013);
        assertEquals(15.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("mn"), TimeUnit.forAbbreviation("s"), 0.25833333333333336), 0.0155);

        assertEquals(14.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("mn"), TimeUnit.forAbbreviation("hr"), 870.0), 0.0145);
        assertEquals(15.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("mn"), TimeUnit.forAbbreviation("h"), 924.0), 0.0154);
        assertEquals(12.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("mn"), TimeUnit.forAbbreviation("hr"), 744.0), 0.0124);

        assertEquals(7.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("min"), TimeUnit.forAbbreviation("d"), 10656.0), 0.0074);
        assertEquals(11.7, TimeUnit.convertBetween(TimeUnit.forAbbreviation("min"), TimeUnit.forAbbreviation("d"), 16847.999999999996), 0.011699999999999999);
        assertEquals(12.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("min"), TimeUnit.forAbbreviation("d"), 17712.000000000004), 0.0123);

        assertEquals(10.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("mn"), TimeUnit.forAbbreviation("y"), 5466240.0), 0.0104);
        assertEquals(1.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("mn"), TimeUnit.forAbbreviation("y"), 683280.0), 0.0013);
        assertEquals(16.0, TimeUnit.convertBetween(TimeUnit.forAbbreviation("mn"), TimeUnit.forAbbreviation("yr"), 8409600.0), 0.016);

        assertEquals(1.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("min"), TimeUnit.forAbbreviation("jiff"), 0.00023333333333333333), 0.0014);
        assertEquals(13.1, TimeUnit.convertBetween(TimeUnit.forAbbreviation("min"), TimeUnit.forAbbreviation("jiffie"), 0.002183333333333333), 0.013099999999999999);
        assertEquals(11.7, TimeUnit.convertBetween(TimeUnit.forAbbreviation("mn"), TimeUnit.forAbbreviation("jiffies"), 0.00195), 0.011699999999999999);

        assertEquals(6.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("hr"), TimeUnit.forAbbreviation("ns"), 1.75e-12), 0.0063);
        assertEquals(13.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("h"), TimeUnit.forAbbreviation("ns"), 3.666666666666666e-12), 0.0132);
        assertEquals(17.1, TimeUnit.convertBetween(TimeUnit.forAbbreviation("h"), TimeUnit.forAbbreviation("ns"), 4.7500000000000006e-12), 0.0171);

        assertEquals(3.6, TimeUnit.convertBetween(TimeUnit.forAbbreviation("h"), TimeUnit.forAbbreviation("ms"), 0.000001), 0.0036);
        assertEquals(6.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("hr"), TimeUnit.forAbbreviation("ms"), 0.0000017222222222222222), 0.0062);
        assertEquals(8.9, TimeUnit.convertBetween(TimeUnit.forAbbreviation("hr"), TimeUnit.forAbbreviation("ms"), 0.0000024722222222222222), 0.0089);

        assertEquals(11.1, TimeUnit.convertBetween(TimeUnit.forAbbreviation("h"), TimeUnit.forAbbreviation("s"), 0.0030833333333333333), 0.0111);
        assertEquals(17.0, TimeUnit.convertBetween(TimeUnit.forAbbreviation("hr"), TimeUnit.forAbbreviation("s"), 0.004722222222222222), 0.017);
        assertEquals(10.6, TimeUnit.convertBetween(TimeUnit.forAbbreviation("hr"), TimeUnit.forAbbreviation("s"), 0.0029444444444444444), 0.0106);

        assertEquals(16.9, TimeUnit.convertBetween(TimeUnit.forAbbreviation("hr"), TimeUnit.forAbbreviation("min"), 0.2816666666666666), 0.0169);
        assertEquals(15.0, TimeUnit.convertBetween(TimeUnit.forAbbreviation("h"), TimeUnit.forAbbreviation("mn"), 0.25), 0.015);
        assertEquals(0.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("h"), TimeUnit.forAbbreviation("mn"), 0.005), 0.0003);

        assertEquals(19.9, TimeUnit.convertBetween(TimeUnit.forAbbreviation("hr"), TimeUnit.forAbbreviation("d"), 477.5999999999999), 0.019899999999999998);
        assertEquals(3.8, TimeUnit.convertBetween(TimeUnit.forAbbreviation("h"), TimeUnit.forAbbreviation("d"), 91.2), 0.0038);
        assertEquals(11.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("hr"), TimeUnit.forAbbreviation("d"), 276.0), 0.0115);

        assertEquals(2.9, TimeUnit.convertBetween(TimeUnit.forAbbreviation("hr"), TimeUnit.forAbbreviation("yr"), 25404.0), 0.0029);
        assertEquals(3.1, TimeUnit.convertBetween(TimeUnit.forAbbreviation("hr"), TimeUnit.forAbbreviation("yr"), 27156.0), 0.0031);
        assertEquals(17.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("hr"), TimeUnit.forAbbreviation("y"), 151548.0), 0.0173);

        assertEquals(1.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("hr"), TimeUnit.forAbbreviation("jiffie"), 0.000003611111111111111), 0.0013);
        assertEquals(18.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("hr"), TimeUnit.forAbbreviation("jiffies"), 0.00005111111111111111), 0.0184);
        assertEquals(15.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("h"), TimeUnit.forAbbreviation("jiff"), 0.00004305555555555555), 0.0155);

        assertEquals(10.0, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("ns"), 1.1574074074074073e-13), 0.01);
        assertEquals(16.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("ns"), 1.886574074074074e-13), 0.016300000000000002);
        assertEquals(10.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("ns"), 1.2037037037037036e-13), 0.0104);

        assertEquals(5.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("ms"), 6.018518518518519e-8), 0.0052);
        assertEquals(4.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("ms"), 4.861111111111111e-8), 0.004200000000000001);
        assertEquals(7.9, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("ms"), 9.143518518518519e-8), 0.0079);

        assertEquals(2.6, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("s"), 0.000030092592592592593), 0.0026);
        assertEquals(8.9, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("s"), 0.00010300925925925926), 0.0089);
        assertEquals(9.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("s"), 0.00010763888888888889), 0.009300000000000001);

        assertEquals(0.8, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("mn"), 0.0005555555555555556), 0.0008);
        assertEquals(18.9, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("min"), 0.013125), 0.0189);
        assertEquals(11.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("min"), 0.00798611111111111), 0.0115);

        assertEquals(10.6, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("h"), 0.44166666666666665), 0.0106);
        assertEquals(16.0, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("h"), 0.6666666666666666), 0.016);
        assertEquals(7.9, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("h"), 0.32916666666666666), 0.0079);

        assertEquals(6.0, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("y"), 2190.0), 0.006);
        assertEquals(13.7, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("yr"), 5000.5), 0.013699999999999999);
        assertEquals(4.6, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("yr"), 1679.0), 0.0046);

        assertEquals(2.8, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("jiffies"), 3.2407407407407406e-7), 0.0028);
        assertEquals(13.6, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("jiffie"), 0.0000015740740740740742), 0.0136);
        assertEquals(0.7, TimeUnit.convertBetween(TimeUnit.forAbbreviation("d"), TimeUnit.forAbbreviation("jiffie"), 8.101851851851852e-8), 0.0007);

        assertEquals(10.1, TimeUnit.convertBetween(TimeUnit.forAbbreviation("yr"), TimeUnit.forAbbreviation("ns"), 3.202688990360223e-16), 0.0101);
        assertEquals(19.8, TimeUnit.convertBetween(TimeUnit.forAbbreviation("y"), TimeUnit.forAbbreviation("ns"), 6.278538812785388e-16), 0.0198);
        assertEquals(18.8, TimeUnit.convertBetween(TimeUnit.forAbbreviation("yr"), TimeUnit.forAbbreviation("ns"), 5.961440892947742e-16), 0.0188);

        assertEquals(17.8, TimeUnit.convertBetween(TimeUnit.forAbbreviation("yr"), TimeUnit.forAbbreviation("ms"), 5.644342973110096e-10), 0.0178);
        assertEquals(5.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("y"), TimeUnit.forAbbreviation("ms"), 1.7440385591070522e-10), 0.0055);
        assertEquals(13.6, TimeUnit.convertBetween(TimeUnit.forAbbreviation("y"), TimeUnit.forAbbreviation("ms"), 4.3125317097919836e-10), 0.0136);

        assertEquals(12.8, TimeUnit.convertBetween(TimeUnit.forAbbreviation("y"), TimeUnit.forAbbreviation("s"), 4.058853373921867e-7), 0.0128);
        assertEquals(13.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("y"), TimeUnit.forAbbreviation("s"), 4.21740233384069e-7), 0.013300000000000001);
        assertEquals(7.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("y"), TimeUnit.forAbbreviation("s"), 2.3465246067985795e-7), 0.0074);

        assertEquals(18.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("yr"), TimeUnit.forAbbreviation("mn"), 0.000035197869101978693), 0.0185);
        assertEquals(18.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("y"), TimeUnit.forAbbreviation("min"), 0.000034627092846270927), 0.0182);
        assertEquals(19.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("yr"), TimeUnit.forAbbreviation("min"), 0.000036529680365296805), 0.0192);

        assertEquals(9.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("y"), TimeUnit.forAbbreviation("hr"), 0.0010730593607305935), 0.0094);
        assertEquals(15.6, TimeUnit.convertBetween(TimeUnit.forAbbreviation("y"), TimeUnit.forAbbreviation("hr"), 0.001780821917808219), 0.0156);
        assertEquals(16.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("yr"), TimeUnit.forAbbreviation("hr"), 0.001872146118721461), 0.016399999999999998);

        assertEquals(16.0, TimeUnit.convertBetween(TimeUnit.forAbbreviation("yr"), TimeUnit.forAbbreviation("d"), 0.043835616438356165), 0.016);
        assertEquals(4.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("yr"), TimeUnit.forAbbreviation("d"), 0.012054794520547947), 0.0044);
        assertEquals(16.1, TimeUnit.convertBetween(TimeUnit.forAbbreviation("y"), TimeUnit.forAbbreviation("d"), 0.044109589041095895), 0.0161);

        assertEquals(2.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("y"), TimeUnit.forAbbreviation("jiffies"), 6.976154236428209e-10), 0.0022);
        assertEquals(14.0, TimeUnit.convertBetween(TimeUnit.forAbbreviation("y"), TimeUnit.forAbbreviation("jiffie"), 4.439370877727042e-9), 0.014);
        assertEquals(18.8, TimeUnit.convertBetween(TimeUnit.forAbbreviation("yr"), TimeUnit.forAbbreviation("jiffy"), 5.961440892947742e-9), 0.0188);

        assertEquals(1.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jiffy"), TimeUnit.forAbbreviation("ns"), 1.5e-7), 0.0015);
        assertEquals(3.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jiffy"), TimeUnit.forAbbreviation("ns"), 3.2999999999999996e-7), 0.0033);
        assertEquals(6.1, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jiffie"), TimeUnit.forAbbreviation("ns"), 6.099999999999999e-7), 0.0060999999999999995);

        assertEquals(12.7, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jiffy"), TimeUnit.forAbbreviation("ms"), 1.27), 0.0127);
        assertEquals(0.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jiffies"), TimeUnit.forAbbreviation("ms"), 0.02), 0.0002);
        assertEquals(7.3, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jiffie"), TimeUnit.forAbbreviation("ms"), 0.73), 0.0073);

        assertEquals(16.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jiffy"), TimeUnit.forAbbreviation("s"), 1620.0), 0.0162);
        assertEquals(16.7, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jiffy"), TimeUnit.forAbbreviation("s"), 1670.0), 0.0167);
        assertEquals(12.0, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jif"), TimeUnit.forAbbreviation("s"), 1200.0), 0.012);

        assertEquals(8.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jiff"), TimeUnit.forAbbreviation("min"), 51000.0), 0.0085);
        assertEquals(1.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jiffie"), TimeUnit.forAbbreviation("mn"), 9000.0), 0.0015);
        assertEquals(6.7, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jiffy"), TimeUnit.forAbbreviation("min"), 40200.0), 0.0067);

        assertEquals(14.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jiff"), TimeUnit.forAbbreviation("hr"), 5112000.0), 0.014199999999999999);
        assertEquals(16.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jiffie"), TimeUnit.forAbbreviation("h"), 5832000.0), 0.0162);
        assertEquals(8.5, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jif"), TimeUnit.forAbbreviation("hr"), 3060000.0), 0.0085);

        assertEquals(7.8, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jiffies"), TimeUnit.forAbbreviation("d"), 67392000.0), 0.0078);
        assertEquals(15.0, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jif"), TimeUnit.forAbbreviation("d"), 129600000.0), 0.015);
        assertEquals(15.1, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jiff"), TimeUnit.forAbbreviation("d"), 130464000.0), 0.015099999999999999);

        assertEquals(8.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jiffies"), TimeUnit.forAbbreviation("y"), 25859519999.999996), 0.008199999999999999);
        assertEquals(7.4, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jif"), TimeUnit.forAbbreviation("y"), 23336640000.0), 0.0074);
        assertEquals(16.2, TimeUnit.convertBetween(TimeUnit.forAbbreviation("jiff"), TimeUnit.forAbbreviation("yr"), 51088320000.0), 0.0162);
    }
}
