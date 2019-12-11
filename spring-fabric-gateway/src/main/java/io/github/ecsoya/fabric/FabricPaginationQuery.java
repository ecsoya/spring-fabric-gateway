package io.github.ecsoya.fabric;

import io.github.ecsoya.fabric.utils.TypeResolver;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
@EqualsAndHashCode(callSuper = true)
public class FabricPaginationQuery<T> extends FabricQuery {

	/**
	 * 查询到的断点
	 */
	private String bookmark = "";

	/**
	 * 每页大小
	 */
	private int pageSize = 10;

	/**
	 * 当前页
	 */
	private int currentPage;

	@Override
	public String getType() {
		String type = super.getType();
		if (type == null) {
			Class<?> clazz = TypeResolver.resolveRawArgument(FabricPaginationQuery.class, getClass());
			if (clazz != null) {
				setType(clazz.getName());
			}
		}
		return super.getType();
	}

}
