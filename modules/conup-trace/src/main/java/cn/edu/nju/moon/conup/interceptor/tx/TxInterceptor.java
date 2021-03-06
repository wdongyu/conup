package cn.edu.nju.moon.conup.interceptor.tx;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

import org.apache.tuscany.sca.assembly.Component;
import org.apache.tuscany.sca.assembly.Endpoint;
import org.apache.tuscany.sca.assembly.EndpointReference;
import org.apache.tuscany.sca.binding.jsonrpc.protocol.JsonRpc10Request;
import org.apache.tuscany.sca.interfacedef.Operation;
import org.apache.tuscany.sca.invocation.Message;
import org.apache.tuscany.sca.invocation.Phase;
import org.apache.tuscany.sca.policy.PolicySubject;

import cn.edu.nju.moon.conup.spi.datamodel.Interceptor;
import cn.edu.nju.moon.conup.spi.datamodel.InvocationContext;
import cn.edu.nju.moon.conup.spi.tx.TxDepMonitor;
import cn.edu.nju.moon.conup.spi.tx.TxLifecycleManager;
/**
 * TxInterceptor is used to transfer TxContext(root tx, parent tx, sub tx)
 * at refernece phase add TxContext to message body
 * at service phase retrieve TxContext from message body
 * @author Guochao Ren<rgc.nju.cs@gmail.com>
 *
 */
public class TxInterceptor extends Interceptor {

	private final static Logger LOGGER = Logger.getLogger(TxInterceptor.class.getName());

	private static String HOSTIDENTIFIER = "HostIdentifier";
	public static final String INVOCATION_CONTEXT = "INVOCATION_CONTEXT";
	
	private PolicySubject subject;
	private Operation operation;
	private String phase;
	private TxDepMonitor txDepMonitor;
	private TxLifecycleManager txLifecycleMgr;
	
	public TxInterceptor(PolicySubject subject, Operation operation, String phase, TxDepMonitor txDepMonitor, TxLifecycleManager txLifecycleMgr){
		this.subject = subject;
		this.operation = operation;
		this.phase = phase;
		this.txDepMonitor = txDepMonitor;
		this.txLifecycleMgr = txLifecycleMgr;
	}


	@Override
	public Message invoke(Message msg) {
		InvocationContext invocationContext = null;
		
		invocationContext = getInvocationCtxFromMsgHeader(msg.getHeaders());
		String hostComponent = getComponent().getName();
		
		if (phase.equals(Phase.SERVICE_POLICY)) {
			msg = traceServicePhase(msg, invocationContext, hostComponent);
		} else if (phase.equals(Phase.REFERENCE_POLICY)) {
			msg = traceReferencePhase(msg, hostComponent, getTargetServiceName(), txDepMonitor);
		} // else if(reference.policy)

		if(phase.equals(Phase.REFERENCE_POLICY)
				|| phase.equals(Phase.SERVICE_POLICY)){
	
			Map<String, Object>  msgHeaders = msg.getHeaders();
			Object invocationCtx = msgHeaders.get(TxInterceptor.INVOCATION_CONTEXT);
			
			LOGGER.fine("\n\t\tinvocationCtxStr:" + invocationCtx.toString());
		}
		return msg;
	}
	
	private InvocationContext getInvocationCtxFromMsgHeader(Map<String, Object> headers) {
		InvocationContext invocationCtx = null;
		Object object = headers.get("RequestMessage");
		if(object instanceof JsonRpc10Request){
			JsonRpc10Request jsonRpc10Request = (JsonRpc10Request)object;
			String invocationCtxStr = jsonRpc10Request.getInvocationCtx();
			invocationCtx = InvocationContext.getInvocationCtx(invocationCtxStr);
		}
		return invocationCtx;
	}


	private String getTargetServiceName(){
		String serviceName = null;
		serviceName = operation.getInterface().toString();	
		return serviceName;
	}
	
	private Component getComponent(){
		if (subject instanceof Endpoint) {
			Endpoint endpoint = (Endpoint) subject;
			return endpoint.getComponent();
		} else if (subject instanceof EndpointReference) {
			EndpointReference endpointReference = (EndpointReference) subject;
			return endpointReference.getComponent();
		} else if (subject instanceof Component) {
			Component component = (Component) subject;
			return component;
		}
		return null;
		
	}
	
	public Message traceServicePhase(Message msg, InvocationContext invocationContext,
			String hostComponent) {
		
		if(invocationContext ==null || invocationContext.toString().equals("")){
			LOGGER.warning("Error: message body cannot be null in a service body");
			// current tx is root Tx
//			invocationContext = new InvocationContext(null, null, null, null, null, null, null);
		}
		
		LOGGER.fine("trace SERVICE_POLICY : " + invocationContext);
		List<Object> originalMsgBody = Arrays.asList((Object [])msg.getBody());
		List<Object> msgBody = new ArrayList<Object>();
		msgBody.addAll(originalMsgBody);
		Map<String, Object> headers = msg.getHeaders();
		if(invocationContext != null){
			headers.put(INVOCATION_CONTEXT, invocationContext);
		}
		
		txLifecycleMgr.resolveInvocationContext(invocationContext, hostComponent);
		
		String hostInfo = HOSTIDENTIFIER + "," + hostComponent;
		msgBody.add(hostInfo);
		msg.setBody((Object [])msgBody.toArray());
		return msg;
	}

	public Message traceReferencePhase(Message msg, String hostComponent, String serviceName, TxDepMonitor txDepMonitor) {
		InvocationContext invocationCtx = txLifecycleMgr.createInvocationCtx(hostComponent, serviceName ,txDepMonitor);
		msg.getHeaders().put(TxInterceptor.INVOCATION_CONTEXT, invocationCtx.toString());
		LOGGER.fine("trace REFERENCE_POLICY : " + invocationCtx);
		return msg;
	}
	
}