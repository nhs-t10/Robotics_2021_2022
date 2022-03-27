var query = require("./query");
var unitConversion = require("../../../../unit-conversion");
const androidStudioLogging = require("../../../../script-helpers/android-studio-logging");

require("..").registerTransmutation({
    requires: ["text-to-syntax-tree"],
    id: "pre-convert-tree-units",
    type: "transmutation",
    run: function (context) {
        context.output = convert(context.inputs["text-to-syntax-tree"], context.sourceFullFileName, context.fileFrontmatter);
        context.status = "pass";
    }
});

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

function convert(tree, loggingFile, frontmatter) {
    var unitvalues = query(tree, "UnitValue");
    unitvalues.forEach(x => rewriteUnitvalue(x, loggingFile, frontmatter));
    return tree;
}

function rewriteUnitvalue(unitValue, loggingFile, frontmatter) {
    if (unitValue.value.type == "NumericValue" && unitValue.unit.type == "Identifier") {
        var uType = unitValue.unit.value;
        var uVal = unitValue.value.v;
        
        if(uType == "m") makeSureUserIsntAFilthyAmerican(unitValue.location, loggingFile, frontmatter);

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
            
            notifyUserOfPrecisionLoss(
                dimConv.autoautoBase, unitValue.value.v, unitRecord.unit,
                uType, uVal, 
                unitValue.location, loggingFile
            );
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

function notifyUserOfPrecisionLoss(newUnit, newUnitQuant, newUnitPreciseDescrip, oldUnit, oldUnitQuant, loggingLocation, loggingFile) {
    if(newUnitQuant > Number.MAX_SAFE_INTEGER || newUnitQuant < Number.MIN_SAFE_INTEGER) {
        var crossedBarrierInt = newUnitQuant > 0 ? Number.MAX_SAFE_INTEGER : Number.MIN_SAFE_INTEGER;
        var overagePercentage = (newUnitQuant / crossedBarrierInt) - 1;
        
        var description = `This program uses the unit '${oldUnitQuant}${oldUnit}' (${newUnitPreciseDescrip}), which Autoauto converts into ${newUnitQuant}${newUnit}. `
            + `Autoauto *must* convert values to compare units, but this is ${Math.round(overagePercentage * 100)}% `
            + `${newUnitQuant > 0 ? "more" : "less"} than the ${newUnitQuant > 0 ? "highest" : "lowest"} number `
            + `that IEEE-754 double-precision numbers can represent safely. `
            + `This may produce odd behavior. There is currently no easy way to avoid this in Autoauto, but you can try breaking your `
            + `unit into separate 'after' statements (e.g. two 'after's with ${oldUnitQuant / 2}${oldUnit} instead of one with ${oldUnitQuant}${oldUnit}).`;
        
        androidStudioLogging.sendTreeLocationMessage({
            kind: "WARNING",
            text: "Loss of precision when calculating units",
            original: description,
            location: loggingLocation
        }, loggingFile);
    }
}

function makeSureUserIsntAFilthyAmerican(loggingLocation, loggingFile, frontmatter) {
    if(frontmatter.ignorewarning_american_m_unit == true) return;
    
    androidStudioLogging.sendTreeLocationMessage({
        kind: "WARNING",
        text: "Possibly ambiguous 'm' unit",
        original: `This program uses the unit 'm'. Although many American people use 'm' to mean minutes, the international standard says that 'm' is metres.
        Please do one of the following: 
            a) Add \`ignorewarning_american_m_unit: true\` to the frontmatter at the start of your file. For example,
                $
                ignorewarning_american_m_unit: true
                $
              If you do that, 'm' will STILL mean 'meters', but this warning won't show every time.
            b) Use 'metres' or 'min' instead of 'm'.`,
        location: loggingLocation
    }, loggingFile);
}