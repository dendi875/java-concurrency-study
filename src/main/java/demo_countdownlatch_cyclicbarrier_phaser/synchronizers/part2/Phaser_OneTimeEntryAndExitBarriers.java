package demo_countdownlatch_cyclicbarrier_phaser.synchronizers.part2;

import demo_countdownlatch_cyclicbarrier_phaser.common.Demo;

import java.util.concurrent.Phaser;

/**
 * 这个例子展示了如何使用 Phaser 来控制线程的同步和协调，
 * 确保一组线程在开始时一起启动，并在所有线程完成工作后同步结束。
 * Phaser 在这里扮演了一个“一次性入口和出口屏障”的角色。
 *
 *
 * 一、运行流程总结
 *     开始阶段：
 *         主线程创建 Phaser，初始状态有 1 个参与者（主线程）。
 *         创建 3 个 Worker 线程，每个线程启动后注册到 Phaser。
 *         主线程等待 1 秒，调用 arriveAndDeregister() 使 Phaser 进入下一个阶段，允许工作线程开始工作。
 *     线程工作：
 *         所有工作线程在 phaser.arriveAndAwaitAdvance() 等待同步，确保它们同时开始执行工作。
 *         每个线程执行自己的工作，模拟工作过程（通过 sleep 来模拟延迟）。
 *     结束阶段：
 *         每个线程在完成工作后调用 phaser.arriveAndDeregister()，通知 Phaser 它们已经完成并注销自己。
 *         主线程重新注册到 Phaser，然后进入一个循环，等待所有线程完成工作。
 *         在 Phaser 的所有参与者完成后，phaser.isTerminated() 返回 true，主线程也完成并注销自己，结束整个流程。
 *
 * 二、Phaser 的作用：
 *     启动屏障：Phaser 在一开始作为一个同步屏障，确保所有线程在同一时间开始工作。
 *     结束屏障：在所有线程完成工作后，通过 arriveAndAwaitAdvance() 和 arriveAndDeregister()，
 *     Phaser 作为结束屏障，确保所有线程都完成后主线程继续执行。
 *
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-09-14 10:22:55
 */
public class Phaser_OneTimeEntryAndExitBarriers extends Demo {

	private static final int PARTIES = 3;

	public static void main(String[] args) {
		// 注册一个参与者，即主线程
		Phaser phaser = new Phaser(1);
		log("after constructor", phaser);

		for (int p = 0; p < PARTIES; p++) {
			int delay = p + 1;
			new Thread(new Worker(delay, phaser)).start();
		}

		log("all threads waiting to start", phaser);
		sleep(1);

		log("before all threads started", phaser);
		phaser.arriveAndDeregister();
		log("after all threads started", phaser);

		// 主线程再次注册到 Phaser，进入一个循环，在所有工作线程完成后调用 arriveAndAwaitAdvance 以同步结束阶段
		phaser.register();
		while (!phaser.isTerminated()) {
			phaser.arriveAndAwaitAdvance(); // 等待所有线程工作完成
			phaser.arriveAndDeregister(); // 注销自身，确保 Phaser 能顺利终止
		}

		log("all treads finished", phaser);
	}

	private static void log(String message, Phaser phaser) {
		logger.info("{} phase: {} registered/arrived/unarrived: {}={}+{}, terminated: {}",
				String.format("%-40s", message),
				phaser.getPhase(),
				phaser.getRegisteredParties(),
				phaser.getArrivedParties(),
				phaser.getUnarrivedParties(),
				phaser.isTerminated()
		);
	}

	private static class Worker implements Runnable {

		private final int delay;
		private final Phaser phaser;

		public Worker(int delay, Phaser phaser) {
			phaser.register();

			this.delay = delay;
			this.phaser = phaser;
		}

		@Override
		public void run() {
			log("before arriveAndAwaitAdvance()", phaser);
			phaser.arriveAndAwaitAdvance();
			log("after arriveAndAwaitAdvance()", phaser);

			work();

			log("before arriveAndDeregister()", phaser);
			phaser.arriveAndDeregister();
			log("after arriveAndDeregister()", phaser);
		}

		private void work() {
			log("work {} started", phaser);
			sleep(delay);
			log("work {} finished", phaser);
		}
	}
}
