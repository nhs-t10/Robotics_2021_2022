package org.firstinspires.ftc.teamcode.unitTests.input;

import org.firstinspires.ftc.teamcode.managers.input.nodes.SwitchNode;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyInputNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class SwitchNodeTest {
    @Test
    public void test() {
        DummyInputNode switcher = new DummyInputNode(0);
        DummyInputNode on = new DummyInputNode(3);
        DummyInputNode off = new DummyInputNode(2);

        SwitchNode switchNode = new SwitchNode(switcher, on, off);

        assertEquals(2, switchNode.getResult().getFloat(), 0);

        switcher.set(20);
        switchNode.update();

        assertEquals(3, switchNode.getResult().getFloat(), 0);
    }
}
