package io.github.vtcakavsmoace.vitsy.test.data;

import io.github.vtcakavsmoace.vitsy.data.Stack;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;

public class StackTest {
	final int[] compare = new int[] { 0, 1, 2, 3, 4, 5, 6, 7, 8, 9 };
	final Stack<Integer> stack = new Stack<Integer>();

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
		System.out.println("Rotate single member:");
		for (boolean bool : new boolean[] { true, false }) {
			stack.rotate(bool, 1);
			compareStackToArray(compare, 0);
		}
	}

	@Test
	public void testRotateFull() {
		System.out.println("Rotate all members:");
		int offset = 0;
		for (int i = 0; i < 10; i++) {
			stack.rotate(false);
			compareStackToArray(compare, ++offset);
		}
		for (int i = 0; i < 10; i++) {
			stack.rotate(true);
			compareStackToArray(compare, --offset);
		}
	}
	
	@Test
	public void testReverse() {
		System.out.println("Reverse all members.");
		int[] compare = new int[] { 9, 8, 7, 6, 5, 4, 3, 2, 1, 0 };
		stack.reverse();
		compareStackToArray(compare, 0);
	}

	private void compareStackToArray(int[] array, int offset) {
		System.out.println(stack);
		for (int i = 0; i < stack.size(); i++) {
			assertEquals(array[(i + offset + array.length) % array.length], stack.elementAt(i).intValue());
		}
	}

}
