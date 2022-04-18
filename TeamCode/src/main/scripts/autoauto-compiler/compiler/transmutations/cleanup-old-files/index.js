const safeFsUtils = require("../../../../script-helpers/safe-fs-utils");

module.exports = function (context, contexts) {
    var newFiles = contexts.map(x => Object.keys(x.writtenFiles)).flat();

    safeFsUtils.cleanDirectory(context.resultRoot, newFiles);
}