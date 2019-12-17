package io.github.ecsoya.fabric;

import lombok.Data;

/**
 * 
 * Page query metadata.
 * 
 * @see FabricPagination
 * @see FabricPaginationQuery
 * 
 * @author Jin Liu (jin.liu@soyatec.com)
 */
@Data
public class FabricQueryResponseMetadata {

	/**
	 * The total count of page query.
	 */
	private int recordsCount;

	/**
	 * The bookmark of page query.
	 */
	private String bookmark;
}
