/**********************************************************************************
 © Copyright Senzing, Inc. 2021
 The source code for this program is not published or otherwise divested
 of its trade secrets, irrespective of what has been deposited with the U.S.
 Copyright Office.
**********************************************************************************/

package com.senzing.g2.engine;

public class Result<T> 
{
	public void setValue(T value)
	{
		mValue = value;
	}
	
	public T getValue()
	{
		return mValue;
	}
	
	private T mValue;
}
