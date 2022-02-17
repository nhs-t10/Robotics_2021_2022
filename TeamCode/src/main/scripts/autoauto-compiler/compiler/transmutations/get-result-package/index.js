var path = require("path");

require("..").registerTransmutation({
    requires: [],
    id: "get-result-package",
    type: "information",
    run: function(context) {
        var sD = context.resultDir.split(path.sep);

        context.output = sD.slice(sD.indexOf("gen") + 1).join(".");
        context.status = "pass";
    }
});