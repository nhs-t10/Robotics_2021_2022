require("../..").registerTransmutation({
    id: "bcoptim-setup",
    requires: ["build-cgraph", "combine-basic-blocks", "single-static", "type-inference"],
    type: "information",
    run: function(context) {
    }
});