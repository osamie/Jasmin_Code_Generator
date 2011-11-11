
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintStream;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Stack;

/**
 * This visitor class generates Jasmin code for the parse tree it is visiting.
 * Each visit method appends Jasmin code to it's second argument (a List<String>)
 * and returns the type (BOOLEAN/NUMBER/STRING) of the value that the Jasmin
 * code leaves on the JVM stack.  If the code does not leave any value on the stack,
 * then NULL is returned.
 * 
 *
 */
public class CMMJasminVisitor implements CMMVisitor<Integer, List<String>> {

	// Types of data in our language
	static int BOOLEAN = 0;
	static int NUMBER = 1;
	static int STRING = 2;
	static int FUNCTION = 3;

	// Some conversion utilities
	static String[] t2a = { "i", "f", "a", "XXX" };
	static String[] t2A = { "I", "F", "Ljava/lang/String;", "XXX" };
	
	static String[] header = {
		"; Begin standard header",
		".class public $basename",
		".super java/lang/Object",
		".method public <init>()V",
		"  aload_0\n",
		"  invokenonvirtual java/lang/Object/<init>()V",
		"  return",
		".end method",
		".method public static main([Ljava/lang/String;)V",
		"  invokestatic $basename/main()F",
		"  return",
		".end method",
		"; End standard header" };
	
	
	static String[] trailer = {
		"; Begin standard trailer",
		"",
		".method public static print(F)F",
		"   .limit stack 2",
		"   .limit locals 1",
		"   getstatic java/lang/System/out Ljava/io/PrintStream;",
		"   fload 0",
		"   invokestatic java/lang/Float/toString(F)Ljava/lang/String;",
		"   invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V",
		"   fload 0",
		"   freturn",
		".end method",
		"",
		".method public static print(Ljava/lang/String;)Ljava/lang/String;",
		"   .limit stack 2",
		"   .limit locals 2",
		"   getstatic java/lang/System/out Ljava/io/PrintStream;",
		"   aload 0",
		"   invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V",
		"   aload 0",
		"   areturn",
		".end method",
		"",
		".method public static print(I)I",
		"   .limit stack 5",
		"   .limit locals 2",
		"   getstatic java/lang/System/out Ljava/io/PrintStream;",
		"   iload 0",
		"   ifeq false_label",
		"   ldc \"true\"",
		"   goto print_it",
		"false_label:",
		"   ldc \"false\"",
		"print_it:",
		"   invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V",
		"   iload 0",
		"   ireturn",
		".end method",
		"",
		"",
		".method public static println(F)F",
		"   .limit stack 2",
		"   .limit locals 1",
		"   getstatic java/lang/System/out Ljava/io/PrintStream;",
		"   fload 0",
		"   invokestatic java/lang/Float/toString(F)Ljava/lang/String;",
		"   invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V",
		"   fload 0",
		"   freturn",
		".end method",
		"",
		".method public static println(Ljava/lang/String;)Ljava/lang/String;",
		"   .limit stack 2",
		"   .limit locals 2",
		"   getstatic java/lang/System/out Ljava/io/PrintStream;",
		"   aload 0",
		"   invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V",
		"   aload 0",
		"   areturn",
		".end method",
		"",
		".method public static println(I)I",
		"   .limit stack 5",
		"   .limit locals 2",
		"   getstatic java/lang/System/out Ljava/io/PrintStream;",
		"   iload 0",
		"   ifeq false_label",
		"   ldc \"true\"",
		"   goto print_it",
		"false_label:",
		"   ldc \"false\"",
		"print_it:",
		"   invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V",
		"   iload 0",
		"   ireturn",
		".end method",
		"",
		".method public static pow(FF)F",
		"   .limit stack 5",
		"   .limit locals 2",
		"   fload 0",
		"   f2d",
		"   fload 1",
		"   f2d",
		"   invokestatic java/lang/Math/pow(DD)D",
		"   d2f",
		"   freturn",
		".end method",
		".method public static cos(F)F",
		"	.limit stack 5",
		"	.limit locals 2",
		"	fload 0",
		"	f2d",
		"	invokestatic java/lang/Math/cos(D)D",
		"	d2f",
		"	freturn",
		".end method",
		".method public static sin(F)F",
		"	.limit stack 5",
		"	.limit locals 2",
		"	fload 0",
		"	f2d",
		"	invokestatic java/lang/Math/sin(D)D",
		"	d2f",
		"	freturn",
		".end method",
		".method public static concat(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;",
		"	.limit stack 20",
		"	.limit locals 20",
		"	new java/lang/StringBuffer",
		"	dup",
		"	invokespecial java/lang/StringBuffer/<init>()V",
		"	aload 0",
		"	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;",
		"	aload 1",
		"	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;",
		"	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;",
		"	areturn",
		"",
		".end method",
		".method public static concat(Ljava/lang/String;I)Ljava/lang/String;",
		"	.limit stack 20",
		"	.limit locals 20",
		"	new java/lang/StringBuffer",
		"	dup",
		"	invokespecial java/lang/StringBuffer/<init>()V",
		"	aload 0",
		"	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;",
		"	iload 1",
		"   ifeq retFalse",
		"	;pop ",
		"	ldc \"true \"",
		"	goto end",
		"	retFalse:",
		"	;pop",
		"	ldc \"false \"",
		"	end:",
		"	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;",
		"	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;",
		"	areturn",
		"",
		".end method",
		".method public static concat(ILjava/lang/String;)Ljava/lang/String;",
		"	.limit stack 20",
		"	.limit locals 20",
		"	new java/lang/StringBuffer",
		"	dup",
		"	invokespecial java/lang/StringBuffer/<init>()V",
		"	iload 0",
		"	",
		"   ifeq retFalse",
		"	;pop ",
		"	ldc \"true \"",
		"	goto end",
		"	retFalse:",
		"	;pop",
		"	ldc \"false \"",
		"	end:",
		"	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;",
		"	aload 1",	
		"	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;",
		"	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;",
		"	areturn",
		
		
		/**
		"	aload 1",	
		"	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;",
		"	iload 0",
		"   ifeq retFalse",
		"	;pop ",
		"	ldc \"true \"",
		"	goto end",
		"	retFalse:",
		"	;pop",
		"	ldc \"false \"",
		"	end:",
		"	invokevirtual java/lang/StringBuffer/append(I)Ljava/lang/StringBuffer;",
		"	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;",
		**/
		
		"",
		".end method",
		"; End of standard trailer"	};

