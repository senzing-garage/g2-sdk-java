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
