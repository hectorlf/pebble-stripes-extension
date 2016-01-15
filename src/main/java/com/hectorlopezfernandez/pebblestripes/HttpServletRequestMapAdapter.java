package com.hectorlopezfernandez.pebblestripes;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

public class HttpServletRequestMapAdapter implements Map<String, Object> {

	private final Map<String, Object> localMap;
	private final HttpServletRequest delegate;

	public HttpServletRequestMapAdapter(HttpServletRequest request) {
		this.delegate = request;
		this.localMap = new HashMap<String,Object>();
	}

	public HttpServletRequestMapAdapter(HttpServletRequest request, int capacity) {
		this.delegate = request;
		this.localMap = new HashMap<String,Object>(capacity);
	}

	@Override
	public boolean containsKey(Object key) {
		if (localMap.containsKey(key)) return true;
		return (key != null && delegate.getAttribute(key.toString()) != null);
	}

	@Override
	public Object get(Object key) {
		if (localMap.containsKey(key)) return localMap.get(key);
		return key == null ? null : delegate.getAttribute(key.toString());
	}

	@Override
	public Object put(String key, Object value) {
		// only local map is ever modified
		return localMap.put(key, value);
	}

	@Override
	public void putAll(Map<? extends String, ? extends Object> m) {
		// only local map is ever modified
		localMap.putAll(m);
	}

	@Override
	public Object remove(Object key) {
		// only local map is ever modified
		return localMap.remove(key);
	}

	@Override
	public void clear() {
		// only local map is ever modified
		localMap.clear();
	}

	/* Unsupported operations */

	@Override
	public Collection<Object> values() {
		throw new UnsupportedOperationException();
	}

	@Override
	public int size() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<java.util.Map.Entry<String, Object>> entrySet() {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean containsValue(Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public boolean isEmpty() {
		throw new UnsupportedOperationException();
	}

	@Override
	public Set<String> keySet() {
		throw new UnsupportedOperationException();
	}

}