	protected class Data {
		int location;
		int type;
		String name;
		public Data(String name, int type, int location) {
			this.name = name;
			this.type = type;
			this.location = location;
			
		}
	}
	
	private int uniq;
	protected String getLabel() {
		return "label" + uniq++;
	}
	
	
	protected String basename;
	
	protected int s2t(String type) {
		if (type.equals("number_t"))
			return NUMBER;
		if (type.equals("string_t"))
			return STRING;
		if (type.equals("boolean_t"))
			return BOOLEAN;
		return -1;
	}
	
	protected class StackFrame {
		Map<String, Data> map;
		protected int offset;
		
		public StackFrame() {
			map = new HashMap<String, Data>();
			offset = 0;
		}

		public StackFrame(StackFrame parent) {
			this();
			offset = parent.offset;
		}

		public void addVariable(String name, int type) {
			Data data = new Data(name,type, offset++);
			map.put(name, data);
			
		    if (data.type == BOOLEAN){
		    	//output.add("  " + t2a[data.type] + "store " + data.location + "   ; " + id);
		    }
			//String id = n.getValue();
			//Data data = lookup(name);
			///if (data == null)
				//throw new RuntimeException("Assigning to undeclared variable " + id);
			//node.getChild(2).accept(this, output);
			
			//return data.type;
			
		}
		
		public void addPseudoVariable(String name, int type) {
			map.put(name, new Data(name, type, -1));			
		}
		
		public void addFunction(String sig, String fullName, int retType) {
			map.put(sig, new Data(fullName, retType, -1));
		}
		
		public Data lookup(String name) {
			return map.containsKey(name) ? map.get(name) : null;
		}
	}
	
	protected Stack<StackFrame> frames;
	
	
	protected  Data lookup(String name) {
		Data data;
		for (StackFrame f : frames) {
			if ((data = f.lookup(name)) != null) 
				return data;
		}
		return null;
	}
	
