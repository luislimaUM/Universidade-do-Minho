%option noyywrap
%option yylineno

%{

#include <stdio.h>
#include <ctype.h>
#include <string.h>

void guardaVerbo(char* verbo);
void inicia();
int getId(char c);
int existe(char* c, int id);
void adiciona(char *c, int id);
void gerarPagina();
void imprime();
char** lista[26];

%}

AntecedenteMaiuscula 	(Can|Could|Shall|Should|Will|Would|May|Might|Do|Did|Does|To)
AntecedenteMinuscula 	(can|could|shall|should|will|would|may|might|do|did|does|to)

Sujeito					([Ii]|[Yy]ou|[Hh]e|[Ss]he|[Ii]t|[Ww]e|[Tt]hey|[Nn]ot)

Declarativa1 			({AntecedenteMaiuscula}[\ \n][a-zA-Z]+{2,})
Declarativa2			((\ ){AntecedenteMinuscula}[\ \n][a-zA-Z]+{2,})
Interrogativa1			({AntecedenteMaiuscula}(\ ){Sujeito}(\ )[a-zA-Z]+{2,})
Interrogativa2			((\ ){AntecedenteMinuscula}(\ ){Sujeito}(\ )[a-zA-Z]+{2,})

%%
{Declarativa1}			{ guardaVerbo(yytext);}
{Declarativa2}			{ guardaVerbo(yytext);  }
{Interrogativa1}		{ guardaVerbo(yytext);  }
{Interrogativa2}		{ guardaVerbo(yytext);  }
<*>.|\n					{ }
%%

void gerarPagina() {
	FILE* f = fopen("alineaB.html", "w");
	char* inicio = "<html> <head> <h1> PL-TP1b </h1> </head> </body>";
	char* fim = "</body> </html>";

	fprintf(f, "\%s", inicio);

	int i, j;

	for (i=0; i<26; i++) {
		for (j=0; lista[i][j]; j++) {
			fprintf(f, "<li> \%s </li>", lista[i][j]);
		}
	}

	fprintf(f, "\%s", fim);
	fclose(f);
}


void inicia() {
	int i;
	for (i=0; i<26; i++) {
		lista[i] = (char**)malloc(1024*sizeof(char**));
	}
}

int existe (char* verbo, int id) {
	int i;

	for (i=0; lista[id][i]!=NULL; i++) {
		if (strcmp(lista[id][i], verbo) == 0) 
			return 1;
	}

	return 0; 
}

void adiciona (char* verbo, int id) {
	int i;

	for(i=0; lista[id][i]!=NULL; i++);
	lista[id][i] = strdup(verbo);

}

int getId(char c) {
	char aux = tolower(c);
	int r = aux-97;
	return r;
}

void guardaVerbo(char* arg){
	char* pal = strtok(arg, " \n");
	char ultima[50];

	while(pal!=NULL) {
		strcpy(ultima,pal);
		pal = strtok(NULL, " \n");
	}

	int id = getId(ultima[0]);
	int r = existe(ultima, id);
	if (r == 0) { 
		adiciona(ultima, id);
	}
} 


void imprime() {
	int i, j;
	for (i=0; i<26; i++) {
		for (j=0; lista[i][j]!=NULL; j++) {
			printf("%s\n", lista[i][j]);
		}
	}
}


int main (int argc, char* argv[]){
	inicia();

	if (argc==2) {
		yyin = fopen(argv[1], "r");
	}

	yylex();

	//imprime();
	gerarPagina();

	return 0;
}