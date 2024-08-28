package demo_completablefuture.pipeline.run;

import demo_completablefuture.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 *
 * runAfterEither 它允许你在两个CompletableFuture中的任意一个完成后，执行一个Runnable任务，
 * 而这个Runnable任务不需要使用任何CompletableFuture的结果。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-23 13:59:26
 */
public class RunAfterEither extends Demo {

	@Test
	public void test() {
		CompletableFuture<String> networkStage = fetchDataFromNetwork();
		CompletableFuture<String> cacheStage = fetchDateFromCache();

		networkStage.runAfterEither(cacheStage, () -> logger.info("Data fetch completed."));

		// 确认主线程不会过造退出
		networkStage.join();
		cacheStage.join();
	}
	
	public CompletableFuture<String> fetchDataFromNetwork() {
		return CompletableFuture.supplyAsync(() -> sleepAndGet(2, "Data from Network"));
	}

	public CompletableFuture<String> fetchDateFromCache() {
		return CompletableFuture.supplyAsync(() -> sleepAndGet(1, "Data from Cache"));
	}
}
