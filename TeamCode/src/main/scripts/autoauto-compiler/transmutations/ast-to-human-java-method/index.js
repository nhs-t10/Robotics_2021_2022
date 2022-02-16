var makeJava = require("./make-java");

require("..").registerTransmutation({
    requires: ["text-to-syntax-tree"],
    id: "ast-to-human-java-method",
    type: "transformation",
    run: function(context) {
        context.output = makeJava(context.inputs["text-to-syntax-tree"]);
        context.status = "pass";
    }
})