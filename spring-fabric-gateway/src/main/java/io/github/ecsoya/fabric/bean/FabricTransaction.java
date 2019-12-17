package io.github.ecsoya.fabric.bean;

import java.util.Date;

import org.hyperledger.fabric.protos.peer.FabricTransaction.TxValidationCode;
import org.hyperledger.fabric.sdk.BlockInfo.EnvelopeInfo;
import org.hyperledger.fabric.sdk.BlockInfo.EnvelopeType;

import lombok.Data;

/**
 * Fabric transaction info. Wrapper class for {@link EnvelopeInfo}
 * 
 * @author ecsoya
 *
 */
@Data
public class FabricTransaction {

	private int index;

	/**
	 * Transaction id.
	 */
	private String txId;

	/**
	 * Transaction type, {@link EnvelopeType}
	 */
	private String type;

	/**
	 * Timestamp of transaction.
	 */
	private Date date;

	/**
	 * MSP if of the creator.
	 */
	private String creator;

	/**
	 * The validation code of this Transaction ({@link TxValidationCode}
	 */
	private int validationCode;

	/**
	 * The channel of transaction.
	 */
	private String channel;
}
