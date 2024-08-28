package demo_completablefuture.pipeline.accept;

import demo_completablefuture.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 *
 *thenAcceptBoth 方法通常用于以下场景：
 *
 * 多任务结果处理：当你需要等待两个独立的异步任务都完成，并且你需要使用这两个任务的结果执行一些操作（如记录、通知等）但不需要进一步的结果传递。
 * 协调操作：在需要从两个源收集信息并协调这些信息以执行操作的情况下使用，例如，更新数据库记录、发送统计数据等。
 * 并行任务同步：用于确保两个相关的并行操作都完成后再进行下一步，例如，在继续前需要两个资源的确认或验证。
 *
 *
 * 它允许你处理两个 CompletionStage 的结果。当这两个 CompletableFuture 都完成时，你可以执行一个操作，
 * 该操作消费（即使用但不返回值）这两个 CompletableFuture 的结果
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-22 18:59:46
 */
public class ThenAcceptBoth extends Demo {

	@Test
	public void testThenAcceptBoth() {
		int userId = 123;

		CompletableFuture<String> profileFuture = fetchUserProfile(userId);
		CompletableFuture<String> historyFuture = fetchUserProfile(userId);

		CompletableFuture<Void> stage = profileFuture.thenAcceptBoth(historyFuture, (profile, history) -> {
			logger.info("Profile: {}, History: {}", profile, history);
		});

		// 确保主线程不会过早退出
		profileFuture.join();
		historyFuture.join();
	}

	public CompletableFuture<String> fetchUserProfile(int userId) {
		return CompletableFuture.supplyAsync(() -> sleepAndGet(2, "User Profile for ID: " + userId));
	}

	public CompletableFuture<String> fetchOrderHistory(int userId) {
		return CompletableFuture.supplyAsync(() -> sleepAndGet(2, "Order History for ID: " + userId));
	}
}
