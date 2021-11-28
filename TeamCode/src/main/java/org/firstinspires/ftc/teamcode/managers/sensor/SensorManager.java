package org.firstinspires.ftc.teamcode.managers.sensor;

import com.qualcomm.robotcore.hardware.HardwareDevice;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.NormalizedColorSensor;
import com.qualcomm.robotcore.hardware.NormalizedRGBA;
import com.qualcomm.robotcore.util.Range;

import org.firstinspires.ftc.teamcode.auxilary.ColorSensor;
import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.Sensor;
import org.firstinspires.ftc.teamcode.managers.CV.CVManager;
import org.firstinspires.ftc.teamcode.managers.feature.FeatureManager;

import java.util.Arrays;

public class SensorManager extends FeatureManager {
    public Sensor[] sensors;

    public String[] sensorNames;

    public SensorManager(HardwareMap hardwareMap, String[] _sensorNames) {
        this.sensorNames = _sensorNames;

        Sensor[] sensors = new Sensor[_sensorNames.length];
        for(int i = 0; i < sensors.length; i++) sensors[i] = new ColorSensor(hardwareMap.get(NormalizedColorSensor.class, _sensorNames[i]));
        this.sensors = sensors;
    }

    public SensorManager(Sensor[] _sensors, String[] _sensorNames) {
        if(_sensorNames.length != _sensors.length) throw new IllegalArgumentException("Sensor Names must be the same length as Sensors");
        this.sensors = _sensors;
        this.sensorNames = _sensorNames;
    }

    public void setSensorNames(String[] _sensorNames) {
        if(_sensorNames.length != sensorNames.length) throw new IllegalArgumentException("Sensor Names must be the same length as Sensors");
        this.sensorNames = _sensorNames;
    }

    public float[] getHSL(String name) {
        int index = (Arrays.asList(sensorNames)).indexOf(name);
        if(index == -1) throw new IllegalArgumentException("Motor " + name + " does not exist or is not registered");
        return getHSL(index);
    }

    public float[] getHSL(int index) {
        NormalizedRGBA color = ((ColorSensor)this.sensors[index]).getNormalizedColors();
        return PaulMath.rgbToHsl(color.red, color.green, color.blue);
    }

    public void update() {
        for(Sensor s : sensors) {
            s.update();
        }
    }

    public void update(int index) {
        this.sensors[index].update();
    }

    public int getColorInteger(int index) {
        NormalizedRGBA color = ((ColorSensor)this.sensors[index]).getNormalizedColors();
        float scale = 256; int min = 0, max = 255;
        return (Range.clip((int)(color.alpha * scale), min, max) << 24) |
                (Range.clip((int)(color.red   * scale), min, max) << 16) |
                (Range.clip((int)(color.alpha * scale), min, max) << 8) |
                Range.clip((int)(color.blue  * scale), min, max);
    }

    public boolean isSpecial(int index) {
        update(index);
        return ((ColorSensor)this.sensors[index]).isSpecial1();
    }
    public boolean isSpecial1(int index) {
        update(index);
        return ((ColorSensor)this.sensors[index]).isSpecial1();
    }


}
