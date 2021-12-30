var units = [
    [["mm"], 1 / 1000, "getMeters"],
    [["m"], 1, "getMeters"],
    [["nm"], 1e-9, "getMeters"],
    [["cm"], 0.01, "getMeters"],
    [["in"], 1 / 39.3700787, "getMeters"],
    [["ft"], 1 / 3.2808399, "getMeters"],
    [["yd"], 1.0936133, "getMeters"],
    [["fathom"], 1.8288, "getMeters"],
    [["km"],  1000.0, "getMeters"],
    [["mil"], 1609.344, "getMeters"],
    [["au"], 149597870700.0, "getMeters"],

    
    [["nanosecond", "ns"], 1e-6, "Time.now"],
    [["millisecond", "ms"], 1, "Time.now"],
    [["second", "s"], 1000, "Time.now"],
    [["min", "mn"], 60 * 1000, "Time.now"],
    [["h", "hr"], 60 * 60 * 1000, "Time.now"],
    [["day", "d"], 24 * 60 * 60 * 1000, "Time.now"],
    [["jif", "jiff", "jiffy", "jiffies", "jiffie"], 10, "Time.now"],

    [["deg", "degs"], 1, "getThirdAngleOrientation"],
    [["rad", "rads"], 2, "getThirdAngleOrientation"],
    [["rot", "rots"], 1 / 360, "getThirdAngleOrientation"],
    [["turny", "tn"], 1 / 90, "getThirdAngleOrientation"],
    [["turny", "hlftn"], 1 / 180, "getThirdAngleOrientation"]

];


module.exports = {
    makeUnitValueGetterBytecode: function makeUnitValueGetterBytecode(unitvalue, instructions) {
        var unit = units.find(x=>x[0].find(y=>y.toLowerCase() == unitvalue.unit.value.toLowerCase()));
        if(!unit) throw "Unknown unit " + unitvalue.unit.value;

        if(unit[2] == "Time.now") return [instructions.PUSH_TIME_MS];

        var bcode = [];
        unit[2].split(".").forEach((x,i)=>{
            bcode.push([ i ? instructions.GET_VAR_STATIC : instructions.GET_PROP_STATIC, x ]);
        });


        bcode.push([instructions.CALL_FUNC, 0]);

        return bcode;
    },
    getRawAmount: function getRawAmount(amount, unitvalue) {
        var unit = units.find(x=>x[0].find(y=>y.toLowerCase() == unitvalue.unit.value.toLowerCase()));
        if(!unit) throw "Unknown unit " + unitvalue.unit.value;

        return amount / unit[1];
    }
}