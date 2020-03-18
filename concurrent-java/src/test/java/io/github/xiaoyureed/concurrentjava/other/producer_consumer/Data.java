package io.github.xiaoyureed.concurrentjava.other.producer_consumer;

/**
 * Data
 */
public class Data {

  private final int intDdata;

  public Data(int value) {
    this.intDdata = value;
  }

  /**
   * @return the intDdata
   */
  public int getIntDdata() {
    return intDdata;
  }

  @Override
  public String toString() {
    return "data:[intData=]" + this.intDdata;
  }

}