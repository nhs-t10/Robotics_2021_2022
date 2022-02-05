# Human Readable Java

This Compiler Mode generates (or, well, *does its best* to generate) human-readable Java code. This makes a `loop()` method that has 0 dependencies on Autoauto runtimes.

## Limitations

This mode *only* supports the lowest common denominator of Autoauto. 

`Human-readable-Java` mode doesn't use the scope system, so system variables like `__statepathName` are *not* accessible!

The following things will cause errors or undefined behaviour:

* Function definition statements
* Function literals
* Dynamic `goto`, `skip`, or `after`
* `Skip`ping around state boundaries
* **Storing non-numbers in variables**
* `log`ging anything that is not a string
* Any usage of tables, including built-in tables like `Math`
* Any of the built-ins, except for `log`
* Else statements
* Named arguments
* Any form of implicit casting.

## Additional Functionality

None

## When to Use

* Explaining how Autoauto works
* Working with other teams that don't use Autoauto

## When Not to Use

* Actually coding Autoauto opmodes

## Maintained By

Nobody