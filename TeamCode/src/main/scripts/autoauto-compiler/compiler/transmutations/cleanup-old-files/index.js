const safeFsUtils = require("../../../../script-helpers/safe-fs-utils");

require("..").registerTransmutation({
    requires: [],
    id: "cleanup-old-files",
    type: "codebase_postprocess",
    run: function(context, contexts) {
        var newFiles = contexts.map(x=>Object.keys(x.writtenFiles)).flat();
        
        safeFsUtils.cleanDirectory(context.resultRoot, newFiles);
    }
});