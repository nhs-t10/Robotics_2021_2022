var parser = require("./aa-parser");

require("..").registerTransmutation({
    requires: [],
    id: "text-to-syntax-tree",
    type: "transformation",
    run: function(context) {
        context.output = parser.parse(context.lastInput);
        context.status = "pass";
    }
})