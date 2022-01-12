package org.firstinspires.ftc.teamcode.__compiledautoauto.teamcode.opmodes.macro;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.ColorRangeSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.Servo;
import org.firstinspires.ftc.teamcode.auxilary.buildhistory.BuildHistory;

import org.firstinspires.ftc.teamcode.auxilary.EncodedMotor;
import org.firstinspires.ftc.teamcode.auxilary.ColorSensor;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.*;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntime;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoOpmode;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoMacro;
import org.firstinspires.ftc.teamcode.managers.*;
import org.firstinspires.ftc.teamcode.managers.imu.ImuManager;
import org.firstinspires.ftc.teamcode.managers.manipulation.ManipulationManager;
import org.firstinspires.ftc.teamcode.managers.movement.MovementManager;
import org.firstinspires.ftc.teamcode.managers.sensor.SensorManager;
import org.firstinspires.ftc.teamcode.managers.telemetry.TelemetryManager;
import org.junit.Test;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyHardwareMap;

import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Location.L;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram.P;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntime.R;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.Statepath.S;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.State.A;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements.NextStatement.N;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements.FunctionCallStatement.F;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements.AfterStatement.W;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.FunctionCall.M;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.ArithmeticValue.O;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoNumericValue.C;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoString.U;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoTable.K;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUnitValue.E;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements.GotoStatement.G;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements.LetStatement.D;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.statements.IfStatement.I;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.BooleanOperator.T;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.VariableReference.H;
import static org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoBooleanValue.B;

import java.util.HashMap;

@SuppressWarnings("WrongPackageStatement")
public class SpinTaunt__macro_autoauto extends AutoautoMacro {
    public AutoautoProgram program = generateProgram();

    public static void sL(AutoautoProgramElement e, Location l) {
        e.setLocation(l);
    }

    /*USER_DEFINED_FUNCTIONS*/

    public AutoautoProgram generateProgram() {

            String g="SpinTaunt";GotoStatement f=G(g);Statement[]$e=new Statement[]{f};State e=A($e);State[]$d=new State[]{e};String h="init";Statepath d=S($d,
h);String q="driveRaw";VariableReference p=H(q);AutoautoNumericValue s=C(1),t=C(-1),u=C(1),v=C(-1);AutoautoValue[]r={s,t,
u,v};FunctionCall o=M(p,r);FunctionCallStatement n=F(o);String y="degs";AutoautoUnitValue x=E(3600,y);NextStatement z=N();AfterStatement w=W(x,
z);Statement[]$m=new Statement[]{n,w};State m=A($m);String e0="stopMacro";VariableReference d0=H(e0);AutoautoValue[]f0={};FunctionCall c0=M(d0,
f0);FunctionCallStatement b0=F(c0);Statement[]$a0=new Statement[]{b0};State a0=A($a0);State[]$l=new State[]{m,a0};Statepath l=S($l,
g);
            HashMap<String, Statepath> c = new HashMap<String, Statepath>();
            c.put(h, d);
c.put(g, l);
            AutoautoProgram i0 = P(c,
 h);
            L(f,h,0,2,1);L(e,h,0,2,1);L(p,g,0,5,2);L(s,g,0,5,12);L(t,g,0,5,14);L(u,g,0,5,17);L(v,g,0,5,19);L(o,g,0,
5,11);L(n,g,0,5,2);L(x,g,0,5,30);L(z,g,0,5,39);L(w,g,0,5,24);L(m,g,0,5,2);L(d0,g,1,6,2);L(c0,g,1,6,11);L(b0,g,1,6,2);L(a0,
g,1,5,44);return i0;
    }
}
