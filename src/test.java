
public class test {
	/**
	 * 
	 * 
	 * //output.add("  pop");
					//output.add("  pop");
					//output.add("  astore ");
					
					//output.add("  ldc \"" +  opr1Val + "\"");
					
					//output.add("  ldc \"" +  opr2Val + "\"");
					//output.add("  astore 1 ");
					//output.add("  invokestatic " + basename + "/concat(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;" );
			
					//if ((opr1 == STRING)||(opr2 == STRING))
					//{
						//output.add("    astore 0");
						//output.add("    astore 1");
						
						/**
						output.add("	new java/lang/StringBuffer");
						output.add("	dup");
						output.add("	invokespecial java/lang/StringBuffer/<init>()V");
						output.add("	aload 0");
						//output.add("	swap");
						output.add("	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;");
						output.add("	aload 1");
						
						output.add("	invokevirtual java/lang/StringBuffer/append(I)Ljava/lang/StringBuffer;");
						output.add("	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;");
						**/
						//output.add("	ldc \" \" ");
						//output.add(" 	aload_0");
						
						
						//output.add(" 	invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;");
						//output.add(" 	aload_1");
						//output.add(" 	invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;");
						//ret = STRING;
												
					//}
					//if (opr1 == STRING)
					//{
						/**
						"	new java/lang/StringBuffer",
						"	dup",
						"	invokespecial java/lang/StringBuffer/<init>()V",
						//"	aload 0",
						"	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;",
						//"	iload 1",
						//"   ifeq retFalse",
						//"	;pop ",
						//"	ldc \"true \"",
						//"	goto end",
						//"	retFalse:",
						//"	;pop",
						//"	ldc \"false \"",
						//"	end:",
						"	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;",
						"	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;",
						
						//output.add("  swap");
						 * 
						 */
					//}
					//output.add("  invokestatic " + basename + "/concat(Ljava/lang/String;I)Ljava/lang/String;" );
					
					/**switch(opr2){
					case 0:
						output.add("  pop");
						output.add("  astore ");
						output.add("  ldc \"" +  opr2Val + "\"");
						output.add("  astore 1 ");
						output.add("  invokestatic " + basename + "/concat(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;" );
						
						
						
					}**/
					//throw new RuntimeException("FOUND 2 strings!");
					/**
					 * if opr1 == STRING
					 * 		switch(opr2):
					 * 			case(BOOLEAN):
					 * 				pop
					 * 				output(" ldc " + \" opr2val \")
					 * 				output.add("  invokestatic " + basename + "/concat(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;" );
					 * 				
					 * 			case(NUMBER): 
					 */
					//output.add("  invokestatic " + basename + "/concat(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;" );
					//ret = STRING;
					//output.add("  invokestatic " + basename + "/pow(FF)F");
	

}
