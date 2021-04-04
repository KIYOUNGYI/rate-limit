import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

public class CountDownLatchExample {

  public static void main(String args[]) throws InterruptedException {
    CountDownLatch countDownLatch = new CountDownLatch(10);
    List<Thread> workers = Stream
        .generate(() -> new Thread(new Worker(countDownLatch)))
        .limit(10)
        .collect(toList());

    System.out.println("Start multi threads (tid: " + Thread.currentThread().getId() + ")");

    workers.forEach(Thread::start);

    System.out.println("Waiting for some work to be finished (tid: " + Thread.currentThread().getId() + ")");

    countDownLatch.await();

    System.out.println("Finished (tid: " + Thread.currentThread().getId() + ")");
  }

  public static class Worker implements Runnable {
    private CountDownLatch countDownLatch;

    public Worker(CountDownLatch countDownLatch) {
      this.countDownLatch = countDownLatch;
    }

    @Override
    public void run() {
      System.out.println("Do something (tid: " + Thread.currentThread().getId() + ")");
      countDownLatch.countDown();
    }
  }
}