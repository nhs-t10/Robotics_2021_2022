package org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.nativefunctions;

import org.firstinspires.ftc.teamcode.auxilary.PaulMath;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.AutoautoProgram;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoPrimitive;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.AutoautoUndefined;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoOpmode;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoSystemVariableNames;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.NativeFunction;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.RobotFunctionLoader;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.encapsulation.AutoautoModule;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.AutoautoArgumentException;
import org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.errors.ManagerSetupException;

public class DelegateFunction extends NativeFunction {
    @Override
    public String[] getArgNames() {
        return new String[] { "Address" };
    }

    private final String currentModuleAddress;
    private final RobotFunctionLoader hardwareAccess;
    AutoautoModule delegatedModule;

    public DelegateFunction(String currentModuleAddress, RobotFunctionLoader hardwareAccess) {
        this.hardwareAccess = hardwareAccess;
        this.currentModuleAddress = currentModuleAddress;
    }

    @Override
    public AutoautoPrimitive call(AutoautoPrimitive[] args) throws ManagerSetupException {
        if(args.length == 0) throw new AutoautoArgumentException("You must give at least 1 argument to delegate()! "  + AutoautoProgram.formatStack(getLocation()));

        String rawAddress = args[0].getString();
        String address = pathnameToClassname(rawAddress, currentModuleAddress);

        //if the cache is empty or invalid, re-create it
        if(delegatedModule == null || !delegatedModule.address.equals(address)) {
            AutoautoProgram program;
            try {
                Class<AutoautoOpmode> p = (Class<AutoautoOpmode>) Class.forName(address);
                program = (AutoautoProgram) p.getMethod("generateProgram").invoke(null);
            } catch (Exception e) {
                throw new AutoautoArgumentException("No such module `" + address + "`"+ AutoautoProgram.formatStack(getLocation()));
            }
            delegatedModule = new AutoautoModule(program, address, hardwareAccess);
        }

        delegatedModule.loop();

        AutoautoPrimitive exported = delegatedModule.globalScope.get(AutoautoSystemVariableNames.EXPORTS);
        if(exported != null) return exported;
        else return new AutoautoUndefined();
    }

    public static String pathnameToClassname(String pathname, String relativeTo) {
        String pathnameNoExtension = pathname.replaceAll("\\.autoauto$", "__autoauto");

        String absolutePackage = relativeTo.substring(0, relativeTo.lastIndexOf("."));
        String relativePackage = pathnameNoExtension
            .replace('/', '.')
            .replace('\\', '.');

        String classishName = relativePackage.substring(Math.max(0,relativePackage.lastIndexOf(".") + 1));

        relativePackage = relativePackage.substring(0, Math.max(0,relativePackage.lastIndexOf(".")));


        String classname = PaulMath.kebabToPascal(classishName);

        return absolutePackage + "." + relativePackage + "." + classname;

    }
}
