package io.github.ecsoya.fabric.bean;

import java.util.Date;

import lombok.Data;

/**
 * Fabric transaction info.
 * 
 * @author ecsoya
 *
 */
@Data
public class FabricTransaction {

	private int index;

	private String txId;

	private String type;

	private Date date;

	private String creator;

	private int validationCode;

	private String channel;
}
