package demo_countdownlatch_cyclicbarrier_phaser.synchronizers.part2;

import demo_countdownlatch_cyclicbarrier_phaser.common.Demo;

import java.util.concurrent.CountDownLatch;

/**
 * “一次性入口和出口屏障”是一种编程模式，主要用于控制多个线程在特定点的同步执行。此模式通过使用同步工具，
 * 如 CountDownLatch，确保所有线程在进入（入口屏障）或离开（出口屏障）某个代码区块之前达到同步。
 *
 * 一、入口屏障（Entry Barrier）
 * 此屏障确保所有线程都在继续执行之前达到某个执行点。这通常用于确保在任何线程开始执行之前，所有线程都已准备好启动。
 * 例如，在并行计算中，可能需要所有线程同时开始处理数据以保持执行的同步性。
 *
 * 在下面的示例代码中，entryBarrier是一个被初始化为1的CountDownLatch。
 * 它的主要作用是阻塞所有工作线程直到主线程调用了entryBarrier.countDown()，
 * 这个调用发生在主线程确定所有工作线程都已经启动并处于就绪状态后。
 *
 * 一、出口屏障（Exit Barrier）
 * 出口屏障用于确保在继续执行之前所有线程都已完成其任务。这在需要所有线程完成其部分工作后才能执行后续操作的场景中特别有用，
 * 如在所有数据部分处理完毕后进行汇总。
 *
 * 在下面的示例代码中，exitBarrier是一个根据工作线程数（PARTIES）初始化的CountDownLatch。
 * 每个工作线程在完成其任务后会调用exitBarrier.countDown()，当所有工作线程都已完成时，计数达到零，
 * 阻塞在exitBarrier.await()上的主线程将继续执行，进行后续的操作。
 *
 * 三、特点
 * 这种模式的特点是它的一次性使用性质。一旦CountDownLatch的计数器达到零，它就不能再被重置了。
 * 如果需要再次进行同样的同步，必须创建新的CountDownLatch实例。
 * 这与某些其他同步辅助工具（如CyclicBarrier）不同，后者可以重置并多次使用。
 *
 * 四、应用场景
 * “一次性入口和出口屏障”模式适用于以下场景：
 *
 * 1. 启动阶段需要初始化多个组件，确保全部组件都就绪后才开始处理。
 * 2. 并行执行的任务需要在所有线程完成后才能进行下一步，如并行算法的阶段性同步。
 * 3. 测试环境中，需要模拟高并发场景，确保所有线程按预期同时启动。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-09-04 17:58:27
 */
public class CountDownLatch_OneTimeEntryAndExitBarriers extends Demo {

	private static final int PARTIES = 3;

	public static void main(String[] args) throws InterruptedException {
		CountDownLatch entryBarrier = new CountDownLatch(1);
		CountDownLatch exitBarrier = new CountDownLatch(PARTIES);

		for (int p = 0; p < PARTIES; p++) {
			int delay = p + 1;
			Runnable task = new Work(delay, entryBarrier, exitBarrier);
			new Thread(task).start();
		}

		logger.info("all threads waiting to start");
		sleep(1);

		entryBarrier.countDown();
		logger.info("all threads started");

		exitBarrier.await();
		logger.info("all threads finished");
	}

	private static class Work implements Runnable {

		private final int delay;
		private final CountDownLatch entryBarrier;
		private final CountDownLatch exitBarrier;

		public Work(int delay, CountDownLatch entryBarrier, CountDownLatch exitBarrier) {
			this.delay = delay;
			this.entryBarrier = entryBarrier;
			this.exitBarrier = exitBarrier;
		}

		@Override
		public void run() {
			try {
				entryBarrier.await();
				work();
				exitBarrier.countDown();
			} catch (InterruptedException e) {
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
