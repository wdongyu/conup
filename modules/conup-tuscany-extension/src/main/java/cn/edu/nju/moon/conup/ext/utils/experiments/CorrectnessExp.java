package cn.edu.nju.moon.conup.ext.utils.experiments;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.logging.Logger;

import cn.edu.nju.moon.conup.ext.utils.experiments.model.ExpSetting;
import cn.edu.nju.moon.conup.ext.utils.experiments.utils.ExpXMLUtil;

/** 
 * 	@author JiangWang<jiang.wang88@gmail.com>
 *	
 */
public class CorrectnessExp {
	private Logger LOGGER = Logger.getLogger(DisruptionExp.class.getName());
	private static String absolutePath = null;
	private static String fileName = null;
	private static CorrectnessExp experiment = null;
	private static PrintWriter out = null;
	private ExpSetting expSetting;

	private CorrectnessExp() {
		ExpXMLUtil xmlUtil = new ExpXMLUtil();
		String tuscanyHomeLocation = xmlUtil.getTuscanyHome();
		String algorithm = xmlUtil.getAlgorithmConf();
		algorithm = algorithm.substring(0, algorithm.indexOf("_ALGORITHM"));
		String freenessStrategy = xmlUtil.getFreenessStrategy();
		freenessStrategy = freenessStrategy.substring(0, freenessStrategy.indexOf("_FOR_FREENESS"));
		expSetting = xmlUtil.getExpSetting();
		String targetComp = expSetting.getTargetComp();
		int rqstInterval = expSetting.getRqstInterval();
	
		absolutePath = tuscanyHomeLocation + "/samples/experiments-result/correctness/";
		
		fileName = algorithm + "_CorrectnessExp_{" + targetComp + "_" + rqstInterval + "}" + ".csv";
		LOGGER.fine("result file:" + fileName);
		try {
			File file = new File(absolutePath + fileName);
			out = new PrintWriter(new FileWriter(file), true);
			out.write("#" + this + "\n");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public static CorrectnessExp getInstance() {
		synchronized (DisruptionExp.class) {
			if (experiment == null) {
				experiment = new CorrectnessExp();
			}
		}
		return experiment;
	}
	
	public void writeToFile(String data) {
		synchronized (experiment) {
			LOGGER.fine("I'm writing: " + data);
			out.write(data);
			out.flush();
		}
	}

	public void close() {
		synchronized (experiment) {
			out.close();
		}
	}
	
	@Override
	public String toString() {
		return "Round, InconsistentReqs, DisruptedReqs";
	}

}
