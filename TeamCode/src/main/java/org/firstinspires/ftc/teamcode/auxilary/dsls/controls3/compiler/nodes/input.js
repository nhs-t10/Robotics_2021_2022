var inputTypes = {
    left_stick_x: "float",
    left_stick_y: "float",
    right_stick_x: "float",
    right_stick_y: "float",
    dpad_up: "boolean",
    dpad_down: "boolean",
    dpad_left: "boolean",
    dpad_right: "boolean",
    a: "boolean",
    b: "boolean",
    x: "boolean",
    y: "boolean",
    guide: "boolean",
    start: "boolean",
    back: "boolean",
    left_bumper: "boolean",
    right_bumper: "boolean",
    left_stick_button: "boolean",
    right_stick_button: "boolean",
    left_trigger: "float",
    right_trigger: "float",
    circle: "boolean",
    cross: "boolean",
    triangle: "boolean",
    square: "boolean",
    share: "boolean",
    options: "boolean",
    touchpad: "boolean",
    ps: "boolean"
}    

module.exports = function(ast, inputs, myName, genNonce, javaImports, methodSources, globals) {
    
    var arg = ast.args.args[0];
    
    var id;
    
    if(arg.type == "AccessOperator") id = arg.left.value + "." + arg.right.value;
    else id = arg.value;
    
    id = camelToSnake(id);
    id = id.replace(/\._/g, ".");
    id = id.replace("joystick", "stick"); 
    
    
    var gamepad = 1;
    
    //find the gamepad number from a variety of formats
    if(id.startsWith("gamepad_one")) {
        id = id.replace("gamepad_one", "");
    } else if(id.startsWith("gamepad_two")) {
        id = id.replace("gamepad_two", "");
        gamepad = 2;
    } else if(id.startsWith("gamepad1")) {
        id = id.replace("gamepad1", "");
    } else if(id.startsWith("gamepad2")) {
        id = id.replace("gamepad2", "");
        gamepad = 2;
    } else if(id.startsWith("1")) {
        id = id.replace("1", "");
    }  else if(id.startsWith("2")) {
        id = id.replace("2", "");
        gamepad = 2;
    }
    
    id = id.replace(/^(\.|_)/, "");
    
    methodSources.push(`public float ${myName}() {
        return gamepad${gamepad}.${id};
    }`);
    
    return myName + "()";
}

function camelToSnake(str) {
    var words = [];

    var currentWord = "";
    for (var i = 0; i < str.length; i++) {
        if (isUpperCase(str[i])) {
            //if multiple uppercase in a row, don't break words
            if (currentWord.toUpperCase() != currentWord) {
                words.push(currentWord);
                currentWord = "";
            }
        }
        currentWord += str[i];
    }
    //add the final word if it's relevant
    if (currentWord != "") words.push(currentWord);

    return words.join("_").toLowerCase();
}

function isUpperCase(char) {
    var code = char.charCodeAt(0);
    return code >= 65 && code <= 90;
}