declare i32 @getchar()
declare i32 @putchar(i32)

define i32 @readInt() {
entry:
%res = alloca i32
%digit = alloca i32
store i32 0, i32* %res
br label %read
read:
%0 = call i32 @getchar()
%1 = sub i32 %0, 48
store i32 %1, i32* %digit
%2 = icmp ne i32 %0, 10
br i1 %2, label %save , label %exit
save:
%3 = load i32* %res
%4 = load i32* %digit
%5 = mul i32 %3, 10
%6 = add i32 %5, %4
store i32 %6, i32* %res
br label %read
exit:
%7 = load i32* %res
ret i32 %7
}

define void @printlnint(i32 %n) {
%digitInChar = alloca [32 x i32]
%number = alloca i32
store i32 %n, i32* %number
%numberOfDigits = alloca i32
store i32 0, i32* %numberOfDigits
br label %beginloop
beginloop:
%1 = load i32* %number
%2 = icmp ne i32 %1, 0
br i1 %2, label %ifloop, label %endloop
ifloop:
%temp1 = load i32* %numberOfDigits
%temp2 = load i32* %number
%divnum = udiv i32 %temp2, 10
%currentDigit = urem i32 %temp2, 10
%arrayElem = getelementptr [32 x i32]* %digitInChar, i32 0, i32 %temp1
store i32 %currentDigit, i32* %arrayElem
%temp3 = add i32 %temp1, 1
store i32 %temp3, i32* %numberOfDigits
store i32 %divnum, i32* %number
br label %beginloop
endloop:
%temp4 = load i32* %numberOfDigits
%temp41 = sub i32 %temp4, 1
store i32 %temp41, i32* %numberOfDigits
br label %beginloop2
beginloop2:
%temp5 = load i32* %numberOfDigits
%arrayElem2 = getelementptr [32 x i32]* %digitInChar, i32 0, i32 %temp5
%arrayElemValue = load i32* %arrayElem2
%arrayElemValue2 = add i32 %arrayElemValue, 48
call i32 @putchar(i32 %arrayElemValue2)
%temp6 = sub i32 %temp5, 1
store i32 %temp6, i32* %numberOfDigits
%temp7 = icmp sge i32 %temp6, 0
br i1 %temp7, label %beginloop2, label %endloop2
endloop2:
call i32 @putchar(i32 10)
ret void
}

define void @printlnbool(i1 %n) {
%temp1 = zext i1 %n to i32
call void @printlnint(i32 %temp1)
ret void
}

define i32 @main() {
%ternaryInt = alloca i32
%ternaryBool = alloca i1
%pow = alloca i32
%res = alloca i32
%var0 = alloca i32
store i32 1, i32* %var0
br label %label0
label0:
%expr0 = load i32* %var0
%expr1 = icmp sle i32 %expr0, 3
br i1 %expr1, label %label1, label %label2
label1:
%expr2 = load i32* %var0
call void @printlnint(i32 %expr2)
%expr3 = load i32* %var0
%expr4 = add i32 %expr3, 1
store i32 %expr4, i32* %var0
br label %label0
label2:
%var1 = alloca i32
store i32 3, i32* %var1
br label %label3
label3:
%expr5 = load i32* %var1
%expr6 = icmp slt i32 %expr5, 8
br i1 %expr6, label %label4, label %label5
label4:
%expr7 = load i32* %var0
%expr8 = add i32 %expr7, 1
store i32 %expr8, i32* %var0
%expr9 = load i32* %var1
%expr10 = add i32 1, %expr9
store i32 %expr10, i32* %var1
br label %label3
label5:
store i32 4294967295, i32* %var0
%expr11 = load i32* %var0
call void @printlnint(i32 %expr11)
%var2 = alloca i1
%expr12 = and i1 0, 1
%expr13 = or i1 1, %expr12
store i1 %expr13, i1* %var2
%var3 = alloca i32
%var4 = alloca i32
store i32 3, i32* %var4
%expr14 = udiv i32 36, 18
store i32 %expr14, i32* %var3
store i32 1, i32 * %res
store i32 2, i32 * %pow
br label %label6
label6:
%expr15 = load i32 * %pow
%expr16 = icmp ne i32 %expr15, 0
br i1 %expr16, label %label7, label %label8
label7:
%expr17 = load i32 * %res
%expr18 = mul i32 %expr17, 8
store i32 %expr18, i32 * %res
%expr19 = load i32 * %pow
%expr20 = sub i32 %expr19, 1
store i32 %expr20, i32 * %base
br label %label6
label8:
store i32 %expr20, i32* %var3
%var5 = alloca i32
store i32 1, i32* %var5
%expr21 = load i32* %var5
%expr22 = add i32 %expr21, 1
store i32 %expr22, i32* %var3
%expr23 = load i32* %var3
%expr24 = trunc i32  %expr23 to i1
store i1 %expr24, i1* %var2
%var6 = alloca i32
%expr25 = add i32 5, 7
store i32 %expr25, i32* %var6
%expr26 = load i32* %var6
%expr27 = add i32 %expr26, 1
store i32 %expr27, i32* %var3
%expr28 = load i32* %var3
