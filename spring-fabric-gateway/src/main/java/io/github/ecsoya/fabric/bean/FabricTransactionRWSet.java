package io.github.ecsoya.fabric.bean;

import java.util.ArrayList;
import java.util.List;

import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset.KVRead;
import org.hyperledger.fabric.protos.ledger.rwset.kvrwset.KvRwset.KVWrite;

import lombok.Data;

@Data
public class FabricTransactionRWSet {

	private List<FabricTransactionRW> reads;

	private List<FabricTransactionRW> writes;

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
}
