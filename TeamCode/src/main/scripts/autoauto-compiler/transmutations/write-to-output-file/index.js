var fs = require("fs");
var safeFsUtils = require("../../../script-helpers/safe-fs-utils");

require("..").registerTransmutation({
    requires: [],
    id: "write-to-output-file",
    type: "output",
    run: function(context) {
        safeFsUtils.createDirectoryIfNotExist(context.resultDir);
        
        fs.writeFileSync(context.resultFullFileName, context.lastInput);
        context.status = "pass";
    }
})