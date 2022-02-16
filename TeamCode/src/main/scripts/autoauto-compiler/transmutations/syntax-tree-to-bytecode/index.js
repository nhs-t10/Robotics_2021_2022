var flattenAndProcessTree = require("./flatten-process-tree");

require("..").registerTransmutation({
    requires: ["text-to-syntax-tree"],
    id: "syntax-tree-to-bytecode",
    type: "transformation",
    run: function(context) {
        context.output = flattenAndProcessTree(context.inputs["text-to-syntax-tree"])
        context.status = "pass";
    }
})