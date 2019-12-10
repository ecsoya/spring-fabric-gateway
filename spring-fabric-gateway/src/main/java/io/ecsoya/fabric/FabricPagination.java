package io.ecsoya.fabric;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 
 * Query with Page.
 * 
 * @author ecsoya
 *
 */
@Data
public class FabricPagination<T> {

	/**
	 * 当前查询的列表数据
	 */
	private List<T> data = new ArrayList<>();

	/**
	 * 查询到的数量
	 */
	private int recordsCount;

	/**
	 * 查询到的断点
	 */
	private String bookmark = "";

	/**
	 * 每页大小
	 */
	private int pageSize = 10;

	/**
	 * Total records, before filtering (i.e. the total number of records in the
	 * database)
	 */
	private int recordsTotal;

	/**
	 * Total records, after filtering (i.e. the total number of records after
	 * filtering has been applied - not just the number of records being returned
	 * for this page of data).
	 */
	private int recordsFiltered;

	/**
	 * 当前页
	 */
	private int currentPage;

	public void updateTotalRecords() {
		int total = currentPage * pageSize + recordsCount;

		if (recordsCount < pageSize) {
			recordsTotal = total;
			recordsFiltered = total;
		} else {
			recordsTotal = total + 1;
			recordsFiltered = total + 1;
		}
	}

	public static <T> FabricPagination<T> create(FabricPaginationQuery<T> query) {
		FabricPagination<T> pagination = new FabricPagination<>();
		pagination.setBookmark(query.getBookmark());
		pagination.setPageSize(query.getPageSize());
		pagination.setCurrentPage(query.getCurrentPage());
		return pagination;
	}

}
