package cn.edu.nju.moon.conup.spi.datamodel;

import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import cn.edu.nju.moon.conup.spi.manager.NodeManager;
import cn.edu.nju.moon.conup.spi.tx.TxDepMonitor;

/**
 * For recording:
 * <ul>
 * 		<li>current transaction id</li>
 *  	<li>current transaction's host component</li>
 *  	<li>current transaction's status</li>
 *  	<li>current futureC and pastC</li>
 *  	<li>current transaction's sub-transactions</li>
 * 		<li>a transaction's root/parent transaction/components </li>
 * 		<li>
 * </ul>
 * 
 * @author Jiang Wang <jiang.wang88@gmail.com>
 * Modified By: Guochao Ren <rgc.nju.cs@gmail.com>
 * Modified Date: Feb 16, 2014 10:51:49 AM add getProxyRootTxId method
 * 
 */
public class TransactionContext {
	
	/** current transaction  */
	private String currentTx;
	/** current(host) component */
	private String hostComponent;
	/** root transaction */
	private String rootTx;
	/** root component */
	private String rootComponent;
	/** parent transaction */
	private String parentTx;
	/** parent component */
	private String parentComponent;
	/** TxEventType: 
	 * TransactionStart,TransactionEnd,	FirstRequestService,DependencesChanged
	 */
	private TxEventType eventType = null;
//	/** components that will never be used. */
//	private Set<String> pastComponents;
//	/** components that will be used later. */
//	private Set<String> futureComponents;
	/** it's used to identify whether a sub-tx is a fake transaction */
	private boolean isFakeTx = false;

	//	private Map<String, SubTransaction> subTxs;
	/** subTx's host components, it takes sub-tx id as the key */
	private Map<String, String> subTxHostComps;
	/** subTx's statuses, it takes sub-tx id as the key */
	private Map<String, TxEventType> subTxStatuses;
	
//	private TxDepMonitor txDepMonitor;
	
	private String invocationSequence;
	
	public String getInvocationSequence() {
		return invocationSequence;
	}

	public void setInvocationSequence(String invocationSequence) {
		this.invocationSequence = invocationSequence;
	}

	public TransactionContext(){
		if(subTxHostComps == null)
			subTxHostComps = new HashMap<String, String>();
		if(subTxStatuses == null)
			subTxStatuses = new HashMap<String, TxEventType>();
	}
	
	/**
	 * get proxy root tx id in specified scope
	 * @param scope
	 * @return proxy root tx id
	 */
	public String getProxyRootTxId(Scope scope){
		if(scope == null || !scope.isSpecifiedScope()){
			return rootTx;
		} else {
			String proxyRootTxId = calcProxyRootTxId(rootTx, hostComponent, scope);
			if(proxyRootTxId == null) {
				return rootTx;
			}
			
			return proxyRootTxId;
		}
	}
	
	private String calcProxyRootTxId(String rootTx, String hostComponent,
			Scope scope) {
		// 1. get invocation sequence
		// invocation sequence example:
		// PortalComponent:a358ab2d-cf1a-4e7e-857a-d89c2db40102>ProcComponent:d130a1f8-657c-4791-8fae-f1cda8d2dd53
		
		// 2. calc the proxy root tx id
		Set<String> proxyRootComps = scope.getRootComp(hostComponent);
		if(invocationSequence == null || invocationSequence.equals("null")){
			return currentTx;
		}
		
		if(scope.contains(invocationSequence.split(">")[0].split(":")[0]) && proxyRootComps.contains(hostComponent)){
			return rootTx;
		}
		
		for(String proxyRootComp : proxyRootComps){
			assert invocationSequence != null;
			if(invocationSequence.contains(proxyRootComp)){
				String[] enties = invocationSequence.split(">");
				for(String entry : enties){
					if(entry.contains(proxyRootComp)){
						return entry.split(":")[1];
					}
				}
			}
		}
//		System.out.println("proxyRootComps: " + proxyRootComps);
//		System.out.println("rootTx: " + rootTx);
//		System.out.println("hostComp: " + hostComponent);
		return currentTx;
	}

