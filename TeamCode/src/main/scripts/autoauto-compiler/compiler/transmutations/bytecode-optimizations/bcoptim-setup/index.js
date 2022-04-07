require("../..").registerTransmutation({
    id: "bcoptim-setup",
    requires: ["build-cgraph", "combine-basic-blocks", "single-static", "type-inference", "type-checking"],
    type: "information",
    run: function(context) {
        context.output = context.inputs["type-checking"];
        context.status = "pass";
    }
});