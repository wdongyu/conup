package cn.edu.nju.moon.conup.txpre;


import java.util.logging.Logger;



/**
 * 
 * 
 * @author Ping Su<njupsu@gmail.com>
 */
public class Event {
    private final static Logger LOGGER = Logger.getLogger(Event.class.getName());
	
	public static Logger getLogger() {
		return LOGGER;
	}
	
	private int head;	
	private int tail;
	private String event;

	public Event(int head, int tail, String event) {		
		super();
		this.head = head;
		this.tail = tail;
		this.event = event;		
		LOGGER.fine(head + "-" + event + "-" + tail);
	}

	/**
	 * @param head
	 * @param tail
	 * @param event
	 */
	public int getHead() {
		return head;
	}

	/**
	 * @param head
	 *            the head to set
	 */
	public void setHead(int head) {
		this.head = head;
	}

	/**
	 * @return the tail
	 */
	public int getTail() {
		return tail;
	}

	/**
	 * @param tail
	 *            the tail to set
	 */
	public void setTail(int tail) {
		this.tail = tail;
	}

	/**
	 * @return the event
	 */
	public String getEvent() {
		return event;
	}

	/**
	 * @param event
	 *            the event to set
	 */
	public void setEvent(String event) {
		this.event = event;
	}

	public String getPort() {
		if (event.contains("COM")) {
//			String comAndMethod = event.split("\\.")[1];
//			LOGGER.fine(event.split("\\.")[1]);
			return event.split("\\.")[1];
//			return comAndMethod.split(":")[0];
			
		}
		return null;

	}

}
