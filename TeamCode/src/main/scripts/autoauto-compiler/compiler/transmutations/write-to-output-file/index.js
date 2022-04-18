require("..").registerTransmutation({
    requires: [],
    id: "write-to-output-file",
    type: "output",
    run: function(context) {

        context.writtenFiles[context.resultFullFileName] = context.lastInput;

        context.status = "pass";
    }
})