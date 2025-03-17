package demo_countdownlatch_cyclicbarrier_phaser.synchronizers.part1;

import demo_countdownlatch_cyclicbarrier_phaser.common.Demo;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

/**
 * CountDownLatch 它是多线程间同步的工具，适用于一个任务等待多个任务的场景。
 *
 * 1. CountDownLatch 是通过一个计数器来实现的，你可以在构造时初始化这个计数器。
 * 2. 任何调用该 CountDownLatch 对象上的 await() 方法的线程都将被阻塞，直到计数器的值变为零。
 * 3. 其他线程可以通过调用 countDown() 方法来减少这个计数器的值。
 * 4. 当计数器的值到达零时，所有等待的线程将被释放并继续执行。
 *
 * 使用场景：
 * 1. 启动依赖：当程序启动时，可能需要等待某些服务或资源准备就绪才能继续执行。可以使用 CountDownLatch 来确保所有必要条件都已经满足。
 * 2. 并行计算：可以将一个大的计算任务分解为多个子任务，由多个线程并行执行，主线程等待所有的子任务完成后，再将结果合并。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-09-02 19:47:59
 */
public class CountDownLatch_Example extends Demo {

	private static final int PARTIES = 3;

	public static void main(String[] args) throws InterruptedException {
		CountDownLatch latch = new CountDownLatch(3);
		log("after constructor", latch);

		for (int p = 0; p < PARTIES; p++) {
			final int delay = p + 1;

			Thread thread = new Thread(() -> {
				try {
					TimeUnit.SECONDS.sleep(delay);

					log("before countDown() " + delay, latch);
					latch.countDown();
					log("after countDown() " + delay, latch);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			});

			thread.start();
		}

		log("before await()", latch);
		latch.await();
		log("after await()", latch);
	}

	private static void log(String message, CountDownLatch latch) {
		logger.info("{} count: {}", String.format("%-40s", message), latch.getCount());
	}
}