	public CMMJasminVisitor(String basename) {
		this.basename = basename;
		frames = new Stack<StackFrame>();
		frames.push(new StackFrame());
		frames.peek().addFunction("print(I)", "print(I)I", BOOLEAN);
		frames.peek().addFunction("print(F)", "print(F)F", NUMBER);
		frames.peek().addFunction("print(Ljava/lang/String;)", "print(Ljava/lang/String;)Ljava/lang/String;", STRING);
		frames.peek().addFunction("println(I)", "println(I)I", BOOLEAN);
		frames.peek().addFunction("println(F)", "println(F)F", NUMBER);
		frames.peek().addFunction("println(Ljava/lang/String;)", "println(Ljava/lang/String;)Ljava/lang/String;", STRING);
		frames.peek().addFunction("cos(F)","cos(F)F", NUMBER);
		frames.peek().addFunction("sin(F)","sin(F)F", NUMBER);
		frames.peek().addFunction("concat(Ljava/lang/String;Ljava/lang/String;)","concat(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;", STRING);
		frames.peek().addFunction("concat(Ljava/lang/String;I)","concat(Ljava/lang/String;I)Ljava/lang/String;", STRING);
		frames.peek().addFunction("concat(ILjava/lang/String;)","concat(ILjava/lang/String;)Ljava/lang/String;", STRING);
	}

	/**
	 * Never called
	 */
	public Integer visit(CMMASTNode node, List<String> output) {
		return null;
	}

	/**
	 * The root of the parse tree
	 */
	public Integer visit(CMMASTProgramNode node, List<String> output) {
		for (String s : header)
			output.add(s.replaceAll("\\$basename", basename));
		visitChildren(node, output);
		for (String s : trailer)
			output.add(s.replaceAll("\\$basename", basename));
		return null;
	}

	/**
	 * Extract a Jasmin method signature from a ParameterList node
	 */
	// ParameterList -> lparen (Parameter (listsep Parameter)*)? rparen
	// Parameter -> Type id
	protected String getSignature(CMMASTParameterListNode node) {
		String sig = "";
		for (int i = 1; i < node.numChildren()-1; i += 2) {
			int it = s2t(node.getChild(i).getChild(0).getChild(0).getName());
			sig += t2A[it];
		}
		return sig;
	}
	
	// FunctionDefinition -> Type id ParameterList Block
	public Integer visit(CMMASTFunctionDefinitionNode node, List<String> output) {
		String fname = node.getChild(1).getValue();
		String stype = node.getChild(0).getChild(0).getName();
		int itype = s2t(stype);
		// build method signature
		String sig = fname + "(" 
			+ getSignature((CMMASTParameterListNode)node.getChild(2)) + ")";
		String fullName = sig + t2A[itype];
		output.add("\n.method public static " + fullName);
		output.add(".limit stack 50");
		output.add(".limit locals 50");
		frames.peek().addFunction(sig, fullName, itype);
		frames.push(new StackFrame());
		frames.peek().addPseudoVariable("22retval", itype);
		node.getChild(2).accept(this, output);   // parameter list
		node.getChild(3).accept(this, output);   // block
		frames.pop();
		output.add(".end method");
		return null;
	}

	// Parameter -> Type id
	public Integer visit(CMMASTParameterNode node, List<String> output) {
		String name = node.getChild(1).getValue();
		String sType = node.getChild(0).getChild(0).getName();
		int type = s2t(sType);
		frames.peek().addVariable(name, type);
		return null;
	}


	public Integer visit(CMMASTLogicalNode node, List<String> output) {
		node.getChild(0).accept(this, output);
		for (int i = 1; i < node.numChildren(); i += 2) {
			node.getChild(i+1).accept(this, output);
			String op = node.getChild(i).getName();
			if (op.equals("and")) {
				output.add("  iand");
			} else if (op.equals("or")) {
				output.add("  ior");
			} else {
				throw new RuntimeException("Unknown operator:" + op);
			}
		}
		return BOOLEAN;
	}

