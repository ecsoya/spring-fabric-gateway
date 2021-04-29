package io.github.ecsoya.fabric.bean;

import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset.KVRead;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset.KVWrite;

import lombok.Data;

/**
 * Fabric transaction reads/writes set.
 * 
 * @author ecsoya
 *
 */
@Data
public class FabricTransactionRWSet {

	/**
	 * Read transactions.
	 */
	private List<FabricTransactionRW> reads = new ArrayList<FabricTransactionRW>();

	/**
	 * Write transactions.
	 */
	private List<FabricTransactionRW> writes = new ArrayList<FabricTransactionRW>();

	public void setReads(List<KVRead> readsList) {
		reads = new ArrayList<>();
		if (readsList != null) {
			for (int i = 0; i < readsList.size(); i++) {
				reads.add(FabricTransactionRW.fromRead(i, readsList.get(i)));
			}
		}
	}

	public void setWrites(List<KVWrite> writesList) {
		writes = new ArrayList<>();
		if (writesList != null && !writesList.isEmpty()) {
			for (int i = 0; i < writesList.size(); i++) {
				writes.add(FabricTransactionRW.fromWrite(i, writesList.get(i)));
			}
		}
	}

	public void addReads(List<KVRead> readsList) {
		if (readsList == null || readsList.isEmpty()) {
			return;
		}
		for (int i = 0; i < readsList.size(); i++) {
			FabricTransactionRW r = FabricTransactionRW.fromRead(reads.size(), readsList.get(i));
			if (r == null) {
				continue;
			}
			reads.add(r);
		}
	}

	public void addWrites(List<KVWrite> writesList) {
		if (writesList == null || writesList.isEmpty()) {
			return;
		}
		for (int i = 0; i < writesList.size(); i++) {
			FabricTransactionRW w = FabricTransactionRW.fromWrite(writes.size(), writesList.get(i));
			if (w == null) {
				continue;
			}
			writes.add(w);
		}
	}
}
