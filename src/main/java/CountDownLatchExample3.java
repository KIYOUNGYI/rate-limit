import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;
import java.util.stream.Stream;

public class CountDownLatchExample3 {

  public static void main(String args[]) throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(5);
    List<Thread> workers = Stream
        .generate(() -> new Thread(new Worker(countDownLatch))).limit(5).collect(toList());

    System.out.println("Start multi threads (tid: " + Thread.currentThread().getId() + ")");

    workers.forEach(Thread::start);

    System.out.println("Waiting for some work to be finished (tid: " + Thread.currentThread().getId() + ")");

    countDownLatch.await(5, TimeUnit.SECONDS);//타임아웃 -> 5초

    System.out.println("Finished (tid: " + Thread.currentThread().getId() + ")");
  }

  public static class Worker implements Runnable {
    private CountDownLatch countDownLatch;

    public Worker(CountDownLatch countDownLatch) {
      this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
      System.out.println("Doing something (tid: " + Thread.currentThread().getId() + ")");
      try {
        Thread.sleep(10000);//10초
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      countDownLatch.countDown();
      System.out.println("Done (tid: " + Thread.currentThread().getId() + ")");
    }
  }
}