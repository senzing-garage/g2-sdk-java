package com.senzing.g2.engine.plugin;

import java.util.Set;
import java.util.ArrayList;
import java.util.AbstractSet;
import java.util.Collection;
import java.util.Iterator;

/**
 * Provides a {@link Set} implementation that is backed by an {@link ArrayList}.
 * 
 */
public class ArraySet<T> extends AbstractSet<T> implements Set<T>
{
	/**
	 * The underlying {@link List} backing this instance.
	 */
	private ArrayList<T> list;
	
	/**
	 * Default constructor.
	 */
	public ArraySet() {
		list = new ArrayList<>();
	}
	
	/**
	 * Constructs a new instance with the specified initial capacity.
	 * @param initialCapacity The initial capacity of the set.
	 */
	public ArraySet(int initialCapacity) {
		list = new ArrayList<>(initialCapacity);
	}
	
	/**
	 * Constructs a new instance with the unique elements 
	 * in the specified array.
	 * @param elements The array of elements.
	 */
	public ArraySet(T[] elements) {
		this(elements.length);
		for (T elem: elements) {
			if (!list.contains(elem)) {
				list.add(elem);
			}
		}
	}
	
	/**
	 * Constructs a new instance with the unique elements
	 * in the specified {@link Collection}.
	 * @param elements The elements.
	 */
	public ArraySet(Collection<? extends T> elements) {
		this(elements.size());
		for (T elem: elements) {
			if (!list.contains(elem)) {
				list.add(elem);
			}
		}
	}

	@Override
	public int size() {
		return list.size();
	}
	
	@Override
	public Iterator<T> iterator() {
		return list.iterator();
	}
	
	@Override
	public boolean add(T elem) {
		if (list.contains(elem)) return false;
		return list.add(elem);
	}

	@Override
	public boolean contains(Object elem) {
		return list.contains(elem);
	}
	
	@Override
	public void clear() {
		list.clear();
	}
	
	@Override
	public boolean remove(Object elem) {
		return list.remove(elem);
	}
	
	@Override
	public boolean removeAll(Collection<?> c) {
		return list.removeAll(c);
	}
	
	@Override 
	public boolean retainAll(Collection<?> c) {
		return list.retainAll(c);
	}
	
	@Override
	public Object[] toArray() {
		return list.toArray();
	}
	
	@Override
	public <E> E[] toArray(E[] array) {
		return list.toArray(array);
	}
}
