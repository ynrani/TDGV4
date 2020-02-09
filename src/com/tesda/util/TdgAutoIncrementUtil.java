/*
 * Object Name : TdgAutoIncrementUtil.java
 * Modification Block
 * ------------------------------------------------------------------
 * S.No.	Name 			Date			Bug_Fix_No			Desc
 * ------------------------------------------------------------------
 * 	1.	  vkrish14		12:18:06 AM				Created
 * ------------------------------------------------------------------
 * Copyrights: 2015 Capgemini.com
 */
package com.tesda.util;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.apache.commons.lang.StringUtils;

/**
 * @author vkrish14
 *
 */
public class TdgAutoIncrementUtil extends TDGBaseUtil {

	public static List<String> generateAutoIncrementValues(String inputValue,
			long itotalRequired) {
		System.out.println("highest value"+inputValue);
		String regexForNumbers = "\\d+";
		List<String> lst = new ArrayList<String>();
		if (inputValue.matches(regexForNumbers)
				&& (!inputValue.startsWith("0") || "0"
						.equalsIgnoreCase(inputValue))) {
			int iPassedValue = Integer.parseInt(inputValue);
			lst.add(iPassedValue + "");
			for (int i = 1; i < itotalRequired; i++)
				lst.add(iPassedValue + i + "");
		} else {

			lst.add(inputValue);
			lst = recurrenceAlphabetCall(lst, itotalRequired - lst.size(), lst
					.get(0).length());
			Pattern regex = Pattern.compile("[_$&+,:;=?@#|-]");
			
			while (lst.size() != itotalRequired) {
				
				if(lst.get(lst.size() - 1).contains(".")||inputValue.contains("."))
				{
					/*float f=Float.parseFloat(lst.get(lst.size() - 1));  
					DecimalFormat value = new DecimalFormat("#.#");
					value.format(f);
					
					System.out.println("format"+f);
					float f1 = (float) (f+0.10);
					value.format(f1);
					System.out.println("format"+f1);
					String numberAsString = Float.toString(f1);
					System.out.println("format"+numberAsString);
					lst.add(numberAsString);*/
					
					
					/*code with substrings*/
					String[] result = lst.get(lst.size() - 1).split("[.]");
					
					 String afterDecimalString = "";
					 String beforeDecimalString = "";
				System.out.println(result[0]);
				System.out.println(result[1]);
				int beforeDecimal = Integer.parseInt(result[0]);
				int afterDecimal =	Integer.parseInt(result[1]);
				/*for(int i =0;i<result[0].length();i++)
				{*/
					
						if(afterDecimal!=9)
						{
							afterDecimal = afterDecimal+1;
							//break;
						}
						else{
							beforeDecimal =	beforeDecimal+1;
							afterDecimal = 0;
						}
					
					 beforeDecimalString = Integer.toString(beforeDecimal);
					 afterDecimalString = Integer.toString(afterDecimal);
				//}
				String strConcatenated = beforeDecimalString+"."+afterDecimalString ;
				
				lst.add(strConcatenated);
		
				}
				
				
				
				
				
				
				
				else if (lst.get(lst.size() - 1).toUpperCase().endsWith("Z")
						/*|| lst.get(lst.size() - 1).toUpperCase().endsWith("9")*/) {
					StringBuffer strBuffer = new StringBuffer(lst.get(lst
							.size() - 1));
					strBuffer.reverse();
					String strFinal = strBuffer.toString();
					StringBuffer strMidreplace = new StringBuffer();
					strBuffer = new StringBuffer("");
					for (int iSize = 0; iSize < strFinal.length(); iSize++) {
						Matcher matcher = regex.matcher(String.valueOf(strFinal
								.charAt(iSize)));
						if (matcher.find()) {
							strMidreplace.append(strFinal.charAt(iSize));
							strBuffer.append(strFinal.charAt(iSize));
						} else if ((int) strFinal.charAt(iSize) == 90) {
							strMidreplace.append(strFinal.charAt(iSize));
							strBuffer.append("A");// added A
						} else if ((int) strFinal.charAt(iSize) == 122) {
							strMidreplace.append(strFinal.charAt(iSize));
							strBuffer.append("a");
						} else {
							try {
								if (Integer.parseInt(String.valueOf(strFinal
										.charAt(iSize))) == 9) {

									strMidreplace
											.append(strFinal.charAt(iSize));
									strBuffer.append("0");
								} else {
									break;
								}
							} catch (NumberFormatException ne) {
								break;
							}

						}
					}
					System.out.println("printing values");
					System.out.println("checking h"+lst.get(lst.size() - 1).charAt(0));
					System.out.println("with int"+(int) (lst.get(lst.size() - 1).charAt(0)));
					int currentChar = (int) (lst.get(lst.size() - 1).charAt(0));
					if (lst.get(0).length() - strMidreplace.toString().length()
							- 1 > 0)
						currentChar = (int) (lst.get(lst.size() - 1).charAt(lst
								.get(0).length()
								- strMidreplace.toString().length() - 1)) + 1;
					System.out.println("adding in list:");
				System.out.println(lst.get(0).length()- strMidreplace.length() - 1 > 0 ? lst.get(0).length()- strMidreplace.length() - 1 : lst
						.get(0).length()
											- strMidreplace.length());
				System.out.println( (char) currentChar);
				System.out.println(strBuffer.reverse().toString());
				System.out.println(lst
							.get(lst.size() - 1)
							.substring(
									0,
									lst.get(0).length()
											- strMidreplace.length() - 1 > 0 ? lst
											.get(0).length()
											- strMidreplace.length() - 1 : lst
											.get(0).length()
											- strMidreplace.length())
							+ (char) currentChar
							+ strBuffer.reverse().toString());
				
				
			
			
					lst.add(lst
							.get(lst.size() - 1)
							.substring(
									0,
									lst.get(0).length()
											- strMidreplace.length() - 1 > 0 ? lst
											.get(0).length()
											- strMidreplace.length() - 1 : lst
											.get(0).length()
											- strMidreplace.length())
							+ (char) currentChar
							+ strBuffer.reverse().toString());
				} else {
					// break;
					lst = recurrenceAlphabetCall(lst,
							itotalRequired - lst.size(), lst
									.get(lst.size() - 1).length());
				}
			}
			System.out.println("string float"+lst);
		}
		System.out.println("list is"+lst);
		return lst;
	}

