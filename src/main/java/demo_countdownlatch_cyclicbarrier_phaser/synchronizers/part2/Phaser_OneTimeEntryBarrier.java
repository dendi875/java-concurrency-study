package demo_countdownlatch_cyclicbarrier_phaser.synchronizers.part2;

import demo_countdownlatch_cyclicbarrier_phaser.common.Demo;

import java.util.concurrent.Phaser;

/**
 * Phaser 在这里的作用是作为一个“一次性进入屏障”，它确保所有线程在开始工作之前都到达同步点，
 * 只有当所有线程都准备好时，它们才会继续进入下一个阶段。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-09-13 20:43:04
 */
public class Phaser_OneTimeEntryBarrier extends Demo {

	private static final int PARTIES = 3;

	public static void main(String[] args) {
		// 注册一个参与者，即主线程
		Phaser phaser = new Phaser(1);
		log("after constructor", phaser);

		for (int p = 0; p  < PARTIES; p++) {
			int delay = p + 1;
			Runnable worker = new Worker(delay, phaser);
			new Thread(worker).start();
		}

		log("all threads waiting to start", phaser);
		sleep(1);

		log("before all threads started", phaser);
		// 主线程等待所有工作线程注册完成后，调用 arriveAndDeregister() 解除主线程的注册，允许所有工作线程同时开始
		phaser.arriveAndDeregister();
		log("after all threads started", phaser);

		sleep(10);
		log("all threads finished", phaser);
	}

	private static void log(String message, Phaser phaser) {
		logger.info("{} phaser: {}, registered/arrived/unarrived: {}={}+{}, terminated: {}",
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
		}

		private void work() {
			logger.info("worker {} started", delay);
			sleep(delay);
			logger.info("worker {} finished", delay);
		}
	}
}
