package demo_countdownlatch_cyclicbarrier_phaser.synchronizers.part2;

import demo_countdownlatch_cyclicbarrier_phaser.common.Demo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;

/**
 *
 * 以下代码实现了一个多线程同步的示例，使用了两个CyclicBarrier实例来创建周期性的入口和出口屏障。
 * 这使得多个工作线程可以在每次迭代中同步启动和完成他们的任务。
 *
 * -----------d
 * 周期性的入口和出口屏障与一次性入口和出口屏障是两种常用于多线程编程中的同步模式。
 * 它们都旨在管理和协调多个线程的执行流程，但它们的特性和应用场景有所不同。
 *
 * 一、周期性的入口和出口屏障（使用 CyclicBarrier）
 * 周期性的入口和出口屏障允许一组线程在达到某个屏障点后停下来等待，直到所有线程都达到这个点后再一起继续执行。
 * 这个过程可以重复多次，即屏障是可以循环使用的。
 *
 * 特点
 * 重用性：CyclicBarrier可以在释放等待的线程后自动重置，因此可以用于多轮的同步任务。
 * 自定义操作：可以在所有线程到达屏障后但在它们被释放前执行一个预定义的操作（称为屏障动作）。
 *
 * 二、一次性入口和出口屏障（使用 CountDownLatch）
 * 一次性入口和出口屏障是一种一次性使用，就不能再被重置。
 *
 * 特点
 * 不可重用：CountDownLatch一旦计数降到零就不能再被重置或重用。
 * 灵活性：可以在任何时刻减少计数器，不需要所有线程同时到达。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-09-05 10:22:28
 */
public class CyclicBarrier_CyclicEntryAndExitBarriers extends Demo {

	private static final int PARTIES = 3;
	private static final int ITERATIONS = 3;

	public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
		CyclicBarrier entryBarrier = new CyclicBarrier(PARTIES + 1, () -> logger.info("iteration started"));
		CyclicBarrier exitBarrier = new CyclicBarrier(PARTIES + 1, () -> logger.info("iteration finished"));

		for (int i = 0; i < ITERATIONS; i++) {
			for (int p = 0; p < PARTIES; p++) {
				int delay = p + 1;
				Runnable task = new Worker(delay, entryBarrier, exitBarrier);
				Thread thread = new Thread(task);
				thread.start();
			}

			logger.info("all threads waiting to start: iteration {}", i);
			sleep(1);

			entryBarrier.await();
			logger.info("all threads started: iteration: {}", i);

			exitBarrier.await();
			logger.info("all threads finished: iteration: {}", i);
		}
	}

	private static class Worker implements Runnable {

		private final int delay;
		private final CyclicBarrier entryBarrier;
		private final CyclicBarrier exitBarrier;

		public Worker(int delay, CyclicBarrier entryBarrier, CyclicBarrier exitBarrier) {
			this.delay = delay;
			this.entryBarrier = entryBarrier;
			this.exitBarrier = exitBarrier;
		}

		@Override
		public void run() {
			try {
				entryBarrier.await();
				work();
				exitBarrier.await();
			} catch (InterruptedException | BrokenBarrierException e) {
				throw new RuntimeException(e);
			}
		}

		private void work() {
			logger.info("work {} started", delay);
			sleep(delay);
			logger.info("work {} finished", delay);
		}
	}
}
