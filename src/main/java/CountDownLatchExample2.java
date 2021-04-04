import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.stream.Stream;

public class CountDownLatchExample2 {

  public static void main(String args[]) throws InterruptedException {
    CountDownLatch readyLatch = new CountDownLatch(5);
    CountDownLatch startLatch = new CountDownLatch(1);
    CountDownLatch finishLatch = new CountDownLatch(5);
    List<Thread> workers = Stream
        .generate(() -> new Thread(new Worker(readyLatch, startLatch, finishLatch)))
        .limit(5)
        .collect(toList());

    System.out.println("Start multi threads (tid: " + Thread.currentThread().getId() + ")");

//    workers.forEach(Thread::start);

    for(int i=0;i<5;i++){
      Thread thread = workers.get(i);
      thread.start();
    }

    readyLatch.await();

    System.out.println("Waited for ready and started doing some work (tid: " + Thread.currentThread().getId() + ")");
    startLatch.countDown();

    finishLatch.await();
    System.out.println("Finished (tid: " + Thread.currentThread().getId() + ")");
  }

  public static class Worker implements Runnable {
    private CountDownLatch readyLatch;
    private CountDownLatch startLatch;
    private CountDownLatch finishLatch;

    public Worker(CountDownLatch readyLatch, CountDownLatch startLatch, CountDownLatch finishLatch) {
      this.readyLatch = readyLatch;
      this.startLatch = startLatch;
      this.finishLatch = finishLatch;
    }

    @Override
    public void run() {
      readyLatch.countDown();
      try {
        startLatch.await();
      } catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("Do something (tid: " + Thread.currentThread().getId() + ")");
      finishLatch.countDown();
    }
  }
}