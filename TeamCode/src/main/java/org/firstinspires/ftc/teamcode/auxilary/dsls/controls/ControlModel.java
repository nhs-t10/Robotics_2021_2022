package org.firstinspires.ftc.teamcode.auxilary.dsls.controls;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.controlmaps.ControlMap;
import org.firstinspires.ftc.teamcode.managers.FeatureManager;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.LinkedHashMap;

public class ControlModel {
    public HashMap<String, Control> controls;
    public ControlModelVariables variables;

    public ControlModel(ControlMap map) {
        this.variables = new ControlModelVariables();
        this.controls = getControlFields(map.getClass());
    }

    public ControlModel() { this.variables = new ControlModelVariables(); }

    private HashMap<String, Control> getControlFields(Class mapClass) {
        HashMap<String, Control> result = new HashMap<String, Control>();
        Field[] allDeclaredFields = mapClass.getDeclaredFields();

        for(Field field : allDeclaredFields) {
            if(field.getType().equals(String.class)) {
                try {
                    Control thisControl = parseField(field);
                    result.put(thisControl.name, thisControl);
                } catch (Exception err) {
                    FeatureManager.logger.add("WARNING: Un-parsable control field `" + field.getName() + "` in control map `"
                            + mapClass.getName() + "`: \n" + Arrays.toString(err.getStackTrace()) + "\n" + err.getMessage());
                }
            }
        }
        return result;
    }

    public Control get(String name) {
        return controls.get(name);
    }

    private void updateVariables(GamepadState state) {
        Collection<Control> ctrlArr = controls.values();
        for(Control control : ctrlArr) {
            if(control.containsVariableSetter()) control.res(state);
        }
    }

    public ControlModelVariables getVariables() {
        if(this.variables == null) throw new IllegalStateException("something has gone very wrong and the variable store is null again");
        return this.variables;
    }

    private Control parseField(Field field) throws IllegalAccessException, IllegalArgumentException {
        String fieldName = field.getName();
        String fieldValue = field.get(null).toString();

        Control result = new Control(this);
        result.setName(fieldName);
        result.parseAdvanced(fieldValue, fieldName);

        return result;
    }

    public static class ControlModelVariables {
        private LinkedHashMap<String, float[]> hashMap;

        public ControlModelVariables() {
            this.hashMap = new LinkedHashMap<String, float[]>();
        }
        public float[] get(float[] nameCode) {
            //transform to a string so it won't have problems with strict equality
            char[] name = new char[nameCode.length];
            for(int i = name.length - 1; i >= 0; i--) name[i] = (char)nameCode[i];

            return hashMap.get(new String(name));
        }

        public void put(float[] nameCode, float[] value) {
            //transform to a string so it won't have problems with strict equality
            char[] name = new char[nameCode.length];
            for(int i = name.length - 1; i >= 0; i--) name[i] = (char)nameCode[i];

            hashMap.put(new String(name), value);
        }


    }

    public static class Control {
        public String name;
        public ControlType type;
        private ControlModel model;
        public Control[] children;
        public int state;
        public float value;
        public Control() { state = 0; }

        public Control(ControlModel mdl) {
            this.model = mdl;
            this.state = 0;
        }

        public Control(String src, ControlModel mdl) {
            this.state = 0;
            this.name = "";
            this.model = mdl;

            this.parseAdvanced(src, name);
        }

        public boolean containsVariableAccess() {
            if(this.type.equals(ControlType.GET_VARIABLE)) return true;
            if(this.children == null) return false;

            for(int i = this.children.length - 1; i >= 0; i--) {
                if(this.children[i].containsVariableAccess()) return true;
            }
            return false;
        }
        public boolean containsVariableSetter() {
            if(this.type.equals(ControlType.SET_VARIABLE)) return true;

            if(this.children == null) return false;

            for(int i = this.children.length - 1; i >= 0; i--) {
                if(this.children[i].containsVariableSetter()) return true;
            }
            return false;
        }

        public static class TimeAwareHistory {
            float[][] valueHistory;

            public TimeAwareHistory(int domain) {

            }
        }

