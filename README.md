# THIS IS WIP, DO NOT USE
However, if you want to build/use this, here's the steps:
- Install a C++ tool chain (VScpp, Clang, Gcc all work)
- Compile the Cpp gradle, this should just be a task under Gradle > Cpp > Build
- Move the built .so or .dll to the Java Resources files. Make sure the file's name is named lib.dll or lib.so
- Finally, you can build/run the Java project with either Java 24 or 25, but 25 is recommended
