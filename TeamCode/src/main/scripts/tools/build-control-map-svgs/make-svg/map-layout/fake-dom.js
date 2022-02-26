if (typeof module !== "object") var module = {};

module.exports = {
    /**
     * Create a text node
     * @param {string} content The content of the text node
     * @returns {FakeDomNode} A #text node 
     */
    createTextNode(content) {
        return new FakeDomNode("#text", content === undefined ? "undefined" : content.toString());
    },
    createElement: function (tag) {
        return new FakeDomNode(tag);
    },
    parseHTML: parseHTML,
    makeDocument: makeDocument,
};

/**
 * @typedef {FakeDomNode|RootNode} FakeDocument
 */

/**
 * @typedef {RootNode} RootNode
 */

 function RootNode() {
    return this;
}
/**
     * Create a given HTML node
     * @param {string} tag The tagName of the object to be created.
     * @returns {FakeDomNode} A node. 
     */
RootNode.prototype.createElement = function (tag) {
    return new FakeDomNode(tag);
}

/**
 * 
 * @callback createElementFunc
 * @param {string} nodeName
 * @returns {FakeDomNode}
 */

/**
 * @typedef {FakeDomNode} FakeDomNode
 * @property {string} nodeName
 */
function FakeDomNode(tag, value) {
    let self = this;

    /**@type {string} */
    this.nodeName = tag;
    this.tagName = tag;
    if (tag.startsWith("#")) this.value = value;

    this.parentNode = null;
    /**@type {FakeDomNode[]} */
    this.childNodes = [];
    this.attributes = {
        get style() {
            return self.style.__buildAsAttribute();
        },
        set style(val) {
            self.style.clear();

            let styles = val.split(";");

            //parse and apply each style
            for (var i = 0; i < styles.length; i++) {
                let trimmed = styles[i].trim();
                if (!trimmed) continue;

                let styleKv = trimmed.split(":");
                let styleName = styleKv[0];
                let styleVal = styleKv[1];

                self.style.setProperty(styleName.trim(), styleVal.trim());
            }
        }
    };

    this.style = {
        setProperty: function (prop, val, attr) {
            this[prop] = val + (attr ? " !" + attr : "");
        },
        getComputed: function () {
            return this;
        },
        getPropertyValue: function (prop) {
            return this[prop];
        },
        __buildAsAttribute: function () {
            let styles = Object.keys(this).map(style => {
                if (typeof this[style] == "function") return "";
                return `${camelToKebab(style)}: ${attributeEncodeCharacterEntities(this[style])}`;
            });

            return styles.filter(x=>x!="").join(";");
        },
        clear: function () {
            let keys = Object.keys(this);
            for (var i = 0; i < keys.length; i++) {
                if (typeof this[keys[i]] == "string") delete this[keys[i]];
            }
        }
    };

    return this;
}

FakeDomNode.prototype.childNodes = [];
/**
 * @type {FakeDomNode}
 */
FakeDomNode.prototype.parentNode = undefined;

/**
 * @type {string}
 */
FakeDomNode.prototype.nodeName = undefined;

/**
 * @type {string}
 */
FakeDomNode.prototype.tagName = undefined;

FakeDomNode.prototype.value = undefined;

/**
 * @returns {FakeDomNode}
 */
FakeDomNode.prototype.collapse = function() {
    if(this.tagName == "#text") {
        this.value = this.value.trim();
        return;
    }
    
    this.childNodes.forEach(x=>x.collapse());
    this.childNodes = this.childNodes.filter(x=>x.tagName != "#text" || x.value.trim() != "");
    return this;
}

