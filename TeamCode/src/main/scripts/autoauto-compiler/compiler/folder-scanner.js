const path = require("path");
const fs = require("fs");

module.exports = folderScanner


function* folderScanner(folder, ending) {
    let folderContents = getMatchingFolderContents(folder, ending);


    for(var i = 0; i < folderContents.length; i++) {
        const subfile = folderContents[i];

        if(subfile.directory) {
            folderContents = folderContents.concat(...getMatchingFolderContents(subfile.name, ending));
        } else {
            yield path.normalize(subfile.name);
        }
    }
    return undefined;
}

function getMatchingFolderContents(folder, ending) {
    const unsorted = fs.readdirSync(folder, {
        withFileTypes: true
    });

    const foldersFirst = [];

    for(const x of unsorted) {
        if(x.isDirectory()) foldersFirst.push({
            name: path.join(folder, x.name),
            directory: true
        });
    }
    for(const x of unsorted) {
        if(x.isFile() && x.name.endsWith(ending)) foldersFirst.push({
            name: path.join(folder, x.name)
        });
    }

    return foldersFirst;
}