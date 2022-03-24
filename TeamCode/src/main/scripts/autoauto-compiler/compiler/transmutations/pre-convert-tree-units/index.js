var query = require("./query");
var unitConversion = require("../../../../unit-conversion");
const androidStudioLogging = require("../../../../script-helpers/android-studio-logging");

var dimensionConversions = {
    "L": {//length
        autoautoBase: "cm",
        coefFromMetricBase: 0.01
    },
    "T": {//time
        autoautoBase: "ms",
        coefFromMetricBase: 0.001
    },
    "A": {//planar angle
        autoautoBase: "degs",
        coefFromMetricBase: Math.PI / 180
    }
}

require("..").registerTransmutation({
    requires: ["text-to-syntax-tree"],
    id: "pre-convert-tree-units",
    type: "transmutation",
    run: function (context) {
        context.output = convert(context.inputs["text-to-syntax-tree"], context.sourceFullFileName);
        context.status = "pass";
    }
});

function convert(tree, loggingFile) {
    var unitvalues = query(tree, "UnitValue");
    unitvalues.forEach(x => rewriteUnitvalue(x, loggingFile));
    return tree;
}

function rewriteUnitvalue(unitValue, loggingFile) {
    if (unitValue.value.type == "NumericValue" && unitValue.unit.type == "Identifier") {
        var uType = unitValue.unit.value;
        var uVal = unitValue.value.v;

        var unitRecord = unitConversion.getUnitForAbbreviation(uType);

        if (!unitRecord) throw { kind: "WARNING", location: unitValue.location, text: `Unknown unit ${uType}` }

        if (unitRecord.constructor == Array) {
            androidStudioLogging.sendTreeLocationMessage({
                kind: "WARNING",
                location: unitValue.location,
                text: "Couldn't decide between different units for abbreviation '" + uType + "': " + fmtChoices(unitRecord) + ".\n Choosing " + unitRecord[0] + "."
            }, loggingFile);
            unitRecord = unitConversion.getUnitForKey(unitRecord[0]);
        }

        var valInMetricBase = uVal * unitRecord.conversionFactors;
        var dimension = unitRecord.dimension;

        var dimConv = dimensionConversions[dimension];
        if (dimConv) {
            unitValue.unit.value = dimConv.autoautoBase;
            unitValue.value.v = valInMetricBase / dimConv.coefFromMetricBase;
        } else {
            throw { kind: "WARNING", location: unitValue.location, text: "No standard measurement for dimension " + dimension }
        }
    }
}

function fmtChoices(u) {
    if (u.length == 1) return u[0];
    else if (u.length == 2) return u.join(" or ");
    else return u.slice(0, -1).join(", ") + " or " + u[u.length - 1];
}