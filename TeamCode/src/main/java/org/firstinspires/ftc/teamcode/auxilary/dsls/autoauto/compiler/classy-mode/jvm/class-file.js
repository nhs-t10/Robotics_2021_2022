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
 * @property {Field[]} fields
 * @property {string} superclass
 * @property {string[]} interfaces
 * @property {Field[]} fields
 */

const byteTools = require("./byte-tools");


var MAGIC_NUMBER = 0xCAFEBABE;


/**
 * 
 * @param {JavaClassConfig} config 
 */
module.export = function(config) {
    var version = config.classVersion || 52;
    var versionMajor = Math.floor(version), versionMinor = +(version % 1).toString().substring(2);

    var classFile = [];

    byteTools.addU4(classFile, MAGIC_NUMBER);

    byteTools.addU2(classFile, versionMinor);
    byteTools.addU2(classFile, versionMajor);


    byteTools.addU2(classFile, constants.length);

    //flatten all constants into the table
    byteTools = byteTools.concat(config.constants.flat());

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
    
    byteTools.addU2(accessMask);

    
}