        public void parseAdvanced(String src, String name) {

            if(src.matches("^\\s*-?[\\d.]+\\s*$")) {
                this.type = ControlType.LITERAL;
                this.setName(name);
                this.children = new Control[0];
                this.value = Float.parseFloat(src);
                return;
            }

            String[] tokens = src.split("\\b");

            int firstWordIdx = 0;
            for(; firstWordIdx < tokens.length; firstWordIdx++) {
                if (!tokens[firstWordIdx].equals("")) break;
            }

            try {
                this.type = ControlType.valueOf(PaulMath.camelToSnake(tokens[firstWordIdx]));
                this.children = new Control[this.type.paramCount];
            } catch(Exception e) {
                //if there is an error at this point, it means the first word isn't a keyword, and we should just make it into an arbitrary name
                initializeArbitraryName(src);
                return;
            }

            if(this.type.isPrivate) {
                FeatureManager.logger.log("Error parsing control " + name + ": illegal access to internal function " + this.type.name());
                return;
            }

            //if there are no parens, it's an inputMethod and doesn't need further parsing.
            if(src.indexOf('(') == -1) return;

            int parenDepth = 0, currentChildIndex = 0;
            String argSrc = "";

            for(int i = src.indexOf('(') + 1; i < src.length() - 1; i++) {
                if(src.charAt(i) == '(') parenDepth++;
                if(src.charAt(i) == ')') parenDepth--;

                //ignore whitespace between arguments
                if(parenDepth == 0 && src.substring(i, i+1).matches("\\s")) continue;

                //yay, it's done aaaaaaaaa i sound like mr dinneen im so sorry.
                //but anyway the argument we're parsing is Done. put it!! :D
                if(parenDepth == 0 && (src.charAt(i) == ',' || i + 1 == src.length() - 1)) {

                    if(i + 1 == src.length() - 1) argSrc += src.substring(i, i+1);

                    Control child = new Control();
                    this.children[currentChildIndex] = child;
                    child.parseAdvanced(argSrc, name);
                    currentChildIndex++;
                    argSrc = "";
                    continue;
                }

                if(parenDepth < 0) {
                    FeatureManager.logger.log("Error parsing control " + name + ": unbalanced parentheses (too many ending parens)");
                    return;
                }

                argSrc += src.substring(i, i+1);
            }
        }

        public void initializeArbitraryName(String str) {
            this.children = new Control[str.length()];
            this.type = ControlType.NAME;

            for(int i = str.length() - 1; i >= 0; i--) {
                Control lit = new Control(this.model);
                lit.type = ControlType.LITERAL;
                lit.value = (float)str.charAt(i);
                this.children[i] = lit;
            }
        }

        public float[] res(GamepadState state) {

            try {
                return res(state, true);
            } catch(Exception e) {
                FeatureManager.logger.add("ERROR: Controls Error at " + name + " - " + this.type.name());
                FeatureManager.logger.add(e.getMessage());
                FeatureManager.logger.add(Arrays.toString(e.getStackTrace()));
            }

            return new float[] {0};
        }

