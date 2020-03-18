package io.github.xiaoyu.java8demo;

import org.junit.Test;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class Java7Test {
    @Test
    public void testPaths() {
        Path path  = Paths.get(".");// 表示当前项目根路径, 等同 System.getProperty("user.dir"), 等同 new File("").getAbsoblutePath
        Path path1 = path.toAbsolutePath();
        System.out.println(path1);// D:\repo\repo_github\shared_already\xiaoyu-recipes\samples\java8-demo\.
        Path root = path.getRoot();
        System.out.println(root);// null
    }

    /**
     * how to create Path
     */
    @Test
    public void testPaths_1() {
        Path path = Paths.get("/");// 表示磁盘根路径
        System.out.println(path.toAbsolutePath());// D:\


        Path path1 = Paths.get("C:/", "Xmp");

        Path path2 = Paths.get("C:/Xmp");

        FileSystem defaultFileSystem = FileSystems.getDefault();
        Path       path3             = defaultFileSystem.getPath("C:/", "aa.log");

        URI  u     = URI.create("file:///C:/Xmp/dd");
        Path path4 = Paths.get(u);

        File file  = new File("C:/aa.log");
        Path path5 = file.toPath();
        URI  uri   = file.toURI();
        File file1 = path5.toFile();

    }

    /**
     * create a file
     *
     * write sth to the file
     *
     * read from the file
     */
    @Test
    public void testPaths_2() throws IOException {

        //Set<PosixFilePermission> perms = PosixFilePermissions.fromString("rw-rw-rw-");
        //FileAttribute<Set<PosixFilePermission>> attrs = PosixFilePermissions.asFileAttribute(perms);

        Path file = this.createFile("d:/_test", "aa.log");

        try (BufferedWriter bufferedWriter = Files.newBufferedWriter(file, StandardCharsets.UTF_8)) {
            bufferedWriter.write("test writing file");
            bufferedWriter.flush();
        }

        try (BufferedReader bufferedReader = Files.newBufferedReader(file, StandardCharsets.UTF_8);) {
            for (;;) {
                String s = bufferedReader.readLine();
                if (s == null) {
                    break;
                }
                System.out.println(s);
            }
        }
    }

    private Path createFile(String dir, String file) throws IOException {
        Path dirPath = Paths.get(dir);
        if (Files.notExists(dirPath)) {
            Files.createDirectories(dirPath);
        }
        Path filePath = Paths.get(dir, file);
        if (Files.notExists(filePath)) {
            Files.createFile(filePath);// cannot create directory, only for file
        }

        return filePath;
    }

    /**
     * 没有递归的遍历
     *
     * 只会遍历第一层目录
     */
    @Test
    public void testTraverse() throws IOException {
        Path path = Paths.get("d:/_learn/");
        DirectoryStream<Path> dirStream = Files.newDirectoryStream(path);

        if (!Files.isDirectory(path)) {
            System.out.println("path is not a dir, path = " + path.toAbsolutePath().toString());
        }
        dirStream.forEach(p -> System.out.println(p.getFileName()));
    }

    /**
     * 等价
     */
    @Test
    public void testTraverse_1() throws IOException {
        Path path = Paths.get("d:/_learn");
        Stream<Path> dirStream = Files.list(path);
        Iterator<Path> it = dirStream.iterator();
        while (it.hasNext()) {
            Path next = it.next();
            System.out.println(next.getFileName());
        }
    }

    /**
     * 递归遍历
     */
    @Test
    public void testTraverse_2() throws IOException {
        Path path = Paths.get("d:/_learn");
        ArrayList<Path> result = new ArrayList<>();
        Files.walkFileTree(path, new SimpleFileVisitor<Path>() {
            @Override
            public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                if (file.toString().endsWith(".php")) {
                    result.add(file);
                }

                return super.visitFile(file, attrs);
            }
        });

        result.forEach(System.out::println);
    }

    /**
     * 递归遍历, 等价上面
     */
    @Test
    public void testTraverse_3() throws IOException {
        Path path = Paths.get("d:/_learn");
        Stream<Path> stream = Files.walk(path);
        List<Path> result = stream.map(p -> p.toString().endsWith(".php") ? p : null)
                .distinct()
                .collect(Collectors.toList());
        result.forEach(System.out::println);
    }

    /**
     * 文件属性
     */
    @Test
    public void testFileProperty() throws IOException {
        Path zip = Paths.get("d:/_test/aa.log");
        System.out.println(Files.getLastModifiedTime(zip));
        System.out.println(Files.size(zip));
        System.out.println(Files.isSymbolicLink(zip));
        System.out.println(Files.isDirectory(zip));
        System.out.println(Files.readAttributes(zip, "*"));// map, 所有属性

        // 2019-05-22T14:34:24.320402Z
        // 17
        // false
        // false
        // {lastAccessTime=2019-05-22T14:21:24.821534Z, lastModifiedTime=2019-05-22T14:34:24.320402Z, size=17, creationTime=2019-05-22T14:21:24.821534Z, isSymbolicLink=false, isRegularFile=true, fileKey=null, isOther=false, isDirectory=false}

    }

    @Test
    public void testFileLocation() {
        String s = this.getClass().getResource(".").toString();
        System.out.println(s);
        // file:/D:/repo/repo_github/shared_already/springboot-demos/java8-demo/target/test-classes/io/github/xiaoyu/java8demo/
    }

    @Test
    public void testFileLocation_1() throws URISyntaxException {
        String s = this.getClass().getResource("/").toString();
        System.out.println(s);
        // file:/D:/repo/repo_github/shared_already/springboot-demos/java8-demo/target/test-classes/
    }

    @Test
    public void testFileLocation_2() throws URISyntaxException {
        String s = Thread.currentThread().getContextClassLoader().getResource(".").getPath();
        System.out.println(s);
        // /D:/repo/repo_github/shared_already/springboot-demos/java8-demo/target/test-classes/
        // 如果是 toURI(), 则是 file:/D:/repo/repo_github/shared_already/springboot-demos/java8-demo/target/test-classes/
    }

    @Test
    public void testFileLocation_3() {
        try {
            String s = Thread.currentThread().getContextClassLoader().getResource("/").toString();
        } catch (Exception ex) {
            System.out.println("throw a exception.");
        }
    }

    @Test
    public void testFileLocation_4() {
        String s = this.getClass().getResource("Java8DemoApplicationTests.class").toString();
        System.out.println(s);
        //file:/D:/repo/repo_github/shared_already/springboot-demos/java8-demo/target/test-classes/io/github/xiaoyu/java8demo/Java8DemoApplicationTests.class
    }

    @Test
    public void test_5() throws URISyntaxException {
        Path path = Paths.get(this.getClass().getResource("Java8DemoApplicationTests.class").toURI());
        System.out.println(path.toString());
    }

    @Test
    public void test_6() {
        Path s = new File("src/main/java/io/github/xiaoyu/java8demo/Java8DemoApplication.java").toPath();
        System.out.println(s);
    }

    @Test
    public void test_7() {
        String absolutePath = new File("").getAbsolutePath();
        System.out.println(absolutePath);
        // D:\repo\repo_github\shared_already\springboot-demos\java8-demo
    }

    @Test
    public void test_8(){
        String s = Paths.get("").toAbsolutePath().toString();
        System.out.println(s);
        // D:\repo\repo_github\shared_already\springboot-demos\java8-demo
    }

}
