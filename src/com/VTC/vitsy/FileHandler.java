package com.VTC.vitsy;

import java.util.ArrayList;

public interface FileHandler {
	ArrayList<String[]> getFileInstruct(ArrayList<String> filename, boolean codeOnly, boolean[] checkUses);
}
