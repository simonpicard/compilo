a::Integer
b::Integer
c::Integer

# Euclidean algorithm

function main()
	a = readint()
	b = readint()
	while(b!=0)
		c=b;
		while(a >= b) # a mod b
			a=a-b
		end
		b=a;a=c
	end
	println(a) 
end

main()
