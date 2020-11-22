package connect;

/**
 * LazySingleton
 */
public class LazySingleton {

    private LazySingleton() {
        System.out.println("Singleton is create");
    }

    private static LazySingleton instance;

    public static synchronized LazySingleton getInstance() {
        if (instance == null) {
            instance = new LazySingleton();
        }
        return instance;
    }
}