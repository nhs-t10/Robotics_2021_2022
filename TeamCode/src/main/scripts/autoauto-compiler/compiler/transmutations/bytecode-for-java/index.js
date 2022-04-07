const cpool = require("./constant-pool");
const makeJava = require("./make-java");

require("..").registerTransmutation({
    id: "bytecode-for-java",
    requires: ["bytecode-flatten"],
    type: "transmutation",
    run: function (context) {
        var constantPool = cpool();
        
        var bytecode = context.inputs["bytecode-flatten"];
        var codes = [];

        //recalculate value codes
        //push the codes into an array. This will later be set-ified, which means that the `switch` statement can be dense
        //  (0,1,2,3), not (0,300,400)-- better optimized!
        bytecode.forEach(x=> {
            if(x.hasOwnProperty("__value")) x.code = constantPool.getCodeFor(x.__value);
            codes.push(x.code);
        });
        var denseCodes = Array.from(new Set(codes));
        bytecode.forEach(x=> {
            x.denseCode = denseCodes.indexOf(x.code);
        });
        
        
        
        context.output = makeJava(denseCodes, constantPool, bytecode);
        context.status = "pass";
    }
});