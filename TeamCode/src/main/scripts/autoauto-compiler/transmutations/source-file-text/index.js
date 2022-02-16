var fs = require("fs");

require("..").registerTransmutation({
    requires: [],
    id: "source-file-text",
    type: "input",
    run: function(context) {
        context.output = fs.readFileSync(context.sourceFullFileName).toString();
        context.status = "pass";
    }
});