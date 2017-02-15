package io.github.vtcakavsmoace.vitsy.test.data;

import io.github.vtcakavsmoace.vitsy.data.Stack;
import io.github.vtcakavsmoace.vitsy.util.Direction;

import static org.junit.Assert.assertEquals;
import static io.github.vtcakavsmoace.vitsy.util.Direction.*;

import org.junit.Before;
import org.junit.Test;

public class StackTest {
	final int[] compare = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	final Stack<Integer> stack = new Stack<Integer>();
	
	String description = "";

	@Before
	public void reset() {
		for (int i = 0; i < 10; i++) {
			compare[i] = i;
		}
		stack.empty();
		for (int a : compare) {
			stack.add(a);
		}
	}

	@Test
	public void testRotateSingleMember() {
		description = "Rotate single member.";
		for (Direction dir : new Direction[]{RIGHT, LEFT}) {
			stack.rotate(dir, 1);
			compareStackToArray(compare, 0);
		}
	}
	
	@Test
	public void testRotatePartialRight() {
		description = "Rotate partial stack right.";
		stack.rotate(RIGHT, 3);
		compareStackToArray(new int[]{0, 1, 2, 3, 4, 5, 6, 9, 7, 8}, 0);
		stack.rotate(RIGHT, 3);
		compareStackToArray(new int[]{0, 1, 2, 3, 4, 5, 6, 8, 9, 7}, 0);
		stack.rotate(RIGHT, 3);
		compareStackToArray(compare, 0);
	}
	
	@Test
	public void testRotatePartialLeft() {
		description = "Rotate partial stack left.";
		stack.rotate(LEFT, 3);
		compareStackToArray(new int[]{0, 1, 2, 3, 4, 5, 6, 8, 9, 7}, 0);
		stack.rotate(LEFT, 3);
		compareStackToArray(new int[]{0, 1, 2, 3, 4, 5, 6, 9, 7, 8}, 0);
		stack.rotate(LEFT, 3);
		compareStackToArray(compare, 0);
	}

	@Test
	public void testRotateFullRight() {
		description = "Rotate all members right.";
		int offset = 0;
		for (int i = 0; i < 10; i++) {
			stack.rotate(RIGHT);
			compareStackToArray(compare, --offset);
		}
	}
	
	@Test
	public void testRotateFullLeft() {
		description = "Rotate all members left.";
		int offset = 0;
		for (int i = 0; i < 10; i++) {
			stack.rotate(LEFT);
			compareStackToArray(compare, ++offset);
		}
	}
	
	@Test
	public void testReverse() {
		description = "Reverse all members.";
		int[] compare = new int[] { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 };
		stack.reverse();
		compareStackToArray(compare, 0);
	}

	private void compareStackToArray(int[] array, int offset) {
		for (int i = 0; i < stack.size(); i++) {
			assertEquals(description, array[(i + offset + array.length) % array.length], stack.elementAt(i).intValue());
		}
	}
}
