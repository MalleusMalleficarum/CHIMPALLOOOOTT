package edu.kit.ipd.creativecrowd.operations;

import java.util.ArrayList;
import java.util.Iterator;

import edu.kit.ipd.chimpalot.util.Email;

/**
 * TODO notifications, main that notificates
 * 
 * @author Pascal Gabriel
 *
 */
public class Alert {
	private ArrayList<String> requester;
	private Email emailSender;
	
	public Alert() {
		requester = new ArrayList<String>();
		emailSender = new Email();
	}
	
	public void registerRequester(String email) {
		if (!requester.contains(email)) {
			requester.add(email);
		}
	}
	
	public void deregisterRequester(String email) {
		requester.remove(email);
	}
	
	public void notifyForDuplicates() {
		String experimentId = null;
		String subject = "Notification for experiment " + experimentId;
		String text = "This Email is a notification, that the experiment " + experimentId + " has had duplicate submits recently.";
		Iterator<String>  reqIt = requester.iterator();
		while (reqIt.hasNext()) {
			emailSender.sendMail(reqIt.next(), subject, text);
		}
		//TODO muss irgendwie experimentID bekommen, ein alert pro experiment?
	}
	
	public void notifyForTermination() {
		String experimentId = null;
		String subject = "Notification for experiment " + experimentId;
		String text = "This Email is a notification, that the experiment " + experimentId + " has terminated.";
		Iterator<String>  reqIt = requester.iterator();
		while (reqIt.hasNext()) {
			emailSender.sendMail(reqIt.next(), subject, text);
		}
		//TODO s.o.
	}
	
	public void notifyForNoTasks() {
		String experimentId = null;
		String subject = "Notification for experiment " + experimentId;
		String text = "This Email is a notification, that the experiment " + experimentId + " has had no tasks submitted recently.";
		Iterator<String>  reqIt = requester.iterator();
		while (reqIt.hasNext()) {
			emailSender.sendMail(reqIt.next(), subject, text);
		}
		//TODO s.o.
	}
}
