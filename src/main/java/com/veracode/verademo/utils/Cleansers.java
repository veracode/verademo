package com.veracode.verademo.utils;

import java.util.*;
import com.veracode.annotation.CRLFCleanser;

public class Cleansers {

	public static String cleanLog(String msg) {
		return msg.replaceAll("\n", "[newline]");
	}
}
