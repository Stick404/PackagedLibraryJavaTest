# THIS IS WIP, DO NOT USE
However, if you want to build/use this, here's the steps:
- Install Golang
- Set the compile targets in the Env (`GOOS=Linux`, `GOARCH=amd64` for example). For a full list of OS/Target pairs, run `go tool dist list`
- Run `go build -o learning.so -buildmode=c-shared learning.go` which builds the shared library for the Target OS
- Copy the build .so/.dll to `./src/main/resources`
- Run the Java Main to test that the libraries work




### **THE BELOW STEPS ARE NO LONGER USED**


- Install a Dlang compiler
- Install gcc
- Compile lib.d to an .obj/.o file (`dmd -c ./d/src/d/lib.d`)
- Then compile the .o* file either a .so or .dll
- Move the fully compiled file to `./src/main/resources`



As a word of warning: **This is untested in several platforms**
