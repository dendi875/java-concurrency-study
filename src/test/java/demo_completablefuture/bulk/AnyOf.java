package demo_completablefuture.bulk;

import demo_completablefuture.common.Demo;
import org.junit.Assert;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * anyOf 用于等待任意一个或多个 CompletableFuture 任务中的任何一个完成。当参数中的任何一个 CompletableFuture 完成时
 * （无论是正常完成还是异常完成），返回的 CompletableFuture 也会相应地完成。
 *
 * 注意事项：
 * 1. 返回类型为 CompletableFuture<Object>，这意味着你可能需要进行类型转换来获取实际的结果类型。
 * 2. 如果首个完成的 CompletableFuture 是异常完成的，通过 get() 获取结果时抛出 ExecutionException，你需要处理这个异常情况。
 * 3. anyOf 不会取消其它仍在进行的 CompletableFuture 实例。如果需要，你可能需要在获取结果后手动取消其他未完成的任务。
 *
 * 与 applyToEither 的关键区别：
 * 1. 功能目的
 * anyOf 可以处理任意数量的 CompletableFuture，并返回第一个完成的 CompletableFuture 的结果。
 * applyToEither 只处理两个 CompletableFuture，并允许你对首先完成的结果应用一个函数。
 *
 * 2. 返回类型
 * anyOf 返回的 CompletableFuture 是 CompletableFuture<Object> 类型，可能需要类型转换。
 * applyToEither 返回的 CompletableFuture 类型由函数 fn 的返回类型确定。
 *
 * 3. 结果处理
 * applyToEither 允许你直接在返回的 CompletableFuture 上应用一个函数来转换结果。
 * 而 anyOf 则需要你额外的步骤中处理结果。
 *
 * 4. 异常处理
 * 两者都会在首个完成的任务是异常完成时传递这个异常。
 *
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-28 14:52:25
 */
public class AnyOf extends Demo {

	@Test
	public void testAnyOf() throws ExecutionException, InterruptedException {
		CompletableFuture<Object> future = CompletableFuture.anyOf(
				CompletableFuture.supplyAsync(() -> sleepAndGet(1, "value1")),
				CompletableFuture.supplyAsync(() -> sleepAndGet(2, "value2")),
				CompletableFuture.supplyAsync(() -> sleepAndGet(3, "value3"))
		);

		Assert.assertEquals("value1", future.get());
	}

	@Test
	public void testApplyToEither() throws ExecutionException, InterruptedException {
		CompletableFuture<String> future1 = CompletableFuture.supplyAsync(() -> sleepAndGet(1, "value1"));
		CompletableFuture<String> future2 = CompletableFuture.supplyAsync(() -> sleepAndGet(2, "value2"));

		CompletableFuture<String> future = future1.applyToEither(future2, value -> value);

		Assert.assertEquals("value1", future.get());
	}
}
