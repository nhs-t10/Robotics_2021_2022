/**
 * @typedef {object} JavaClassConfig
 * @property {number[][]} constants 
 * @property {number} [classVersion]
 * @property {boolean} isPublic
 * @property {boolean} isFinal
 * @property {boolean} isInterface
 * @property {boolean} isAbstract
 * @property {boolean} isSynthetic
 * @property {boolean} isAnnotation
 * @property {boolean} isEnum
 * @property {number[][]} fields
 * @property {number[][]} methods
 * @property {string} superclass
 * @property {number[]} interfaces
 * @property {number} thisClassIndex
 */

const byteTools = require("./byte-tools");


var MAGIC_NUMBER = 0xCAFEBABE;


/**
 * 
 * @param {JavaClassConfig} config 
 */
module.exports = function(config) {
    var version = config.classVersion || 52;
    var versionMajor = Math.floor(version), versionMinor = +(version % 1).toString().substring(2);

    var classFile = [];

    byteTools.addU4(classFile, MAGIC_NUMBER);

    byteTools.addU2(classFile, versionMinor);
    byteTools.addU2(classFile, versionMajor);


    byteTools.addU2(classFile, config.constants.length + 1);

    //flatten all constants into the table
    classFile = classFile.concat(config.constants.flat());

    console.log(config.constants.map((x,i)=>i + " : " + config.constants.slice(0,i+1).map(x=>x.length).reduce((a,b)=>a+b,0)));

    //create the access_flags bitmask
    var accessFlags = [
        +!!config.isPublic,
        +!!config.isFinal,
        +!!true //"Compilers to the instruction set of the Java Virtual Machine should set the ACC_SUPER flag" ~https://docs.oracle.com/javase/specs/jvms/se7/html/jvms-4.html
        +!!config.isInterface,
        +!!config.isAbstract,
        +!!config.isSynthetic,
        +!!config.isAnnotation,
        +!!config.isEnum,
    ];

    //add the bitmask itself to the class file
    var accessMask = 0;
    accessFlags.forEach((x,i)=>accessMask|=(x << i));

    byteTools.addU2(classFile, accessMask);

    byteTools.addU2(classFile, config.thisClassIndex || 0);

    byteTools.addU2(classFile, 0);

    byteTools.addU2(classFile, config.interfaces.length);
    classFile.push(config.interfaces.flat());

    byteTools.addU2(classFile, config.fields.length);
    classFile.push(config.fields.flat());

    byteTools.addU2(classFile, config.methods.length);
    classFile.push(config.methods.flat());

    byteTools.addU2(classFile, config.attributes.length);
    classFile.push(config.attributes.flat());

    classFile = classFile.flat(Infinity);

    var classBuffer = [];
    //flatten buffers too
    function flat(x) {
        for(var i = 0; i < x.length; i++) {
            if(typeof x[i] === "number") classBuffer.push(x[i]);
            else if(x[i].constructor === Buffer) flat(x[i]);
        }
    }
    flat(classFile);

    return classBuffer;
}