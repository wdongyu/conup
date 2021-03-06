package cn.edu.nju.moon.conup.ext.update;

import cn.edu.nju.moon.conup.ext.freeness.BlockingStrategy;
import cn.edu.nju.moon.conup.ext.freeness.ConcurrentVersionStrategy;
import cn.edu.nju.moon.conup.ext.freeness.WaitingStrategy;
import cn.edu.nju.moon.conup.spi.datamodel.FreenessStrategy;
import cn.edu.nju.moon.conup.spi.update.CompLifeCycleManager;
import cn.edu.nju.moon.conup.spi.update.ComponentUpdator;
import cn.edu.nju.moon.conup.spi.update.Transformer;

/**
 * There are many different implementation type for a component, different implementation may have different
 * strategies for dynamic update.
 * 
 * @author JiangWang<jiang.wang88@gmail.com>
 *
 */
public class UpdateFactory {
	/**
	 * 
	 * @param implType implementation type of the component
	 * @return
	 */
	public static ComponentUpdator createCompUpdator(String implType){
//		ServiceLoader<ComponentUpdator> updators = ServiceLoader.load(ComponentUpdator.class); 
//		for(ComponentUpdator updator : updators){
//			if(updator.getCompImplType().equals(implType)){
//				return updator;
//			}
//		}
//		return null;
		
		return new JavaCompUpdatorImpl();
	}
	
	/**
	 * 
	 * @return
	 */
	public static Transformer createTransformer(){
		
		return null;
	}
	
	/**
	 * Currently, we have three strategies for achieving freeness,
	 * this factory is responsible for creating a FreenessStrategy implementation 
	 * according to the user configuration 
	 * @param strategy freeness strategy
	 * @return FreenessStrategy
	 */
	public static FreenessStrategy createFreenessStrategy(String strategy, CompLifeCycleManager compLifeCycleMgr){
		if(strategy.equals(BlockingStrategy.BLOCKING)){
			return new BlockingStrategy(compLifeCycleMgr);
		} else if(strategy.equals(WaitingStrategy.WAITING)){
			return new WaitingStrategy(compLifeCycleMgr);
		} else if(strategy.equals(ConcurrentVersionStrategy.CONCURRENT_VERSION)){
			return new ConcurrentVersionStrategy(compLifeCycleMgr);
		} else{
			return null;
		}
	}

}