	public Integer visit(CMMASTComparisonNode node, List<String> output) {
		node.getChild(0).accept(this, output);
		for (int i = 1; i < node.numChildren(); i += 2) {
			node.getChild(i+1).accept(this, output);
			String op = node.getChild(i).getName();
			String label1 = getLabel();
			String label2 = getLabel();
			output.add("  fcmpl");
			//if 
			output.add("  if" + op + " " + label1);
			output.add("  ldc 0");
			output.add("  goto " + label2);
			output.add(label1 + ":");
			output.add("  ldc 1");
			output.add(label2 + ":");
		}
		return BOOLEAN;
	}

	
	public Integer visitTEST(CMMASTSumNode node, List<String> output)
	{
		
		node.getChild(0).accept(this, output);
		
		int ret = NUMBER;
		for (int i = 1; i < node.numChildren(); i += 2) {
			
			
			
			String op = node.getChild(i).getName();
			if (op.equals("plus")) {
				
				

				String sType1 = node.getChild(i-1).getChild(0).getChild(0).getName();
				String sType2 = node.getChild(i+1).getChild(0).getChild(0).getName();
				
				
				
				int op1type = s2t(sType1); //t2A
				int op2type = s2t(sType2);
				
				if (op1type == NUMBER) throw new RuntimeException("helloo??");
				
				
				if((op1type == 2) || (op2type == 2)){
					ret = STRING;
				//B N S F
				switch (op1type){
					case 0: //boolean value
						String op1val = node.getChild(0).getChild(0).getChild(0).getValue();
						output.add("  pop");
						output.add("  ldc " + ((op1val == "true")? 1:0) );
						break;
						
					case 1: //number like a float
						output.add("  invokestatic java/lang/Float/toString(F)Ljava/lang/String");
						break;
					
					case 2: // string
						break;
						
					default: break;
						
					
				}
				
				switch (op2type){
				case 0: //boolean value
					//node.getChild(i+1).accept(this, output);
					String op2val = node.getChild(0).getChild(0).getChild(0).getValue();
					//output.add("  pop");
					output.add("  ldc " + ((op2val == "true")? 1:0) );
					break;
					
				case 1: //number like a float
					node.getChild(i+1).accept(this, output);
					output.add("  invokestatic java/lang/Float/toString(F)Ljava/lang/String");
					break;
				
				case 2: //string
					break;
					
				default: break;
				
				
				
				}//end switch2
				
				output.add("  invokestatic java/lang/String/concat(Ljava/lang/String)Ljava/lang/String");
				
			}// end if any string operands
			else output.add("  fadd");
		} else if (op.equals("minus")) {
			output.add("  fsub");
		} else {
			throw new RuntimeException("Unknown operator:" + op);
		}
				
		}
		
		//output.add("  invokevirtual java/lang/concat(Ljava/lang/String;)Ljava/lang/String;");
		return ret;
	}
	
