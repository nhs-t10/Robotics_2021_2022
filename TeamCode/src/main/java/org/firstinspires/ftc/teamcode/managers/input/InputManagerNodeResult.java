package org.firstinspires.ftc.teamcode.managers.input;

import org.firstinspires.ftc.teamcode.managers.input.nodes.InputManagerInputNode;

public class InputManagerNodeResult {
    private float value;
    private InputManagerNodeResult childs[];

    public InputManagerNodeResult(float v) {
        this.value = v;
    }
    public InputManagerNodeResult(float[] v) {
        this.childs = new InputManagerNodeResult[v.length];
        for(int i = 0; i < v.length; i++) childs[i] = new InputManagerNodeResult(v[i]);
    }
    public InputManagerNodeResult(InputManagerNodeResult[] v) {
        this.childs = v;
    }

    public float[] getFloatArray() {
        if(this.childs == null) return new float[] {value};

        float[] values = new float[childs.length];
        for(int i = 0; i < values.length; i++) {
            values[i] = childs[i].getFloat();
        }
        return values;
    }

    public float getFloat() {
        if(childs == null) {
            return value;
        } else if(childs.length > 0) {
            return childs[0].getFloat();
        } else {
            return 0;
        }
    }
    public String toString() {
        if(childs == null || childs.length == 0) return "" + value;

        StringBuilder r = new StringBuilder("[");
        for(InputManagerNodeResult c : childs) r.append(c).append(",");
        return r.append("]").toString();
    }
}
