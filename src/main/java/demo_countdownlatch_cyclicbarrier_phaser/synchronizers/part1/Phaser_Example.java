package demo_countdownlatch_cyclicbarrier_phaser.synchronizers.part1;

import demo_countdownlatch_cyclicbarrier_phaser.common.Demo;

import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;

/**
 *
 * Phaser 是 Java 中用于并发编程的一个同步工具，属于 java.util.concurrent 包的一部分。
 * Phaser 类似于 CyclicBarrier 和 CountDownLatch，但功能更加灵活，适合动态变化的线程数量同步。
 * 它通过阶段（phase）控制任务的执行，每一个阶段中的任务执行完成后会进入下一个阶段。
 *
 * 基本概念
 *     Phase（阶段）：Phaser 的一个执行周期，可以视为一轮任务。每一轮任务结束后，所有参与的线程都会进入下一个阶段。
 *     Parties（参与者）：注册到 Phaser 的线程，Phaser 控制这些参与者之间的同步。
 *     Advance（前进）：当所有参与者到达一个阶段后，Phaser 会自动进入下一阶段。
 *
 * 使用步骤
 *     1. 创建 Phaser 实例：通过构造函数可以初始化 Phaser，同时指定初始参与者数量。
 *     		Phaser phaser = new Phaser(3); // 初始有 3 个参与者
 *	   2. 线程参与同步: 调用 phaser.arriveAndAwaitAdvance() 方法来通知 Phaser 当前线程已到达，并等待其他线程到达。
 *	   3. 动态增加或减少参与者
 *	   		phaser.register() // 注册新参与者
 *	   		phaser.arriveAndDeregister(); // 减少一个参与者
 *	   4. 获取当前阶段：
 *	   		phaser.getPhase() // 获取当前阶段的编号
 *
 * 主要方法
 * 		arrive(): 				 通知 Phaser 当前线程到达，不等待其他线程，调用者不阻塞。
 * 	    arriveAndAwaitAdvance(): 通知 Phaser 当前线程到达，等待其他线程，调用者阻塞。
 * 	    arriveAndDeregister():   通知 Phaser 当前线程到达，注销当前线程，调用者不阻塞。
 * 	    getPhase(): 			 获取当前阶段的编号。
 * 	    register(): 			 注册一个新参与者。
 * 	    bulkRegister(int parties): 批量注册多个参与者。
 *
 * 使用场景
 *     适合用在分阶段的并发任务中，如有多个线程执行分批任务，每个批次任务完成后再进入下一阶段的情况。
 *     适用于参与者数量动态变化的场景，比如需要在任务执行期间增加或减少线程的场景。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-09-12 20:35:21
 */
public class Phaser_Example extends Demo {

	private static final int PARTIES = 1 + 3;

	/**
	 * 这里的 PARTIES 表示共有 4 个参与者，
	 *
	 * Phaser phaser = new Phaser(1); 初始化 Phaser 时指定了 1 个初始参与者。
	 * 这个参与者通常是主线程，它将负责协调其他线程。
	 *
	 * 创建和启动线程： 这里分为两类线程：
	 *     第一类：到达阶段后不取消注册。
	 *     第二类：到达阶段后取消注册。
	 */
	public static void main(String[] args) {
		Phaser phaser = new Phaser(1);
		log("after constructor", phaser);

		phaser.bulkRegister(PARTIES - 1);
		log("after bulkRegister(3)", phaser);

		// 2 个线程，到达阶段后不取消注册。
		for (int p = 0; p < PARTIES - 2; p++) {
			final int delay = p + 1;

			Thread thread = new Thread(() -> {
				try {
					TimeUnit.SECONDS.sleep(delay);

					log("before arrive() " + delay, phaser);
					phaser.arrive();
					log("after arrive() " + delay, phaser);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			});
			thread.start();
		}

		// 1 个线程，到达阶段后取消注册
		for (int p = PARTIES - 2; p < PARTIES - 1; p++) {
			final int delay = p + 1;

			Thread thread = new Thread(() -> {
				try {
					TimeUnit.SECONDS.sleep(delay);

					log("before arriveAndDeregister() " + delay, phaser);
					phaser.arriveAndDeregister();
					log("after arriveAndDeregister() " + delay, phaser);
				} catch (InterruptedException e) {
					throw new RuntimeException(e);
				}
			});
			thread.start();
		}

		// 主线程等待所有线程到达后才继续进行
		log("before arriveAndAwaitAdvance()", phaser);
		phaser.arriveAndAwaitAdvance();
		log("after arriveAndAwaitAdvance()", phaser);
	}

	private static void log(String message, Phaser phaser) {
		logger.info("{} phase: {}, redistered/arrived/unarrvied: {}={}+{}, terminated: {}",
					String.format("%-40s", message),
					phaser.getPhase(),
					phaser.getRegisteredParties(), // 已注册的参与者数量
					phaser.getArrivedParties(), // 已到达的参与者数量
					phaser.getUnarrivedParties(), // 未到达的参与者数量
					phaser.isTerminated() // 是否终止（当所有参与者完成时，Phaser 会被终止）
				);
	}
}
