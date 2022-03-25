var fs = require("fs");

require("../..").registerTransmutation({
    id: "inverted-cgraph",
    requires: ["build-cgraph"],
    type: "information",
    run: function (context) {

        context.output = invertControlGraph(context.inputs["build-cgraph"]);
        context.status = "pass";

        fs.writeFileSync(__dirname + "/cgraph", JSON.stringify(context.output, null, 2));
    }
});

function invertControlGraph(cgraph) {
    var igc = {};

    Object.entries(cgraph).forEach(x => {
        var from = x[0];
        var tos = x[1];

        if (!igc[from]) igc[from] = [];

        tos.forEach(to => {
            if (!igc[to]) igc[to] = [];

            if (!igc[to].includes(from)) igc[to].push(from);
        });
    });
    return igc;
}