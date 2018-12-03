%{
#include <stdio.h>
#include <stdlib.h>
#include <search.h>
#include <string.h>
#include "./stack.h"

typedef struct variavel{
	char *nome, *tipo; 
	int nrLinha;
	int nrColuna;
	int endereco;
}*Variavel;

int yylex();
int yyerror();
void addVariavel(char *nome, char*tipo, int l, int c);
Variavel getVariavel(char* nome);
void cantBeType(char *s, char *s1);
void checkType(char *variavel, char *tipo);


int nrVariaveis = 0, nrIF = 0, nrCiclos = 0, end = 0;
char str[20];
Variavel aux;
%}

%union{ int ival; char code; char* sval;}

%token <ival>num
%token <sval>id
%token F INT BOOL WRITE READ TRUE FALSE EQUAL DIFFERENT SUP SUPEQUAL INF INFEQUAL AND OR IF ELSE WHILE
%type <sval> ID CONST Termo OpLog OpLogAO OpArit

%%

Programa:	  Funcoes						{;}
			;
Funcoes:	  Funcao
			| Funcoes Funcao 				{;}
			;
Funcao:		  F id '{' 						{printf("start\n");} Declaracoes	Instrucoes '}' {printf("stop\n");}
			;
Declaracoes:  Declaracao
			| Declaracoes Declaracao
			;
Declaracao:   INT id ';'					{addVariavel($2,"INTEGER",0,0);printf("pushi 0\n");nrVariaveis++;}
			| INT '[' num ']' id ';'		{addVariavel($5,"ARRAY",$3,0);printf("pushn %d\n",$3);nrVariaveis+=$3;}
			| INT '[' num ']' '[' num ']' id ';'{addVariavel($8,"MATRIX",$3,$6);printf("pushn %d\n",$3*$6);nrVariaveis+=$3*$6;}
			| BOOL id ';' 					{addVariavel($2,"BOOL",0,0);printf("pushi 0\n");nrVariaveis++;}
			;
Instrucoes:   /* empty */
			| Instrucoes Instrucao			{;}
			;
Instrucao:	  Atribuicao ';'				{;}
			| IO ';'						{;}
			| SE 							{;}
			| CICLO							{;}
			;
CICLO:		  WHILE '(' 					{insertWHILE(++nrCiclos);sprintf(str,"%d",nrCiclos);printf("lblwhile%s: nop\n",str);} 
				OpLogAO 					{printf("jz lblfimwhile%s\n",str);}
					')' '{' Instrucoes 		{sprintf(str, "%d", rmvWHILE());printf("jump lblwhile%s\nlblfimwhile%s: nop\n",str,str);}
					'}'
			;
SE: 		IF '(' OpLogAO 					{insertIF(++nrIF);sprintf(str, "%d", nrIF);printf("lblif%s: nop\njz lblelse%s\n",str,str);}
				')' '{' Instrucoes 			{sprintf(str, "%d", tellmeIF());printf("jump lblelseend%s\nlblifend%s: nop\n",str,str);}
				'}' SENAO
			;
SENAO:		ELSE '{' 						{sprintf(str, "%d", tellmeIF());printf("lblelse%s: nop\n",str);} 
				Instrucoes  				{sprintf(str, "%d", rmvIF());printf("lblelseend%s: nop\n",str);}
				'}'							{;}
			;
IO:			WRITE Termo 		 			{printf("writei\n");}
			;
Atribuicao:	  id '=' Op 					{printf("storel %d\n", getVariavel($1)->endereco);}
			| id '=' OpLogAO				{checkType($1,"BOOL");printf("storel %d\n", getVariavel($1)->endereco);}
			| '[' num ']' id '=' Op			{checkType($4,"ARRAY");printf("storel %d\n", getVariavel($4)->endereco + $2);}
			| '[' id ']' id '='				{checkType($4,"ARRAY");printf("pushfp\npushi %d\npadd\npushl %d\n", getVariavel($4)->endereco,getVariavel($2)->endereco);} 
				Op							{printf("storen\n");}
			| '[' num ']' '[' num ']' id '=' Op	{checkType($7,"MATRIX");aux = getVariavel($7); printf("storel %d\n", aux->endereco + aux->nrColuna*$2 + $5);}
			| '[' num ']' '[' id ']' id '=' {checkType($7,"MATRIX");printf("pushfp\npushi %d\npadd\n", getVariavel($7)->endereco);/* end inicial matriz */printf("pushi %d\npushi %d\nmul\npushl %d\nadd\n", getVariavel($7)->nrColuna,$2, getVariavel($5)->endereco);/* pos na matriz*/} 
					Op 						{printf("storen\n");}
			| '[' id ']' '[' num ']' id '=' {checkType($7,"MATRIX");printf("pushfp\npushi %d\npadd\n", getVariavel($7)->endereco);/* end inicial matriz */printf("pushi %d\npushl %d\nmul\npushi %d\nadd\n", getVariavel($7)->nrColuna,getVariavel($2)->endereco, $5);/* pos na matriz*/} 
					Op 						{printf("storen\n");}
			| '[' id ']' '[' id ']' id '=' 	{checkType($7,"MATRIX");printf("pushfp\npushi %d\npadd\n", getVariavel($7)->endereco);/* end inicial matriz */printf("pushi %d\npushl %d\nmul\npushl %d\nadd\n", getVariavel($7)->nrColuna,getVariavel($2)->endereco, getVariavel($5)->endereco);/* pos na matriz*/} 
					Op 						{printf("storen\n");}
			;
