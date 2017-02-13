package io.github.vtcakavsmoace.vitsy.data;

import java.util.Collections;

public class Stack<E> extends java.util.Stack<E> {
	private static final long serialVersionUID = -2664983766744338488L;
	
	public synchronized void rotate(boolean direction) {
		this.rotate(direction, this.size());
	}
	
	public synchronized void rotate(boolean direction, int items) {
		int loc = this.size() - items;
		if (direction) {
			this.add(loc, this.pop());
		} else {
			E moved = this.elementAt(loc);
			this.removeElementAt(loc);
			this.push(moved);
		}
	}
	
	public synchronized void reverse() {
		Collections.reverse(this);
	}

}
