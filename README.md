# THIS IS WIP, DO NOT USE
However, if you want to build/use this, here's the steps:
- Install a Dlang compiler
- Install gcc
- Compile lib.d to an .obj/.o file (`dmd -c ./d/src/d/lib.d`)
- Then compile the .o* file either a .so or .dll
- Move the fully compiled file to `./src/main/resources`

As a word of warning: **This is untested in several platforms**
