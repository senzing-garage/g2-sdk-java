package com.senzing.g2.engine.plugin;

import java.util.Objects;

/**
 * A simple pair/tuple class.
 * 
 * @param <T1> The type of the first value.
 * @param <T2> The type of the second value.
 */
public class Pair<T1, T2> {
	/**
	 * The first value.
	 */
	private T1 first = null;
	
	/**
	 * The second value.
	 */
	private T2 second = null;
	
	/**
	 * Default constructor.
	 */
	public Pair() {
		this.first 	= null;
		this.second = null;
	}
	
	/**
	 * Constructs with both values.
	 * 
	 * @param first The first value.
	 * @param second The second value.
	 */
	public Pair(T1 first, T2 second) {
		this.first 	= first;
		this.second = second;
	}
	
	/**
	 * Gets the first value of the pair.
	 * @return The first value of the pair.
	 */
	public T1 getFirst() {
		return first;
	}
	
	/**
	 * Sets the first value of the pair.
	 * @param value The first value of the pair.
	 */
	public void setFirst(T1 value) {
		first = value;
	}
	
	/**
	 * Gets the second value of the pair.
	 * @return The second value of the pair.
	 */
	public T2 getSecond() {
		return second;
	}
	
	/**
	 * Sets the second value.
	 * @param value The second value of the pair.
	 */
	public void setSecond(T2 value) {
		second = value;
	}
	
	@Override
	public int hashCode() {
		return Objects.hash(getFirst(), getSecond());
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj == null) return false;
		if (this == obj) return true;
		if (this.getClass() != obj.getClass()) return false;
		Pair<?,?> pair = (Pair<?,?>) obj;
		return Objects.equals(getFirst(),  pair.getFirst())
				&& Objects.equals(getSecond(), pair.getSecond());
	}
	
	@Override
	public String toString() {
		return ("{ first=[ " + getFirst() + " ], second=[ " + getSecond() + " ] }");
	}
}
