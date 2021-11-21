package org.firstinspires.ftc.teamcode.managers.input;

import org.firstinspires.ftc.teamcode.managers.input.nodes.InputManagerInputNode;

public class InputManagerNodeResult {
    public static final InputManagerNodeResult FALSE = new InputManagerNodeResult(false);

    private float value;
    private InputManagerNodeResult[] childs;

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

    public InputManagerNodeResult(boolean result) {
        this(result?1f:0f);
    }

    public InputManagerNodeResult() {
        this.value = 0;
    }

    public float[] getFloatArray() {
        if(this.childs == null || this.childs.length == 0) return new float[] {value};

        float[] values = new float[childs.length];
        for(int i = 0; i < values.length; i++) {
            values[i] = childs[i].getFloat();
        }
        return values;
    }
    public void setFloatArray(float[] f) {
        this.childs = new InputManagerNodeResult[f.length];
        for(int i = 0; i < f.length; i++) childs[i] = new InputManagerNodeResult(f[i]);
    }

    public float getFloat() {
        if(childs == null || childs.length == 0) {
            return value;
        } else {
            return childs[0].getFloat();
        }
    }
    public void setFloat(float f) {
        this.childs = new InputManagerNodeResult[0];
        this.value = f;
    }

    public String toString() {
        if(childs == null || childs.length == 0) return "" + value;

        StringBuilder r = new StringBuilder("[");
        for(InputManagerNodeResult c : childs) r.append(c).append(",");
        return r.append("]").toString();
    }

    public boolean getBool() {
        return getFloat() != 0;
    }
    public void setBool(boolean b) {
        this.childs = new InputManagerNodeResult[0];
        this.value = b ? 1f : 0f;
    }

    public void copyValuesFrom(InputManagerNodeResult other) {
        this.childs = other.childs;
        this.value = other.value;
    }

    public void setChildren(InputManagerNodeResult[] c) {
        this.childs = c;
    }
}
