package com.senzing.g2.engine.plugin;


public class G2EngineContextJNI implements G2EngineContext
{
	// an internal engine handle, used to access engine resources
	private long engineHandle;

	// static initializer
	static
	{
		System.loadLibrary("G2");
	}

	// function to score strings
	public native int scoreStrings(StringScoringContext context);
	
	// function to retrieve system parameters
	public native int getSystemParameter(SystemParameterContext context);

}
