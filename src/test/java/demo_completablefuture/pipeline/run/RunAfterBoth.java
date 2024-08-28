package demo_completablefuture.pipeline.run;

import demo_completablefuture.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * runAfterBoth 允许您在两个 CompletableFuture 任务都完成后执行一个 Runnable 任务，
 * 此 Runnable 不会接收任何参数，也不会返回任何结果，通常用于执行一些只需在两个任务都完成后进行的后续操作，例如清理工作、日志记录或触发其他过程。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-23 15:24:12
 */
public class RunAfterBoth extends Demo {

	@Test
	public void testRunAfterBoth() {
		CompletableFuture<String> dbConfigStage = loadConfigFromDatabase();
		CompletableFuture<String> fileConfigStage = loadConfigFromFileSystem();

		CompletableFuture<Void> stage = dbConfigStage.runAfterBoth(fileConfigStage,
				() -> logger.info("Both configuration are loaded.")
		);

		// 确保主线程不会过早退出
		dbConfigStage.join();
		fileConfigStage.join();
	}

	public CompletableFuture<String> loadConfigFromDatabase() {
		return CompletableFuture.supplyAsync(() -> sleepAndGet(2, "Database Config"));
	}

	public CompletableFuture<String> loadConfigFromFileSystem() {
		return CompletableFuture.supplyAsync(() -> sleepAndGet(2, "File System Config"));
	}
}
