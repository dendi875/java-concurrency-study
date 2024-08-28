package demo_completablefuture.overview;

import demo_completablefuture.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * 同步执行与异步执行
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-22 10:31:12
 */
public class SynchronousVsAsynchronousExecution extends Demo {

	@Test
	public void testSynchronous() {
		logger.info("this task stared");

		int netAmountInUsd = getPriceInEur() * getExchangeRateEurToUsd();  // blocking
		float tax = getTax(netAmountInUsd); // blocking
		float grossAmountInUsd = netAmountInUsd * (1 + tax);

		logger.info("this task finished: {}", grossAmountInUsd);

		logger.info("another task stared");
	}

	@Test
	public void testAsynchronousWithFuture() throws InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newCachedThreadPool();

		logger.info("this task stared");

		Future<Integer> priceInEur = executorService.submit(this::getPriceInEur);
		Future<Integer> exchangeRateEurToUsd = executorService.submit(this::getExchangeRateEurToUsd);

		while (!priceInEur.isDone() || !exchangeRateEurToUsd.isDone()) {
			Thread.sleep(100);
			logger.info("another task1 is running");
		}

		int netAmountInUsd = priceInEur.get() * exchangeRateEurToUsd.get(); // actually non-blocking
		Future<Float> tax = executorService.submit(() -> getTax(netAmountInUsd));

		while (!tax.isDone()) {
			Thread.sleep(100);
			logger.info("another task2 is running");
		}

		float grossAmountInUsd = netAmountInUsd * (1 + tax.get()); // actually non-blocking

		logger.info("this is finished: {}", grossAmountInUsd);

		executorService.shutdown();
		executorService.awaitTermination(60, TimeUnit.SECONDS);

		logger.info("another task3 is running");
	}

	@Test
	public void testAsynchronousWithCompletableFuture() throws InterruptedException {
		CompletableFuture<Integer> priceInEur = CompletableFuture.supplyAsync(this::getPriceInEur);
		CompletableFuture<Integer> exchangeRateEurToUsd = CompletableFuture.supplyAsync(this::getExchangeRateEurToUsd);

		// 这个方法等待两个CompletableFuture都完成
		CompletableFuture<Integer> netAmountInUsd = priceInEur.thenCombine(exchangeRateEurToUsd, (price, exchangeRate) -> price * exchangeRate);

		logger.info("this task started");

		netAmountInUsd
				.thenCompose(amount -> CompletableFuture.supplyAsync( () -> amount * (1 + getTax(amount))))
				.whenComplete((grossAmountInUsd, throwable) -> {
					if (throwable == null) {
						logger.info("this task finished: {}", grossAmountInUsd);
					} else {
						logger.warn("this task failed: {}", throwable.getMessage());
					}
				}); // non-blocking

		logger.info("another task started");
		Thread.sleep(10000);
	}

	// 获取欧元价格
	private int getPriceInEur() {
		return sleepAndGet(2);
	}

	// 欧元对美元的汇率
	private int getExchangeRateEurToUsd() {
		return sleepAndGet(4);
	}

	private float getTax(int amount) {
		return sleepAndGet(50) / 100f;
	}
}
