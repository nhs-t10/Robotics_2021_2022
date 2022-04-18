var workerThreads = require("worker_threads");
const compileFile = require("./compile-file");

if(workerThreads.isMainThread) {
    module.exports = evaluateJob;
} else {
    process.argv = process.argv.concat(workerThreads.workerData);
    workerThreads.parentPort.on("message", function(m) {
        if(m.type == "newJob") {
            workerThreads.parentPort.postMessage({
                type: "jobDone",
                id: m.id,
                body: evaluateJob(m.body)
            });
        }
    });
}




function evaluateJob(fileContext) {
    return compileFile(fileContext);
}