package org.freitas.customitems.data;


/**
 * This class a derivative of the Apache Commons Lang implementation '
 * of HashCodeBuilder.  It is used to create a specific hashCode for 
 * ItemStack objects to be able to keep track of them to be able to 
 * retrieve them from a HashMap at runtime.  
 *
 */
public class CustomItemHashCodeBuilder {

	private final int iConstant;
	private int iTotal = 0;

	public CustomItemHashCodeBuilder() {
		iConstant = 37;
		iTotal = 17;
	}

	public CustomItemHashCodeBuilder append(final byte value) {
		iTotal = iTotal * iConstant + value;
		return this;
	}

	public CustomItemHashCodeBuilder append(final int value) {
		iTotal = iTotal * iConstant + value;
		return this;
	}

	public CustomItemHashCodeBuilder append(final short value) {
		iTotal = iTotal * iConstant + value;
		return this;
	}

	public CustomItemHashCodeBuilder append(final String object) {
		if (object == null){
			return this;
		}
		iTotal = iTotal * iConstant + object.hashCode();;
		return this;
	}

	public int toHashCode() {
		return iTotal;
	}

	public int hashCode() {
		return toHashCode();
	}

}
