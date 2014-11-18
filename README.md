# chesstricks

It solves chess problems of placing specified pieces types on chess board, so they don't capture eachother.

To compile and run just start sbt shell with `sbt` command. If you don't have sbt installed and using bash you can try with sbt launcher which is in repo `./sbt` it will download proper version for you.

Inside sbt shell try `run -- help` command:
```
Usage: chesstricks <options>

Options:
 
  -B, --bishops  <arg>                (default = 2)
  -X, --board-x  <arg>                (default = 7)
  -Y, --board-y  <arg>                (default = 7)
  -K, --kings  <arg>                  (default = 2)
  -k, --knights  <arg>                (default = 1)
  -p, --print-first  <arg>            (default = 10)
  -Q, --queens  <arg>                 (default = 2)
  -R, --rooks  <arg>                  (default = 0)
  -s, --show-number-of-solutions
      --noshow-number-of-solutions
      --help                         Show help message
```

By default it will solve a problem with 2 Bishops, 2 Kings, 1 Knight, 2 Queens on a 7 by 7 board, printing first 10 solutions and number of all possible.

You can use eg. `run -R 1` to alter number of Rooks on the board

You can also use `sbt stage` command to generate launcher scripts for the application. They can be used as:
`target/universal/stage/bin/chesstricks --help`

