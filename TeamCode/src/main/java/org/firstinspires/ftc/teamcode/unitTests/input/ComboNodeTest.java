package org.firstinspires.ftc.teamcode.unitTests.input;

import org.firstinspires.ftc.teamcode.managers.input.nodes.ComboNode;
import org.firstinspires.ftc.teamcode.unitTests.dummy.DummyInputNode;
import org.junit.Test;

import static org.junit.Assert.assertEquals;

public class ComboNodeTest {
    @Test
    public void test() {
        DummyInputNode a = new DummyInputNode();
        DummyInputNode b = new DummyInputNode();
        DummyInputNode c = new DummyInputNode();
        ComboNode combo = new ComboNode(a, b, c);

        assertEquals(0, combo.getResult().getFloat(), 0.5);
        a.set(1);
        b.set(1);
        c.set(1);
        assertEquals(1, combo.getResult().getFloat(), 0.5);
    }
}