	/**
	 * @return root transaction ID
	 */
	public String getRootTx() {
		return rootTx;
	}

	/**
	 * @param rootTx root transaction ID
	 */
	public void setRootTx(String rootTx) {
		this.rootTx = rootTx;
	}

	/**
	 * @return root transaction's component object identifier
	 */
	public String getRootComponent() {
		return rootComponent;
	}

	/**
	 * @param rootComponent root transaction's component object identifier
	 */
	public void setRootComponent(String rootComponent) {
		this.rootComponent = rootComponent;
	}

	/**
	 * @return parent transaction ID
	 */
	public String getParentTx() {
		return parentTx;
	}

	/**
	 * @param parentTx parent transaction ID
	 */
	public void setParentTx(String parentTx) {
		this.parentTx = parentTx;
	}

	/**
	 * @return parent transaction's component object identifier
	 */
	public String getParentComponent() {
		return parentComponent;
	}

	/**
	 * @param parentComponent parent transaction's component object identifier
	 */
	public void setParentComponent(String parentComponent) {
		this.parentComponent = parentComponent;
	}


	/**
	 * @return current transaction ID
	 */
	public String getCurrentTx() {
		return currentTx;
	}

	/**
	 * @param currentTx current transaction ID
	 */
	public void setCurrentTx(String currentTx) {
		this.currentTx = currentTx;
	}

	/**
	 * @return the hostComponent
	 */
	public String getHostComponent() {
		return hostComponent;
	}

	/**
	 * @param hostComponent the hostComponent to set
	 */
	public void setHostComponent(String hostComponent) {
		this.hostComponent = hostComponent;
	}

//	public Set<String> getPastComponents() {
//		return pastComponents;
//	}
//
//	public void setPastComponents(Set<String> pastComponents) {
//		this.pastComponents = pastComponents;
//	}
//
//	public Set<String> getFutureComponents() {
//		return futureComponents;
//	}
//
//	public void setFutureComponents(Set<String> futureComponents) {
//		this.futureComponents = futureComponents;
//	}
	
	/**
	 * @return key is txID, value is compIdentifier
	 */
	public Map<String, String> getSubTxHostComps() {
		return subTxHostComps;
	}
	
	/**
	 * @return key is txID, value is tx status
	 */
	public Map<String, TxEventType> getSubTxStatuses() {
		return subTxStatuses;
	}

	public TxEventType getEventType() {
		return eventType;
	}

	public void setEventType(TxEventType eventType) {
		this.eventType = eventType;
	}
	
//	/**
//	 * Each tx has one TxDepMonitor
//	 * @return
//	 */
//	public TxDepMonitor getTxDepMonitor() {
//		return txDepMonitor;
//	}
//
//	/**
//	 * Each tx has one TxDepMonitor
//	 */
//	public void setTxDepMonitor(TxDepMonitor txDepMonitor) {
//		this.txDepMonitor = txDepMonitor;
//	}
	
	public boolean isFakeTx() {
		return isFakeTx;
	}

	public void setFakeTx(boolean isFakeTx) {
		this.isFakeTx = isFakeTx;
	}

	@Override
	public String toString() {
		String result = null;
		
		TxDepMonitor txDepMonitor = NodeManager.getInstance().getTxDepMonitor(hostComponent);
		TxDepRegistry txDepRegistry = txDepMonitor.getTxDepRegistry();
		result = "root:" + rootComponent + " " + rootTx + ", " +
				"parent:" + parentComponent + " " + parentTx +", " +
				"current:" + hostComponent + " " + currentTx + " " + eventType + ", " +
				"futureC:" + txDepRegistry.getLocalDep(currentTx).getFutureComponents() + 
				"pastC:" + txDepRegistry.getLocalDep(currentTx).getPastComponents() +
				"subTxs:";
		for (Entry<String, String> subHostComps : subTxHostComps.entrySet()) {
			result += "\n" + subHostComps.getKey() + " " + subHostComps.getValue() + " " + subTxStatuses.get(subHostComps.getKey());
		}
				
		return result;
	}

	
}
