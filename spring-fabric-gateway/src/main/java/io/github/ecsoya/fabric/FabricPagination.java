package io.github.ecsoya.fabric;

import java.util.ArrayList;
import java.util.List;

import lombok.Data;

/**
 * 
 * Query object with Page.
 * 
 * @author ecsoya
 *
 */
@Data
public class FabricPagination<T> {

	/**
	 * Results List
	 */
	private List<T> data = new ArrayList<>();

	/**
	 * Total counts of records.
	 */
	private int recordsCount;

	/**
	 * The bookmark of current query.
	 */
	private String bookmark = "";

	/**
	 * The count of each page.
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
	 * Current page index.
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
