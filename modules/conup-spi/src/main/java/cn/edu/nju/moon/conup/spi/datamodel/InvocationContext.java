package cn.edu.nju.moon.conup.spi.datamodel;

import java.io.Serializable;

import cn.edu.nju.moon.conup.spi.exception.ConupException;
/**
 * 
 * @author Guochao Ren<rgc.nju.cs@gmail.com>
 * @version Created time: Jul 28, 2013 11:03:41 PM
 */
public class InvocationContext implements Serializable {

	private static final long serialVersionUID = 1L;
	private String rootTx;
	private String rootComp;
	private String parentTx;
	private String parentComp;
	private String subTx;
	private String subComp;
	// invokeSequence contains all the parent component tx information
	// for example: A -> B -> C -> D
	//					 |
	//					 -----> E
	// B will get a inovkeSequence("A:tx1Id")
	// C will get a inovkeSequence("A:tx1Id,B:tx2Id")
	// E will get a invokeSequence("A:tx1Id,B:tx2Id,E:tx5Id")
	/**
	 * @author Guochao Ren<rgc.nju.cs@gmail.com>
	 */
	private String invokeSequence;

	public InvocationContext(String rootTx, String rootComp, String parentTx,
			String parentComp, String subTx, String subComp, String invokeSequence) {
		super();
		this.rootTx = rootTx;
		this.rootComp = rootComp;
		this.parentTx = parentTx;
		this.parentComp = parentComp;
		this.subTx = subTx;
		this.subComp = subComp;
		this.invokeSequence = invokeSequence;
	}

	public InvocationContext() {
	}

	public String getRootTx() {
		return rootTx;
	}

	public void setRootTx(String rootTx) {
		this.rootTx = rootTx;
	}

	public String getRootComp() {
		return rootComp;
	}

	public void setRootComp(String rootComp) {
		this.rootComp = rootComp;
	}

	public String getParentTx() {
		return parentTx;
	}

	public void setParentTx(String parentTx) {
		this.parentTx = parentTx;
	}

	public String getParentComp() {
		return parentComp;
	}

	public void setParentComp(String parentComp) {
		this.parentComp = parentComp;
	}

	public String getSubTx() {
		return subTx;
	}

	@Override
	public String toString() {
		return "INVOCATION_CONTEXT[" + rootTx + ":" + rootComp + 
				"," + parentTx + ":" + parentComp + 
				"," + subTx + ":" + subComp +
				"," + invokeSequence +
				"]";
	}

	public void setSubTx(String subTx) {
		this.subTx = subTx;
	}

	public String getSubComp() {
		return subComp;
	}

	public void setSubComp(String subComp) {
		this.subComp = subComp;
	}

	public static long getSerialversionuid() {
		return serialVersionUID;
	}

	public static InvocationContext getInvocationCtx(String ctxString) {
		if(ctxString == null)
			return null;
		
		if(!ctxString.contains("INVOCATION_CONTEXT")){
			throw new ConupException("Invalid INVOCATION_CONTEXT String...");
		} else {
			
			String target = getTargetString(ctxString);
			String[] txInfos = target.split(","); 
			String rootInfo = txInfos[0];
			String parentInfo = txInfos[1];
			String subInfo = txInfos[2];
			String invokeSequence = txInfos[3];
			
			String[] rootInfos = rootInfo.split(":");
			String rootTx = rootInfos[0].equals("null") ? null : rootInfos[0];
			String rootComponent = rootInfos[1].equals("null") ? null : rootInfos[1];
			
			String[] parentInfos = parentInfo.split(":");
			String parentTx = parentInfos[0].equals("null") ? null : parentInfos[0];
			String parentComponent = parentInfos[1].equals("null") ? null : parentInfos[1];
			
			String[] subInfos = subInfo.split(":");
			String subTx = subInfos[0].equals("null") ? null : subInfos[0];
			String subComp = subInfos[1].equals("null") ? null : subInfos[1];
			return new InvocationContext(rootTx, rootComponent, parentTx, parentComponent, subTx, subComp, invokeSequence);
		}
		
	}
	
	/**
	 *  transaction context information is stored in the format: VcTransactionRootAndParentIdentifier[ROOT_ID,PARENT_ID].
	 * 
	 * @return ROOT_ID,PARENT_ID
	 * 
	 * */
	private static String getTargetString(String raw){
		if(raw == null){
			return null;
		}
		if(raw.startsWith("\"")){
			raw = raw.substring(1);
		}
		if(raw.endsWith("\"")){
			raw = raw.substring(0, raw.length()-1);
		}
		int index = raw.indexOf("INVOCATION_CONTEXT");
		int head = raw.substring(index).indexOf("[")+1;
		int tail = raw.substring(index).indexOf("]");
		return raw.substring(head, tail);
	}

	public String getInvokeSequence() {
		return invokeSequence;
	}

	public void setInvokeSequence(String invokeSequence) {
		this.invokeSequence = invokeSequence;
	}

}