	String c = 1 + 1 + " ";
	// Sum -> Term ((plus|minus) Term)*  [>1]
	public Integer visit(CMMASTSumNode node, List<String> output) {
		//Integer op1 = node.getChild(0).accept(this, output);
		List<String> tempOut = new ArrayList<String>();
		
		int o1 = node.getChild(0).accept(this, output);
		//int opr1 = node.getChild(0).accept(this, output);
		//node.getChild(0).accept(this, output);
		//int opr1 = s2t() 
		int ret = NUMBER;
		int stringConcatFlag = 0;
		for (int i = 1; i < node.numChildren(); i += 2) {
			
			String op = node.getChild(i).getName();
			//String opr =node.getChild(0).getChild(0).getChild(0).getName() ;
			//String op1 =node.getChild(i-1).getChild(0).getChild(0).getName() ;
			//String op2 =node.getChild(i+1).getChild(0).getChild(0).getName() ;
			
			int o2 = node.getChild(i+1).accept(this,tempOut);
			
			
			
			
			//int opr2 = node.getChild(i+1).accept(this, tempOut);
			if (op.equals("plus")) {
				//if ((o2 == STRING)&&(o1 == NUMBER))
						//throw new RuntimeException("we found lovve");
				//if ((op1.equals("string"))||(opr.equals("string"))||(op2.equals("string")||(stringConcatFlag == 1)))
				if((o1==STRING)||(o2 == STRING) || (stringConcatFlag == 1))
				{
					ret = STRING;
					
					//--if ((op2.equals("number"))&& (op1.equals("string")))
					if ((o2==NUMBER) && (o1==STRING))	
					{
						//--node.getChild(i+1).accept(this, tempOut); //putting op2 on temporary stack 
						tempOut.add("  invokestatic java/lang/Float/toString(F)Ljava/lang/String;");
						tempOut.add("  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;");
						//throw new RuntimeException("we found this!");
					}
					
					else if((stringConcatFlag==1)&&(o2==STRING))
					{
						tempOut.add("  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;");
					}
					
					
					//if ((op2.equals("boolean"))){
					else if((o2 == BOOLEAN) && (o1 == STRING)){
						tempOut.add("  invokestatic " + basename + "/concat(Ljava/lang/String;I)Ljava/lang/String;");//cross check this!
						
						
					}
					
					//if ((op2.equals("string"))&&(op1.equals("number"))){
					else if((o1 == NUMBER)&&(o2==STRING)){
						//tempOut.add("  invokestatic java/lang/Float/toString(F)Ljava/lang/String;");
						tempOut.add("  astore 3");
						tempOut.add("  invokestatic java/lang/Float/toString(F)Ljava/lang/String;");
						tempOut.add("  aload 3");
						tempOut.add("  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;");
						
						
					}
					else if((o1 == BOOLEAN)&&(o2==STRING)){
						tempOut.add("  invokestatic " + basename + "/concat(ILjava/lang/String;)Ljava/lang/String;");//cross check this!
						
					}
					
					else if ((stringConcatFlag==1)&&(o2!=STRING)){
						
						if (o2 == BOOLEAN){
							tempOut.add("  invokestatic " + basename + "/concat(Ljava/lang/String;I)Ljava/lang/String;");//cross check this!
							//throw new RuntimeException("CRAP!");
						}
						else if (o2 == NUMBER){
							tempOut.add("  invokestatic java/lang/Float/toString(F)Ljava/lang/String;");
							
							tempOut.add("  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;");
						}
						//throw new RuntimeException("CRAP!");
					}
					
					
					else if ((o1==STRING)&&(o2==STRING)){
							tempOut.add("  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String;");
						
					}
						
					stringConcatFlag = 1;
					
					
				}//end if string concatenation
				
				else {
					tempOut.add("  fadd");
					
				}
				
				
				
				
			}// end if plus
			else if (op.equals("minus")) {
				//node.getChild(i+1).accept(this, tempOut); //putting op2 on temporary stack
				tempOut.add("  fsub");
			} 
			else {
				throw new RuntimeException("Unknown operator:" + op);
			}
			
			
			
		}// end FOR LOOP
		
		output.addAll(tempOut);
		return ret;
	}
	
	/**
	 //if ((opr1Val.equals("string")) || (opr2Val.equals("string")))
				/**if((opr1 == STRING) )	
				{
					//if opr2 is a string then call static concat
					
					ret = STRING;
					
					if ((opr2 == STRING))
					{
						tempOut.add("  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String; " );
					}
					
					
					
					if((opr2 == BOOLEAN))
					{
						tempOut.add("  invokestatic " + basename + "/concat(Ljava/lang/String;I)Ljava/lang/String;");
						
					}
					
					
					if((opr2 == NUMBER))
					{
						
						tempOut.add("  invokestatic java/lang/Float/toString(F)Ljava/lang/String;");
						//output.add("	invokestatic java/lang/Integer/toString()Ljava/lang/String;");
						tempOut.add("  invokestatic " + basename + "/concat(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
					}
						
				 }
				else if ((opr2 == STRING))
				{
					ret = STRING;
					
					if((opr1 == BOOLEAN)  ){
						tempOut.add("  invokestatic " + basename + "/concat(ILjava/lang/String;)Ljava/lang/String;");
						
						
					}
					
					if((opr1 == NUMBER) ){
						tempOut.add("  astore 0");
						tempOut.add("  invokestatic java/lang/Float/toString(F)Ljava/lang/String;");
						tempOut.add("  aload 0");
						tempOut.add("  invokestatic " + basename + "/concat(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;");
						//throw new RuntimeException("found a number");
						//return STRING;
					}
					
					
					if((opr1 == STRING)){
					//else
						tempOut.add("  invokevirtual java/lang/String/concat(Ljava/lang/String;)Ljava/lang/String; " );
						
					}**/
					
					//output.add(tempOut.);
				//
				//} 
				//else {output.add("  fadd");}
				//output.addAll(tempOut);
	 
	

