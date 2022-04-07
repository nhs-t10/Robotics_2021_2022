package org.firstinspires.ftc.teamcode.managers.autocorrection;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements.AfterStatement;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoNumericValue;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;
import org.firstinspires.ftc.teamcode.managers.imu.ImuManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;

public class AutocorrectionManager extends FeatureManager {
    MovementManager driving;
    ImuManager imu;

    private int lastFL;
    private int lastFR;
    private int lastBL;
    private int lastBR;
    private float X;
    private float Y;
    private float lastX;
    private float lastY;
    private float Theta;
    private float[] correction;
    public AutocorrectionManager(MovementManager driving, ImuManager imu){
        this.driving =driving;
        this.imu = imu;
    }
    public void calculateCorrection(){
        //Compute change in encoder positions


        int fl = driving.frontLeft.getCurrentPosition();
        int fr = driving.frontRight.getCurrentPosition();
        int bl = driving.backLeft.getCurrentPosition();
        int br = driving.backRight.getCurrentPosition();
        int delt_fl = fl - lastFL;
        int delt_fr = fr - lastFR;
        int delt_bl = bl - lastBL;
        int delt_br = br - lastBR;
//Compute displacements for each wheel
        float displ_m0 = PaulMath.encoderDistanceCm(delt_fl);
        float displ_m1 = PaulMath.encoderDistanceCm(delt_fr);
        float displ_m2 = PaulMath.encoderDistanceCm(delt_bl);
        float displ_m3 = PaulMath.encoderDistanceCm(delt_br);
//Compute the average displacement in order to untangle rotation from displacement
        float displ_average = (float) ((displ_m0 + displ_m1 + displ_m2 + displ_m3) / 4.0);
//Compute the component of the wheel displacements that yield robot displacement
        float dev_m0 = displ_m0 - displ_average;
        float dev_m1 = displ_m1 - displ_average;
        float dev_m2 = displ_m2 - displ_average;
        float dev_m3 = displ_m3 - displ_average;
//Compute the displacement of the holonomic drive, in robot reference frame
        //float twoSqrtTwo = (float) (2.0*Math.sqrt(2));
        if(displ_m1 == displ_m2&&displ_m2 == displ_m0&&displ_m0 == displ_m3){
            logger.log("Displacements match (Moving forward or backward");
            float delt_Xr = displ_average;
            float delt_Yr = 0;
        }
        if(displ_m0 == displ_m2&&displ_m2 == -displ_m0&&displ_m1 == displ_m3){
            logger.log("Displacements inverse (Moving left or right)");
            float delt_Xr = displ_average;
            float delt_Yr = 0;
        }
//Move this holonomic displacement from robot to field frame of reference
        float robotTheta = imu.getThirdAngleOrientation();
        float cosTheta = (float)Math.cos(robotTheta);
        float sinTheta = (float)Math.sin(robotTheta);
        float delt_Xf = delt_Xr * cosTheta - delt_Yr * sinTheta;
        float delt_Yf = delt_Yr * cosTheta + delt_Xr * sinTheta;
//Update the position
        lastX = X;
        lastY = Y;
        X = lastX + delt_Xf;
        Y = lastY + delt_Yf;
        Theta = robotTheta;
        lastFL = fl;
        lastFR = fr;
        lastBL = bl;
        lastBR = br;
        correction = new float[]{X, Y, Theta};
    }
    public float[] getCorrection(){
        return correction;
    }
    public void resetCorrection(){
        lastX = 0;
        lastY = 0;
        lastFL = 0;
        lastFR = 0;
        lastBL = 0;
        lastBR = 0;
        X=0;
        Y=0;
        Theta=0;
    }
    public void doCorrection(float targetX, float targetY, float targetTheta){

    }
}