FakeDomNode.prototype.hideCircular = function () {
    return {
        childNodes: this.childNodes.map(x => x.hideCircular()),
        nodeName: this.nodeName,
        attributes: this.attributes
    }
};
FakeDomNode.prototype.appendChild = function (child) {
    if (child == this) throw "You cannot append a node to itself";
    this.childNodes.push(child);
    if(child.parentNode) child.parentNode.removeChild(child);
    child.parentNode = this;

    return child;
};
FakeDomNode.prototype.insertBefore = function (newChild, reference) {
    if (newChild == this) throw "You cannot append a node to itself";
    let index = this.childNodes.indexOf(reference);
    if (index == -1) index = this.childNodes.length;

    this.childNodes.splice(index, 0, newChild);
    newChild.parentNode = this;

    return newChild;
};
FakeDomNode.prototype.removeChild = function (child) {
    this.childNodes.splice(this.childNodes.indexOf(child), 1);
    child.parentNode = null;

    return child;
};

Object.defineProperty(FakeDomNode.prototype, "offsetWidth", {
    get: function () {
        return this.attributes.width ||
            10 * Math.max(arrayMax(this.textContent.split("\n")).length,
                (this.attributes.text || "").length);
    }
});

Object.defineProperty(FakeDomNode.prototype, "offsetHeight", {
    get: function () {
        return this.attributes.height || this.textContent.split("\n").length * 20;
    }
});

Object.defineProperty(FakeDomNode.prototype, "offsetLeft", {
    get: function () {
        return this.attributes.x || this.attributes.left || 0;
    }
});

Object.defineProperty(FakeDomNode.prototype, "offsetTop", {
    get: function () {
        return this.attributes.y || this.attributes.top || 0;
    }
});

Object.defineProperty(FakeDomNode.prototype, "offsetParent", {
    get: function () {
        return this.parentNode;
    }
});

FakeDomNode.prototype.getBoundingClientRect = function () {
    return {
        top: this.offsetTop,
        left: this.offsetLeft,
        width: this.offsetWidth,
        height: this.offsetHeight,
        x: this.offsetLeft,
        y: this.offsetTop
    };
};
FakeDomNode.prototype.cloneNode = function () {
    let copy = new FakeDomNode(this.nodeName);
    Object.assign(copy, this);

    copy.childNodes = this.childNodes.map(x=>x.cloneNode());
    copy.parentNode = null;

    return copy;
};
FakeDomNode.prototype.setAttribute = function (attr, val) {
    this.attributes[attr] = val + "";
};
FakeDomNode.prototype.setAttributeNS = function (ns, attr, val) {
    this.setAttribute(ns + ":" + attr, val);
};


Object.defineProperty(FakeDomNode.prototype, "textContent", {
    get: function () {
        if (this.nodeName.startsWith("#")) return this.value;

        return this.childNodes.map(node => {
            return node.textContent;
        }).join("");
    },
    set: function (val) {
        this.childNodes = [
            module.exports.createTextNode(val)
        ];
    }
});
Object.defineProperty(FakeDomNode.prototype, "parentElement", {
    get: function () {
        return this.parentNode;
    },
    set: function (val) {
        node.appendChild(this);
    }
});
Object.defineProperty(FakeDomNode.prototype, "innerHTML", {
    get: function () {
        return this.__buildInnerHTML(true);
    },
    set: function (val) {
        if (val === "") {
            this.childNodes = [];
        }
        let parsed = parseHTML(val);
        for (var i = 0; i < parsed.length; i++) {
            this.appendChild(parsed[i]);
        }
    }
});
Object.defineProperty(FakeDomNode.prototype, "outerHTML", {
    get: function () {
        return this.__buildOuterHTML(true);
    }
});
FakeDomNode.prototype.__buildInnerHTML = function (includeStyles,pretty) {
     
    if(isUnsyntaxedElement(this.nodeName)) return this.textContent;
    if(this.nodeName == "#text") {
        //collapse empty text nodes
        if(pretty && this.value.match(/^[\s\n]+$/)) return " ";
        
        return minimalTextEncodeCharacterEntities(this.value || "");
    }
    if(this.nodeName == "#comment") return this.value || "";
    
    if(pretty) {
        return this.childNodes.map(node => node.__buildOuterHTML(includeStyles, pretty)).join("\n");
    } else {
        return this.childNodes.map(node => node.__buildOuterHTML(includeStyles, pretty)).join("");
    }
};

