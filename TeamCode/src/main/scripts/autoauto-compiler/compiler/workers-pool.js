var workerThreads = require("worker_threads");
var cpuCount = require('os').cpus().length;

module.exports = function (workerCount) {
    var pool = [], queue = [];

    for(var i = workerCount||cpuCount; i>=0; i--) pool.push(createWorkerWrap(queue));

    function findOpenWorker() {
        for(var i = 0; i < pool.length; i++) {
            if(!pool[i].busy) return pool[i];
        }
    }

    return {
        giveJob: function (fileContext, cb) {
            var w = findOpenWorker();
            if(w) {
                w.assignJob(fileContext, cb);
            } else {
                queue.push({job:fileContext,cb:cb});
            }
        }
    }

}

function createWorkerWrap(queue) {
    var worker = new workerThreads.Worker(__dirname + "/worker.js");
    var callbacks = {}, jobNumber = 0;

    var wrap = {
        worker: worker,
        busy: false,
        assignJob: assignJob
    };

    function assignJob(job, cb) {
        wrap.busy = true;
        var id = jobNumber++;
        callbacks[id] = cb;
        worker.postMessage({
            type: "newJob",
            body: job,
            id: id
        });
    }

    worker.on("message", function(m) {
        if(m.type == "jobDone") {
            if(callbacks[m.id]) callbacks[m.id](m.body);

            if(queue.length > 0) {
                var n = queue.shift();
                assignJob(n.job, n.cb);
            }
            else {
                wrap.busy = false;
            }
        }
    });

    return wrap;
}