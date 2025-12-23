package testing.stick404.mindlesstoys.com;

import java.io.IOException;
import java.io.InputStream;
import java.lang.foreign.*;
import java.lang.invoke.MethodHandle;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;

import static java.lang.foreign.ValueLayout.*;

public class Main {
    // TODO: https://www.slingacademy.com/article/cross-compiling-go-programs-for-any-os/
    static {
        try {
            //InputStream input = Main.class.getResourceAsStream("cpp/libtest.so");
            String file;
            String os = System.getProperty("os.name").toLowerCase();
            if (os.contains("win")) file = ".dll";
            else if (os.contains("lin")) file = ".so";
            else file = "uh oh";

            if (Main.class.getClassLoader().getResource("learning" + file) != null){
                System.out.println("Could find library!");
            } else {
                System.out.println("\nCould not find library! \n");
            }

            InputStream input = Main.class.getClassLoader().getResourceAsStream("learning" + file);
            if (input == null) {
                throw new RuntimeException("Could not find lib" + file + "!");
            }
            Path tempPath = Files.createTempFile("library", file);
            Files.copy(input, tempPath, StandardCopyOption.REPLACE_EXISTING);
            System.load(tempPath.toString());
            input.close();

            System.out.println(" \n==== Loaded Library! ==== \n");
            //System.load(Main.class.getClassLoader().getResource("cpp/libtest.so").toURI().getPath());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    static void main() throws Throwable {
        // 1. Get a lookup object for the standard C library
        SymbolLookup stdlib = Linker.nativeLinker().defaultLookup();
        System.out.println(Linker.nativeLinker().canonicalLayouts());


        // 2. Get a handle to the 'strlen' function
        MethodHandle strcpy = Linker.nativeLinker().downcallHandle(
            stdlib.find("strcpy").orElseThrow(),
            FunctionDescriptor.ofVoid(ADDRESS, ADDRESS)
        );

        // 3. Allocate some off-heap memory and write a C string into it
        try (Arena arena = Arena.ofConfined()) {

            MemorySegment value = arena.allocate(JAVA_CHAR, 50);
            MemorySegment toCopy = arena.allocateFrom("ABCDEFGHIJKLMNOPQRSTUVWXYZ", StandardCharsets.UTF_8);

            // 4. Invoke the native function directly from Java
            strcpy.invoke(value, toCopy);

            System.out.println("printing AAAA");
            for (int i = 0 ; i < 26 ; i++){
                int z = (int) JAVA_BYTE.arrayElementVarHandle().get(value, 0, i) & 0xFF;
                System.out.print((char) z);
                //System.out.println((int) (char) z);
            }
            System.out.println();
        }


        MethodHandle printf = Linker.nativeLinker().downcallHandle(
                stdlib.find("printf").orElseThrow(),
                FunctionDescriptor.of(JAVA_INT, ADDRESS)
        );

        try (Arena arena = Arena.ofConfined()) {
            var cStringFromAllocator = arena.allocateFrom("Hello World" + "\n");
            printf.invoke(cStringFromAllocator);
        }

        MethodHandle addTest = Linker.nativeLinker().downcallHandle(
                lookup("Add"),
                FunctionDescriptor.of(JAVA_INT, JAVA_INT, JAVA_INT)
        );

        int ret = (int) addTest.invoke(5, 5);
        System.out.println(ret);

        /*MethodHandle stringCopyTest = Linker.nativeLinker().downcallHandle(
                lookup("testingStrings"),
                FunctionDescriptor.ofVoid(ADDRESS, ADDRESS)
        );

        try (Arena arena = Arena.ofConfined()) {
            var loc = arena.allocateFrom("Hello Dlang!");
            var to = arena.allocate(50);

            stringCopyTest.invoke(to, loc);

            for (int i = 0 ; i < 50 ; i++){
                int z = (int) JAVA_BYTE.arrayElementVarHandle().get(to, 0, i);
                System.out.print((char) z);
                //System.out.println((int) (char) z);
            }
            System.out.println();
        }
         */
        System.out.println("We should now be done!");
    }

    public static MemorySegment lookup(final String symbol) {
        return Linker.nativeLinker().defaultLookup().find(symbol)
                .or(() -> SymbolLookup.loaderLookup().find(symbol))
                .orElseThrow();
    }
}