Op:			  READ 							{printf("read\natoi\n");}
			| OpArit						{;}
			;

OpArit:	      Termo							{;}
			| OpArit '+' Termo				{printf("add\n");}
			| OpArit '-' Termo				{printf("sub\n");}
			| OpArit '*' Termo				{printf("mul\n");}
			| OpArit '/' Termo				{printf("div\n");}
			| OpArit '%' Termo				{printf("mod\n");}
			;
OpLogAO		: OpLog 						{;}
			| OpLogAO AND OpLog 			{printf("mul\n");}
			| OpLogAO OR OpLog 				{printf("add\npushi 0\nsup\n");}
			;
OpLog:		  Termo 						{;}
			| Termo EQUAL Termo				{printf("equal\n");} 
			| Termo DIFFERENT Termo			{printf("equal\npushi 1\nswap\nsub\n");} 
			| Termo SUP Termo				{printf("sup\n");}
			| Termo INF Termo				{printf("inf\n");}
			| Termo SUPEQUAL Termo			{printf("supeq\n");}
			| Termo INFEQUAL Termo			{printf("infeq\n");} 
			;
Termo:		  CONST							{$$ = $1;} // $$ INTEGER OU BOOL
			| ID 							{$$ = $1;} // $$ INTEGER, BOOL, ARRAY ou MATRIZZ
			;
ID:			  id 							{$$ = getVariavel($1)->tipo;printf("pushl %d\n", getVariavel($1)->endereco);}
			| '[' num ']' id 				{aux = getVariavel($4); printf("pushl %d\n", aux->endereco + $2);}
			| '[' id ']' id 				{aux = getVariavel($4); printf("pushfp\npushi %d\npadd\npushl %d\nloadn\n",getVariavel($4)->endereco,getVariavel($2)->endereco);}
			| '[' num ']' '[' num ']' id 	{aux = getVariavel($7); printf("pushl %d\n", aux->endereco + aux->nrColuna*$2 + $5);$$ = $7;}	
			| '[' num ']' '[' id ']' id 	{printf("pushfp\npushi %d\npadd\n", getVariavel($7)->endereco);/* end inicial matriz */	printf("pushi %d\npushi %d\nmul\npushl %d\nadd\nloadn\n", getVariavel($7)->nrColuna,$2, getVariavel($5)->endereco);/* pos na matriz*/}
			| '[' id ']' '[' num ']' id 	{printf("pushfp\npushi %d\npadd\n", getVariavel($7)->endereco);/* end inicial matriz */printf("pushi %d\npushl %d\nmul\npushi %d\nadd\nloadn\n", getVariavel($7)->nrColuna,getVariavel($2)->endereco, $5);/* pos na matriz*/}
			| '[' id ']' '[' id ']' id 		{printf("pushfp\npushi %d\npadd\n", getVariavel($7)->endereco);/* end inicial matriz */printf("pushi %d\npushl %d\nmul\npushl %d\nadd\nloadn\n", getVariavel($7)->nrColuna,getVariavel($2)->endereco, getVariavel($5)->endereco);/* pos na matriz*/}
			;
CONST:		  num							{printf("pushi %d\n",$1);}
			| TRUE							{printf("pushi 1\n");}
			| FALSE							{printf("pushi 0\n");}			
			;
%%

#include "lex.yy.c"

void checkType(char *variavel, char *tipo){
	Variavel v = getVariavel(variavel);
	if(strcmp(v->tipo,tipo) == 0){return;}
	else{ yyerror("Tipo incompatível\n"); exit(1);}
}

void cantBeType(char *s, char *s1){
	if(strcmp(s,s1)==0){
		yyerror("Tipo incompatível\n"); exit(1);
	}
}
int yyerror(char* s) {
	printf("Erro: %s\n", s);
	return 1;
}

void addVariavel(char *nome, char*tipo, int l, int c){
	ENTRY item, *r;

	Variavel v = (Variavel)malloc(sizeof(struct variavel));
	v->nome = strdup(nome);
	v->tipo = strdup(tipo);
	v->endereco = nrVariaveis;
	v->nrLinha = l;
	v->nrColuna = c;

	item.key = strdup(v->nome);
	item.data =  v;

	if(hsearch(item,FIND) == NULL){
		hsearch(item, ENTER);
	}
	else{
		yyerror("Variável já definida\n");
		exit(1);
	}
}

Variavel getVariavel(char* nome){
	ENTRY item, *resultado;
	Variavel found_item = NULL;

	item.key = strdup(nome);
	resultado = hsearch(item, FIND);
	if(resultado != NULL){
		found_item = resultado->data;
	}
	return (found_item);
}

int main () {
	hcreate(500);
  	yyparse();
  	hdestroy();
	return 0;
}