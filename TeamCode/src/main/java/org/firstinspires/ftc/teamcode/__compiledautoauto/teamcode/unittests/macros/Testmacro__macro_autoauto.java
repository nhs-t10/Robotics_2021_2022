package org.firstinspires.ftc.teamcode.__compiledautoauto.teamcode.unittests.macros;

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
public class Testmacro__macro_autoauto extends AutoautoMacro {
    public AutoautoProgram program = generateProgram();

    public static void sL(AutoautoProgramElement e, Location l) {
        e.setLocation(l);
    }

    /*USER_DEFINED_FUNCTIONS*/

    public AutoautoProgram generateProgram() {

            String j="log";VariableReference h=H(j);String n="First state";AutoautoString m=U(n);AutoautoValue[]l={m};FunctionCall g=M(h,
l);FunctionCallStatement f=F(g);NextStatement o=N();Statement[]$e=new Statement[]{f,o};State e=A($e);VariableReference s=H(j);String w="Second state";AutoautoString v=U(w);AutoautoValue[]u={v};FunctionCall r=M(s,
u);FunctionCallStatement q=F(r);String a0="stopMacro";VariableReference z=H(a0);AutoautoValue[]b0={};FunctionCall y=M(z,
b0);FunctionCallStatement x=F(y);VariableReference e0=H(j);String i0="Second state, after stopMacro()";AutoautoString h0=U(i0);AutoautoValue[]g0={h0};FunctionCall d0=M(e0,
g0);FunctionCallStatement c0=F(d0);NextStatement j0=N();Statement[]$p=new Statement[]{q,x,c0,j0};State p=A($p);VariableReference n0=H(j);String r0="failed :( we got to the next state despite stopping";AutoautoString q0=U(r0);AutoautoValue[]p0={q0};FunctionCall m0=M(n0,
p0);FunctionCallStatement l0=F(m0);Statement[]$k0=new Statement[]{l0};State k0=A($k0);State[]$d=new State[]{e,p,k0};String k="init";Statepath d=S($d,
k);
            HashMap<String, Statepath> c = new HashMap<String, Statepath>();
            c.put(k, d);
            AutoautoProgram u0 = P(c,
 k);
            L(h,k,0,2,5);L(m,k,0,2,9);L(g,k,0,2,8);L(f,k,0,2,5);L(o,k,0,2,25);L(e,k,0,2,5);L(s,k,1,3,5);L(v,k,1,3,9);L(r,
k,1,3,8);L(q,k,1,3,5);L(z,k,1,3,26);L(y,k,1,3,35);L(x,k,1,3,26);L(e0,k,1,3,39);L(h0,k,1,3,43);L(d0,k,1,3,42);L(c0,k,1,3,
39);L(j0,k,1,3,79);L(p,k,1,2,30);L(n0,k,2,4,5);L(q0,k,2,4,9);L(m0,k,2,4,8);L(l0,k,2,4,5);L(k0,k,2,3,84);return u0;
    }
}
