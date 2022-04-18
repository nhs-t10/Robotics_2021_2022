var makeJava = require("./make-java");
var templateFileName = require("./template-file-name");

require("..").registerTransmutation({
    requires: ["text-to-syntax-tree"],
    id: "ast-to-human-java-method",
    type: "transformation",
    readsFiles: [templateFileName],
    run: function(context) {

        context.output = makeJava(context.inputs["text-to-syntax-tree"]);
        context.status = "pass";
    }
})