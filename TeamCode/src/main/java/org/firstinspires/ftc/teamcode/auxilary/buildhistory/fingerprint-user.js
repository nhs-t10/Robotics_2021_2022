var http = require("http");
var cp = require("child_process");
var path = require("path");
var fs = require("fs");

var port = 10838;
module.exports = function(timeout) {
    return new Promise(function(resolve, reject) {
        if(timeout === undefined) timeout = 30000;

        var result = "";

        var server = http.createServer(function (req, res) {
            req.path = req.path || req.url;

            if(req.path.startsWith("/handoff-browser-fingerprint/")) {
                result = req.path.split("/")[2];
                res.writeHead(201);
                res.end("201 Created");

                server.close();
                resolve(result);
            } else {
                res.writeHead(200, "OK", {
                   "Content-Type": "text/html"
               });
               res.end(fs.readFileSync(path.join(__dirname, "fingerprint.html")).toString());
            }
        });
        server.listen(port);

        var url = "http://localhost:" + port;
        var start = (process.platform == "darwin"? "open": process.platform == "win32"? "start": "xdg-open");
        cp.exec(start + " " + url);
    });
}