	public Integer visit(CMMASTTermNode node, List<String> output) {
		node.getChild(0).accept(this, output);
		for (int i = 1; i < node.numChildren(); i += 2) {
			node.getChild(i+1).accept(this, output);
			String op = node.getChild(i).getName();
			if (op.equals("multiply")) {
				output.add("  fmul");
			} else if (op.equals("divide")) {
				output.add("  fdiv");
			} else if (op.equals("mod")) {
				output.add("  frem");
			} else {
				throw new RuntimeException("Unknown operator:" + op);
			}
		}
		return NUMBER;
	}

	public Integer visit(CMMASTExpNode node, List<String> output) {
		int t1 = node.getChild(0).accept(this, output);
		
		int ret = NUMBER;
		
		 
		for (int i = 1; i < node.numChildren(); i += 2) {
			
			int t2 = node.getChild(i+1).accept(this, output);
			
			String op = node.getChild(i).getName();
			
			if (op.equals("exp")) {
				if ((t1 == BOOLEAN) && (t2 == BOOLEAN))
				{
					output.add("  ixor");
					ret = BOOLEAN;
					
					
				}
				else {
					output.add("  invokestatic " + basename + "/pow(FF)F");
				}
				
				
				
				
			} 
			
			
			
			else {
				throw new RuntimeException("Unknown operator:" + op);
			}
		}
		//java.lang.Math.
		return ret;
	}

	


	/**
	 * Recursively visit all the children of a node
	 * @param node
	 * @param output
	 */
	protected Integer visitChildren(CMMASTNode node, List<String> output) {
		Integer t = null;
		for (int i = 0; i < node.numChildren(); i++) {
			Integer t2 = node.getChild(i).accept(this, output);
			if (t2 != null) t = t2;
		}
		return t;
	}
	
	public Integer visit(CMMASTSimpleStatementNode node, List<String> output) {
		 //visitChildren(node, output);
		/**if (visitChildren(node, output) == null){
			
			
			return null;
		}**/
		return visitChildren(node, output);
	}

	public Integer visit(CMMASTConstantNode node, List<String> output) {
		return visitChildren(node, output);
	}


	public Integer visit(CMMASTParameterListNode node, List<String> output) {
		visitChildren(node, output);
		return null;
	}

	public Integer visit(CMMASTArgumentListNode node, List<String> output) {
		throw new RuntimeException("Internet error - evaluating argument list");
	}

	public Integer visit(CMMASTElementNode node, List<String> output) {
		return visitChildren(node, output);
	}

	public Integer visit(CMMASTExpressionListNode node, List<String> output) {
		visitChildren(node, output);
		return null;
	}

	public Integer visit(CMMASTElementPlusNode node, List<String> output) {
		if (node.numChildren() == 1) { // just an identifier
			return node.getChild(0).accept(this, output); 
		} else { // a function call
			String fname = node.getChild(0).getValue();
			CMMASTNode argList = node.getChild(1);
			String sig = "";
			for (int i = 1; i < argList.numChildren()-1; i += 2) {
				int t = argList.getChild(i).accept(this, output);
				sig += t2A[t];
			}
			fname += "(" + sig + ")";
			Data f = lookup(fname);
			if (f == null)
				throw new RuntimeException("Attempt to call non-existent function: " + fname);
			output.add("  invokestatic " + basename + "/" + f.name);
			// TODO: figure out the return type here
			return f.type;
		}
	}

	// WhileLoop -> while Condition Block
	public Integer visit(CMMASTWhileLoopNode node, List<String> output) {
		String labelTop = getLabel();
		String labelBottom = getLabel();
		output.add(labelTop + ":");
		node.getChild(1).accept(this, output);
		output.add("  ifeq " + labelBottom);
		node.getChild(2).accept(this, output);
		output.add("  goto " + labelTop);
		output.add(labelBottom + ":");
		return null;
	}

	public Integer visit(CMMASTConditionNode node, List<String> output) {
		Integer t = node.getChild(1).accept(this, output);
		if (t != BOOLEAN) 
			throw new RuntimeException("Condition not evaluating to boolean");
		return null;
	}
	
//          0    1     2        3     4
//DoLoop -> do Block while Condition eol
	public Integer visit(CMMASTDoLoopNode node, List<String> output) {
		
		String doLoop = getLabel();
		String doneDoLoop = getLabel();
		
		node.getChild(1).accept(this, output);
		
		//node.getChild(3).accept(this,output);
		//output.add(" ifeq " + doneDoLoop);
		output.add(doLoop + ":");
		node.getChild(1).accept(this, output);
		node.getChild(3).accept(this, output);
		
		output.add(" ifeq " + doneDoLoop );
		output.add(" goto " + doLoop);
		
		
		//write do label
		output.add(doneDoLoop + ":");
		
		
		//throw new UnsupportedOperationException("do-while loops not yet implemented");
		 return null;
	}

