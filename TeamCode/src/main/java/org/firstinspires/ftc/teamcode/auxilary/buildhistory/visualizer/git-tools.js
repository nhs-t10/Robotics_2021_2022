
var zlib = require("zlib");
var path = require("path");
var fs = require("fs");
var crypto = require("crypto");

var objectCache = {};

var objectParsers = {
    commit: parseCommit,
    tree: parseTree,
    blob: parseBlob
}

var directoryTerms = __dirname.split(path.sep);
var gitDirectory;

for(var i = directoryTerms.length; i >= 0; i--) {
    var scanDirectory = directoryTerms.slice(0, i).join(path.sep);
    if(fs.existsSync(path.join(scanDirectory, ".git"))) {
        gitDirectory = path.join(scanDirectory, ".git");
        break;
    }
}

if(!gitDirectory) throw "no git directory :/"

module.exports = {
    loadAllObjectsFromPack: loadAllObjectsFromPack,
    gitDirectory: gitDirectory,
    getCurrentBranchName: getCurrentBranchName,
    getHead: getHead,
    getAllVersionsOfFile: getAllVersionsOfFile,
    getFileInTree: getFileInTree,
    loadObject: loadObject,
    gitRelativeFile: gitRelativeFile,
    readZlibDynamicLength: readZlibDynamicLength
}

function getCurrentBranchName() {
    var HEADfilecontent = fs.readFileSync(path.join(gitDirectory, "HEAD")).toString();
    var HEADref = HEADfilecontent.split(":").slice(1).join(":").trim();

    return path.basename(HEADref);
}

function getHead() {
    var HEADfilecontent = fs.readFileSync(path.join(gitDirectory, "HEAD")).toString();
    var HEADref = HEADfilecontent.split(":").slice(1).join(":").trim();
    
    var headSha = getRefPointer(HEADref);
    
    return loadObject(headSha);
}

function gitRelativeFile(filename) {
    var posixd = filename.replace(/\\/g, "/");
    var posixdGitDir = gitDirectory.replace(/\\/g, "/");

    var norm = path.posix.normalize(posixd);
    var gitNorm = path.posix.normalize(posixdGitDir).replace(/\/\.git$/, "");

    return norm.replace(gitNorm, "");
}

function getRefPointer(refName) {
    var normalRefFile = path.join(gitDirectory, refName);
    if(fs.existsSync(normalRefFile)) return fs.readFileSync(normalRefFile).toString();

    //if the ref doesn't exist, look to packed-refs
    var packedRefs = fs.readFileSync(path.join(gitDirectory, "packed-refs"))
        .toString()
        .split("\n");
    
    for(var i = 0; i < packedRefs.length; i++) {
        var ref = packedRefs[i].trim();
        //comments
        if(ref.startsWith("#")) continue;
        //annotated tags
        if(ref.startsWith("^")) continue;

        if(ref.endsWith(refName)) return ref.substring(0, ref.indexOf(" "));
    }
    return -1;
}

function getAllVersionsOfFile(filename, commit, earlyStopList) {
    var versions = [];
    if(typeof commit == "string") commit = loadObject(commit);

    if(!commit) commit = getHead();

    var tree = loadObject(commit.headers.tree);

    var thisFileInCommit = getFileInTree(tree, filename, "", earlyStopList);

    if(thisFileInCommit != -1) {
        earlyStopList = thisFileInCommit.shaPath;
        versions.push(thisFileInCommit);
    }

    //load this commit's parent(s)
    if(typeof commit.headers.parent === "string" && commit.headers.parent != "") {
        if(!objectExists(commit.headers.parent)) console.log(commit)
        versions = versions.concat(getAllVersionsOfFile(filename, commit.headers.parent, earlyStopList));
    } else {
        versions = versions.concat(...commit.headers.parent.map(x=>getAllVersionsOfFile(filename, x, earlyStopList)));
    }

    return versions;
}

