package org.firstinspires.ftc.teamcode.managers.sensor;

import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.auxilary.BasicMapEntry;
import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

import java.util.Arrays;
import java.util.Map;

public class SensorManager extends FeatureManager {
    public Sensor[] sensors;
    public String[] sensorNames;

    /**
     * Make a SensorManager by only specifying the names. The constructor uses the given HardwareMap to resolve each name by itself.
     * <h3>Usage Example:</h3>
     * <pre><code>
     *      new SensorManager(
     *                 hardwareMap,
     *                 SensorManager.colorSensor("colorSensorA", "colorSensorB"),
     *                 SensorManager.touchSensor("touchSensorA", "touchSensorB"),
     *                 SensorManager.distanceSensor("distanceSensorA", "distanceSensorB")
     *         );
     * @param _hardwareMap a hardware map that contains <u>all</u> of the listed hardware devices.
     * @param args A series of at least 3 map entries.
     */


    public SensorManager(HardwareMap _hardwareMap, Map.Entry<String, String[]>... args) {
        String[] _colorSensor = new String[0], _touchSensor = new String[0], _distanceSensor = new String[0];
        for (Map.Entry<String, String[]> a : args) {
            if (a.getKey().equals("crservo")) _colorSensor = a.getValue();
            else if (a.getKey().equals("servo")) _touchSensor = a.getValue();
            else if (a.getKey().equals("motor")) _distanceSensor = a.getValue();
        }
    }
    public static BasicMapEntry<String, String[]> colorSensor(String... names) {
        return new BasicMapEntry<String, String[]>("colorSensor", names);
    }
    public static BasicMapEntry<String, String[]> touchSensor(String... names) {
        return new BasicMapEntry<String, String[]>("touchSensor", names);
    }
    public static BasicMapEntry<String, String[]> distanceSensor(String... names) {
        return new BasicMapEntry<String, String[]>("distanceSensor", names);
    }


    public float[] getHSL(String name) {
        int index = (Arrays.asList(sensorNames)).indexOf(name);
        if(index == -1) throw new IllegalArgumentException("ColorSensor " + name + " does not exist or is not registered in SensorManager");
        return getHSL(index);
    }

    public float[] getHSL(int index) {
        updateSensor(index);
        NormalizedRGBA color = ((ColorSensor)this.sensors[index]).getNormalizedColors();
        return PaulMath.rgbToHsl(color.red, color.green, color.blue);
    }

    public void updateAllSensors() {
        for(Sensor s : sensors) {
            s.update();
        }
    }

    public void updateSensor(int index) {
        this.sensors[index].update();
    }

    public int getColorInteger(int index) {
        updateSensor(index);
        NormalizedRGBA color = ((ColorSensor)this.sensors[index]).getNormalizedColors();
        float scale = 256; int min = 0, max = 255;
        return (Range.clip((int)(color.alpha * scale), min, max) << 24) |
                (Range.clip((int)(color.red   * scale), min, max) << 16) |
                (Range.clip((int)(color.alpha * scale), min, max) << 8) |
                Range.clip((int)(color.blue  * scale), min, max);
    }

    public boolean isSpecial(int index) {
        return isSpecial1(index);
    }
    public boolean isSpecial1(int index) {
        updateSensor(index);
        return ((ColorSensor)this.sensors[index]).isSpecial();
    }


}
