package io.github.xiaoyu.javabasic;


import org.junit.Test;

/**
 * @author xiaoyu
 * @since 1.0
 */
public class DemoTest {

    @Test
    public void testDemo() {
        loop:
        while (true) {
            while (true) {
                break loop;
            }
            //break;
        }
        System.out.println(">>>");
    }

    @Test
    public void testClassName() {
        // 对于 数组类型的类，类名 “[” + "基本类型 or L" + 引用类型类名
        byte[]   bytes  = new byte[1024];
        Object[] objArr = new Object[10];
        System.out.println(">>> bytes name: " + bytes.toString());
        System.out.println(">>> objArr name: " + objArr.toString());
        // result:
        //>>> bytes name: [B@11028347
        //>>> objArr name: [Ljava.lang.Object;@14899482
    }

    @Test
    public void testClassLoader() {
        ClassLoader classLoader = DemoTest.class.getClassLoader();
        ClassLoader parent      = classLoader.getParent();
        ClassLoader grand       = parent.getParent();
        System.out.println(">>> classloader: " + classLoader);
        System.out.println(">>> parent: " + parent);
        System.out.println(">>> grand: " + grand);
        /* result:
        *   >>> classloader: sun.misc.Launcher$AppClassLoader@14dad5dc
            >>> parent: sun.misc.Launcher$ExtClassLoader@49c2faae
            >>> grand: null
        * */
    }
}
