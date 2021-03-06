package com.tuscanyscatours.launcher;

import java.util.logging.Logger;

import org.apache.tuscany.sca.Node;
import org.apache.tuscany.sca.TuscanyRuntime;
import org.apache.tuscany.sca.contribution.processor.ContributionReadException;
import org.apache.tuscany.sca.monitor.ValidationException;
import org.apache.tuscany.sca.node.ContributionLocationHelper;

import com.tuscanyscatours.bank.Bank;

import cn.edu.nju.conup.comm.api.manager.CommServerManager;
import cn.edu.nju.moon.conup.core.algorithm.VersionConsistencyImpl;
import cn.edu.nju.moon.conup.ext.lifecycle.CompLifecycleManager;
import cn.edu.nju.moon.conup.remote.services.impl.RemoteConfServiceImpl;
import cn.edu.nju.moon.conup.spi.manager.NodeManager;
import cn.edu.nju.moon.conup.spi.utils.DepRecorder;

public class BankLauncher {
	private static Logger LOGGER = Logger.getLogger(BankLauncher.class.getName());

	public static void main(String[] args) throws Exception {
			
		LOGGER.fine("Starting bank node...");
		String domainURI = "uri:default";
		TuscanyRuntime runtime = TuscanyRuntime.newInstance();
		Node node = runtime.createNode(domainURI);
		String contributionURL = ContributionLocationHelper
				.getContributionLocation(BankLauncher.class);
		node.installContribution(contributionURL);
		node.startComposite("fullapp-bank", "bank.composite");
		LOGGER.fine("bank.composite is ready!");
		
        NodeManager nodeMgr;
        nodeMgr = NodeManager.getInstance();
        
        nodeMgr.loadConupConf("Bank", "oldVersion");
        CompLifecycleManager.getInstance("Bank").setNode(node);
        CommServerManager.getInstance().start("Bank");
        
        //launch DepRecorder
        DepRecorder depRecorder;
        depRecorder = DepRecorder.getInstance();
        
        //access
//        updateBank(node);
	}
	
	private static void updateBank(Node node) throws Exception {
		LOGGER.fine("\nTry to access Bank#service-binding(Bank/Bank):");
		Bank bank0 = node.getService(Bank.class,
				"Bank#service-binding(Bank/Bank)");
		LOGGER.fine("Before update USD:GBP -->" + bank0.getExchangeRate("USD", "GBP"));

		testUpdate();
		Thread.sleep(1000);

		LOGGER.fine("\nTry to access Bank#service-binding(Bank/Bank):");
		Bank bank = node.getService(Bank.class,
				"Bank#service-binding(Bank/Bank)");
		LOGGER.fine("After Update USD:GBP -->" + bank.getExchangeRate("USD", "GBP"));
	}

	private static void testUpdate() {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				RemoteConfServiceImpl rcs =  new RemoteConfServiceImpl();
				String targetIdentifier1 = "Bank";
				int port1 = 22313;
				String baseDir1 = "/home/rgc";
				String classFilePath1 = "com.tuscanyscatours.bank.impl.BankImpl";
				String contributionUri1 = "fullapp-bank";
				String compsiteUri1 = "bank.composite";
				rcs.update("172.16.154.128", port1, targetIdentifier1, "CONSISTENCY", baseDir1, classFilePath1, contributionUri1, compsiteUri1);
			}
		});
		
		thread.start();
	}

}
