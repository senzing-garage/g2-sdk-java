package com.senzing.g2.engine.plugin;

import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.ArrayList;
import java.util.AbstractMap;
import java.util.AbstractSet;
import java.util.Iterator;

/**
 * Provides a {@link Map} implementation that is backed by an {@link ArrayList}.
 * 
 */
public class ArrayMap<K, V> extends AbstractMap<K, V> implements Map<K, V>
{
	/**
	 * The underlying {@link List} backing this instance.
	 */
	private ArrayList<Map.Entry<K,V>> entries;

	/**
	 * Internal representation of the entry set.
	 */
	private class EntrySet extends AbstractSet<Map.Entry<K, V>>
	{
		@Override
		public int size() {
			return ArrayMap.this.entries.size();
		}
		
		@Override
		public Iterator<Map.Entry<K, V>> iterator() {
			return ArrayMap.this.entries.iterator();
		}
	}
	
	/**
	 * Default constructor.
	 */
	public ArrayMap() {
		entries = new ArrayList<>();
	}
	
	/**
	 * Constructs a new instance with the specified initial capacity.
	 * @param initialCapacity The initial capacity of the map.
	 */
	public ArrayMap(int initialCapacity) {
		entries = new ArrayList<>(initialCapacity);
	}
	
	@Override
	public int size() {
		return entries.size();
	}
	
	@Override
	public Set<Map.Entry<K,V>> entrySet() {
		return new EntrySet();
	}
	
	/**
	 * Internal method to find the {@link Map.Entry} for the specified
	 * @param key
	 * @return
	 */
	private Map.Entry<K, V> findEntry(Object key) {
		for (Map.Entry<K, V> entry : entries) {
			if (Objects.equals(entry.getKey(), key)) return entry;
		}
		return null;
	}
	
	@Override
	public V put(K key, V value) {
		Map.Entry<K, V> entry = findEntry(key);
		if (entry == null) {
			entry = new SimpleEntry<>(key, value);
			entries.add(entry);
			return null;
		} else {
			return entry.setValue(value);
		}
	}

	@Override
	public boolean containsKey(Object key) {
		return findEntry(key) != null;
	}

	@Override
	public boolean containsValue(Object value) {
		for (Map.Entry<K,V> entry : entries) {
			if (Objects.equals(entry.getValue(), value)) return true;
		}
		return false;
	}
	
	@Override
	public void clear() {
		entries.clear();
	}
	
	@Override
	public V remove(Object key) {
		Iterator<Map.Entry<K,V>> iter = entries.iterator();
		while (iter.hasNext()) {
			Map.Entry<K, V> entry = iter.next();
			if (Objects.equals(entry.getKey(), key)) {
				iter.remove();
				return entry.getValue();
			}
		}
		return null;
	}	
}
