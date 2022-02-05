# Compiler Modes

Each **Compiler Mode** is a folder. It must have a file named `index.js` inside it.

Compiler modes can be used by Autoauto coders like so: 

```
$
compilerMode: "name-of-folder"
$

The compiler assumes that each `index.js` file exports a function, which takes 1 argument (an Autoauto AST) and returns a string. 

This string should "fill in the blank" of the following method:

```java

public static AutoautoProgram p() {
    //generated code here:
}

```

i.e. it should be Java code that `return`s an `AutoautoProgram`, or some subclass of `AutoautoProgram`.

The default Compiler Mode is `default`.