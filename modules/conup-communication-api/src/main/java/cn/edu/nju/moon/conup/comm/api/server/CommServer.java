package cn.edu.nju.moon.conup.comm.api.server;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.logging.Logger;

import org.apache.mina.core.service.IoAcceptor;
import org.apache.mina.filter.codec.ProtocolCodecFilter;
import org.apache.mina.filter.codec.serialization.ObjectSerializationCodecFactory;
import org.apache.mina.filter.logging.LogLevel;
import org.apache.mina.filter.logging.LoggingFilter;
import org.apache.mina.transport.socket.nio.NioSocketAcceptor;

/**
 * @author rgc
 */
public class CommServer {
	private static final Logger LOGGER = Logger.getLogger(CommServer.class
			.getName());
	IoAcceptor acceptor = null;
	private ServerIoHandler serverIOHandler = null;
	
	public ServerIoHandler getServerIOHandler() {
		return serverIOHandler;
	}

	public CommServer() {
	}

	public boolean start(String ip, int port) {
		acceptor = new NioSocketAcceptor();
		
		ObjectSerializationCodecFactory objSerialCodecFactory = new ObjectSerializationCodecFactory();
		objSerialCodecFactory.setEncoderMaxObjectSize(2048576);	//2MB
		objSerialCodecFactory.setDecoderMaxObjectSize(2048576);
		
		acceptor.getFilterChain().addLast("codec",
				new ProtocolCodecFilter(objSerialCodecFactory));
		
		LoggingFilter logFilter = new LoggingFilter();
		logFilter.setSessionClosedLogLevel(LogLevel.DEBUG);
		logFilter.setSessionCreatedLogLevel(LogLevel.DEBUG);
		logFilter.setSessionOpenedLogLevel(LogLevel.DEBUG);
		logFilter.setSessionIdleLogLevel(LogLevel.DEBUG);
		logFilter.setMessageSentLogLevel(LogLevel.DEBUG);
		logFilter.setMessageReceivedLogLevel(LogLevel.DEBUG);
		acceptor.getFilterChain().addLast("logger", logFilter);
		serverIOHandler = new ServerIoHandler();
		acceptor.setHandler(serverIOHandler);

		try {
			acceptor.bind(new InetSocketAddress(ip, port));
			LOGGER.info("component communication server(" + ip + ":" + port + ") start...");
		} catch (IOException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public boolean stop(){
		assert acceptor != null;
		if(acceptor != null){
			acceptor.unbind();
			acceptor.dispose(true);
			LOGGER.info("unbinding...");
			return true;
		}else{
			return false;
		}
	}
}
