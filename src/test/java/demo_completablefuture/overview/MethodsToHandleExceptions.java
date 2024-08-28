package demo_completablefuture.overview;

import demo_completablefuture.common.Demo;
import org.junit.Test;

import java.util.concurrent.CompletableFuture;

/**
 * 处理异常的方法
 *
 * 下面的代码示例演示了如何使用这些方法来处理异常。在执行阶段1时发生异常(除零)。跳过阶段2的执行，因为它的前一阶段异常完成。
 * whenComplete方法确定上一阶段异常完成，但不会从异常中恢复。阶段3的执行也被跳过，因为它的前一阶段仍然异常完成。
 * handle方法标识上一阶段异常完成，并用默认值替换异常。阶段4的执行最终正常执行。
 *
 * @author <a href="mailto:quanzhang875@gmail.com">quanzhang875</a>
 * @since  2024-08-22 10:32:56
 */
public class MethodsToHandleExceptions extends Demo {

	@Test
	public void test() {
		Integer result = CompletableFuture.supplyAsync(() -> 0)
				.thenApply(i -> { logger.info("stage 1: {}", i); return 1 / i; }) // executed and failed
				.thenApply(i -> { logger.info("stage 2: {}", i); return 1 / i; }) // skipped
				.whenComplete((value, t) -> {
					if (t == null) {
						logger.info("success: {}", value);
					} else {
						logger.warn("failure: {}", t.getMessage()); // executed
					}
				})
				.thenApply(i -> { logger.info("stage 3: {}", i); return 1 / i; }) // skipped
				.handle((value, t) -> {
					if (t == null) {
						return value + 1;
					} else {
						return -1; // executed and recovered
					}
				})
				.thenApply(i -> { logger.info("stage 4: {}", i); return 1 / i; }) // executed
				.join();

		System.out.println("result: " + result);
	}
}
