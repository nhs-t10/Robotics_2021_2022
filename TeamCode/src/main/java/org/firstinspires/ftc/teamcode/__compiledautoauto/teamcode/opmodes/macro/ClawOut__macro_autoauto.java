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
public class ClawOut__macro_autoauto extends AutoautoMacro {
    public AutoautoProgram program = generateProgram();

    public static void sL(AutoautoProgramElement e, Location l) {
        e.setLocation(l);
    }

    /*USER_DEFINED_FUNCTIONS*/

    public AutoautoProgram generateProgram() {

            String j="setServoPosition";VariableReference h=H(j);String n="intakeMoverLeft";AutoautoString m=U(n);AutoautoNumericValue o=C(-1);AutoautoValue[]l={m,
o};FunctionCall g=M(h,l);FunctionCallStatement f=F(g);VariableReference r=H(j);String v="intakeMoverRight";AutoautoString u=U(v);AutoautoNumericValue w=C(1);AutoautoValue[]t={u,
w};FunctionCall q=M(r,t);FunctionCallStatement p=F(q);NextStatement x=N();Statement[]$e=new Statement[]{f,p,x};State e=A($e);String c0="setServoPower";VariableReference b0=H(c0);String f0="nateMoverLeft";AutoautoString e0=U(f0);AutoautoNumericValue g0=C(-1);AutoautoValue[]d0={e0,
g0};FunctionCall a0=M(b0,d0);FunctionCallStatement z=F(a0);VariableReference j0=H(c0);String n0="nateMoverRight";AutoautoString m0=U(n0);AutoautoNumericValue o0=C(1);AutoautoValue[]l0={m0,
o0};FunctionCall i0=M(j0,l0);FunctionCallStatement h0=F(i0);String r0="ms";AutoautoUnitValue q0=E(2650,r0);NextStatement s0=N();AfterStatement p0=W(q0,
s0);Statement[]$y=new Statement[]{z,h0,p0};State y=A($y);VariableReference w0=H(c0);AutoautoString z0=U(f0);AutoautoNumericValue a1=C(0);AutoautoValue[]y0={z0,
a1};FunctionCall v0=M(w0,y0);FunctionCallStatement u0=F(v0);VariableReference d1=H(c0);AutoautoString g1=U(n0);AutoautoNumericValue h1=C(0);AutoautoValue[]f1={g1,
h1};FunctionCall c1=M(d1,f1);FunctionCallStatement b1=F(c1);NextStatement i1=N();Statement[]$t0=new Statement[]{u0,b1,i1};State t0=A($t0);String n1="stopMacro";VariableReference m1=H(n1);AutoautoValue[]o1={};FunctionCall l1=M(m1,
o1);FunctionCallStatement k1=F(l1);Statement[]$j1=new Statement[]{k1};State j1=A($j1);State[]$d=new State[]{e,y,t0,j1};String k="clawout";Statepath d=S($d,
k);
            HashMap<String, Statepath> c = new HashMap<String, Statepath>();
            c.put(k, d);
            AutoautoProgram r1 = P(c,
 k);
            L(h,k,0,2,5);L(m,k,0,2,22);L(o,k,0,2,41);L(g,k,0,2,21);L(f,k,0,2,5);L(r,k,0,2,48);L(u,k,0,2,65);L(w,k,0,
2,85);L(q,k,0,2,64);L(p,k,0,2,48);L(x,k,0,2,91);L(e,k,0,2,5);L(b0,k,1,3,5);L(e0,k,1,3,19);L(g0,k,1,3,36);L(a0,k,1,3,18);L(z,
k,1,3,5);L(j0,k,1,3,43);L(m0,k,1,3,57);L(o0,k,1,3,75);L(i0,k,1,3,56);L(h0,k,1,3,43);L(q0,k,1,3,87);L(s0,k,1,3,94);L(p0,k,
1,3,81);L(y,k,1,2,96);L(w0,k,2,4,5);L(z0,k,2,4,19);L(a1,k,2,4,36);L(v0,k,2,4,18);L(u0,k,2,4,5);L(d1,k,2,4,42);L(g1,k,2,4,
56);L(h1,k,2,4,74);L(c1,k,2,4,55);L(b1,k,2,4,42);L(i1,k,2,4,80);L(t0,k,2,3,99);L(m1,k,3,5,5);L(l1,k,3,5,14);L(k1,k,3,5,5);L(j1,
k,3,4,85);return r1;
    }
}
