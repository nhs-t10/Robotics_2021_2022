
var VARIABLE_SCOPE_CLASSNAME = "org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoRuntimeVariableScope";
var PARENT_CLASSNAME = "org.firstinspires.ftc.teamcode.auxilary.dsls.autoauto.runtime.AutoautoClassyBytecodeHolder"

const makeClassFile = require("./jvm/class-file");
const aaBytecodeMaker = require("./make-aa-bytecode");
const makeStatementList = require("./make-statement-list");
const aaBytecodeToJvmBytecode = require("./aa-bytecode-to-jvm-bytecode");
const byteTools = require("./jvm/byte-tools");
const jvmBytecodeInstructions = require("./jvm-bytecode-instructions");

module.exports = function(ast) {
    var thisClassName = "class";
    var statements = makeStatementList(ast);
    var aaBytecode = aaBytecodeMaker(statements);
    var jvmBytecode = aaBytecodeToJvmBytecode(aaBytecode, thisClassName);

    var constantPool = jvmBytecode.constantPool;

    var jvmBytecodeContent = jvmBytecode.bytecode.flat(Infinity);

    var interfaces = [[]]
    byteTools.addU2(interfaces[0], constantPool.addClass(PARENT_CLASSNAME));

    var fields = [
        makeField(0x0001, "s", "int", constantPool)
    ];

    var methods = [
        makeConstructor(constantPool, thisClassName),
        makeClinit(constantPool),
        //the actual run method!
        makeMethodInfo(0x0001, "execLoop", "void", [VARIABLE_SCOPE_CLASSNAME], jvmBytecodeContent, [], constantPool)
    ];

    var thisClassIndex = constantPool.addClass(thisClassName);

    return makeClassFile({
        constants: constantPool.toBuffers(),
        methods: methods,
        fields: fields,
        interfaces: interfaces,
        attributes: [],
        thisClassIndex: thisClassIndex
    });
}

function makeClinit(constantPool) {
    return makeMethodInfo(0x0001 | 0x0008, "<clinit>", "void", [], [ jvmBytecodeInstructions.nop() ], [], constantPool);
}

function makeConstructor(constantPool, thisClass) {
    var setField = constantPool.addField(thisClass, "s", "int");
    return makeMethodInfo(1, "<init>", "void", [], [
        jvmBytecodeInstructions.aload_0(), //load `this`
        jvmBytecodeInstructions.iconst_0(), //load `0`
        jvmBytecodeInstructions.putfield((setField >>> 8) * 0xff, setField & 0xff)
    ], ["Synthetic"], constantPool);
}

function makeMethodInfo(access, name, returnType, argTypes, code, attrs, constantPool) {
    var f = [];
    
    byteTools.addU2(f, access);
    byteTools.addU2(f, constantPool.addUTF8(name));
    byteTools.addU2(f, constantPool.addUTF8(constantPool.makeDescriptor(returnType, argTypes)));

    

    var attributeBytes = [];

    if(attrs.includes("Synthetic")) attributeBytes.push(attrSynthetic(constantPool));

    attributeBytes.push(makeCodeAttribute(code, constantPool));

    //attribute count
    byteTools.addU2(f, attributeBytes.length);

    f = f.concat(attributeBytes.flat());

    return f;
}

function makeField(access, name, type, constantPool) {
    var f = [];

    byteTools.addU2(f, access);
    byteTools.addU2(f, constantPool.addUTF8(name));
    byteTools.addU2(f, constantPool.addUTF8(constantPool.makeDescriptor(type)));

    byteTools.addU2(f, 1);
    f = f.concat(attrSynthetic(constantPool));

    return f;
}

function attrSynthetic(constantPool) {
    var a = [];
    byteTools.addU2(a, constantPool.addUTF8("Synthetic"));
    byteTools.addU4(a, 0);
    return a;
}

function makeCodeAttribute(code, constantPool) {
    var a = [];

    byteTools.addU2(a, constantPool.addUTF8("Code"));

    var aContent = [];

    //assume that max stack size will be "like, a bunch"
    byteTools.addU2(aContent, 4096);
    
    //max local variables-- only 2 (`this` & the scope)
    byteTools.addU2(aContent, 4);

    //code!
    code = code.flat();
    byteTools.addU4(aContent, code.length);
    aContent = aContent.concat(code);

    //no exception handlers
    byteTools.addU2(aContent, 0);

    //no other attributes
    byteTools.addU2(aContent, 0);


    //compute length & then add content onto the header
    byteTools.addU4(a, aContent.length);
    a = a.concat(aContent);

    return a;
}