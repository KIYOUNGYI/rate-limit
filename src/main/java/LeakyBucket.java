public class LeakyBucket extends RateLimiter {

  private long nextAllowedTime;

  private final long REQUEST_INTERVAL_MILLIS;

  protected LeakyBucket(int maxRequestPerSec) {
    super(maxRequestPerSec);
    REQUEST_INTERVAL_MILLIS = 1000 / maxRequestPerSec;
    nextAllowedTime = System.currentTimeMillis();
    System.out.println("REQUEST_INTERVAL_MILLIS = " + REQUEST_INTERVAL_MILLIS);
    System.out.println("nextAllowedTime = " + nextAllowedTime);
  }

  @Override
  boolean allow() {
    long curTime = System.currentTimeMillis();
    synchronized (this) {
      System.out.println("curTime = " + curTime);
      System.out.println("nextAllowedTime = " + nextAllowedTime);
      System.out.println("curTime >= nextAllowedTime = " + (curTime>=nextAllowedTime));
      if (curTime >= nextAllowedTime) {
        nextAllowedTime = curTime + REQUEST_INTERVAL_MILLIS;
        return true;
      }
      return false;
    }
  }
}