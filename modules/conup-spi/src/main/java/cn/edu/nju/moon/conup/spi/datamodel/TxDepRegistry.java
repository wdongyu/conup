package cn.edu.nju.moon.conup.spi.datamodel;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class TxDepRegistry {

	/** take txId as key */
	private Map<String, TxDep> txDeps;
	
	public TxDepRegistry(){
		txDeps = new ConcurrentHashMap<String, TxDep>();
	}
	
	public TxDep getLocalDep(String txId){
		return txDeps.get(txId);
	}
	
	public void addLocalDep(String txId, TxDep txDep){
		txDeps.put(txId, txDep);
	}
	
	public void removeLocalDep(String txId){
		if(contains(txId))
			txDeps.remove(txId);
	}
	
	public boolean contains(String txId){
		return txDeps.containsKey(txId);
	}
	
	public void updateLocalDep(String txId, TxDep txDep){
		txDeps.remove(txId);
		txDeps.put(txId, txDep);
	}

}