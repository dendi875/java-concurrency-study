package demo_completablefuture.get;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * getNow 提供一个非阻塞方式来获取 CompletableFuture 的结果。
 * 如果 CompletableFuture 已经完成，那么它会立即返回结果；如果尚未完成，则返回给定的默认值，而不是阻塞等待结果。
 *
 * 使用场景
 * 1. 非阻塞获取结果：当你需要立即了解 CompletableFuture 的状态，并根据这一状态做进一步的处理，但又不希望线程被阻塞。
 * 2. 默认值处理：在某些情况下，如果异步计算还没有结果，你可能需要一个合理的默认值来继续其他操作。
 * 3. 响应性系统：在高响应性系统中，阻塞操作可能会导致系统性能问题，使用 getNow 可以避免这种情况。
 *
 * 注意事项
 * 1. getNow 方法不会阻塞调用线程，这意味着如果 CompletableFuture 的计算仍在进行中，你将得到一个默认值。
 * 2. 使用 getNow 需要仔细考虑默认值的合理性和对业务逻辑的影响，确保在 CompletableFuture 未完成时返回的默认值是可接受的。
 * 3. 如果 CompletableFuture 完成时抛出了异常，getNow 不会抛出该异常，它只会返回结果或默认值。
 * 如果需要处理可能的异常，还需要额外的逻辑来检查 CompletableFuture 是否异常完成。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-27 20:35:33
 */
public class GetNow extends Demo {

	@Test
	public void getNow() {
		CompletableFuture<String> future = CompletableFuture.completedFuture("value");

		Assert.assertEquals("value", future.getNow("default"));
	}

	@Test
	public void getNowValueIfAbsent() {
		CompletableFuture<String> future = CompletableFuture.supplyAsync(() -> sleepAndGet("value"));

		Assert.assertEquals("default", future.getNow("default"));
		Assert.assertFalse(future.isDone());
	}
}