        private float[] res(GamepadState state, boolean recurse) {
            //update variables if we need them
            if(this.containsVariableAccess() && recurse) model.updateVariables(state);

            //simple input method
            if(type.subtype.equals("InputMethod")) {
                String inKey = type.name().toLowerCase();
                return new float[]{state.getButtonState(inKey)};
            } else {
                switch(type) {
                    case SCALAR:
                        return new float[]{children[0].res(state)[0]};
                    case VECTOR2:
                        return new float[]{
                                children[0].res(state)[0],
                                children[1].res(state)[0]};
                    case VECTOR3:
                        return new float[]{
                                children[0].res(state)[0],
                                children[1].res(state)[0],
                                children[2].res(state)[0]};
                    case VECTOR4:
                        return new float[]{
                                children[0].res(state)[0],
                                children[1].res(state)[0],
                                children[2].res(state)[0],
                                children[3].res(state)[0]};
                    case TOGGLE:
                        boolean currentState = this.state != 0;

                        //only on rising edge so that it doesn't toggle onoffonoffonoff when people press a button
                        if(children[0].res(state)[0] != 0 && this.value == 0) currentState = !currentState;

                        this.state = currentState?1:0;
                        this.value = children[0].res(state)[0];

                        return new float[]{(float) this.state};
                    case HOLD:
                        boolean res = children[0].res(state)[0] != 0;

                        return new float[]{res?1f:0f};
                    case PUSH:
                        //rising edge only
                        float[] r = new float[]{
                            (children[0].res(state)[0] != 0 && this.value == 0)?1f:0f
                        };
                        //remember past value for testing the rising edge later
                        this.value = children[0].res(state)[0];

                        return r;
                    case COMBO:
                        if(children[0].res(state)[0] != 0) return children[1].res(state);
                        else return children[0].res(state);
                    case DEADZONE:
                        float[] a = children[0].res(state);
                        float[] b = children[1].res(state);

                        if (Math.abs(a[0]) > Math.abs(b[0])) return a;
                        else return new float[] {0f, 0f, 0f};
                    case TOGGLE_BETWEEN:
                        boolean currentTbState = this.state != 0;

                        //rising edge? flip!
                        if(children[0].res(state)[0] != 0 && this.value == 0) currentTbState = !currentTbState;
                        this.state = currentTbState?1:0;

                        //remember past value for testing the rising edge later
                        this.value = children[0].res(state)[0];

                        if(currentTbState) return children[1].res(state);
                        else return children[0].res(state);
                    case NOT:
                        return new float[]{
                                (children[0].res(state)[0]==0)?1f:0f
                        };
                    case TERNARY:
                    case IF:
                        if(children[0].res(state)[0] != 0) return children[1].res(state);
                        else return children[2].res(state);
                    case SCALE:
                        float[] toScale = children[0].res(state);
                        float scaleFactor = children[1].res(state)[0];
                        for(int i = 0; i < toScale.length; i++) {
                            toScale[i] *= scaleFactor;
                        }

                        return toScale;
                    case LITERAL:
                        return new float[]{this.value};
                    case SET_VARIABLE:
                        float[] variableSetValue = children[1].res(state);
                        model.getVariables().put(children[0].res(state), variableSetValue);
                        return variableSetValue != null ? variableSetValue : new float[] {0f};
                    case GET_VARIABLE:
                        float[] varVal = model.getVariables().get(children[0].res(state));
                        return varVal != null ? varVal : new float[] {0f};
                    case DUMMY:
                    case NAME:
                        float[] results = new float[children.length];
                        for(int i = children.length - 1; i >= 0; i--) results[i] = children[i].res(state)[0];
                        return results;
                    case GREATER_THAN:
                        return new float[] { children[0].res(state)[0] > children[1].res(state)[0] ? 1f : 0f};
                    case LESS_THAN:
                        return new float[] { children[0].res(state)[0] < children[1].res(state)[0] ? 1f : 0f};
                    case OR:
                        return new float[] {
                                (children[0].res(state)[0] != 0 || children[1].res(state)[0] != 0) ? 1f : 0f};
                    case AND:
                        return new float[] {
                                (children[0].res(state)[0] != 0 && children[1].res(state)[0] != 0) ? 1f : 0f};
                    case NULL:
                    default:
                        return new float[]{0f};
                }
            }
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public ControlType getType() {
            return type;
        }

        public void setType(ControlType type) {
            this.type = type;
        }

        public Control[] getChildren() {
            return children;
        }

        public void setInputs(Control[] children) {
            this.children = children;
        }

        public String toString() {
            if(this.type == ControlType.LITERAL) return this.value + "";

            if(this.type == ControlType.NAME) {
                char[] name = new char[this.children.length];
                for(int i = name.length - 1; i >= 0; i--) name[i] = (char)this.children[i].value;
                return new String(name);
            }

            StringBuilder paramsAsStrings = new StringBuilder();
            for(int i = 0; i < children.length; i++) {
                if(children[i] == null) paramsAsStrings.append("<null>");
                else paramsAsStrings.append(children[i].toString());

                if(i + 1 < children.length) paramsAsStrings.append(", ");
            }
            return this.type.toString() /*+ "<" + this.value + "," + this.state + ">"*/ + (this.type.paramCount > 0 ? "(" + paramsAsStrings + ")" : "");
        }
    }

