package org.firstinspires.ftc.teamcode.__compiledautoauto;

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
public class TestMacro__macro_autoauto extends AutoautoMacro {
    public static AutoautoProgram program = generateProgram();

    public static void sL(AutoautoProgramElement e, Location l) {
        e.setLocation(l);
    }

    /*USER_DEFINED_FUNCTIONS*/

    public static AutoautoProgram generateProgram() {
final String i="setServoPosition",j="i",m="arm",v="s",q0="end";

AutoautoUnitValue u=E(3,v);AutoautoNumericValue o=C(3),k0=C(1);AutoautoString l=U(m),i0=U(m);AutoautoValue[]k={l,o},h0={i0,
k0};FunctionCall h=M(i,k),e0=M(i,h0);NextStatement y=N();FunctionCallStatement g=F(h),d0=F(e0);GotoStatement p0=G(q0);AfterStatement t=W(u,
y);Statement[]$f=new Statement[]{g,t},$c0={d0,p0};State f=A($f),c0=A($c0);State[]$e=new State[]{f,c0};Statepath e=S($e,j);HashMap<String,
Statepath> d = new HashMap<String,Statepath>();d.put(j,e);AutoautoProgram x0 = P(d,j);sL(l,L(j,0,2,18));sL(o,L(j,0,2,25));sL(h,
L(j,0,2,1));sL(g,L(j,0,2,1));sL(u,L(j,0,2,35));sL(y,L(j,0,2,38));sL(t,L(j,0,2,29));sL(f,L(j,0,2,1));sL(i0,L(j,1,3,18));sL(k0,
L(j,1,3,25));sL(e0,L(j,1,3,1));sL(d0,L(j,1,3,1));sL(p0,L(j,1,3,29));sL(c0,L(j,1,2,43));return x0;
    }
}
