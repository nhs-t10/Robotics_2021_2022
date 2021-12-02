package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model;

public class Location {
    public String statepath;
    public int stateNumber;


    public int line;
    public int col;

    public Location clone() {
        return new Location(statepath, stateNumber, line, col);
    }

    //static alias to constructor
    //for code compression purposes
    private static String lastStatepath;
    private static int lastStatenumber;
    private static int lastLine;
    private static int lastCol;
    public static Location L (String statepath, int stateNumber, int line, int col) {
        lastStatepath = statepath;
        lastStatenumber = stateNumber;
        lastLine = line;
        lastCol = col;

        return new Location(statepath, stateNumber, line, col);
    }
    public static Location L(int stateNumber, int line, int col) {
        lastStatenumber = stateNumber;
        lastLine = line;
        lastCol = col;
        return new Location(lastStatepath, stateNumber, line, col);
    }
    public static Location L(int line, int col) {
        lastLine = line;
        lastCol = col;
        return new Location(lastStatepath, lastStatenumber, line, col);
    }
    public static Location L(int col) {
        lastCol = col;
        return new Location(lastStatepath, lastStatenumber, lastLine, col);
    }
    public static Location L() {
        return new Location(lastStatepath, lastStatenumber, lastLine, lastCol);
    }

    public static void L(AutoautoProgramElement e, String statepath, int stateNumber, int line, int col) {
        e.setLocation(L(statepath, stateNumber, line, col));
    }
    public static void L(AutoautoProgramElement e, int stateNumber, int line, int col) {
        e.setLocation(L(stateNumber, line, col));
    }
    public static void L(AutoautoProgramElement e, int line, int col) {
        e.setLocation(L(line, col));
    }
    public static void L(AutoautoProgramElement e, int col) {
        e.setLocation(L(col));
    }
    public static void L(AutoautoProgramElement e) {
        e.setLocation(L());
    }

    public Location(String statepath, int stateNumber, int line, int col) {
        assert statepath != null;

        this.statepath = statepath;
        this.stateNumber = stateNumber;
        this.line = line;
        this.col = col;
    }

    public String toString() {
        return "Statepath " + statepath + " / state " + stateNumber + "; line " + line + " : col " + col;
    }

    public void setStatepath(String name) {
        this.statepath = name;
    }
}
