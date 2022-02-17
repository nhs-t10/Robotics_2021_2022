var astTools = require("./ast-tools");

require("..").registerTransmutation({
    requires: ["text-to-syntax-tree"],
    id: "ast-to-java-setup-method",
    type: "transformation",
    run: function(context) {
        context.output = astTools(context.inputs["text-to-syntax-tree"]);
        context.status = "pass";
    }
})