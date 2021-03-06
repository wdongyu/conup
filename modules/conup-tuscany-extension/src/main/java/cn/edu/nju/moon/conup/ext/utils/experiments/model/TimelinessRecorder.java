package cn.edu.nju.moon.conup.ext.utils.experiments.model;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.concurrent.ConcurrentHashMap;
import java.util.logging.Logger;


public class TimelinessRecorder {
	private static Map<Integer, Long> updateCostTime = new ConcurrentHashMap<Integer, Long>();
	private static int count = 0;
	private static final Logger LOGGER = Logger.getLogger(TimelinessRecorder.class.getName());
	
	public Map<Integer, Long> getUpdateCostTime() {
		return updateCostTime;
	}
	
	public List<Double> getAllUpdateCostTime(){
		List<Double> allUpdateCostTime = new ArrayList<Double>();
		Iterator<Entry<Integer, Long>> iter = updateCostTime.entrySet().iterator();
		while(iter.hasNext()){
			allUpdateCostTime.add(iter.next().getValue() / 1000000.0);
		}
		System.out.println(allUpdateCostTime);
		return allUpdateCostTime;
	}

	public void addUpdateCostTime(long time) {
		count ++;
		LOGGER.info("update is done:" + time);
		updateCostTime.put(count, time);
	}

	public double getTotalUpdateCostTime() {
		long totalTime = 0;
		Iterator<Entry<Integer, Long>> iterator;

		iterator = updateCostTime.entrySet().iterator();
		while (iterator.hasNext()) {
			Entry<Integer, Long> entry = iterator.next();
			totalTime += entry.getValue();
		}
		return totalTime / 1000000.0;
	}

	public double getAverageNormalCostTime() {
		int totalSize = updateCostTime.size();
		return getTotalUpdateCostTime() / totalSize;
	}

}
