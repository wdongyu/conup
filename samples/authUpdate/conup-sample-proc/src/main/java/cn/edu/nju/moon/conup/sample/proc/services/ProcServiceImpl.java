package cn.edu.nju.moon.conup.sample.proc.services;

import org.oasisopen.sca.annotation.Reference;
import org.oasisopen.sca.annotation.Service;

import cn.edu.nju.moon.conup.comm.api.remote.RemoteConfigTool;
import cn.edu.nju.moon.conup.spi.datamodel.ConupTransaction;
import cn.edu.nju.moon.conup.spi.datamodel.InterceptorCache;
import cn.edu.nju.moon.conup.spi.datamodel.RemoteConfigContext;
import cn.edu.nju.moon.conup.spi.datamodel.TransactionContext;
import cn.edu.nju.moon.conup.spi.utils.ExecutionRecorder;

@Service(ProcService.class)
public class ProcServiceImpl implements ProcService {
	private VerificationService verify;
	private DBService db;
	String COMP_VERSION = "version.1";
	public VerificationService getVerify() {
		return verify;
	}

	@Reference
	public void setVerify(VerificationService verify) {
		this.verify = verify;
	}

	public DBService getDb() {
		return db;
	}

	@Reference
	public void setDb(DBService db) {
		this.db = db;
	}

	@ConupTransaction
	public String process(String exeProc, String token, String data) {
//		String threadID = getThreadID();
//		InterceptorCache interceptorCache = InterceptorCache.getInstance("ProcComponent");
//		TransactionContext txContextInCache = interceptorCache.getTxCtx(threadID);
//		String rootTx = txContextInCache.getRootTx();
//		ExecutionRecorder exeRecorder;
//		exeRecorder = ExecutionRecorder.getInstance("ProcComponent");
////		exeRecorder.addAction(rootTx, exeProc);
////		exeRecorder.addAction(rootTx, "ProcComponent.process." + COMP_VERSION);
//		exeProc += "; ProcComponent.process." + COMP_VERSION;
//		
		exeProc = verify.verify(exeProc, token);
//		
		exeProc = db.dbOperation(exeProc);
//		
//		exeRecorder.addAction(rootTx, exeProc);
//		
//		return exeRecorder.getAction(rootTx);
		
		return exeProc + " " + token + " " + data;
		
		//test dynamic update
//		testUpdate();
//		try {
//			Thread.sleep(2000);
//		} catch (InterruptedException e) {
//			e.printStackTrace();
//		}

//		if (authResult) {
//
//			List<String> result = db.dbOperation(exeProc);
//			return result;
//		} else {
//			return null;
//		}
	}
	
	private void testUpdate() {
		Thread thread = new Thread(new Runnable() {
			
			@Override
			public void run() {
				RemoteConfigTool rcs =  new RemoteConfigTool();
				String targetIdentifier = "AuthComponent";
				int port = 18082;
				String baseDir = "/home/nju/deploy/sample/update";
				String classFilePath = "cn.edu.nju.moon.conup.sample.auth.services.AuthServiceImpl";
				String contributionUri = "conup-sample-auth";
				String compsiteUri = "auth.composite";
				
				String protocol = "CONSISTENCY";
				String ipAddress = "172.16.154.128";
				RemoteConfigContext rcc = new RemoteConfigContext(ipAddress,
						port, targetIdentifier, protocol, baseDir,
						classFilePath, contributionUri, null, compsiteUri);
				rcs.update(rcc);
				
			}
		});
		
		thread.start();
	}
	
	private String getThreadID(){
		return new Integer(Thread.currentThread().hashCode()).toString();
	}
}
