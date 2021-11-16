package org.firstinspires.ftc.teamcode.unitTests.unitconversion;

import org.firstinspires.ftc.teamcode.auxilary.units.DistanceUnit;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class DistanceUnitTest {
    @Test
    public void test() {
        assertEquals(16.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("m"), 16600.0), 0.0166);
        assertEquals(3.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("m"), 3299.9999999999995), 0.0033);
        assertEquals(18.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("m"), 18200.0), 0.0182);

        assertEquals(4.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("nm"), 0.000004), 0.004);
        assertEquals(16.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("nm"), 0.000016100000000000002), 0.0161);
        assertEquals(8.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("nm"), 0.000008400000000000001), 0.008400000000000001);

        assertEquals(3.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("cm"), 32.0), 0.0032);
        assertEquals(5.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("cm"), 50.99999999999999), 0.0050999999999999995);
        assertEquals(17.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("cm"), 178.00000000000003), 0.0178);

        assertEquals(5.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("in"), 149.85999999999999), 0.005900000000000001);
        assertEquals(10.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("in"), 256.53999999999996), 0.0101);
        assertEquals(9.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("in"), 248.92), 0.009800000000000001);

        assertEquals(8.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("ft"), 2438.4), 0.008);
        assertEquals(17.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("ft"), 5242.56), 0.0172);
        assertEquals(10.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("ft"), 3230.88), 0.0106);

        assertEquals(2.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("yd"), 2011.68), 0.0022);
        assertEquals(12.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("yd"), 11155.679999999998), 0.012199999999999999);
        assertEquals(5.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("yd"), 4937.76), 0.0054);

        assertEquals(0.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("fathom"), 365.76000000000005), 0.0002);
        assertEquals(0.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("fathom"), 1645.92), 0.0009);
        assertEquals(13.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("fathom"), 24688.8), 0.0135);

        assertEquals(12.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("km"), 12900000.0), 0.0129);
        assertEquals(13.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("km"), 13900000.0), 0.013900000000000001);
        assertEquals(9.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("km"), 9600000.0), 0.0096);

        assertEquals(9.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("mil"), 15610636.8), 0.009699999999999999);
        assertEquals(18.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("mil"), 30416601.599999998), 0.0189);
        assertEquals(8.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("mil"), 13357555.200000001), 0.0083);

        assertEquals(15.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("au"), 2333726782920000.0), 0.0156);
        assertEquals(17.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("au"), 2573083376040000.0), 0.0172);
        assertEquals(10.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("au"), 1570777642350000.0), 0.0105);

        assertEquals(2.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("fbf"), 317147.85699999996), 0.0029);
        assertEquals(15.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("fbf"), 1651356.0829999999), 0.015099999999999999);
        assertEquals(18.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("fbf"), 2045056.8709999998), 0.018699999999999998);

        assertEquals(10.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("ftcf"), 55961.27999999999), 0.010199999999999999);
        assertEquals(15.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("ftcf"), 83941.92), 0.015300000000000001);
        assertEquals(15.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("ftcf"), 83393.27999999998), 0.0152);

        assertEquals(2.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("rbtw"), 1143.0), 0.0025);
        assertEquals(6.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("rbtw"), 3108.9599999999996), 0.0068);
        assertEquals(1.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mm"), DistanceUnit.forAbbreviation("rbtw"), 868.6799999999998), 0.0019);

        assertEquals(2.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("mm"), 0.0025), 0.0025);
        assertEquals(9.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("mm"), 0.009000000000000001), 0.009);
        assertEquals(12.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("mm"), 0.0128), 0.0128);

        assertEquals(4.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("nm"), 4.4000000000000005e-9), 0.0044);
        assertEquals(19.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("nm"), 1.93e-8), 0.0193);
        assertEquals(15.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("nm"), 1.52e-8), 0.0152);

        assertEquals(0.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("cm"), 0.003), 0.0003);
        assertEquals(12.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("cm"), 0.128), 0.0128);
        assertEquals(15.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("cm"), 0.156), 0.0156);

        assertEquals(12.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("in"), 0.30479999999999996), 0.012);
        assertEquals(13.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("in"), 0.35306), 0.013900000000000001);
        assertEquals(8.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("in"), 0.22097999999999998), 0.0087);

        assertEquals(6.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("ft"), 1.9812), 0.0065);
        assertEquals(3.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("ft"), 1.09728), 0.0036);
        assertEquals(10.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("ft"), 3.2918400000000005), 0.0108);

        assertEquals(15.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("yd"), 14.081760000000001), 0.0154);
        assertEquals(1.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("yd"), 1.28016), 0.0014);
        assertEquals(11.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("yd"), 10.5156), 0.0115);

        assertEquals(17.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("fathom"), 31.0896), 0.017);
        assertEquals(4.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("fathom"), 8.59536), 0.0047);
        assertEquals(2.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("fathom"), 4.93776), 0.0027);

        assertEquals(6.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("km"), 6600.0), 0.0066);
        assertEquals(15.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("km"), 15700.0), 0.0157);
        assertEquals(4.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("km"), 4300.0), 0.0043);

        assertEquals(2.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("mil"), 4345.228800000001), 0.0027);
        assertEquals(4.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("mil"), 6920.1792), 0.0043);
        assertEquals(0.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("mil"), 321.8688), 0.0002);

        assertEquals(11.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("au"), 1705415725980.0), 0.0114);
        assertEquals(16.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("au"), 2438445292410.0), 0.016300000000000002);
        assertEquals(17.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("au"), 2677801885530.0), 0.0179);

        assertEquals(15.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("fbf"), 1695.1006149999998), 0.0155);
        assertEquals(12.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("fbf"), 1399.825024), 0.0128);
        assertEquals(5.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("fbf"), 645.231847), 0.005900000000000001);

        assertEquals(18.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("ftcf"), 99.30384000000001), 0.0181);
        assertEquals(10.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("ftcf"), 57.05856), 0.0104);
        assertEquals(13.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("ftcf"), 74.0664), 0.0135);

        assertEquals(7.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("rbtw"), 3.56616), 0.0078);
        assertEquals(19.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("rbtw"), 8.77824), 0.0192);
        assertEquals(13.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("m"), DistanceUnit.forAbbreviation("rbtw"), 6.30936), 0.013800000000000002);

        assertEquals(4.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("mm"), 4700000.0), 0.0047);
        assertEquals(6.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("mm"), 6700000.0), 0.0067);
        assertEquals(6.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("mm"), 6600000.0), 0.0066);

        assertEquals(15.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("m"), 15099999999.999998), 0.015099999999999999);
        assertEquals(16.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("m"), 16399999999.999998), 0.016399999999999998);
        assertEquals(10.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("m"), 10400000000.0), 0.0104);

        assertEquals(0.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("cm"), 1000000.0), 0.0001);
        assertEquals(14.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("cm"), 141999999.99999997), 0.014199999999999999);
        assertEquals(15.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("cm"), 154000000.0), 0.0154);

        assertEquals(15.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("in"), 386079999.99999994), 0.0152);
        assertEquals(17.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("in"), 452120000.0), 0.0178);
        assertEquals(11.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("in"), 294639999.99999994), 0.0116);

        assertEquals(18.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("ft"), 5577840000.0), 0.0183);
        assertEquals(13.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("ft"), 4114799999.9999995), 0.0135);
        assertEquals(16.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("ft"), 4907280000.000001), 0.0161);

        assertEquals(5.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("yd"), 5394960000.0), 0.005900000000000001);
        assertEquals(2.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("yd"), 2103119999.9999995), 0.0023);
        assertEquals(2.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("yd"), 1828799999.9999998), 0.002);

        assertEquals(1.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("fathom"), 1828799999.9999998), 0.001);
        assertEquals(0.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("fathom"), 1097280000.0), 0.0006);
        assertEquals(15.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("fathom"), 27797759999.999996), 0.0152);

        assertEquals(13.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("km"), 13600000000000.0), 0.0136);
        assertEquals(5.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("km"), 5000000000000.0), 0.005);
        assertEquals(12.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("km"), 12900000000000.0), 0.0129);

        assertEquals(17.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("mil"), 28807257599999.996), 0.0179);
        assertEquals(0.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("mil"), 1448409600000.0), 0.0009);
        assertEquals(13.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("mil"), 21726144000000.0), 0.0135);

        assertEquals(4.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("au"), 628311056940000000000.0), 0.004200000000000001);
        assertEquals(18.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("au"), 2.7675606079499996e+21), 0.0185);
        assertEquals(18.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("au"), 2.72268124674e+21), 0.0182);

        assertEquals(2.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("fbf"), 229658793000.0), 0.0021000000000000003);
        assertEquals(3.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("fbf"), 415573053999.99994), 0.0038);
        assertEquals(14.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("fbf"), 1585739284999.9998), 0.0145);

        assertEquals(14.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("ftcf"), 81198719999.99998), 0.0148);
        assertEquals(5.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("ftcf"), 30175199999.999996), 0.0055);
        assertEquals(13.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("ftcf"), 72420480000.0), 0.0132);

        assertEquals(15.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("rbtw"), 7223760000.0), 0.0158);
        assertEquals(13.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("rbtw"), 5989320000.0), 0.013099999999999999);
        assertEquals(4.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("nm"), DistanceUnit.forAbbreviation("rbtw"), 2194560000.0), 0.0048);

        assertEquals(1.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("mm"), 0.11), 0.0011);
        assertEquals(17.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("mm"), 1.77), 0.0177);
        assertEquals(2.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("mm"), 0.21000000000000002), 0.0021000000000000003);

        assertEquals(14.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("m"), 1470.0), 0.0147);
        assertEquals(10.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("m"), 1000.0), 0.01);
        assertEquals(6.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("m"), 610.0), 0.0060999999999999995);

        assertEquals(15.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("nm"), 0.00000159), 0.0159);
        assertEquals(19.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("nm"), 0.00000193), 0.0193);
        assertEquals(19.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("nm"), 0.0000019500000000000004), 0.0195);

        assertEquals(16.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("in"), 41.402), 0.016300000000000002);
        assertEquals(3.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("in"), 9.652), 0.0038);
        assertEquals(19.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("in"), 49.022), 0.0193);

        assertEquals(17.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("ft"), 524.256), 0.0172);
        assertEquals(11.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("ft"), 347.47200000000004), 0.0114);
        assertEquals(18.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("ft"), 563.8800000000001), 0.0185);

        assertEquals(11.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("yd"), 1051.56), 0.0115);
        assertEquals(17.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("yd"), 1618.4879999999998), 0.0177);
        assertEquals(6.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("yd"), 548.64), 0.006);

        assertEquals(7.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("fathom"), 1371.6), 0.0075);
        assertEquals(10.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("fathom"), 1901.952), 0.0104);
        assertEquals(4.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("fathom"), 877.824), 0.0048);

        assertEquals(6.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("km"), 680000.0), 0.0068);
        assertEquals(10.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("km"), 1080000.0), 0.0108);
        assertEquals(12.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("km"), 1260000.0), 0.0126);

        assertEquals(14.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("mil"), 2349642.2399999998), 0.0146);
        assertEquals(10.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("mil"), 1673717.7600000002), 0.0104);
        assertEquals(8.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("mil"), 1367942.4000000001), 0.0085);

        assertEquals(18.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("au"), 270772145967000.0), 0.0181);
        assertEquals(17.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("au"), 261796273725000.0), 0.0175);
        assertEquals(6.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("au"), 95742637248000.0), 0.0064);

        assertEquals(5.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("fbf"), 55774.27829999999), 0.0050999999999999995);
        assertEquals(7.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("fbf"), 79833.77089999999), 0.0073);
        assertEquals(3.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("fbf"), 33902.0123), 0.0031);

        assertEquals(20.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("ftcf"), 10972.8), 0.02);
        assertEquals(1.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("ftcf"), 1042.416), 0.0019);
        assertEquals(15.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("ftcf"), 8558.784), 0.0156);

        assertEquals(0.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("rbtw"), 27.432), 0.0006);
        assertEquals(6.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("rbtw"), 297.18), 0.0065);
        assertEquals(1.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("cm"), DistanceUnit.forAbbreviation("rbtw"), 64.008), 0.0014);

        assertEquals(19.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("mm"), 0.7637795275590552), 0.019399999999999997);
        assertEquals(17.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("mm"), 0.6929133858267718), 0.0176);
        assertEquals(15.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("mm"), 0.6062992125984252), 0.0154);

        assertEquals(2.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("m"), 98.4251968503937), 0.0025);
        assertEquals(11.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("m"), 464.5669291338583), 0.011800000000000001);
        assertEquals(3.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("m"), 153.54330708661416), 0.0039);

        assertEquals(2.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("nm"), 8.267716535433073e-8), 0.0021000000000000003);
        assertEquals(17.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("nm"), 6.811023622047245e-7), 0.0173);
        assertEquals(2.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("nm"), 7.874015748031497e-8), 0.002);

        assertEquals(13.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("cm"), 5.118110236220473), 0.013);
        assertEquals(3.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("cm"), 1.535433070866142), 0.0039);
        assertEquals(19.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("cm"), 7.559055118110237), 0.0192);

        assertEquals(19.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("ft"), 228.0), 0.019);
        assertEquals(7.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("ft"), 90.0), 0.0075);
        assertEquals(5.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("ft"), 60.0), 0.005);

        assertEquals(18.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("yd"), 648.0), 0.018);
        assertEquals(16.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("yd"), 586.8000000000001), 0.016300000000000002);
        assertEquals(15.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("yd"), 547.1999999999999), 0.0152);

        assertEquals(5.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("fathom"), 360.0), 0.005);
        assertEquals(15.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("fathom"), 1130.3999999999999), 0.0157);
        assertEquals(0.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("fathom"), 28.800000000000004), 0.0004);

        assertEquals(9.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("km"), 374015.74803149607), 0.0095);
        assertEquals(11.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("km"), 460629.9212598425), 0.011699999999999999);
        assertEquals(7.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("km"), 311023.6220472441), 0.0079);

        assertEquals(9.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("mil"), 601920.0), 0.0095);
        assertEquals(10.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("mil"), 665280.0000000001), 0.0105);
        assertEquals(1.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("mil"), 107712.0), 0.0017);

        assertEquals(1.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("au"), 10601423907874.016), 0.0018);
        assertEquals(17.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("au"), 101891463114566.94), 0.0173);
        assertEquals(0.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("au"), 1766903984645.6694), 0.0003);

        assertEquals(7.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("fbf"), 30138.949212598425), 0.007);
        assertEquals(19.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("fbf"), 83097.38854330708), 0.0193);
        assertEquals(8.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("fbf"), 35736.18263779528), 0.0083);

        assertEquals(7.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("ftcf"), 1555.2), 0.0072);
        assertEquals(6.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("ftcf"), 1360.8), 0.0063);
        assertEquals(16.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("ftcf"), 3456.0), 0.016);

        assertEquals(2.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("rbtw"), 36.0), 0.002);
        assertEquals(15.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("rbtw"), 286.2), 0.0159);
        assertEquals(8.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("in"), DistanceUnit.forAbbreviation("rbtw"), 144.0), 0.008);

        assertEquals(18.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("mm"), 0.05905511811023623), 0.018);
        assertEquals(1.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("mm"), 0.003937007874015747), 0.0012);
        assertEquals(14.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("mm"), 0.04691601049868766), 0.0143);

        assertEquals(9.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("m"), 29.527559055118108), 0.009);
        assertEquals(18.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("m"), 59.38320209973753), 0.0181);
        assertEquals(16.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("m"), 54.46194225721785), 0.0166);

        assertEquals(12.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("nm"), 3.9370078740157486e-8), 0.012);
        assertEquals(6.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("nm"), 2.1981627296587926e-8), 0.0067);
        assertEquals(11.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("nm"), 3.805774278215223e-8), 0.0116);

        assertEquals(4.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("cm"), 0.14435695538057744), 0.0044);
        assertEquals(8.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("cm"), 0.28215223097112857), 0.0086);
        assertEquals(9.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("cm"), 0.3215223097112861), 0.009800000000000001);

        assertEquals(1.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("in"), 0.14166666666666664), 0.0017);
        assertEquals(11.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("in"), 0.9499999999999998), 0.0114);
        assertEquals(16.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("in"), 1.3833333333333333), 0.0166);

        assertEquals(4.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("yd"), 12.899999999999999), 0.0043);
        assertEquals(7.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("yd"), 21.599999999999998), 0.0072);
        assertEquals(13.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("yd"), 41.4), 0.013800000000000002);

        assertEquals(17.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("fathom"), 107.39999999999998), 0.0179);
        assertEquals(17.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("fathom"), 104.39999999999999), 0.0174);
        assertEquals(3.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("fathom"), 21.0), 0.0035);

        assertEquals(13.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("km"), 44619.422572178475), 0.0136);
        assertEquals(13.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("km"), 44619.422572178475), 0.0136);
        assertEquals(9.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("km"), 30183.727034120733), 0.0092);

        assertEquals(10.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("mil"), 54912.00000000001), 0.0104);
        assertEquals(4.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("mil"), 25344.0), 0.0048);
        assertEquals(8.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("mil"), 42768.0), 0.0081);

        assertEquals(18.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("au"), 9227165253149.605), 0.0188);
        assertEquals(11.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("au"), 5742437950098.425), 0.011699999999999999);
        assertEquals(3.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("au"), 1865065317125.9841), 0.0038);

        assertEquals(5.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("fbf"), 2081.022683727034), 0.0058);
        assertEquals(13.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("fbf"), 4700.240889107611), 0.013099999999999999);
        assertEquals(8.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("fbf"), 2978.015219816273), 0.0083);

        assertEquals(16.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("ftcf"), 295.19999999999993), 0.016399999999999998);
        assertEquals(16.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("ftcf"), 291.59999999999997), 0.0162);
        assertEquals(10.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("ftcf"), 185.4), 0.0103);

        assertEquals(5.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("rbtw"), 8.7), 0.0058);
        assertEquals(17.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("rbtw"), 26.099999999999998), 0.0174);
        assertEquals(3.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ft"), DistanceUnit.forAbbreviation("rbtw"), 4.65), 0.0031);

        assertEquals(3.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("mm"), 0.00426509186351706), 0.0039);
        assertEquals(1.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("mm"), 0.0016404199475065617), 0.0015);
        assertEquals(7.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("mm"), 0.007655293088363955), 0.007);

        assertEquals(16.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("m"), 18.37270341207349), 0.016800000000000002);
        assertEquals(9.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("m"), 10.608048993875764), 0.009699999999999999);
        assertEquals(10.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("m"), 11.482939632545932), 0.0105);

        assertEquals(1.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("nm"), 1.3123359580052494e-9), 0.0012);
        assertEquals(13.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("nm"), 1.509186351706037e-8), 0.013800000000000002);
        assertEquals(6.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("nm"), 7.217847769028872e-9), 0.0066);

        assertEquals(15.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("cm"), 0.17279090113735784), 0.0158);
        assertEquals(10.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("cm"), 0.10936132983377078), 0.01);
        assertEquals(16.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("cm"), 0.17935258092738407), 0.016399999999999998);

        assertEquals(13.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("in"), 0.38333333333333336), 0.013800000000000002);
        assertEquals(9.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("in"), 0.26944444444444443), 0.009699999999999999);
        assertEquals(14.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("in"), 0.38888888888888884), 0.014);

        assertEquals(8.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("ft"), 2.9), 0.0087);
        assertEquals(7.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("ft"), 2.466666666666667), 0.0074);
        assertEquals(2.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("ft"), 0.9000000000000001), 0.0027);

        assertEquals(13.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("fathom"), 26.8), 0.0134);
        assertEquals(1.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("fathom"), 2.0), 0.001);
        assertEquals(10.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("fathom"), 20.0), 0.01);

        assertEquals(2.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("km"), 2952.755905511811), 0.0027);
        assertEquals(1.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("km"), 1640.4199475065616), 0.0015);
        assertEquals(8.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("km"), 9186.351706036745), 0.008400000000000001);

        assertEquals(16.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("mil"), 29040.0), 0.0165);
        assertEquals(0.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("mil"), 176.0), 0.0001);
        assertEquals(14.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("mil"), 24640.0), 0.014);

        assertEquals(16.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("au"), 2732157087368.7666), 0.0167);
        assertEquals(9.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("au"), 1554221097604.9868), 0.0095);
        assertEquals(6.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("au"), 1047054213123.3596), 0.0064);

        assertEquals(5.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("fbf"), 597.9950240594926), 0.005);
        assertEquals(17.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("fbf"), 2033.1830818022745), 0.017);
        assertEquals(6.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("fbf"), 729.5539293525809), 0.0060999999999999995);

        assertEquals(0.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("ftcf"), 2.4), 0.0004);
        assertEquals(6.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("ftcf"), 41.400000000000006), 0.006900000000000001);
        assertEquals(0.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("ftcf"), 5.4), 0.0009);

        assertEquals(10.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("rbtw"), 5.45), 0.0109);
        assertEquals(4.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("rbtw"), 2.45), 0.004900000000000001);
        assertEquals(12.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("yd"), DistanceUnit.forAbbreviation("rbtw"), 6.4), 0.0128);

        assertEquals(0.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("mm"), 0.00005468066491688539), 0.0001);
        assertEquals(11.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("mm"), 0.006288276465441819), 0.0115);
        assertEquals(2.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("mm"), 0.0015310586176727908), 0.0028);

        assertEquals(10.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("m"), 5.741469816272966), 0.0105);
        assertEquals(14.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("m"), 7.928696412948382), 0.0145);
        assertEquals(16.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("m"), 9.186351706036746), 0.016800000000000002);

        assertEquals(15.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("nm"), 8.256780402449694e-9), 0.015099999999999999);
        assertEquals(6.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("nm"), 3.608923884514436e-9), 0.0066);
        assertEquals(19.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("nm"), 1.0826771653543308e-8), 0.0198);

        assertEquals(7.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("cm"), 0.038823272090988625), 0.0070999999999999995);
        assertEquals(8.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("cm"), 0.04538495188101488), 0.0083);
        assertEquals(1.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("cm"), 0.006561679790026247), 0.0012);

        assertEquals(9.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("in"), 0.1375), 0.0099);
        assertEquals(11.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("in"), 0.15555555555555553), 0.0112);
        assertEquals(1.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("in"), 0.018055555555555557), 0.0013);

        assertEquals(1.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("ft"), 0.31666666666666665), 0.0019);
        assertEquals(8.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("ft"), 1.45), 0.0087);
        assertEquals(17.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("ft"), 2.9333333333333336), 0.0176);

        assertEquals(8.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("yd"), 4.25), 0.0085);
        assertEquals(17.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("yd"), 8.85), 0.0177);
        assertEquals(13.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("yd"), 6.75), 0.0135);

        assertEquals(1.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("km"), 984.2519685039371), 0.0018);
        assertEquals(0.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("km"), 218.72265966754156), 0.0004);
        assertEquals(7.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("km"), 4101.049868766404), 0.0075);

        assertEquals(4.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("mil"), 3872.0000000000005), 0.0044);
        assertEquals(16.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("mil"), 14344.000000000002), 0.016300000000000002);
        assertEquals(15.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("mil"), 13728.0), 0.0156);

        assertEquals(0.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("au"), 57260777280.18373), 0.0007);
        assertEquals(6.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("au"), 564427661761.811), 0.006900000000000001);
        assertEquals(0.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("au"), 49080666240.15748), 0.0006);

        assertEquals(14.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("fbf"), 849.1529341644793), 0.014199999999999999);
        assertEquals(8.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("fbf"), 484.3759694881889), 0.0081);
        assertEquals(14.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("fbf"), 843.1729839238845), 0.0141);

        assertEquals(19.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("ftcf"), 56.99999999999999), 0.019);
        assertEquals(13.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("ftcf"), 41.699999999999996), 0.013900000000000001);
        assertEquals(4.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("ftcf"), 13.799999999999997), 0.0046);

        assertEquals(14.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("rbtw"), 3.575), 0.0143);
        assertEquals(6.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("rbtw"), 1.725), 0.006900000000000001);
        assertEquals(19.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fathom"), DistanceUnit.forAbbreviation("rbtw"), 4.8), 0.0192);

        assertEquals(13.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("mm"), 0.0000135), 0.0135);
        assertEquals(16.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("mm"), 0.000016100000000000002), 0.0161);
        assertEquals(17.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("mm"), 0.000017100000000000002), 0.0171);

        assertEquals(3.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("m"), 0.0033), 0.0033);
        assertEquals(16.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("m"), 0.0161), 0.0161);
        assertEquals(6.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("m"), 0.0063), 0.0063);

        assertEquals(13.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("nm"), 1.3e-11), 0.013);
        assertEquals(11.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("nm"), 1.1e-11), 0.011);
        assertEquals(3.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("nm"), 3.700000000000001e-12), 0.0037);

        assertEquals(7.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("cm"), 0.000079), 0.0079);
        assertEquals(4.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("cm"), 0.000046), 0.0046);
        assertEquals(0.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("cm"), 0.000009), 0.0009);

        assertEquals(11.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("in"), 0.00028447999999999993), 0.0112);
        assertEquals(13.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("in"), 0.00033782), 0.013300000000000001);
        assertEquals(12.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("in"), 0.00032004), 0.0126);

        assertEquals(2.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("ft"), 0.00079248), 0.0026);
        assertEquals(18.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("ft"), 0.005699760000000001), 0.018699999999999998);
        assertEquals(18.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("ft"), 0.00557784), 0.0183);

        assertEquals(8.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("yd"), 0.00786384), 0.0086);
        assertEquals(3.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("yd"), 0.0032918400000000003), 0.0036);
        assertEquals(6.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("yd"), 0.0059436), 0.0065);

        assertEquals(6.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("fathom"), 0.0109728), 0.006);
        assertEquals(17.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("fathom"), 0.03145536), 0.0172);
        assertEquals(6.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("fathom"), 0.01170432), 0.0064);

        assertEquals(4.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("mil"), 7.563916800000001), 0.0047);
        assertEquals(14.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("mil"), 22.530815999999998), 0.014);
        assertEquals(6.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("mil"), 9.656064), 0.006);

        assertEquals(18.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("au"), 2812439969.16), 0.0188);
        assertEquals(17.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("au"), 2662842098.46), 0.0178);
        assertEquals(8.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("au"), 1271581900.95), 0.0085);

        assertEquals(5.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("fbf"), 0.6124234479999999), 0.0056);
        assertEquals(10.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("fbf"), 1.192038497), 0.0109);
        assertEquals(6.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("fbf"), 0.65616798), 0.006);

        assertEquals(17.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("ftcf"), 0.09491472000000001), 0.0173);
        assertEquals(6.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("ftcf"), 0.03511296), 0.0064);
        assertEquals(17.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("ftcf"), 0.09546336), 0.0174);

        assertEquals(12.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("rbtw"), 0.005715), 0.0125);
        assertEquals(5.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("rbtw"), 0.00237744), 0.0052);
        assertEquals(9.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("km"), DistanceUnit.forAbbreviation("rbtw"), 0.00452628), 0.0099);

        assertEquals(10.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("mm"), 0.000006710808876163207), 0.0108);
        assertEquals(1.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("mm"), 0.0000011806052652509345), 0.0019);
        assertEquals(4.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("mm"), 0.000002609759007396803), 0.004200000000000001);

        assertEquals(2.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("m"), 0.0018019764574882685), 0.0029);
        assertEquals(3.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("m"), 0.0024233476497256025), 0.0039);
        assertEquals(16.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("m"), 0.010376898910363476), 0.0167);

        assertEquals(8.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("nm"), 5.530203610912272e-12), 0.0089);
        assertEquals(19.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("nm"), 1.1806052652509346e-11), 0.019);
        assertEquals(9.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("nm"), 5.716614968583472e-12), 0.0092);

        assertEquals(16.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("cm"), 0.00010190487552692275), 0.016399999999999998);
        assertEquals(8.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("cm"), 0.000050952437763461375), 0.008199999999999999);
        assertEquals(19.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("cm"), 0.00012116738248628013), 0.0195);

        assertEquals(1.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("in"), 0.000015782828282828283), 0.001);
        assertEquals(15.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("in"), 0.0002446338383838384), 0.0155);
        assertEquals(2.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("in"), 0.00003472222222222222), 0.0022);

        assertEquals(16.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("ft"), 0.0030303030303030303), 0.016);
        assertEquals(18.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("ft"), 0.003522727272727273), 0.018600000000000002);
        assertEquals(14.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("ft"), 0.0026515151515151512), 0.014);

        assertEquals(4.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("yd"), 0.002727272727272727), 0.0048);
        assertEquals(19.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("yd"), 0.01130681818181818), 0.019899999999999998);
        assertEquals(6.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("yd"), 0.003693181818181818), 0.0065);

        assertEquals(14.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("fathom"), 0.016022727272727272), 0.0141);
        assertEquals(19.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("fathom"), 0.022272727272727274), 0.019600000000000003);
        assertEquals(11.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("fathom"), 0.013409090909090909), 0.011800000000000001);

        assertEquals(2.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("km"), 1.429153742145868), 0.0023);
        assertEquals(0.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("km"), 0.0621371192237334), 0.0001);
        assertEquals(2.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("km"), 1.4912908613696014), 0.0024);

        assertEquals(4.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("au"), 381118809.81940466), 0.0040999999999999995);
        assertEquals(4.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("au"), 446187874.9105225), 0.0048);
        assertEquals(7.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("au"), 725055296.7295991), 0.0078);

        assertEquals(1.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("fbf"), 0.10193097001014076), 0.0015);
        assertEquals(10.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("fbf"), 0.7067213920703094), 0.0104);
        assertEquals(9.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("fbf"), 0.6115858200608446), 0.009);

        assertEquals(6.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("ftcf"), 0.021818181818181816), 0.0064);
        assertEquals(4.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("ftcf"), 0.01636363636363636), 0.0048);
        assertEquals(7.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("ftcf"), 0.02625), 0.0077);

        assertEquals(9.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("rbtw"), 0.002613636363636363), 0.0092);
        assertEquals(3.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("rbtw"), 0.0008806818181818182), 0.0031);
        assertEquals(1.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("mil"), DistanceUnit.forAbbreviation("rbtw"), 0.0002840909090909091), 0.001);

        assertEquals(14.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("mm"), 9.960034812179983e-14), 0.0149);
        assertEquals(5.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("mm"), 3.342293561134223e-14), 0.005);
        assertEquals(16.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("mm"), 1.1163260494188303e-13), 0.0167);

        assertEquals(10.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("m"), 6.75143299349113e-11), 0.0101);
        assertEquals(4.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("m"), 2.673834848907378e-11), 0.004);
        assertEquals(16.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("m"), 1.1029568751742935e-10), 0.0165);

        assertEquals(17.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("nm"), 1.1363798107856358e-19), 0.017);
        assertEquals(10.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("nm"), 7.152508220827236e-20), 0.0107);
        assertEquals(13.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("nm"), 8.957346743839717e-20), 0.0134);

        assertEquals(3.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("cm"), 2.6069889776846937e-13), 0.0039);
        assertEquals(8.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("cm"), 5.414515569037441e-13), 0.0081);
        assertEquals(1.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("cm"), 8.68996325894898e-14), 0.0013);

        assertEquals(8.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("in"), 1.3752869545355098e-12), 0.0081);
        assertEquals(0.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("in"), 1.3583081032449482e-13), 0.0008);
        assertEquals(8.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("in"), 1.3752869545355098e-12), 0.0081);

        assertEquals(16.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("ft"), 3.4229364201772696e-11), 0.016800000000000002);
        assertEquals(16.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("ft"), 3.321063312433898e-11), 0.016300000000000002);
        assertEquals(19.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("ft"), 3.993425823540148e-11), 0.019600000000000003);

        assertEquals(16.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("yd"), 1.0207685395885785e-10), 0.0167);
        assertEquals(19.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("yd"), 1.2102525199912486e-10), 0.0198);
        assertEquals(7.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("yd"), 4.767661442389768e-11), 0.0078);

        assertEquals(15.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("fathom"), 1.9437388957435207e-10), 0.0159);
        assertEquals(19.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("fathom"), 2.3227068565488612e-10), 0.019);
        assertEquals(17.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("fathom"), 2.1760095813984073e-10), 0.0178);

        assertEquals(17.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("km"), 1.1764873335192463e-7), 0.0176);
        assertEquals(13.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("km"), 9.024192615062401e-8), 0.0135);
        assertEquals(10.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("km"), 6.75143299349113e-8), 0.0101);

        assertEquals(2.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("mil"), 2.5818720426479975e-8), 0.0024);
        assertEquals(14.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("mil"), 1.5060920248779983e-7), 0.014);
        assertEquals(8.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("mil"), 9.57444215815299e-8), 0.0089);

        assertEquals(2.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("fbf"), 1.6082777440227297e-9), 0.0022);
        assertEquals(14.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("fbf"), 1.0819323005243818e-8), 0.0148);
        assertEquals(5.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("fbf"), 4.020694360056824e-9), 0.0055);

        assertEquals(4.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("ftcf"), 1.5403213890797712e-10), 0.004200000000000001);
        assertEquals(11.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("ftcf"), 4.3275696169384045e-10), 0.011800000000000001);
        assertEquals(11.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("ftcf"), 4.3275696169384045e-10), 0.011800000000000001);

        assertEquals(1.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("rbtw"), 3.9730512019914735e-12), 0.0013);
        assertEquals(13.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("rbtw"), 4.1869847282525524e-11), 0.013699999999999999);
        assertEquals(15.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("au"), DistanceUnit.forAbbreviation("rbtw"), 4.8287853070357905e-11), 0.0158);

        assertEquals(17.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("mm"), 0.0001581911997595494), 0.0173);
        assertEquals(19.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("mm"), 0.00017647919973175165), 0.0193);
        assertEquals(18.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("mm"), 0.00017282159973731117), 0.0189);

        assertEquals(2.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("m"), 0.019202399970812353), 0.0021000000000000003);
        assertEquals(3.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("m"), 0.030175199954133695), 0.0033);
        assertEquals(16.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("m"), 0.14721839977622805), 0.0161);

        assertEquals(9.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("nm"), 8.503919987074043e-11), 0.009300000000000001);
        assertEquals(14.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("nm"), 1.353311997942966e-10), 0.0148);
        assertEquals(14.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("nm"), 1.2984479980263591e-10), 0.014199999999999999);

        assertEquals(19.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("cm"), 0.0018013679972619207), 0.0197);
        assertEquals(16.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("cm"), 0.0014630399977761794), 0.016);
        assertEquals(7.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("cm"), 0.0006949439989436851), 0.0076);

        assertEquals(1.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("in"), 0.00044128943932924007), 0.0019);
        assertEquals(13.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("in"), 0.0031122518352693774), 0.0134);
        assertEquals(1.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("in"), 0.00039483791939984634), 0.0017);

        assertEquals(10.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("ft"), 0.029264457555518028), 0.0105);
        assertEquals(17.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("ft"), 0.049331514165016105), 0.0177);
        assertEquals(2.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("ft"), 0.006967727989409054), 0.0025);

        assertEquals(16.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("yd"), 0.13963326890775743), 0.0167);
        assertEquals(11.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("yd"), 0.09197400946019951), 0.011);
        assertEquals(18.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("yd"), 0.15635581608233917), 0.018699999999999998);

        assertEquals(18.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("fathom"), 0.3060226132948457), 0.0183);
        assertEquals(13.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("fathom"), 0.22575438685685334), 0.0135);
        assertEquals(15.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("fathom"), 0.25585497177110045), 0.015300000000000001);

        assertEquals(1.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("km"), 10.058399984711233), 0.0011);
        assertEquals(11.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("km"), 100.58399984711232), 0.011);
        assertEquals(1.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("km"), 14.630399977761792), 0.0016);

        assertEquals(2.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("mil"), 29.431683027263844), 0.002);
        assertEquals(19.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("mil"), 279.6009887590065), 0.019);
        assertEquals(4.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("mil"), 64.74970265998046), 0.0044);

        assertEquals(1.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("au"), 1504715220.361713), 0.0011);
        assertEquals(5.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("au"), 7797160687.3288765), 0.0057);
        assertEquals(6.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("au"), 8207537565.609344), 0.006);

        assertEquals(7.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("ftcf"), 0.3561902548185908), 0.0070999999999999995);
        assertEquals(19.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("ftcf"), 0.9531851889511584), 0.019);
        assertEquals(9.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("ftcf"), 0.46154230201845564), 0.0092);

        assertEquals(3.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("rbtw"), 0.013378037739665385), 0.0032);
        assertEquals(10.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("rbtw"), 0.04222443161581886), 0.0101);
        assertEquals(0.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("fbf"), DistanceUnit.forAbbreviation("rbtw"), 0.0004180636793645433), 0.0001);

        assertEquals(17.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("mm"), 0.0031714785651793527), 0.0174);
        assertEquals(11.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("mm"), 0.0020231846019247595), 0.0111);
        assertEquals(6.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("mm"), 0.0012029746281714787), 0.0066);

        assertEquals(7.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("m"), 1.3670166229221348), 0.0075);
        assertEquals(3.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("m"), 0.6014873140857393), 0.0033);
        assertEquals(15.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("m"), 2.7340332458442695), 0.015);

        assertEquals(17.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("nm"), 3.226159230096238e-9), 0.0177);
        assertEquals(3.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("nm"), 5.650335374744825e-10), 0.0031);
        assertEquals(6.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("nm"), 1.166520851560222e-9), 0.0064);

        assertEquals(0.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("cm"), 0.0009113444152814232), 0.0005);
        assertEquals(19.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("cm"), 0.03627150772820064), 0.019899999999999998);
        assertEquals(12.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("cm"), 0.022965879265091863), 0.0126);

        assertEquals(15.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("in"), 0.06944444444444445), 0.015);
        assertEquals(11.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("in"), 0.05509259259259259), 0.0119);
        assertEquals(4.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("in"), 0.02175925925925926), 0.0047);

        assertEquals(19.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("ft"), 1.1055555555555556), 0.019899999999999998);
        assertEquals(13.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("ft"), 0.7555555555555556), 0.0136);
        assertEquals(3.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("ft"), 0.17222222222222225), 0.0031);

        assertEquals(7.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("yd"), 1.2833333333333334), 0.0077);
        assertEquals(9.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("yd"), 1.6333333333333335), 0.009800000000000001);
        assertEquals(17.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("yd"), 2.85), 0.0171);

        assertEquals(5.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("fathom"), 1.8666666666666667), 0.0056);
        assertEquals(5.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("fathom"), 1.8), 0.0054);
        assertEquals(2.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("fathom"), 0.7666666666666666), 0.0023);

        assertEquals(8.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("km"), 1622.1930592009332), 0.0089);
        assertEquals(15.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("km"), 2806.9407990667833), 0.0154);
        assertEquals(10.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("km"), 1840.9157188684749), 0.0101);

        assertEquals(13.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("mil"), 4077.3333333333335), 0.013900000000000001);
        assertEquals(8.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("mil"), 2346.666666666667), 0.008);
        assertEquals(15.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("mil"), 4458.666666666667), 0.0152);

        assertEquals(0.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("au"), 5453407360.017498), 0.0002);
        assertEquals(17.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("au"), 485353255041.5573), 0.0178);
        assertEquals(4.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("au"), 119974961920.38496), 0.0044);

        assertEquals(18.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("fbf"), 370.7569149168854), 0.018600000000000002);
        assertEquals(5.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("fbf"), 103.65247083697871), 0.0052);
        assertEquals(13.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("fbf"), 271.0910775736366), 0.0136);

        assertEquals(15.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("rbtw"), 1.2583333333333333), 0.015099999999999999);
        assertEquals(9.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("rbtw"), 0.7583333333333334), 0.0091);
        assertEquals(8.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("ftcf"), DistanceUnit.forAbbreviation("rbtw"), 0.6916666666666668), 0.0083);

        assertEquals(13.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("mm"), 0.02952755905511811), 0.0135);
        assertEquals(1.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("mm"), 0.003937007874015749), 0.0018);
        assertEquals(11.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("mm"), 0.025371828521434818), 0.0116);

        assertEquals(10.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("m"), 23.184601924759406), 0.0106);
        assertEquals(12.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("m"), 26.684164479440067), 0.012199999999999999);
        assertEquals(9.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("m"), 21.434820647419073), 0.009800000000000001);

        assertEquals(6.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("nm"), 1.4435695538057744e-8), 0.0066);
        assertEquals(13.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("nm"), 2.887139107611549e-8), 0.0132);
        assertEquals(6.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("nm"), 1.4654418197725286e-8), 0.0067);

        assertEquals(1.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("cm"), 0.028433945756780404), 0.0013);
        assertEquals(13.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("cm"), 0.28652668416447946), 0.013099999999999999);
        assertEquals(0.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("cm"), 0.010936132983377079), 0.0005);

        assertEquals(10.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("in"), 0.6055555555555555), 0.0109);
        assertEquals(3.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("in"), 0.17777777777777778), 0.0032);
        assertEquals(7.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("in"), 0.4388888888888889), 0.0079);

        assertEquals(18.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("ft"), 12.066666666666668), 0.0181);
        assertEquals(4.6, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("ft"), 3.066666666666667), 0.0046);
        assertEquals(8.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("ft"), 5.533333333333335), 0.0083);

        assertEquals(2.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("yd"), 4.0), 0.002);
        assertEquals(13.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("yd"), 27.4), 0.013699999999999999);
        assertEquals(8.1, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("yd"), 16.2), 0.0081);

        assertEquals(13.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("fathom"), 54.0), 0.0135);
        assertEquals(9.4, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("fathom"), 37.6), 0.0094);
        assertEquals(7.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("fathom"), 30.8), 0.0077);

        assertEquals(0.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("km"), 0.0), 0);
        assertEquals(4.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("km"), 9405.074365704288), 0.0043);
        assertEquals(9.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("km"), 21434.820647419074), 0.009800000000000001);

        assertEquals(12.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("mil"), 42240.0), 0.012);
        assertEquals(18.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("mil"), 66176.00000000001), 0.0188);
        assertEquals(2.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("mil"), 8096.0), 0.0023);

        assertEquals(12.5, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("au"), 4090055520013.1235), 0.0125);
        assertEquals(15.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("au"), 4973507512335.958), 0.0152);
        assertEquals(11.2, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("au"), 3664689745931.759), 0.0112);

        assertEquals(8.0, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("fbf"), 1913.5840769903762), 0.008);
        assertEquals(14.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("fbf"), 3420.5315376202975), 0.0143);
        assertEquals(16.3, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("fbf"), 3898.9275568678913), 0.016300000000000002);

        assertEquals(3.7, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("ftcf"), 44.4), 0.0037);
        assertEquals(1.8, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("ftcf"), 21.6), 0.0018);
        assertEquals(7.9, DistanceUnit.convertBetween(DistanceUnit.forAbbreviation("rbtw"), DistanceUnit.forAbbreviation("ftcf"), 94.8), 0.0079);
    }
}
