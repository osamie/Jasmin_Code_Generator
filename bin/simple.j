; Begin standard header
.class public simple
.super java/lang/Object
.method public <init>()V
  aload_0

  invokenonvirtual java/lang/Object/<init>()V
  return
.end method
.method public static main([Ljava/lang/String;)V
  invokestatic simple/main()F
  return
.end method
; End standard header

.method public static gt(FF)I
.limit stack 50
.limit locals 50
  fload 0   ;a
  fload 1   ;b
  fcmpl
  ireturn
.end method

.method public static count(F)F
.limit stack 50
.limit locals 50
 ldc 0.0
  fstore 1   ; i
  ldc 0.0
  fstore 1   ; i
label2:
  fload 1   ;i
  fload 0   ;n
  fcmpl
  ifge label3
  fload 1   ;i
  ldc 2.0
  invokestatic simple/pow(FF)F
  invokestatic simple/println(F)F
pop
  fload 1   ;i
  ldc 1.0
  fadd
  fstore 1   ; i
  goto label2
label3:
  fload 1   ;i
  freturn
.end method

.method public static numtester(FF)F
.limit stack 50
.limit locals 50
  fload 0   ;x
  invokestatic simple/println(F)F
pop
  fload 1   ;y
  invokestatic simple/println(F)F
pop
  fload 0   ;x
  fload 1   ;y
  fadd
  invokestatic simple/println(F)F
pop
  fload 0   ;x
  fload 1   ;y
  fsub
  invokestatic simple/println(F)F
pop
  fload 0   ;x
  fload 1   ;y
  fmul
  invokestatic simple/println(F)F
pop
  fload 0   ;x
  fload 1   ;y
  fdiv
  invokestatic simple/println(F)F
pop
  fload 0   ;x
  fload 1   ;y
  invokestatic simple/pow(FF)F
  invokestatic simple/println(F)F
pop
  fload 0   ;x
  fload 1   ;y
  frem
  invokestatic simple/println(F)F
pop
  fload 0   ;x
  freturn
.end method

.method public static booltester(II)I
.limit stack 50
.limit locals 50
  iload 0   ;x
  invokestatic simple/println(I)I
pop
  iload 1   ;y
  invokestatic simple/println(I)I
pop
  iload 0   ;x
  iload 1   ;y
  iand
  invokestatic simple/println(I)I
pop
  iload 0   ;x
  iload 1   ;y
  ior
  invokestatic simple/println(I)I
pop
  iload 0   ;x
  ireturn
.end method

.method public static inittest()I
.limit stack 50
.limit locals 50
 ldc 0
  istore 0   ; x
  iload 0   ;x
  invokestatic simple/println(I)I
pop
  ldc 0
  ireturn
.end method

.method public static nottest(I)I
.limit stack 50
.limit locals 50
  ldc 1
  ireturn
.end method

.method public static main()F
.limit stack 50
.limit locals 50
 ldc 0.0
  fstore 0   ; x
  ldc 1
  invokestatic simple/nottest(I)I
  invokestatic simple/println(I)I
pop
  fload 0   ;x
  freturn
.end method
; Begin standard trailer

.method public static print(F)F
   .limit stack 2
   .limit locals 1
   getstatic java/lang/System/out Ljava/io/PrintStream;
   fload 0
   invokestatic java/lang/Float/toString(F)Ljava/lang/String;
   invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
   fload 0
   freturn
.end method

.method public static print(Ljava/lang/String;)Ljava/lang/String;
   .limit stack 2
   .limit locals 2
   getstatic java/lang/System/out Ljava/io/PrintStream;
   aload 0
   invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
   aload 0
   areturn
.end method

.method public static print(I)I
   .limit stack 5
   .limit locals 2
   getstatic java/lang/System/out Ljava/io/PrintStream;
   iload 0
   ifeq false_label
   ldc "true"
   goto print_it
false_label:
   ldc "false"
print_it:
   invokevirtual java/io/PrintStream/print(Ljava/lang/String;)V
   iload 0
   ireturn
.end method


.method public static println(F)F
   .limit stack 2
   .limit locals 1
   getstatic java/lang/System/out Ljava/io/PrintStream;
   fload 0
   invokestatic java/lang/Float/toString(F)Ljava/lang/String;
   invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
   fload 0
   freturn
.end method

.method public static println(Ljava/lang/String;)Ljava/lang/String;
   .limit stack 2
   .limit locals 2
   getstatic java/lang/System/out Ljava/io/PrintStream;
   aload 0
   invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
   aload 0
   areturn
.end method

.method public static println(I)I
   .limit stack 5
   .limit locals 2
   getstatic java/lang/System/out Ljava/io/PrintStream;
   iload 0
   ifeq false_label
   ldc "true"
   goto print_it
false_label:
   ldc "false"
print_it:
   invokevirtual java/io/PrintStream/println(Ljava/lang/String;)V
   iload 0
   ireturn
.end method

.method public static pow(FF)F
   .limit stack 5
   .limit locals 2
   fload 0
   f2d
   fload 1
   f2d
   invokestatic java/lang/Math/pow(DD)D
   d2f
   freturn
.end method
.method public static cos(F)F
	.limit stack 5
	.limit locals 2
	fload 0
	f2d
	invokestatic java/lang/Math/cos(D)D
	d2f
	freturn
.end method
.method public static sin(F)F
	.limit stack 5
	.limit locals 2
	fload 0
	f2d
	invokestatic java/lang/Math/sin(D)D
	d2f
	freturn
.end method
.method public static concat(Ljava/lang/String;Ljava/lang/String;)Ljava/lang/String;
	.limit stack 20
	.limit locals 20
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	aload 0
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload 1
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	areturn

.end method
.method public static concat(Ljava/lang/String;I)Ljava/lang/String;
	.limit stack 20
	.limit locals 20
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	aload 0
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	iload 1
   ifeq retFalse
	;pop 
	ldc "true "
	goto end
	retFalse:
	;pop
	ldc "false "
	end:
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	areturn

.end method
.method public static concat(ILjava/lang/String;)Ljava/lang/String;
	.limit stack 20
	.limit locals 20
	new java/lang/StringBuffer
	dup
	invokespecial java/lang/StringBuffer/<init>()V
	iload 0
	
   ifeq retFalse
	;pop 
	ldc "true "
	goto end
	retFalse:
	;pop
	ldc "false "
	end:
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	aload 1
	invokevirtual java/lang/StringBuffer/append(Ljava/lang/String;)Ljava/lang/StringBuffer;
	invokevirtual java/lang/StringBuffer/toString()Ljava/lang/String;
	areturn

.end method
; End of standard trailer
