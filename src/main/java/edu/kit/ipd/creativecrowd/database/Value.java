package edu.kit.ipd.creativecrowd.database;

/**
 * 
 * @author Philipp
 *
 */
public class Value {
  String strVal;
  Float fVal;
  Long lVal;

  private Value(String str, Float fl, Long in) {
    strVal = str;
    fVal = fl;
    lVal = in;
  }

  public static Value fromLong(long i) {
    return new Value(null, null, new Long(i));
  }
  
  public static Value fromInt(int i) {
	    return new Value(null, null, new Long(i));
  }

  public static Value fromFloat(float f) {
    return new Value(null, new Float(f), null);
  }

  public static Value fromString(String str) {
    return new Value(str, null, null);
  }
  

  public String asString() {
    return strVal;
  }

  public Float asFloat() {
    return fVal;
  }

  public Long asLong() {
    return lVal;
  }
  public Integer asInt() {
	Integer ret = null;
	if(lVal != null) {
		ret = lVal.intValue();
	}
	return ret;
  } 
  
  public String toString() {
		return asString() + " " + asFloat() + " " + asLong() + " " + asInt();
	}

}
