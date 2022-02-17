require("../..").registerTransmutation({
    id: "build-cgraph",
    requires: ["build-bcreqs"],
    type: "information",
    run: function(context) {
        console.log("f")
    }
})