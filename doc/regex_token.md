
| **Name**                     | **Type**                  | **Regular expression**         |
|-----------------------------|---------------------------|---------------------------------|
| Comment                     | /                         | ` #.*(\\r)?\\n`                 |
| Circumflex (^)              | POWER                     | ` \^ `                          |
| Percentage (\%)             | REMAINDER                 | ` % `                           |
| Tilde (   extasciitilde)    | BITWISE\_NOT              | ` ~ `                           |
| Pipe (\|)                   | BITWISE\_OR               | ` \| `                          |
| Dollar (\$)                 | BITWISE\_XOR              | ` \$ `                          |
| Dubble greater (>>)         | ARITHMETIC\_SHIFT\_RIGHT  | ` >> `                          |
| Dubble lower (<<)           | ARITHMETIC\_SHIFT\_LEFT   | ` << `                          |
| Dubble equal (==)           | EQUALITY                  | ` == `                          |
| Exclamation equal (!=)      | INEQUALITY                | ` != `                          |
| Function                    | FUNCTION                  | ` function `                    |
| Return                      | RETURN                    | ` return `                      |
| Arrow right (->)            | MAP\_TO                   | ` -> `                          |
| Question mark (?)           | TERNARY\_IF               | ` \? `                          |
| Exclamation mark (!)        | NEGATION                  | ` ! `                           |
| Colon (:)                   | TERNARY\_ELSE             | ` : `                           |
| Doubble ampersand (&&)      | LAZY\_AND                 | ` && `                          |
| Doubble pipe (\|\|)         | LAZY\_OR                  | ` \|\| `                        |
| While                       | WHILE                     | ` while `                       |
| For                         | FOR                       | ` for `                         |
| Semicolon (;)               | END\_OF\_INSTRUCTION      | ` ; `                           |
| Println                     | PRINTLN                   | ` println `                     |
| Const                       | CONST                     | ` const `                       |
| Let                         | LET                       | ` let `                         |
| Dubble colon (::)           | TYPE\_DEFINITION          | ` :: `                          |
| Boolean (type)              | BOOLEAN\_TYPE             | ` Bool `                        |
| Real (type)                 | REAL\_TYPE                | ` FloatingPoint `               |
| Integer (type)              | INTEGER\_TYPE             | ` Integer `                     |
| Integer (cast)              | INTEGER\_CAST             | ` int `                         |
| Real (cast)                 | REAL\_CAST                | ` float `                       |
| Read integer                | READ\_INTEGER             | ` readint `                     |
| Read real                   | READ\_REAL                | ` readfloat `                   |
| Boolean (cast)              | BOOLEAN\_CAST             | ` bool `                        |
| Backslash (\\)              | INVERSE\_DIVIDE           | ` \\ `                          |
| Do                          | DO                        | ` do `                          |
| End                         | END                       | ` end `                         |
| Comma (,)                   | COMMA                     | ` , `                           |
| Left parenthesis(()         | LEFT\_PARENTHESIS         | ` \( `                          |
| Right parenthesis ())       | RIGHT\_PARENTHESIS        | ` \) `                          |
| Minus sign (-)              | MINUS                     | ` - `                           |
| Plus sign (+)               | PLUS                      | ` \+ `                          |
| Equal sign (=)              | ASSIGNATION               | ` = `                           |
| Asterisk (*)                | TIMES                     | ` \* `                          |
| Slash (/)                   | DIVIDE                    | ` / `                           |
| True                        | BOOLEAN                   | ` true `                        |
| False                       | BOOLEAN                   | ` false `                       |
| Lower sign (<)              | LESS\_THAN                | ` < `                           |
| Greater sign (>)            | GREATER\_THAN             | ` > `                           |
| Lower or equals (<=)        | LESS\_OR\_EQUALS\_THAN    | ` <= `                          |
| Greater or equals (>=)      | GREATER\_OR\_EQUALS\_THAN | ` >= `                          |
| If                          | IF                        | ` if `                          |
| Else                        | ELSE                      | ` else `                        |
| Elseif                      | ELSE\_IF                  | ` elseif `                      |
| Identifier                  | IDENTIFIER                | ` ([a-z]\|[A-Z]\|_) `           |
|                             |                           | ` ([a-z]\|[A-Z]\|[0-9]\|_)* `   |
| Integer                     | INTEGER                   | ` (([1-9][0-9]*) \| 0) `        |
| Real                        | REAL                      | ` (([1-9][0-9]*) \| 0)\.[0-9]+ `|