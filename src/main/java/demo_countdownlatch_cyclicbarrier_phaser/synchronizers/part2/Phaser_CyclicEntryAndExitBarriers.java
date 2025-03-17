package demo_countdownlatch_cyclicbarrier_phaser.synchronizers.part2;

import demo_countdownlatch_cyclicbarrier_phaser.common.Demo;

import java.util.concurrent.Phaser;

/**
 * 这个示例展示了 Phaser 的高级用法，利用它可以实现多个线程在循环中进行同步，并在满足特定条件后终止同步过程。
 * 通过 Phaser，可以轻松地管理多次循环中的线程同步，确保每个线程在每个阶段中都按顺序执行。
 *
 * 一、运行流程
 *     初始化和准备
 *         主线程创建 Phaser，设置初始参与者为 1（主线程本身）。
 *         主线程启动 3 个工作线程，每个线程在启动时注册到 Phaser，然后等待同步。
 *
 *     工作线程执行
 *         每个线程在循环中执行工作，通过 phaser.arriveAndAwaitAdvance() 同步，在每次循环结束时等待其他线程完成当前阶段。
 *         循环执行，直到达到预定的迭代次数（ITERATIONS）。
 *
 *     主线程同步和结束
 *         主线程在工作线程启动后，解除自身的注册，使得 Phaser 进入下一个阶段。
 *         主线程重新注册到 Phaser，并在一个循环中等待所有线程完成每次循环操作。
 *         Phaser 在每个阶段结束时检查是否应该终止，达到最大阶段数时（即执行了 ITERATIONS 次），Phaser 终止。
 *
 * 二、Phaser 的作用
 *     循环同步：Phaser 用于在每个阶段中同步多个线程的执行，确保线程在每次循环中同时开始工作。
 *     终止控制：通过重写 onAdvance 方法，控制 Phaser 在完成所有预定的循环后终止，避免无限循环
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-09-14 19:22:50
 */
public class Phaser_CyclicEntryAndExitBarriers extends Demo {

	private static final int PARTIES = 3;
	// 最大迭代次数
	private static final int ITERATIONS = 3;

	public static void main(String[] args) {

		Phaser phaser = new Phaser(1) {
			final private int maxPhase = ITERATIONS;

			@Override
			protected boolean onAdvance(int phase, int registeredParties) {
				return (phase >= maxPhase -1) || (registeredParties == 0);
			}
		};
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

		phaser.register();
		while (!phaser.isTerminated()) {
			phaser.arriveAndAwaitAdvance();
		}

		log("all threads finished", phaser);
	}

	private static void log(String message, Phaser phaser) {
		logger.info("{} phase: {}, registered/arrived/unarrived: {}={}+{}, terminated: {}",
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
			do {
				work();
				log("before arriveAndAwaitAdvance()", phaser);
				phaser.arriveAndAwaitAdvance();
				log("after arriveAndAwaitAdvance()", phaser);
			} while (!phaser.isTerminated());
		}

		private void work() {
			logger.info("work {} started", delay);
			sleep(delay);
			logger.info("work {} finished", delay);
		}
	}
}