function getFileInTree(tree, filename, root, earlyStopList) {
    root = root || "";
    earlyStopList = earlyStopList || [];

    filename = filename.replace(/\\/g, "/")
    root = root.replace(/\\/g, "/")

    //checks to normalize
    if(filename.startsWith(root)) filename = filename.replace(root, "");
    while(filename.startsWith("/")) filename = filename.substring(1);
    if(filename.endsWith("/")) throw filename + " ends with /";

    var filenameTerms = filename.split("/");
    var shaPath = [];
    var nextTree = tree;
    
    for(var i = 0; filenameTerms.length; i++) {
        var nextSha = nextTree.entries.find(x=>x.name == filenameTerms.shift());
        
        if(nextSha == earlyStopList[i]) return -1;
        else shaPath.push(nextSha);

        nextTree = loadObject(nextSha);
    }

    return {
        file: nextTree,
        shaPath: shaPath
    }
}

function objectExists(sha) {
    sha = sha.replace(/\s/g, "");
    var folder = sha.substring(0, 2);
    var file = sha.substring(2);

    var shaPath = path.join(gitDirectory, "objects", folder, file);

    return fs.existsSync(shaPath);
}

function loadObject(sha) {
    var object = loadRawObjectBuffer(sha);

    var objectData = zlib.inflateSync(object);
    var firstNulByteIndex = objectData.findIndex(x => x == 0x00);

    var objectText = objectData.slice(0, firstNulByteIndex).toString();

    var type = objectText.substring(0, objectText.indexOf(" "));
    if (!objectParsers[type]) throw "Unknown type " + type;

    var data = objectData.slice(firstNulByteIndex + 1);

    var parsed = objectParsers[type](data);
    parsed.type = type;
    parsed.selfSha = sha;
    return parsed;
}

function loadRawObjectBuffer(sha) {
    if(objectCache[sha]) return objectCache[sha];
    
    sha = sha.replace(/\s/g, "");
    var folder = sha.substring(0, 2);
    var file = sha.substring(2);

    var shaPath = path.join(gitDirectory, "objects", folder, file);

    if(!fs.existsSync(shaPath)) return loadObjectBufferFromPackfile(sha);

    var object = fs.readFileSync(shaPath);

    objectCache[sha] = object;
    
    return object;
}

function parseCommit(buffer) {
    var text = buffer.toString();
    var parsed = {
        headers: {},
        message: "",
    }
    var lines = text.split(/\r?\n/);

    var inHead = true;
    for (var i = 0; i < lines.length; i++) {
        if (lines[i] == "") {
            inHead = false;
            continue;
        }
        if (inHead) {
            var words = lines[i].split(" ");

            //if it already exists in the headers, change the header into an array.
            if(!parsed.headers[words[0]]) {
                parsed.headers[words[0]] = words.slice(1).join(" ");
            } else {
                parsed.headers[words[0]] = [
                    parsed.headers[words[0]],
                    words.slice(1).join(" ")
                ];
            }
        } else {
            parsed.message += lines[i] + "\n";
        }
    }
    return parsed;
}

function parseTree(buffer) {
    var entries = [];
    var lastEntryEnd = -1;
    for (var i = 0; i < buffer.length; i++) {
        if (buffer[i] == 0x00) {
            var modeName = buffer.slice(lastEntryEnd + 1, i).toString();

            var sha = buffer.slice(i + 1, i + 21);
            var shaText = sha.toString("hex");

            i += 20;

            var mode = modeName.substring(0, modeName.indexOf(" "));
            var name = modeName.substring(modeName.indexOf(" ") + 1);

            entries.push({
                isFile: mode == "100644",
                isExecutable: mode == "100755",
                isSymbolicLink: mode == "120000",
                isDirectory: mode == "40000",
                mode: mode,
                name: name,
                sha: shaText
            });
            lastEntryEnd = i;
        }
    }
    return {
        entries: entries
    };
}

function parseBlob(buffer) {
    var nullByteIndex = buffer.indexOf("\u0000");
    var header = buffer.slice(0, nullByteIndex).toString();
    var size = parseInt(header.split(" ")[1]);
    var blob = buffer.slice(nullByteIndex + 1).toString()
    return {
        type: "blob",
        size: size,
        blob: blob
    };
}

