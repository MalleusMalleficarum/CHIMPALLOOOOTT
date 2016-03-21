/**
 * 
 */
package edu.kit.ipd.creativecrowd.view;

import java.util.List;

/**
 * Wrapperklasse für Kalibrierungsfragen
 * @author Basti
 *
 */
public class CalibData{
	private String quest;
	private List<AnsData> ans;
	public CalibData(String quest, List<AnsData> ans){
		this.quest = quest;
		this.ans = ans;
	}
	public String getQuest() {return quest;}
	public List<AnsData> getAns(){return ans;};
    
}
