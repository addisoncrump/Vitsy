package io.github.vtcakavsmoace.vitsy.data;

import java.util.Collections;

import io.github.vtcakavsmoace.vitsy.util.Direction;

public class Stack<E> extends java.util.Stack<E> {
	private static final long serialVersionUID = -2664983766744338488L;
	
	public synchronized void rotate(Direction direction) {
		this.rotate(direction, this.size());
	}
	
	public synchronized void rotate(Direction direction, int items) {
		int loc = this.size() - items;
		switch(direction) {
		case LEFT:
			E moved = this.elementAt(loc);
			this.removeElementAt(loc);
			this.push(moved);
			break;
		case RIGHT:
			this.add(loc, this.pop());
			break;
		}
	}
	
	public synchronized void reverse() {
		Collections.reverse(this);
	}

}
