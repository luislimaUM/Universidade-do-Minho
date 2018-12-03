all: flex yacc gcc clean

flex: flex.l
	flex flex.l

yacc: yacc.y
	yacc -d yacc.y

gcc: y.tab.c lex.yy.c stack.c
	gcc stack.c y.tab.c -o exec

clean: 
	rm lex.yy.c y.tab.c y.tab.h 

testar:
	./exec < ./Codigo/code.txt 

VM:
	java -jar "VM/dist/vm.jar"	

ex1: all
	./exec < ./Codigo/ex1.txt > ./CodigoMaquina/ex1.txt
ex2: all
	./exec < ./Codigo/ex2.txt > ./CodigoMaquina/ex2.txt
ex3: all
	./exec < ./Codigo/ex3.txt > ./CodigoMaquina/ex3.txt
ex4: all
	./exec < ./Codigo/ex4.txt > ./CodigoMaquina/ex4.txt
ex5: all
	./exec < ./Codigo/ex5.txt > ./CodigoMaquina/ex5.txt
ex6: all
	./exec < ./Codigo/ex6.txt > ./CodigoMaquina/ex6.txt

code: all
	./exec < ./Codigo/code.txt > ./CodigoMaquina/code.txt


erro1: all
	./exec < ./Codigo/Erros/erro1.txt > ./CodigoMaquina/Erros/erro1.txt
erro2: all
	./exec < ./Codigo/Erros/erro2.txt > ./CodigoMaquina/Erros/erro2.txt
erro3: all
	./exec < ./Codigo/Erros/erro3.txt > ./CodigoMaquina/Erros/erro3.txt


div: all
	./exec < ./Codigo/divisao.txt > ./CodigoMaquina/divisao.txt