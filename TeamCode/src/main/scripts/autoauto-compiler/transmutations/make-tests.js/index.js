var path = require("path");

require("..").registerTransmutation({
    requires: [],
    id: "make-tests",
    type: "codebase_postprocess",
    run: function(context, contexts) {
        var testDir = path.join(context.sourceRoot, "test/java/org/firstinspires/ftc/teamcode/unitTests/__testedautoauto");
    }
});
