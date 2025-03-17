package demo_countdownlatch_cyclicbarrier_phaser.synchronizers.part1;

import demo_countdownlatch_cyclicbarrier_phaser.common.Demo;

import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 *  CyclicBarrier 是一个线程同步工具，它允许一组线程互相先行，直到所有线程都达到一个屏障点后再同时继续执行。
 *  CyclicBarrier 可以重置并重用，这是其与 CountDownLatch 的主要区别。
 *  一、基本使用方法
 *  1. 创建 CyclicBarrier 实例
 *  // 创建一个 CyclicBarrier，计数为 3 ，没有屏障操作
 *  CyclicBarrier barrier = new CyclicBarrier(3);
 *  CyclicBarrier barrier = new CyclicBarrier(3, () -> {
 *		System.out.println("All parties are arrived at barrier, lets play");
 *  });
 *
 *  2. 在每个线程使用 await() 方法
 *  每个线程执行到一定点后调用 await() 方法等待其他线程。所有线程调用 await() 后，它们将继续执行。
 *
 *  3. 重用 CyclicBarrier
 *  当所有线程都到达屏障点后，CyclicBarrier 会自动重置，可以再次使用
 *
 * 二、注意事项：
 * 1. 如果任一参与线程在等待过程中被中断或超时（如果使用了超时的 await()），那么将抛出异常，
 * 并且其他所有等待线程也将收到 BrokenBarrierException，屏障被视为损坏。
 *
 * 2. CyclicBarrier 提供了一个 reset() 方法可以重置屏障，但这应当谨慎使用，因为如果有线程正在等待，
 * 这将导致它们接收到 BrokenBarrierException。
 *
 * getNumberWaiting() 它返回当前在屏障处等待的线程数量
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-09-04 18:43:42
 */
public class CyclicBarrier_Example extends Demo {

	private static final int PARTIES = 1 + 3;

	public static void main(String[] args) throws BrokenBarrierException, InterruptedException {
		CyclicBarrier barrier = new CyclicBarrier(PARTIES, () -> {
			logger.info("在所有的线程都到达屏障时，但在它们被释放之前执行");
		});
		log("after constructor", barrier);

		for (int p = 0; p < PARTIES - 1; p++) {
			final int delay = p + 1;

			Thread thread = new Thread(() -> {
				try {
					TimeUnit.SECONDS.sleep(delay);

					log("before await() " + delay, barrier);
					barrier.await();
					log("after await() " + delay, barrier);
				} catch (InterruptedException | BrokenBarrierException e) {
					throw new RuntimeException(e);
				}
			});
			thread.start();
		}

		log("before await()", barrier);
		barrier.await();
		log("after await()", barrier);
	}

	private static void log(String message, CyclicBarrier barrier) {
		logger.info("{} waiting: {}", String.format("%-40s", message), barrier.getNumberWaiting());
	}
}
