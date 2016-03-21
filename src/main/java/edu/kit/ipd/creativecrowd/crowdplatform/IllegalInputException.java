package edu.kit.ipd.creativecrowd.crowdplatform;

public class IllegalInputException extends Exception{
	private String message;
	private String corruptField;
	/**
	 * 
	 */
	private static final long serialVersionUID = -5339290921566468606L;
	
	public IllegalInputException(String message, String corruptField)
	{
		super(message);
		this.message=message;
		this.corruptField=corruptField;
	}
	
	public String getMessage()
	{
		return message;
	}
	public String getCorruptField()
	{
		return corruptField;
	}

}
