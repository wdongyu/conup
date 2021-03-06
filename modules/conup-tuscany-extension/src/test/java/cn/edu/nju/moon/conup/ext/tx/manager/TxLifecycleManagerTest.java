package cn.edu.nju.moon.conup.ext.tx.manager;

import static org.junit.Assert.*;

import java.util.Set;
import java.util.UUID;
import org.junit.Before;
import org.junit.Test;
import cn.edu.nju.moon.conup.spi.datamodel.ComponentObject;
import cn.edu.nju.moon.conup.spi.datamodel.InterceptorCache;
import cn.edu.nju.moon.conup.spi.datamodel.TransactionContext;
import cn.edu.nju.moon.conup.spi.datamodel.TransactionRegistry;
import cn.edu.nju.moon.conup.spi.manager.NodeManager;

/**
 * @author rgc
 */
public class TxLifecycleManagerTest {
	TxLifecycleManagerImpl txLifecycleMgr = null;
	ComponentObject compObj = null;
	NodeManager nodeMgr = null;
	TransactionRegistry txRegistry = null;
	
	@Before
	public void setUp() throws Exception {
		String compIdentifier = null;
		String compVer = null;
		String algorithmConf = null;
		String freenessConf = null;
		compIdentifier = "AuthComponent";
		compVer = "1.1";
		algorithmConf = "";
		final String CONCURRENT_VERSION = "CONCURRENT_VERSION_FOR_FREENESS";
		freenessConf = CONCURRENT_VERSION;
		String JAVA_POJO_IMPL_TYPE = "JAVA_POJO";
		
		nodeMgr = NodeManager.getInstance();
		compObj = new ComponentObject(compIdentifier, compVer, algorithmConf, freenessConf, 
				null,null, JAVA_POJO_IMPL_TYPE);
		nodeMgr.addComponentObject(compObj.getIdentifier(), compObj);
		
		txLifecycleMgr = new TxLifecycleManagerImpl(compObj);
		txRegistry = txLifecycleMgr.getTxRegistry();
		
		if(txLifecycleMgr.getTxs().size() != 0){
//			Map<String, TransactionContext> txCtxs = txRegistry.getAllTxIds();
			Set<String> allTxIds = txRegistry.getAllTxIds();
			for(String txId : allTxIds){
				txRegistry.removeTransactionContext(txId);
			}
			
//			Iterator<Entry<String, TransactionContext>> iterator = txCtxs.entrySet().iterator();
//			while(iterator.hasNext()){
//				String key = iterator.next().getKey();
//				txLifecycleMgr.destroyID(key);
//			}
		}
		
	}

	@Test
	public void testCreateID() {
		String componentIdentifier = "AuthComponent";
//		txLifecycleMgr.addToAssociateTx(getThreadID(), componentIdentifier);
		InterceptorCache interceptorCache = InterceptorCache.getInstance(componentIdentifier);
		
		String hostComp = componentIdentifier;
		String parentComp = "PortalComponent";
		String rootComp = "PortalComponent";
		TransactionContext txContext = interceptorCache.getTxCtx(getThreadID());
		String rootTx = UUID.randomUUID().toString();
		
		if(txContext == null){
			// generate and init TransactionDependency
			txContext = new TransactionContext();
			txContext.setCurrentTx(null);
			txContext.setHostComponent(hostComp);
			txContext.setParentTx(rootTx);
			txContext.setParentComponent(parentComp);
			txContext.setRootTx(rootTx);
			txContext.setRootComponent(rootComp);
			txContext.setInvocationSequence("PortalComponent:a358ab2d-cf1a-4e7e-857a-d89c2db40102>ProcComponent:d130a1f8-657c-4791-8fae-f1cda8d2dd53");
			//add to InterceptorCacheImpl
			interceptorCache.addTxCtx(getThreadID(), txContext);
		} else{
			txContext.setHostComponent(hostComp);
		}
		
		String currentTxID = txLifecycleMgr.createID();
		assertEquals(currentTxID, txContext.getCurrentTx());
		txLifecycleMgr.destroyID(currentTxID);
	}

	private String getThreadID() {
		return new Integer(Thread.currentThread().hashCode()).toString();
	}

	@Test
	public void testDestroyID() {
		String currentTxID = UUID.randomUUID().toString();
//		txLifecycleMgr.TX_IDS.put(currentTxID, new TransactionContext());
		txRegistry.addTransactionContext(currentTxID, new TransactionContext());
		
		txLifecycleMgr.destroyID(currentTxID);
		assertTrue(!txRegistry.contains(currentTxID));
	}

	@Test
	public void testGetTxs() {
		String currentTxID = UUID.randomUUID().toString();
//		txLifecycleMgr.TX_IDS.put(currentTxID, new TransactionContext());
		txRegistry.addTransactionContext(currentTxID, new TransactionContext());
		
		assertEquals(1, txLifecycleMgr.getTxs().size());
	}

}
