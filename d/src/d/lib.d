extern(C) int add(int a, int b) {
    return a + b;
}

extern(C) pure void testingStrings(char[]* toWrite, char[]* toCopy) {
    *toWrite = *toCopy;
}
