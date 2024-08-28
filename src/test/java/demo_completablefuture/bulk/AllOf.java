package demo_completablefuture.bulk;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.Arrays;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * allOf 用于等待一组 CompletableFuture 实例全部完成。这个方法返回一个新的 CompletableFuture，
 * 这个新的 CompletableFuture 完成的条件是所有传入的 CompletableFuture 实例都已完成（不论是正常完成还是异常完成）。
 *
 * 使用场景：
 * 1. 并行任务：当你有多个独立的异步任务需要并行执行，并且你需要在它们都完成后才进行下一步操作时，allOf 非常有用
 * 2. 聚合结果：虽然 allOf 不直接提供访问各个 CompletableFuture 的结果的方式，但可以在所有 future 完成后
 * 通过 thenApply，thenAccept 等方法来处理或聚合各个结果。
 *
 * 注意事项：
 * 1. allOf 返回的 CompletableFuture<Void> 只是告诉你任务是否完成，并不直接提供每个任务的结果。
 * 如果你需要结果，需要单独地从原来的 CompletableFuture 实例中获取。
 *
 * 2. 如果任何一个传入的 CompletableFuture 异常完成，那么通过 allOf 返回的 CompletableFuture 在调用 get()
 * 或 join() 时也会抛出异常。但它不会自动取消其他仍在执行的 CompletableFuture 实例。
 *
 *
 * 与 runAfterBoth 的区别：
 *
 * 1. 任务数量: allOf 可以等待任意数量的 CompletableFuture 完成，而 runAfterBoth 仅适用于两个。
 *
 * 2. 结果处理: allOf 不直接处理结果，你需要手动获取每个 CompletableFuture 的结果；
 * runAfterBoth 通常用于在两个任务完成后执行一些不依赖于这些任务结果的动作。
 *
 * 3. 适用性: allOf 适用于需要所有任务完成后统一处理的场景，
 * 而 runAfterBoth 适用于只有两个任务并且完成后只需执行一些如日志记录等附加操作的场景。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-27 20:46:42
 */
public class AllOf extends Demo {

	@Test
	public void testAllOf() throws ExecutionException, InterruptedException {
		List<CompletableFuture<String>> futures = Arrays.asList(
				CompletableFuture.supplyAsync(() -> sleepAndGet(1, "value1")),
				CompletableFuture.supplyAsync(() -> sleepAndGet(2, "value2")),
				CompletableFuture.supplyAsync(() -> sleepAndGet(3, "value3"))
		);

		CompletableFuture<Void> future = CompletableFuture.allOf(futures.toArray(new CompletableFuture[0]));

		future.get();

		String result = futures.stream()
				.map(CompletableFuture::join)
				.collect(Collectors.joining(" "));

		Assert.assertEquals("value1 value2 value3", result);
	}

	@Test
	public void testRunAfterBoth() throws ExecutionException, InterruptedException {
		CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> sleepAndGet(1, "value1"));
		CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> sleepAndGet(2, "value2"));

		CompletableFuture<Void> future = future1.runAfterBoth(future2, () -> {});

		future.get();

		String result = Stream.of(future1, future2)
				.map(CompletableFuture::join)
				.collect(Collectors.joining(" "));

		Assert.assertEquals("value1 value2", result);

	}
}
