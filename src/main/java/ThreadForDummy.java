import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

public class ThreadForDummy {

  static final int MAX_REQUESTS_PER_SEC = 10;

  public static void main(String[] args) throws InterruptedException {

    RateLimiter rateLimiter = new LeakyBucket(MAX_REQUESTS_PER_SEC); // new a RateLimiter here

    Thread r = new Thread(() -> {
//      sendRequest(rateLimiter, 10, 1);//10초 소요
//      sendRequest(rateLimiter, 20, 2);//초당 2번 요청을 처리한다.
//      sendRequest(rateLimiter, 50, 5);//초당 5번의 요청을 처리한다. 총 50개의 요청을 날린다.
//      sendRequest(rateLimiter, 100, 10);//초당 10개의 요청을 처리한다. 총 100개의 요청을 날린다.
//      sendRequest(rateLimiter, 200, 20);//초당 20개의 요청을 처리한다.
//      sendRequest(rateLimiter, 250, 25);//초당 25개의 요청을 처리한다.
      sendRequest(rateLimiter, 500, 50);//초당 50개를 날리지만, 초당 호출 개수가 10개로 제약이 걸려있다면 50초 전후로 걸리겠지
//      sendRequest(rateLimiter, 1000, 100);
    });
//    Thread r2 = new Thread(() -> {
//      sendRequest(rateLimiter, 10, 1);
//      sendRequest(rateLimiter, 20, 2);
//      sendRequest(rateLimiter, 50, 5);
//      sendRequest(rateLimiter, 100, 10);
//      sendRequest(rateLimiter, 200, 20);
//      sendRequest(rateLimiter, 250, 25);
//      sendRequest(rateLimiter, 500, 50);
//      sendRequest(rateLimiter, 1000, 100);
//    });

    r.start();
//    r2.start();

    r.join();
//    r2.join();
  }


  private static void sendRequest(RateLimiter rateLimiter, int totalCnt, int requestPerSec) {
    System.out.println("rateLimiter = " + rateLimiter + ", totalCnt = " + totalCnt + ", requestPerSec = " + requestPerSec);
    long startTime = System.currentTimeMillis();
    CountDownLatch doneSignal = new CountDownLatch(totalCnt);
    for (int i = 0; i < totalCnt; i++) {
      try {
        new Thread(() -> {
          while (!rateLimiter.allow()) {
            try {
              TimeUnit.MILLISECONDS.sleep(10);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
          doneSignal.countDown();
        }).start();
        TimeUnit.MILLISECONDS.sleep(1000 / requestPerSec);
        //1초에 2번의 요청을 날리면 중간에 0.5초 쉰다고 보면 된다.
        //초당 25개의 요청을 처리하면, 요청 1번 할때마다 중간에 1000/25 ms 쉰다고 보면 된다.
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
    }
    try {
      doneSignal.await();
    } catch (InterruptedException e) {
      e.printStackTrace();
    }
    double duration = (System.currentTimeMillis() - startTime) / 1000.0;
    System.out.println("MAX_REQUESTS_PER_SEC : " + MAX_REQUESTS_PER_SEC + "  totalCnt : " + totalCnt
        + " requests processed in " + duration + " seconds. " + "Rate: "
        + (double) totalCnt / duration + " per second");
    System.out.println("============================================================");
  }


  public static void hello() {
    System.out.println();
    for (int i = 0; i < 10; i++) {
      System.out.println("ThreadName : " + Thread.currentThread().getName() + " i = " + i
          + " System.currentTimeMillis : " + System.currentTimeMillis());
    }
  }
}

//rateLimiter = LeakyBucket@8005263, totalCnt = 250, requestPerSec = 25
//    MAX_REQUESTS_PER_SEC : 10  totalCnt : 250 requests processed in 25.56 seconds. Rate: 9.780907668231613 per second

//rateLimiter = LeakyBucket@8005263, totalCnt = 250, requestPerSec = 25
//    MAX_REQUESTS_PER_SEC : 1000  totalCnt : 250 requests processed in 10.714 seconds. Rate: 23.33395557214859 per second