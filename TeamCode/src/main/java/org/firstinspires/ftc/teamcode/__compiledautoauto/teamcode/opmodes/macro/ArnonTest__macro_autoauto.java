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
public class ArnonTest__macro_autoauto extends AutoautoMacro {
    public AutoautoProgram program = generateProgram();

    public static void sL(AutoautoProgramElement e, Location l) {
        e.setLocation(l);
    }

    /*USER_DEFINED_FUNCTIONS*/

    public AutoautoProgram generateProgram() {

            String j="log";VariableReference h=H(j);AutoautoNumericValue m=C(6);AutoautoValue[]l={m};FunctionCall g=M(h,
l);FunctionCallStatement f=F(g);Statement[]$e=new Statement[]{f};State e=A($e);State[]$d=new State[]{e};String k="init";Statepath d=S($d,
k);
            HashMap<String, Statepath> c = new HashMap<String, Statepath>();
            c.put(k, d);
            AutoautoProgram p = P(c,
 k);
            L(h,k,0,2,1);L(m,k,0,2,5);L(g,k,0,2,4);L(f,k,0,2,1);L(e,k,0,2,1);return p;
    }
}
