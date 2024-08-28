package demo_completablefuture.overview;

import demo_completablefuture.common.Demo;
import org.junit.Test;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

/**
 * 演示 CompletableFuture 的生命周期
 * create future
 * check future
 * complete future
 * get 		future
 * bulk     future
 *
 * 下面的代码示例演示如何使用这些方法来处理CompletableFuture类的生命周期。
 * 在第一个线程中创建不完整的未来。然后，同一个线程开始检查未来是否完成。
 * 在一个延迟之后，模拟一个长时间的操作，未来在第二个线程中完成。
 * 最后，第一个线程完成检查并读取已经完成的future值。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-22 11:28:27
 */
public class MethodsOfCompletableFutureLifecycle extends Demo {

	@Test
	public void test() throws InterruptedException, ExecutionException {
		ExecutorService executorService = Executors.newSingleThreadExecutor();

		CompletableFuture<String> future = new CompletableFuture<>(); // 创建一个 incomplete（不完整的）future

		executorService.submit(() -> {
			TimeUnit.SECONDS.sleep(1);
			future.complete("value"); // 完成 incomplete 的 future
			return null;
		});

		while (!future.isDone()) {
			TimeUnit.SECONDS.sleep(2); // 检查 future 是否已完成
		}

		String result = future.get(); // 从已完成的 future 中获取值
		logger.info("result: {}", result);

		executorService.shutdown();
	}
}
