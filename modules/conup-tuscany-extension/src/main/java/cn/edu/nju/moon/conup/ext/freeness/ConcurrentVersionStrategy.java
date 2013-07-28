/**
 * 
 */
package cn.edu.nju.moon.conup.ext.freeness;

import java.util.Set;
import java.util.logging.Logger;

import cn.edu.nju.moon.conup.ext.comp.manager.CompLifecycleManagerImpl;
import cn.edu.nju.moon.conup.spi.datamodel.FreenessStrategy;
import cn.edu.nju.moon.conup.spi.datamodel.TransactionContext;
import cn.edu.nju.moon.conup.spi.manager.DynamicDepManager;
import cn.edu.nju.moon.conup.spi.manager.NodeManager;
import cn.edu.nju.moon.conup.spi.update.CompLifeCycleManager;
import cn.edu.nju.moon.conup.spi.update.UpdateManager;

/**
 * Implementation of concurrent version strategy for achieving freeness
 * @author JiangWang<jiang.wang88@gmail.com>
 *
 */
public class ConcurrentVersionStrategy implements FreenessStrategy {
	/** represent concurrent version strategy */
	public final static String CONCURRENT_VERSION = "CONCURRENT_VERSION_FOR_FREENESS";
	
	private final static Logger LOGGER = Logger.getLogger(ConcurrentVersionStrategy.class.getName());
	
	public static Logger getLogger() {
		return LOGGER;
	}

	@Override
	public Class<?> achieveFreeness(String rootTxID, String rootComp, String parentComp,
			String curTxID, String hostComp) {
		NodeManager nodeMgr;
		nodeMgr = NodeManager.getInstance();
		CompLifeCycleManager compLifeCycleMgr = nodeMgr.getCompLifecycleManager(hostComp);
		UpdateManager updateMgr = nodeMgr.getUpdateManageer(hostComp);
		
		Set<String> algorithmOldVersionRootTxs;
		algorithmOldVersionRootTxs = updateMgr.getUpdateCtx().getAlgorithmOldRootTxs();
		synchronized(compLifeCycleMgr.getValidToFreeSyncMonitor()){
			if((algorithmOldVersionRootTxs!=null) 
					&& (algorithmOldVersionRootTxs.contains(rootTxID))){
				LOGGER.fine(rootTxID + " is dispatched to old version");
				return updateMgr.getUpdateCtx().getOldVerClass();
			}else{
				LOGGER.fine(rootTxID + " is dispatched to new version");
				return updateMgr.getUpdateCtx().getNewVerClass();
			}
		}
		
	}

	@Override
	public String getFreenessType() {
		return ConcurrentVersionStrategy.CONCURRENT_VERSION;
	}

	@Override
	public boolean isInterceptRequiredForFree(String rootTx, String compIdentifier, TransactionContext txCtx, boolean isUpdateRCVD) {
		return false;
	}

	@Override
	public boolean isReadyForUpdate(String hostComp) {
		NodeManager nodeMgr = NodeManager.getInstance();
		DynamicDepManager depManager = nodeMgr.getDynamicDepManager(hostComp);
//		CompLifecycleManager compLcMgr;
		UpdateManager updateMgr = nodeMgr.getUpdateManageer(hostComp);
		
		Set<String> oldVersionRootTxs;
//		compLcMgr = CompLifecycleManagerImpl.getInstance(hostComp);
		oldVersionRootTxs = updateMgr.getUpdateCtx().getAlgorithmOldRootTxs();
		LOGGER.fine("oldVersionRootTxs.size()=" + oldVersionRootTxs.size());
		LOGGER.fine("oldVersionRootTxs:\n" + oldVersionRootTxs);
//		return oldVersionRootTxs.size()==0;
		return depManager.isReadyForUpdate() || oldVersionRootTxs.size()==0;
	}

}
