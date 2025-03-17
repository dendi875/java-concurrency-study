package demo_countdownlatch_cyclicbarrier_phaser.synchronizers.part2;

import demo_countdownlatch_cyclicbarrier_phaser.common.Demo;

import java.util.concurrent.Phaser;

/**
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-09-13 18:15:51
 */
public class Phaser_Introduction extends Demo {

	public static void main(String[] args) {

		// onAdvance 是 Phaser 在所有参与者到达阶段时调用的钩子方法。
		// 通过重写该方法，可以控制 Phaser 何时终止或如何在阶段之间进行切换。
		// 返回 true 表示 Phaser 将终止，即不再进入下一个阶段。若返回 false，则继续下一个阶段的同步。
		Phaser phaser = new Phaser(3) {
			@Override
			protected boolean onAdvance(int phase, int registeredParties) {
				log("inside onAdvance()", this);
				return true;
			}
		};
		log("after constructor", phaser);

		phaser.register();
		log("after register()", phaser);

		// 通知 Phaser 当前线程到达
		phaser.arrive();
		log("after arrive()", phaser);

		Thread thread = new Thread(() -> {
			log("before arriveAndAwaitAdvance()", phaser);
			// 通知 Phaser 当前线程到达，并阻塞等待其他线程到达
			phaser.arriveAndAwaitAdvance();
			log("after arrvieAndAwaitAdvance()", phaser);
		});
		thread.start();

		phaser.arrive();
		log("after arrive()", phaser);

		phaser.arriveAndDeregister();
		log("after arriveAndDeregister()", phaser);
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
}