FakeDomNode.prototype.__buildOuterHTML = function (includeStyles, pretty) {
    if (this.nodeName == "#text") return minimalTextEncodeCharacterEntities(this.value || "");
    if(this.nodeName == "#comment") return this.value || "";
    else if(this.nodeName == "#root") return this.__buildInnerHTML(includeStyles, pretty);

    let attrs = Object.keys(this.attributes).map(attribute => {
        if (attribute == "style" && !includeStyles) return "";
        //since getters are defined, it always has style; drop it if not needed
        else if (attribute == "style" && this.attributes.style == "") return "";
        //if it's ?, that's a meta-key!
        else if(attribute == "?") return "";
        //if it's truly `true`, then treat it as boolean
        else if(this.attributes[attribute] === true) return ` ${attribute}`;

        else return ` ${attribute}="${attributeEncodeCharacterEntities(this.attributes[attribute])}"`
    });

    if(this.attributes["?"]) {
        return "<" + this.nodeName + attrs.join("") + "?>";
    }
    else if(this.nodeName == "!DOCTYPE") {
        return "<" + this.nodeName + attrs.join("") + ">"; 
    }
    else if(isSelfClosingTag(this.nodeName)) {
        return "<" + this.nodeName + attrs.join("") + " />";
    } else {
        if(pretty) {
            //don't indent a text node
            if(this.childNodes.length == 1 && this.childNodes[0].nodeName == "#text") {
                return "<" + this.nodeName + attrs.join("") + ">" + this.childNodes[0].__buildOuterHTML(includeStyles, pretty) + "</" + this.nodeName + ">";
            } else {
                return "<" + this.nodeName + attrs.join("") + ">\n" +
                    indent(this.__buildInnerHTML(includeStyles, pretty), "    ") +
                    "\n</" + this.nodeName + ">";
            }
        } else {
            return "<" + this.nodeName + attrs.join("") + ">" +
                this.__buildInnerHTML(includeStyles, pretty) +
                "</" + this.nodeName + ">";
        }
    }
};
/**
 * Search and retrieve an array of nodes with the specified tag name
 * @param {string} tagName Tag name to retrieve elements by
 * @returns {FakeDomNode[]} Array of nodes matching the tag name
 */
FakeDomNode.prototype.getElementsByTagName = function (tagName) {
    let children = this.childNodes.filter(node => {
        return node.nodeName == tagName;
    });

    this.childNodes.forEach(node => {
        children = children.concat(node.getElementsByTagName(tagName));
    });

    return children;
};
/**
 * Search and retrieve an array of nodes with the specified class name
 * @param {string} className Class name to retrieve elements by
 * @returns {FakeDomNode[]} Array of nodes matching the class name
 */
FakeDomNode.prototype.getElementsByClassName = function (className) {
    let children = this.childNodes.filter(node => {
        return (node.attributes.class||"").split(" ").includes(className);
    });

    this.childNodes.forEach(node => {
        children = children.concat(node.getElementsByClassName(className));
    });

    return children;
};
/**
 * 
 * @param {string} property A property
 * @param {string} value A value 
 * @returns {FakeDomNode[]} All child nodes with the specified value in the specified property.
 */
FakeDomNode.prototype.getElementsByPropertyValue = function (property, value) {
    let children = this.childNodes.filter(node => {
        return (node.attributes[property]||"") == value;
    });

    this.childNodes.forEach(node => {
        children = children.concat(node.getElementsByPropertyValue(property, value));
    });

    return children;
};
/**
 * Find an element by its ID
 * @param {string} id ID to search for
 * @returns {FakeDomNode} The node with the specified ID, or `null`
 */
FakeDomNode.prototype.getElementById = function (id) {
    let child = this.childNodes.find(node => {
        return node.attributes.id == id;
    });

    if(child) return child;

    for(var i = this.childNodes.length - 1; i >= 0; i--) {
        var search = this.childNodes[i].getElementById(id);
        if(search) return search;
    }
    return null;
};
FakeDomNode.prototype.getAttribute = function (attr) {
    return this.attributes[attr];
};
FakeDomNode.prototype.removeAttribute = function (attr) {
    delete this.attributes[attr];
};
FakeDomNode.prototype.isConnected = function () {
    return this.parentNode == true;
};

