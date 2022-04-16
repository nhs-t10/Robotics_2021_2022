const safeFsUtils = require("../../../../script-helpers/safe-fs-utils");

require("..").registerTransmutation({
    requires: [],
    id: "cleanup-old-files",
    type: "codebase_postprocess",
    run: function(context, contexts) {
        safeFsUtils.cleanDirectory(context.resultRoot, contexts.map(x=>x.usedFiles).flat());
    }
});