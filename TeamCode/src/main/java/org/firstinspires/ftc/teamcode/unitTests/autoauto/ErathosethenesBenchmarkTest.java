package org.firstinspires.ftc.teamcode.unitTests.autoauto;

import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.junit.Test;

public class ErathosethenesBenchmarkTest {
    @Test
    public void test() {


        int primesTarget = 10001;
        boolean[] composite = new boolean[primesTarget];
        boolean[] prime = new boolean[primesTarget];

        int nextPrime = 2;

        prime[nextPrime] = true;

        while(true) {
            //mark all multiples of this prime as composite
            for(int i = 2; i < primesTarget; i++) {
                if(i * nextPrime >= primesTarget) continue;
                composite[i * nextPrime] = true;
            }

            //find the next prime!
            for(int i = nextPrime + 1; true; i++) {
                nextPrime = i;

                if(i >= primesTarget) break;
                else if(!composite[i]) break;
            }
            if(nextPrime >= primesTarget) break;
            else prime[nextPrime] = true;
        }
        StringBuilder primesStr = new StringBuilder();
        for(int i = 0; i < prime.length; i++) {
            if(prime[i]) primesStr.append(i).append(",");
        }
        FeatureManager.logger.log(primesStr.toString());
    }
}
