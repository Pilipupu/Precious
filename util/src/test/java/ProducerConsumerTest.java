/**
 * Created by IntelliJ IDEA.
 *
 * @Author : jingwang
 * @create 2023/3/1 6:02 PM
 */

import java.util.concurrent.Semaphore;

public class ProducerConsumerTest {

    private static final int BUFFER_SIZE = 10;
    private static final Semaphore empty = new Semaphore(BUFFER_SIZE);
    private static final Semaphore full = new Semaphore(0);
    private static final Semaphore mutex = new Semaphore(1);
    private static final int[] buffer = new int[BUFFER_SIZE];
    private static int in = 0;
    private static int out = 0;

    public static void main(String[] args) {
        Thread producer = new Thread(new Producer(),"Product-1");
        Thread producer2 = new Thread(new Producer(), "Product-2");
        Thread consumer = new Thread(new Consumer());
        producer.start();
//        producer2.start();
        consumer.start();
    }

    private static class Producer implements Runnable {
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    empty.acquire();
                    mutex.acquire();
                    buffer[in] = i;
                    in = (in + 1) % BUFFER_SIZE;
                    System.out.println(Thread.currentThread().getName() + ": " + i);
                    mutex.release();
                    full.release();
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    private static class Consumer implements Runnable {
        public void run() {
            for (int i = 0; i < 10; i++) {
                try {
                    full.acquire();
                    mutex.acquire();
                    int item = buffer[out];
                    out = (out + 1) % BUFFER_SIZE;
                    System.out.println("Consumed: " + item);
                    mutex.release();
                    empty.release();
                    Thread.sleep(2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
