package io.ecsoya.fabric.bean;

import java.util.Date;

import lombok.Data;

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
