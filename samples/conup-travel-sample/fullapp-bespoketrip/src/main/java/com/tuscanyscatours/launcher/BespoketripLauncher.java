package com.tuscanyscatours.launcher;

import org.apache.tuscany.sca.Node;
import org.apache.tuscany.sca.TuscanyRuntime;
import org.apache.tuscany.sca.node.ContributionLocationHelper;
import org.oasisopen.sca.NoSuchServiceException;

import com.tuscanyscatours.common.Search;
import com.tuscanyscatours.common.TripLeg;

import cn.edu.nju.conup.comm.api.manager.CommServerManager;
import cn.edu.nju.moon.conup.ext.lifecycle.CompLifecycleManager;
import cn.edu.nju.moon.conup.remote.services.impl.RemoteConfServiceImpl;
import cn.edu.nju.moon.conup.spi.manager.NodeManager;
import cn.edu.nju.moon.conup.spi.utils.DepRecorder;

public class BespoketripLauncher {

	public static void main(String[] args) throws Exception{
		System.out.println("Starting bespoketrip node...");
		String domainURI = "uri:default";
		TuscanyRuntime runtime = TuscanyRuntime.newInstance();
		Node node = runtime.createNode(domainURI);
		String contributionURL = ContributionLocationHelper
				.getContributionLocation(BespoketripLauncher.class);
		node.installContribution(contributionURL);

		node.startComposite("fullapp-bespoketrip",
				"fullapp-bespoketrip.composite");
		System.out.println("fullapp-bespoketrip.composite is ready!");

		NodeManager nodeMgr;
		nodeMgr = NodeManager.getInstance();
		nodeMgr.loadConupConf("HotelPartner", "oldVersion");
		//nodeMgr.getDynamicDepManager("HotelPartner").ondemandSetupIsDone();
		CompLifecycleManager.getInstance("HotelPartner").setNode(node);
		CommServerManager.getInstance().start("HotelPartner");

		nodeMgr.loadConupConf("FlightPartner", "oldVersion");
//		nodeMgr.getDynamicDepManager("FlightPartner").ondemandSetupIsDone();
		CompLifecycleManager.getInstance("FlightPartner").setNode(node);
		CommServerManager.getInstance().start("FlightPartner");
		
		nodeMgr.loadConupConf("CarPartner", "oldVersion");
//		nodeMgr.getDynamicDepManager("CarPartner").ondemandSetupIsDone();
		CompLifecycleManager.getInstance("CarPartner").setNode(node);
		CommServerManager.getInstance().start("CarPartner");

		// launch DepRecorder
		DepRecorder depRecorder;
		depRecorder = DepRecorder.getInstance();

		// access
//		 accessServices(node);
	}
	
	public static void accessServices(Node node){
		try {
//			System.out.println("\nTry to update currency component");
//			testUpdate();
			
			TripLeg tripLeg = new TripLeg("", "", "FLR", "06/12/09", "06/12/09", "2");
			System.out.println("\nTry to access CarPartner#service-binding(Search/Search):");
			Search carSearch = node.getService(Search.class, "CarPartner#service-binding(Search/Search)");
			System.out.println("\t" + "carSearch.searchSynch(tripLeg)=" + carSearch.searchSynch(tripLeg));
//			System.out
//				.println("\nTry to access CarPartner#service-binding(Search/searchws):");
//			carSearch = node.getService(Search.class,
//					"CarPartner#service-binding(Search/searchws)");
//			System.out.println("\t" + "carSearch.searchSynch(tripLeg)=" + carSearch.searchSynch(tripLeg));

			System.out.println("\nTry to access HotelPartner#service-binding(Search/Search):");
            TripLeg flightTrip = new TripLeg("", "LGW", "FLR", "06/12/09", "06/12/09", "1");
			Search hotelSearch = node.getService(Search.class, "HotelPartner#service-binding(Search/Search)");
			System.out.println("\t" + "hotelSearch.searchSynch(tripLeg)=" + hotelSearch.searchSynch(flightTrip));

		} catch (NoSuchServiceException e) {
			e.printStackTrace();
		}
	}
	
	private static void testUpdate() {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				RemoteConfServiceImpl rcs =  new RemoteConfServiceImpl();
				String targetIdentifier = "CurrencyConverter";
				int port = 11230;
				String baseDir = "/home/stone/deploy/travleSample/currencyNew";
				String classFilePath = "com.tuscanyscatours.currencyconverter.impl.CurrencyConverterImpl";
				String contributionUri = "fullapp-currency";
				String compsiteUri = "fullapp-currency.composite";
				rcs.update("192.168.137.133", port, targetIdentifier, "CONSISTENCY", baseDir, classFilePath, contributionUri, compsiteUri);
				
			}
		});
		
		thread.start();
	}

}