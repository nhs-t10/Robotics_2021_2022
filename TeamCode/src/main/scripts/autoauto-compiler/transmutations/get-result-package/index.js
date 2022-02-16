var path = require("path");

require("..").registerTransmutation({
    requires: [],
    id: "get-result-package",
    type: "information",
    run: function(context) {
        var sD = context.sourceDir.split(path.sep);

        context.output = sD.slice(sD.indexOf("java") + 1).join(".");
        context.status = "pass";
    }
});