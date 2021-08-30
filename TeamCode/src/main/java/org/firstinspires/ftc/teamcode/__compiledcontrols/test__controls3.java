package org.firstinspires.ftc.teamcode.__compiledcontrols;
import org.firstinspires.ftc.teamcode.auxilary.dsls.controls3.runtime.Controls3SkipPathException;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import com.qualcomm.robotcore.hardware.DcMotor;
@TeleOp
public class test__controls3 extends OpMode {
    public DcMotor f;
    public DcMotor g;
    public DcMotor h;
    public DcMotor i;
    public void init() {
        f = hardwareMap.get(DcMotor.class, "fl");
        g = hardwareMap.get(DcMotor.class, "bl");
        h = hardwareMap.get(DcMotor.class, "fr");
        i = hardwareMap.get(DcMotor.class, "br");
    }
    public void loop() {
        a();
    }
     public float c() { return gamepad1.left_stick_x; }
     public float d() { return gamepad1.left_stick_y; }
     public float e() { return gamepad1.right_stick_x; }
     public float[] b() { return PaulMath.omniCalc(c(),d(), e()); }
     public void a() { float[] a = b();f.setPower(a[0]); g.setPower(a[1]); h.setPower(a[2]); i.setPower(a[3]); }
}