	public Integer visit(CMMASTReturnStatementNode node, List<String> output) {
		node.getChild(1).accept(this, output);
		Data r = lookup("22retval");
		output.add("  " + t2a[r.type] + "return");
		return null;
	}

	public Integer visit(CMMASTAssignmentNode node, List<String> output) {
		if (node.numChildren() == 1) {  // not really an assignment
			return visitChildren(node, output);
		} else {
			CMMASTNode n = node.getChild(0);  // Element
			if (!n.getName().equals("Element") || n.numChildren() != 1)
				throw new RuntimeException("Assigning to non-lvalue");
			n = n.getChild(0);   // ElementPlus
			if (!n.getName().equals("ElementPlus") || n.numChildren() != 1) 
				throw new RuntimeException("Assigning to non-lvalue");
			n = n.getChild(0);   // Token
			if (!n.getName().equals("id"))
				throw new RuntimeException("Assigning to non-lvalue");
			String id = n.getValue();
			Data data = lookup(id);
			if (data == null)
				throw new RuntimeException("Assigning to undeclared variable " + id);
			node.getChild(2).accept(this, output);
			if (!(node.getParent().getName().equals("SimpleStatement"))){
				//throw new RuntimeException("found redun!");
				output.add("  dup");
				output.add("  " + t2a[data.type] + "store " + data.location + "   ; " + id);
				return data.type;
			}
			output.add("  " + t2a[data.type] + "store " + data.location + "   ; " + id);
			return null;
				
			
		}
		
	}

	public Integer visit(CMMASTStatementNode node, List<String> output) {
		if (visitChildren(node, output) != null){
			//if (node.getChild(0).getChild(0).getValue() != null){
				output.add(  "pop");
			//}
			//output.add("  pop");
			
		}
			
		return null;
	}

	//@Override
	public Integer visit(CMMASTTypeNode node, List<String> output) {
		throw new RuntimeException("Internal error: visiting Type node");
	}
//IfStatement -> if Condition Block (elsif Condition Block)* (else Block)?
	public Integer visit(CMMASTIfStatementNode node, List<String> output) {
		/**
		String labelTop = getLabel();
		String labelBottom = getLabel();
		output.add(labelTop + ":");
		node.getChild(1).accept(this, output);
		output.add("  ifeq " + labelBottom);
		node.getChild(2).accept(this, output);
		output.add("  goto " + labelTop);
		output.add(labelBottom + ":");
		
		
		//iflabel:
		
		node.getChild(1).accept(this, output);
		ifeq iflabel
		   
		 
		**/
		
		String ifLabel = getLabel();
		//String elseLabel = getLabel();
		//String doneLabel = getLabel();
		String ifLabelEnd = getLabel();
		
		
		
		
		//CMMData cont = node.getChild(1).accept(this, data);
		
		//if (!(cont instanceof CMMBoolean)) {
			//throw new RuntimeException("Invalid (non-boolean) condition in if statement");
		//}
		
		
		//If child[1] is boolean true visit child[2],Block...return CMMData
		node.getChild(1).accept(this,output);
		output.add("ifeq " + ifLabelEnd);
		
		output.add(ifLabel + " : ");
		node.getChild(2).accept(this, output);
		
		
		output.add(ifLabelEnd + ":");
		//generate your elseifs (condition equals 0)
		/**if (node.numChildren()==3) return null; 
		int i = 3;
		
		if (node.getChild(i).getName().equals("elsif")){
			while(node.getChild(i).getName().equals("elsif")){
				String elseIfLabel = getLabel();
				String elseIfEnd = elseIfLabel + "End";
				node.getChild(i+1).accept(this, output);
				output.add(" ifeq " + elseIfEnd);
				output.add(elseIfLabel + " :");
				node.getChild(i+2).accept(this, output);
				output.add(" goto " + doneLabel);
				output.add(elseIfEnd + ":");
				i+=3;
			}
		}
		
		
		
		//else label
		output.add(elseLabel + ":");
		node.getChild(i+1).accept(this,output);
		output.add(" goto " + doneLabel);
		
		
		
		
		//done label:
		output.add(doneLabel + ":");
		
		//done here --end of jasmin script
		 */
		return null;
		
		
		
		
		
		//throw new UnsupportedOperationException("if statements are not yet implemented");
		// return null
	}

