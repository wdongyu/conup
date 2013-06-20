package cn.edu.nju.moon.conup.spi.tx;

public interface TxLifecycleManager {

	/**
	 * create transaction id
	 * @return
	 */
	public String createID();

	/**
	 * create a temporary transaction id
	 * @return
	 */
	public String createFakeTxId();

	/**
	 * @param id the transaction id that needs to be destroyed
	 */
	public void destroyID(String id);

	/**
	 * @return total transactions that are running
	 */
	public int getTxs();
	
	/**
	 * the host component started a sub-transaction on a remote component
	 * @param subComp
	 * @param curComp
	 * @param rootTx
	 * @param parentTx
	 * @param subTx
	 * @return
	 */
	public boolean startRemoteSubTx(String subComp, String curComp, String rootTx,
			String parentTx, String subTx);
	
	/**
	 * a sub-transaction just ended and returned from a remote component 
	 * @param subComp
	 * @param curComp
	 * @param rootTx
	 * @param parentTx
	 * @param subTx
	 * @return
	 */
	public boolean endRemoteSubTx(String subComp, String curComp, String rootTx,
			String parentTx, String subTx);
	
	/**
	 * the host component is going to init a sub-transaction for another component.
	 * However, the sub-transaction has not truely been started.
	 * 
	 * @param hostComp
	 * @param fakeSubTx the fake tx id
	 * @param rootTx
	 * @param rootComp
	 * @param parentTx
	 * @param parentComp
	 * @return
	 */
	public boolean initLocalSubTx(String hostComp, String fakeSubTx, String rootTx, String rootComp, String parentTx, String parentComp);
	
	/**
	 * 
	 * @param hostComp
	 * @param fakeSubTx
	 * @return
	 */
	public boolean endLocalSubTx(String hostComp, String fakeSubTx);
	
	/**
	 * when a root tx ends, TxDepMonitor should be notified.
	 * Given a parentTxId, which means that only the root Txs associated with the parentTxId
	 * @param hostComp
	 * @param rootTxId
	 * @return
	 */
	public void rootTxEnd(String hostComp, String rootTxId);
	
	/**
	 * because every component type 1 to 1 correspondence to a TxLifecycleManager
	 * @return component identifier
	 */
	public String getCompIdentifier();

}