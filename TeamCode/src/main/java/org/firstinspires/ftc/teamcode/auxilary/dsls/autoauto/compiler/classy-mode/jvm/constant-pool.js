const byteTools = require("./byte-tools");

var structures = {
    CONSTANT_Utf8: 1,
    CONSTANT_Float: 4,
    CONSTANT_Class: 7,
    CONSTANT_String: 8,
    CONSTANT_Fieldref: 9,
    CONSTANT_Methodref: 10,
    CONSTANT_NameAndType: 12,
};


module.exports = function () {
    //push all indexes + 1
    var constantPool = [{tag: 0}];

    function addClass(classname) {
        var internalName = getInternalName(classname);

        var nameIndex = addUTF8(internalName);

        var c = constantPool.findIndex(x => x.tag == structures.CONSTANT_Class && x.name_index == nameIndex);

        if (c > -1) return c;

        constantPool.push({ tag: structures.CONSTANT_Class, name_index: nameIndex });
        return constantPool.length - 1;
    }

    function getInternalName(classname) {
        var arrDepth = 0;
        while (classname.endsWith("[]")) {
            arrDepth++;
            classname = classname.slice(0, -2);
        }
        var javaPrimitives = {
            "byte": "B",
            "char": "C",
            "double": "D",
            "float": "F",
            "double": "D",
            "int": "I",
            "long": "J",
            "short": "S",
            "boolean": "Z",
            "void": "V"
        };
        return "[".repeat(arrDepth) + (javaPrimitives[classname] || "L" + classname.replace(/\./g, "/") + ";");
    }

    function addField(classname, fieldname, fieldtype) {
        var classIndex = addClass(classname);
        var nameAndTypeIndex = addNameType(fieldname, fieldtype);

        var c = constantPool.findIndex(x => x.tag == structures.CONSTANT_Fieldref && x.class_index == classIndex && x.name_and_type_index == nameAndTypeIndex);
        if (c > -1) return c;

        var fieldref = {
            tag: structures.CONSTANT_Fieldref,
            class_index: classIndex,
            name_and_type_index: nameAndTypeIndex
        }
        constantPool.push(fieldref);
        return constantPool.length - 1;
    }

    function addMethod(classname, methodname, returntype, argtypes) {
        var classIndex = addClass(classname);
        var nameAndTypeIndex = addNameType(methodname, returntype, argtypes);

        var c = constantPool.findIndex(x => x.tag == structures.CONSTANT_Methodref && x.class_index == classIndex && x.name_and_type_index == nameAndTypeIndex);
        if (c > -1) return c;

        var methodref = {
            tag: structures.CONSTANT_Methodref,
            class_index: classIndex,
            name_and_type_index: nameAndTypeIndex
        }
        constantPool.push(methodref);
        return constantPool.length - 1;
    }

    function addNameType(name, type, args) {
        var nameIndex = addUTF8(name);
        var descriptorIndex = addUTF8(makeDescriptor(type, args));

        var c = constantPool.findIndex(x => x.tag == structures.CONSTANT_NameAndType && x.name_index == nameIndex && x.descriptor_index == descriptorIndex);
        if (c > -1) return c;

        constantPool.push({
            tag: structures.CONSTANT_NameAndType,
            name_index: nameIndex,
            descriptor_index: descriptorIndex
        });
        return constantPool.length - 1;
    }

    function makeDescriptor(type, args) {
        if (args) {
            return "(" + args.map(x => getInternalName(x)).join("") + ")" + getInternalName(type);
        } else {
            return getInternalName(type);
        }
    }

    /**
    * 
    * @param {string} s 
    */
    function addUTF8(s) {
        var c = constantPool.findIndex(x => x.tag == structures.CONSTANT_Utf8 && x.str == s);
        if (c > -1) return c;

        var utf8Bytes = Buffer.from(s, "utf-8");

        var javaBytes = [];

        //see info about the conversion here: https://docs.oracle.com/javase/8/docs/api/java/io/DataInput.html
        for(var i = 0; i < utf8Bytes.length; i++) {
            // All characters in the range '\u0001' to '\u007F' are represented by a single byte:
            if(utf8Bytes[i] >= 0x0001 && utf8Bytes[i] <= 0x007F) {
                javaBytes.push(utf8Bytes[i]);
            }
            //The null character '\u0000' and characters in the range '\u0080' to '\u07FF' are represented by a pair of bytes:
            else if(utf8Bytes[i] == 0b0000 || utf8Bytes[i] <= 0x07FF) {
                javaBytes.push((0b110 << 5) | (utf8Bytes >>> 6));
                javaBytes.push((0b10 << 6) | (utf8Bytes[i] & 0b111111));
            }
            // char values in the range '\u0800' to '\uFFFF' are represented by three bytes:
            else if(utf8Bytes[i] <= 0xffff) {
                javaBytes.push((0b1110 << 4) | (utf8Bytes >>> 12));
                javaBytes.push((0b10 << 6) | ((utf8Bytes[i] >>> 6) & 0b111111));
                javaBytes.push((0b10 << 6) | (utf8Bytes[i] & 0b111111));
            }
        }

        constantPool.push({ tag: structures.CONSTANT_Utf8, str: s, length: javaBytes.length, bytes: javaBytes });
        return constantPool.length - 1;
    }

    function addConstant(v) {
        if (typeof v === "string") {
            var sIndex = addUTF8(v);
            var c = constantPool.findIndex(x => x.tag == structures.CONSTANT_String && x.string_index == sIndex);
            if (c > -1) return c;

            constantPool.push({ tag: structures.CONSTANT_String, string_index: sIndex });
            return constantPool.length - 1;
        } else if (typeof v === "number") {
            var fBytes = floatBytes(v);
            var c = constantPool.findIndex(x => x.tag == structures.CONSTANT_Float && x.num == v);
            if (c > -1) return c;

            constantPool.push({ tag: structures.CONSTANT_Float, num: v, bytes: fBytes });
            return constantPool.length - 1;
        } else {
            throw "huh?? " + v;
        }
    }

    function floatBytes(v) {
        var buf = new ArrayBuffer(4);
        var view = new DataView(buf);
        view.setFloat32(0, v, false);

        return [
            view.getUint8(0),
            view.getUint8(1),
            view.getUint8(2),
            view.getUint8(3)
        ];
    }

    function toBuffers() {
        return constantPool.slice(1).map(x => constantToBuffer(x));
    }

    var t = {
        addClass: addClass,
        getInternalName: getInternalName,
        addField: addField,
        addMethod: addMethod,
        addNameType: addNameType,
        makeDescriptor: makeDescriptor,
        addUTF8: addUTF8,
        addConstant: addConstant,
        toBuffers: toBuffers,
        constantPool: constantPool,
        constantToBuffer: constantToBuffer
    };
    return t;
}

// CONSTANT_Utf8: 1,
// CONSTANT_Float: 4,
// CONSTANT_Class: 7,
// CONSTANT_String: 8,
// CONSTANT_Fieldref: 9,
// CONSTANT_Methodref: 10,
// CONSTANT_NameAndType: 12,

function constantToBuffer(c) {
    var b = [];
    byteTools.addU1(b, c.tag);
    switch (c.tag) {
        case structures.CONSTANT_Utf8:
            byteTools.addU2(b, c.bytes.length);
            return b.concat(c.bytes);
        case structures.CONSTANT_Float:
            return b.concat(c.bytes);
        case structures.CONSTANT_Class:
            byteTools.addU2(b,c.name_index);
            return b;
        case structures.CONSTANT_String: 
            byteTools.addU2(b,c.string_index);
            return b;
        case structures.CONSTANT_Methodref:
        case structures.CONSTANT_Fieldref:
            byteTools.addU2(b,c.class_index); 
            byteTools.addU2(b,c.name_and_type_index);
            return b;
        case structures.CONSTANT_NameAndType:
            byteTools.addU2(b,c.name_index); 
            byteTools.addU2(b,c.descriptor_index);
            return b;
        case 0: return [];
        default: throw c;
    }
}