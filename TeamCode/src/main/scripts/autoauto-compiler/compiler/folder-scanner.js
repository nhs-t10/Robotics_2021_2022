const path = require("path");
const fs = require("fs");

module.exports = loadAutoautoFilesFromFolder


function* loadAutoautoFilesFromFolder(folder) {
    let folderContents = getFolderAutoautoContents(folder);

    for(const subfile of folderContents) {
        if(subfile.directory) {
            folderContents = folderContents.concat(...getFolderAutoautoContents(subfile.name));
        } else {
            yield path.normalize(subfile.name);
        }
    }
    return undefined;
}

function getFolderAutoautoContents(folder) {
    const unsorted = fs.readdirSync(folder, {
        withFileTypes: true
    });

    const foldersFirst = [];

    for(const x of unsorted) {
        if(x.isDirectory()) foldersFirst.push({
            name: folder + path.sep + x.name,
            directory: true
        });
    }
    for(const x of unsorted) {
        if(x.isFile() && x.name.endsWith(".autoauto")) foldersFirst.push({
            name: folder + path.sep + x.name
        });
    }

    return foldersFirst;
}