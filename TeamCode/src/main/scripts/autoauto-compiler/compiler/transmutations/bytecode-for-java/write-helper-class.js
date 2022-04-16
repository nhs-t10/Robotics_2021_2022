const safeFsUtils = require("../../../../script-helpers/safe-fs-utils");

var PRIMITIVES_PACKAGE = `org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.model.values.primitives`;

module.exports = function(filename, package, className, javaParts) {
    safeFsUtils.safeWriteFile(filename, template(package, className, javaParts));
}

function template(package, className, javaParts) {
    return `package ${package};
    import ${PRIMITIVES_PACKAGE}.*;
    public class ${className} extends ${javaParts.fullExtendsName} {
        private static ${javaParts.constants}

        public ${className}() {
            super(${javaParts.instructions}, ${javaParts.bytecodes});
        }
    }`
}