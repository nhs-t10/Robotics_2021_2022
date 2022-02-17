var c = require("./checks/run-checks");

require("..").registerTransmutation({
    requires: ["text-to-syntax-tree"],
    id: "tree-check-legacy-checks",
    type: "check",
    run: function(context) {
        var suc = c(context.inputs["text-to-syntax-tree"], context.sourceFullFileName);
        
        if(suc) context.status = "pass";
    }
})