	public static List<String> recurrenceAlphabetCall(List<String> lst,
			long itotalRequired, int iLength) {
		String regex = "\\d+";
		int charVal = 0;
		if (String.valueOf(lst.get(lst.size() - 1).charAt(iLength - 1))
				.matches(regex)) {
			charVal = Integer.parseInt(String.valueOf(lst.get(lst.size() - 1)
					.charAt(iLength - 1)));
		} else
			charVal = (int) (lst.get(lst.size() - 1).charAt(iLength - 1));
		String str = lst.get(lst.size() - 1);
		while (charVal >= 65 && charVal < 90 && itotalRequired > 0) {
			charVal++;
			lst.add(str.substring(0, iLength - 1) + (char) (charVal));
			itotalRequired--;
		}

		while (charVal >= 65 + 32 && charVal < 90 + 32 && itotalRequired > 0) {
			charVal++;
			lst.add(str.substring(0, iLength - 1) + (char) (charVal));
			itotalRequired--;
		}
		while (charVal >= 0 && charVal < 9 && itotalRequired > 0) {
			charVal++;
			lst.add(str.substring(0, iLength - 1) + (charVal));
			itotalRequired--;
		}

		return lst;
	}

	public static void main(String[] args) {
		/*System.out.println(TdgAutoIncrementUtil.generateAutoIncrementValues(
				"000001", 100000));*/
		//System.out.println(generateSSN("", 11));
		System.out.println(new Date(System.currentTimeMillis()-24*60*60*1000));
		
	}

	public static List<String> generateSSN( long count) {
		List<String> listGeneratedSSN = new ArrayList<String>();
		long value = Long.parseLong(String.valueOf(new Random().nextInt(565)+100)+"010001");
		
		for(long i =0;i<count;i++)
			listGeneratedSSN.add(value+++"");
		return listGeneratedSSN;
	}
	
	public static String fixedLength(String strValue, int count) {
		StringBuffer strBuffer = new StringBuffer(strValue);
		//int iLength = count-strValue;
		if(StringUtils.isNotEmpty(strValue))
		for(int i=0;i<(count-strValue.length());i++)
			strBuffer.append(" ");
		return strBuffer.toString();
	}
}
