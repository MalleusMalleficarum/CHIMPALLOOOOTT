package edu.kit.ipd.chimpalot.jsonclasses;

import edu.kit.ipd.creativecrowd.persistentmodel.DatabaseException;
import edu.kit.ipd.creativecrowd.readablemodel.RatingOption;

public class RatingOptionJson implements RatingOption {
	
	private String iD;
	private float value;
	private String text;
	
	/**
	 * Empty constructor.
	 */
	public RatingOptionJson(){
	}
	
	/**
	 * Construct RatingOptionJson from existing RatingOption
	 * @param opt the RatingOption
	 * @throws DatabaseException if the SQL request fails.
	 */
	public RatingOptionJson(RatingOption opt) throws DatabaseException {
		this.setID(opt.getID());
		this.setValue(opt.getValue());
		this.setText(opt.getText());
	}

	/**
	 * @param iD the iD to set
	 */
	public void setID(String iD) {
		this.iD = iD;
	}

	/**
	 * @param value the value to set
	 */
	public void setValue(float value) {
		this.value = value;
	}

	/**
	 * @param text the text to set
	 */
	public void setText(String text) {
		this.text = text;
	}

	@Override
	public String getID() {
		return iD;
	}

	@Override
	public float getValue() throws DatabaseException {
		return value;
	}

	@Override
	public String getText() throws DatabaseException {
		return text;
	}

}