    public enum ControlType {
        DPAD_LEFT("InputMethod", 0), DPAD_RIGHT("InputMethod", 0), DPAD_DOWN("InputMethod", 0),
        DPAD_UP("InputMethod", 0), A("InputMethod", 0), X("InputMethod", 0), B("InputMethod", 0),
        Y("InputMethod", 0), LEFT_BUMPER("InputMethod", 0), RIGHT_BUMPER("InputMethod", 0),
        LEFT_TRIGGER("InputMethod", 0), RIGHT_TRIGGER("InputMethod", 0), LEFT_STICK_X("InputMethod", 0),
        LEFT_STICK_Y("InputMethod", 0), LEFT_STICK_BUTTON("InputMethod", 0), RIGHT_STICK_X("InputMethod", 0),
        RIGHT_STICK_Y("InputMethod", 0), RIGHT_STICK_BUTTON("InputMethod", 0),
        SQUARE("InputMethod", 0), CIRCLE("InputMethod", 0), CROSS("InputMethod", 0), TRIANGLE("InputMethod", 0),

        GAMEPAD2_DPAD_LEFT("InputMethod", 0), GAMEPAD2_DPAD_RIGHT("InputMethod", 0), GAMEPAD2_DPAD_DOWN("InputMethod", 0),
        GAMEPAD2_DPAD_UP("InputMethod", 0), GAMEPAD2_A("InputMethod", 0), GAMEPAD2_X("InputMethod", 0), GAMEPAD2_B("InputMethod", 0),
        GAMEPAD2_Y("InputMethod", 0), GAMEPAD2_LEFT_BUMPER("InputMethod", 0), GAMEPAD2_RIGHT_BUMPER("InputMethod", 0),
        GAMEPAD2_LEFT_TRIGGER("InputMethod", 0), GAMEPAD2_RIGHT_TRIGGER("InputMethod", 0), GAMEPAD2_LEFT_STICK_X("InputMethod", 0),
        GAMEPAD2_LEFT_STICK_Y("InputMethod", 0), GAMEPAD2_LEFT_STICK_BUTTON("InputMethod", 0), GAMEPAD2_RIGHT_STICK_X("InputMethod", 0),
        GAMEPAD2_RIGHT_STICK_Y("InputMethod", 0), GAMEPAD2_RIGHT_STICK_BUTTON("InputMethod", 0),
        GAMEPAD2_SQUARE("InputMethod", 0), GAMEPAD2_CIRCLE("InputMethod", 0), GAMEPAD2_CROSS("InputMethod", 0), GAMEPAD2_TRIANGLE("InputMethod", 0),

        SCALAR("OutputType", 1), VECTOR2("OutputType", 2), VECTOR3("OutputType", 3), VECTOR4("OutputType", 4),
        TOGGLE("OutputType", 1), HOLD("OutputType", 1), PUSH("OutputType", 1), COMBO("OutputType", 2),
        TOGGLE_BETWEEN("OutputType", 3),

        NOT("Middleware", 1), TERNARY("Middleware", 3), IF("Middleware", 3), SCALE("Middleware", 2),
        DUMMY("Middleware", 1), NULL("Middleware", 0), GREATER_THAN("Middleware", 2), LESS_THAN("Middleware", 2),
        AND("Middleware", 2), OR("Middleware", 2), DEADZONE("Middleware", 2),

        SET_VARIABLE("Variable", 2), GET_VARIABLE("Variable", 1), LITERAL("Literal", 1), NAME("Literal", 1, true);


        public String subtype;
        public int paramCount;
        public boolean isTerminal;
        public boolean isPrivate;

        ControlType(String subtype, int paramCount) {
            this.subtype = subtype;
            this.paramCount = paramCount;
            this.isTerminal = paramCount == 0;
        }

        ControlType(String subtype, int paramCount, boolean isPrivate) {
            this.subtype = subtype;
            this.isPrivate = isPrivate;
            this.paramCount = paramCount;
            this.isTerminal = paramCount == 0;
        }
    }

    public float[] findMidpoint(float x1, float y1, float x2, float y2)  {
        return new float[]{x2 + (x1 - x2), y2 + (y1 - y2)};
    }

    public static class InWhichItIsRevealedThatTheAuthorIsAnIdiotControlsRuntimeException extends Exception {

    }
}