	// Declaration -> Type Identifier (listsep Identifier)* eol
	public Integer visit(CMMASTDeclarationNode node, List<String> output) {
		CMMASTNode type = node.getChild(0);
		String stype = type.getChild(0).getName();
		int itype = s2t(stype);
		for (int i = 1; i < node.numChildren(); i += 2)
		{
			frames.peek().addVariable(node.getChild(i).getValue(), itype);
			Data data = frames.peek().map.get(node.getChild(i).getValue());
			
			
			//No check to confirm datatype
			if (data.type==NUMBER) 
				output.add(" ldc 0.0");
			if (data.type == BOOLEAN)
				output.add(" ldc 0");
			if(data.type == STRING)
				output.add(" ldc \"  \" ");
			
			
			output.add("  " + t2a[data.type] + "store " + data.location + "   ; " + data.name);
		}
		return null;
	}

	public Integer visit(CMMASTBlockNode node, List<String> output) {
		frames.push(new StackFrame(frames.peek()));
		visitChildren(node, output);
		frames.pop();
		return null;
	}

	public Integer visit(CMMASTToken node, List<String> output) {
		if (node.getName().equals("number")) {
			output.add("  ldc " + Float.valueOf(node.getValue()));
			return NUMBER;
		} else if (node.getName().equals("string")) {
			output.add("  ldc " + node.getValue());
			return STRING;
		} else if (node.getName().equals("boolean")) {
			output.add("  ldc " + (node.getValue().equals("true") ? "1" : "0"));
			return BOOLEAN;
		} else if (node.getName().equals("id")) {
			Data data = lookup(node.getValue());
			output.add("  " + t2a[data.type] + "load " + data.location + "   ;" + node.getValue());
			return data.type;
		}
		return null;
	}

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		Reader r = null;
		String basename = "a";
		if (args.length == 0) {
			r = new InputStreamReader(System.in);
			basename = "a";
		} else {
			basename = args[0].replaceAll("\\.cmm$", "");
			try {
				r = new FileReader(args[0]);
			} catch (IOException e) {
				System.err.println("Error occurred while opening input file " + args[0]);
				System.err.println(e);
				System.exit(-1);
			}
		}
		String outfile = basename + ".j";     // TODO: handle path components better
		CMMTokenizer t = new CMMTokenizer(r);
		CMMParser p = new CMMParser(t);
		CMMASTNode n = null;
		System.out.print("Parsing...");
		System.out.flush();
		try {
			n = p.parse();
		} catch (CMMTokenizerException e) {
			System.err.println("A tokenizer exception occured:" + e);
			System.exit(-1);
		} catch (CMMParserException e) {
			System.err.println("A parse exception occured:" + e);
			System.exit(-1);
		}
		System.out.print("compiling...");
		System.out.flush();
		CMMJasminVisitor v = new CMMJasminVisitor(basename);
		List<String> output = new ArrayList<String>();
		n.accept(v, output);
		try {
			PrintStream os = new PrintStream(new FileOutputStream(outfile));
			for (String l : output) {
				os.println(l);
			}
		} catch (IOException e) {
			System.err.println(e);
		}
		System.out.println("done\nOutput written to " + outfile);
	}
    //                  0    1
	//NottedElement -> not Element
	//@Override
	public Integer visit(CMMASTNottedElementNode node, List<String> data) {
		//String ReturnTrue = getLabel();
		String ReturnTrue = getLabel();
		String done = getLabel();
		node.getChild(1).accept(this, data);
		data.add(" ifeq " + ReturnTrue );
		data.add(" ldc 0"); //if equals 1
		
		
		
		data.add(" goto " + done);
		
		
		
		data.add(ReturnTrue + ":");
		data.add(" ldc 1");
		data.add(" goto " + done );
		
		data.add(done + ":");
		
		
		return 0;
		
	}
}
