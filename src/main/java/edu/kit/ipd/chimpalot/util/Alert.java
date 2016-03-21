package edu.kit.ipd.chimpalot.util;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

public final class Alert {

	private static final ArrayList<String> requester = new ArrayList<>();
	private static final Email emailSender = new Email();
	
	private Alert() {
		
	}
	
	public static void registerRequester(String mailAdress) {
		if (!requester.contains(mailAdress)) {
			requester.add(mailAdress);
		}
	}
	
	public static void deregisterRequester(String mailAdress) {
		requester.remove(mailAdress);
	}
	public static List<String> getRegisteredMails() {
		return requester;
	}
	private static void notify(String experimentId, String subjectHeader, String textRaw) {
		String subject = "[Chimpalot] " + subjectHeader;
		String text = textRaw + " \n \n Chimpalot Team";
		Iterator<String>  reqIt = requester.iterator();
		while (reqIt.hasNext()) {
			emailSender.sendMail(reqIt.next(), subject, text);
		}
	}
	public static void notifyForDuplicates(String experimentId) {
		String header = "Experiment " + experimentId + "has duplicate entries";
		String text = "This email is a notification that the experiment " + experimentId + "has had recent duplicate submits.";
		notify(experimentId, header, text);
		
	}
	
	public static void notifyForTermination(String experimentId) {
		String subject = "Notification for experiment " + experimentId;
		String text = "This Email is a notification, that the experiment " + experimentId + " has terminated.";
		notify(experimentId, subject, text);
	}
	
	public static void notifyForNoTasks(String experimentId) {
		String subject = "Notification for experiment " + experimentId;
		String text = "This Email is a notification, that the experiment " + experimentId + " has had no tasks submitted recently.";
		notify(experimentId, subject, text);
	}

}
