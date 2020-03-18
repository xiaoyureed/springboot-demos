package io.github.xiaoyureed.concurrentjava.other;

public class Singleton {
  private Singleton() {
      System.out.println("Singleton is create");
  }

  private static Singleton instance = new Singleton();

  public static Singleton getInstance() {
      return instance;
  }
}