function loadAllObjectsFromPack() {
    var packsDir = path.join(gitDirectory, "objects/pack");

    var idxFile, packFile;

    if(fs.existsSync(packsDir)) {
        var packfiles = fs.readdirSync(packsDir);

        var pack = packfiles.find(x=>x.endsWith(".pack"));
        if(pack) packFile = path.join(packsDir, pack);

        var idx = packfiles.find(x=>x.endsWith(".idx"));
        if(idx) idxFile = path.join(packsDir, idx);
    }

    //if(packFile && !idxFile) loadNewPackfileNoIndex(packFile);
    //else loadAllPackedObjects(packFile, idxFile);

    loadNewPackfileNoIndex(packFile);
}

function loadAllPackedObjects(packFile, idxFile) {
    var idxBuffer = fs.readFileSync(idxFile),
        packBuffer = fs.readFileSync(packFile);

    var idxOffset = 0;

    var magic = idxBuffer.readUInt32BE(0);
    var version = idxBuffer.readUInt32BE(4);
    idxOffset += 8;

    if(magic != 0xff744f63 || version != 2) throw "Unsupported version of the idx file!";

    var fanout = [];
    for(var i = 0; i < 256; i++) {
        fanout.push(idxBuffer.readUInt32BE(idxOffset));
        idxOffset += 4;
    }
}

function loadNewPackfileNoIndex(path) {
    var fileContent = fs.readFileSync(path);
    
    var position = 0;

    var signature = fileContent.slice(0, 4).toString();
    if(signature != "PACK") throw "bad packfile " + path;
    position+=4;

    var endian = "BE";

    var version = fileContent.readUInt32LE(position);
    if(version > 3) {
        endian = "BE";
        version = fileContent["readUInt32" + endian](position);
    }
    position += 4;

    var packFileCount = fileContent["readUInt32" + endian](position);
    position += 4;

    //apparently there's an undocumented extra 8-bit (2-byte) offset????
    //https://github.com/robisonsantos/packfile_reader
    position += 2;

    var files = [];

    var filecount = 0;

    //for docs on these weird bitwise ops, see the pack format documentation: https://git-scm.com/docs/pack-format http://shafiul.github.io/gitbook/7_the_packfile.html
    while(position < fileContent.length) {
        var entryHead = readVarint(fileContent, position);

        position += entryHead.length;

        var entryType = (entryHead.result & 0b111_0000) >>> 4;
        var entryLength = (entryHead.result & 0b000_1111) | ((entryHead.result >>> 7) << 4);

        console.log("type", entryType);

        var baseEntryReference;
        //some entry types (deltas) have additional data before the compressed stream starts
        if(entryType == 6) {
            baseEntryReference = fileContent.slice(position, position + 20);
            position += 20;
        } else if(entryType == 7) {
            baseEntryReference = readVarint(fileContent, position);
            position += baseEntryReference.length;
            baseEntryReference = baseEntryReference.result;
        }

        var data = readZlibDynamicLength(fileContent, position, entryLength);

        files[crypto.createHash("sha1").update(data).digest("hex")] = data;
        
        filecount++;

        position += (entryLength - entryHead.length);
    }

    return files;
}

/**
 * 
 * @param {Buffer} buffer 
 * @param {number} position
 * @param {number} length 
 */
function readZlibDynamicLength(buffer, position, length) {

    console.log(buffer.readUInt8(position).toString(2), buffer.readUInt8(position + 1).toString(2));

    var bufferToInflate = buffer.slice(position);
    return zlib.inflateSync(bufferToInflate);
}

/**
 * 
 * @param {Buffer} buffer 
 * @param {number} offset 
 */
function readVarint(buffer, offset) {
    var byteCount = 1;
    var b = buffer[offset];

    offset++;

    var LAST_SEVEN_MASK = 0b0111_1111

    var result = b & LAST_SEVEN_MASK;

    while((b & ~LAST_SEVEN_MASK) != 0) {
        b = buffer[offset];
        offset++;
        
        result |= ((b & LAST_SEVEN_MASK) << (7 * byteCount));
        byteCount++;
    }

    return {
        result: result,
        length: byteCount
    };
}