function camelToKebab(str) {
    let words = [];

    let wordStartIndex = 0;
    for (var i = 0; i < str.length; i++) {
        if (str[i].toUpperCase() == str[i]) {
            words.push(str.substring(wordStartIndex, i).toLowerCase());
            wordStartIndex = i;
        }
    }
    words.push(str.substring(wordStartIndex).toLowerCase())

    return words.join("-").replace(/-+/g, "-");
}

function attributeEncodeCharacterEntities(str) {
    return (str+"").replace(/"/g, "&quot;");
}

function minimalTextEncodeCharacterEntities(str) {
    return str.replace(/</g, "&lt;")
        .replace(/>/g, "&gt;");
}

function encodeCharacterEntities(str) {
    return str.replace(/&/g, "&amp;")
        .replace(/"/g, "&quot;")
        .replace(/'/g, "&apos;")
        .replace(/</g, "&lt;")
        .replace(/>/g, "&gt;");
}

function parseCharacterEntities(str) {
    return str.replace(/&amp;/g, "&")
        .replace(/&quot;/g, "\"")
        .replace(/&apos;/g, "'")
        .replace(/&lt;/g, "<")
        .replace(/&gt;/g, ">")
        .replace(/&#39;/g, "'");
}

function arrayMax(arr) {
    let max = arr[0];
    let i = arr.length;
    while (i--) {
        if (arr[i] > max) max = arr[i]
    };

    return max;
}

/**
 * @param {FakeDomNode[]} elems 
 * @returns FakeDocument
 */
function makeDocument(elems) {
    var document = new FakeDomNode("#root");
    document.createElement = module.exports.createElement;
    for(var i = 0; i < elems.length; i++) document.appendChild(elems[i]);
    return document;
}

/**
 * Parse HTML into elements
 * @param {string} str HTML source
 * @returns {FakeDomNode[]} An array of HTML elements represented by the source
 */
function parseHTML(str) {
    var elements = [], context = "base", content = "", currentTag = "", currentAttribute = "", currentAttributeValue = "", currentQuotesUsed = "",
        attributes = {}, depth = 0, stack = [], currentCloseTag = "";
    for (var i = 0; i < str.length; i++) {
        switch (context) {
            case "base":
                if (str[i] == "<") {
                    //if it's an unsyntaxed element, it *cannot* have children. The only working closing tag is itself
                    if(stack.length && isUnsyntaxedElement(stack[stack.length - 1].nodeName)) {
                        var closer = `</${stack[stack.length - 1].nodeName}`;
                        if(str.startsWith(closer, i)) {
                            add(new FakeDomNode("#text", parseCharacterEntities(content)));
                            content = "";

                            currentCloseTag = "";
                            context = "close_tag";
                        } else {
                            content += str[i];
                        }
                    } else {
                        if (isLetter(str[i + 1]) || str[i + 1] == "!" || str[i + 1] == "?") {
                            add(new FakeDomNode("#text", parseCharacterEntities(content)));
                            if(str[i + 1] == "!" && str[i + 2] == "-" && str[i + 3] == "-") {
                                context = "comment";
                                content = "<";
                            } else {
                                content = "";

                                context = "open_tag";
                            }
                        } else if (str[i + 1] == "/") {
                            add(new FakeDomNode("#text", parseCharacterEntities(content)));
                            content = "";

                            currentCloseTag = "";
                            context = "close_tag";
                        } else {
                            content += str[i];
                        }
                    } 
                } else {
                    content += str[i];
                }
                break;
            case "open_tag":
                if (context == "open_tag") {
                    currentTag += str[i];
                    if(isWhitespace(str[i + 1]) || 
                        str[i + 1] == ">" || 
                        str[i + 1] == "/") {
                        attributes = {};
                        currentAttribute = "";
                        currentAttributeValue = "";
                        context = "attributes";
                    }
                }
                break;
            case "attributes":
                currentQuotesUsed = "";
                if (!isWhitespace(str[i])) {
                    if (str[i] == "=") {
                        currentAttributeValue = "";
                        context = "attribute_value";
                    } else {
                        if(str[i] != "/" && str[i] != ">") currentAttribute += str[i];
                    }
                } else {
                    if(currentAttribute) attributes[currentAttribute] = true;
                    currentAttribute = "";
                }
                //treat as self-closing
                if (str[i] == "/") {

                    if(currentAttribute) attributes[currentAttribute] = true;

                    var node = new FakeDomNode(currentTag);
                    var attrs = Object.keys(attributes);
                    for (var j = 0; j < attrs.length; j++) node.setAttribute(attrs[j], attributes[attrs[j]]);
                    add(node, true);
                    if(stack[stack.length - 1].nodeName == currentTag) stack.splice(stack.length - 1, 1);
                    context = "base";
                    currentTag = "";

                    //skip ahead & past the `>` closer
                    while(str[i] && str[i] != ">") i++;
                    break;
                }

                if (str[i] == ">") {

                    if(currentAttribute) attributes[currentAttribute] = true;

                    var node = new FakeDomNode(currentTag);
                    var attrs = Object.keys(attributes);
                    for (var j = 0; j < attrs.length; j++) node.setAttribute(attrs[j], attributes[attrs[j]]);
                    add(node);
                    context = "base";
                    currentTag = "";
                }
                break;
            case "attribute_value":
                if (!currentQuotesUsed) {
                    if (str[i] == "'") currentQuotesUsed = "'";
                    if (str[i] == '"') currentQuotesUsed = '"';
                } else {
                    if (str[i] == currentQuotesUsed) {
                        attributes[currentAttribute] = parseCharacterEntities(currentAttributeValue);
                        currentAttribute = "";
                        context = "attributes";
                    } else {
                        currentAttributeValue += str[i];
                    }
                }
                break;
            case "close_tag":
                if (str[i] == ">") {
                    var latestStackElemIndex = -1;
                    for (var j = stack.length - 1; j >= 0; j--) {
                        if (stack[j].nodeName == currentCloseTag) {
                            latestStackElemIndex = j;
                            break;
                        }
                    }
                    
                    if (latestStackElemIndex != -1) {
                        stack.splice(latestStackElemIndex, stack.length);
                        if(!isSelfClosingTag(currentCloseTag)) depth--;
                    }
                    context = "base";
                }
                if (str[i] != "/" && !isWhitespace(str[i])) currentCloseTag += str[i];
                break;
            case "comment":
                content += str[i];
                if(str[i] == ">" && str[i - 1] == "-" && str[i - 2] == "-") {
                    add(new FakeDomNode("#comment", content), true);
                    content = "";
                    context = "base";
                }
                break;
        }
    }
    function add(elem, selfClosing) {
        if(elem.nodeName == "#text" && elem.value == "") return false;

        if (depth == 0) {
            elements.push(elem);
        }
        else {
            stack[stack.length - 1].appendChild(elem);
        }

        if (elem.nodeName[0] != "#" && !(selfClosing || isSelfClosingTag(elem.nodeName))) {
            stack.push(elem);
            
            depth++;
        }
    }
    if(content) add(new FakeDomNode("#text", parseCharacterEntities(content)))
    return elements;
}

function isWhitespace(c) {
    return c == " " || c == "\t" || c == "\n" || c == "\r";
}

function isLetter(c) {
    var code = c.charAt(0).toLowerCase().charCodeAt(0);
    return code >= 97 && code <= 122;
}

function isSelfClosingTag(tagName) {
    return tagName.startsWith("?") || [
        "circle", "ellipse", "line", "path", "polygon", "polyline", "rect", "stop", "use",
        "area", "base", "br", "col", "embed", "hr", "img", "input", "link", "meta",
        "param", "source", "track", "wbr", "command", "keygen",
        "menuitem", "!doctype"].includes(tagName.toLowerCase());
}

function isUnsyntaxedElement(tagName) {
    return ["style", "script"].includes(tagName.toLowerCase());
}
function indent(text, ind) {
    return text.split("\n").map(x=>ind+x).join("\n");
}