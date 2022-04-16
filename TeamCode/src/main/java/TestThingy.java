import org.firstinspires.ftc.teamcode.__compiledautoauto.teamcode.opmodes.auto.Everything;
import org.firstinspires.ftc.teamcode.__compiledautoauto.teamcode.opmodes.auto.Everything____AutoautoProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyGamepad;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyHardwareMap;

public class TestThingy {
    public static void main(String[] args) {
        AutoautoProgram p = new Everything____AutoautoProgram();

        p.init();
        p.stepInit();

        while(true) p.loop();
    }
}
