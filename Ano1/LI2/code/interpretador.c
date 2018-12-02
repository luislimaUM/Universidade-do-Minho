#include <stdio.h>
#include <string.h>
#include <stdlib.h>

#include "comandos.h"
#include "tabuleiro.h"


/* Tamanho máximo de uma linha */
#define MAX_LINHA    1024

/* Tamanho máximo do buffer */
#define MAX_BUFFER   1024


Tab_BN *interpretar(Tab_BN *tab, char *comando)
{
	char com[MAX_LINHA], arg1[MAX_LINHA], arg2[MAX_LINHA];
	char buffer[MAX_BUFFER];
	int nargs = 0;
	char estado;

	nargs = sscanf(comando, "%s %s %s%[^\n]", com, arg1, arg2, buffer);

	if(!strcmp(com, "c") && nargs == 1) tab = cmd_c(tab);
	else if(!strcmp(com, "m") && nargs == 1) tab = cmd_m(tab);
	else if(!strcmp(com, "l") && nargs == 2) tab = cmd_l(tab, arg1);
	else if(!strcmp(com, "e") && nargs == 2) tab = cmd_e(tab, arg1);
	else if(!strcmp(com, "h") && nargs == 2) tab = cmd_h(tab, atoi(arg1));
	else if(!strcmp(com, "v") && nargs == 2) tab = cmd_v(tab, atoi(arg1));
	else if(sscanf(com, "p%c", &estado) == 1 && nargs == 3) tab = cmd_p(tab, estado, atoi(arg1), atoi(arg2));
	else if(!strcmp(com, "E1") && nargs == 1) tab = cmd_e1(tab);
	else if(!strcmp(com, "E2") && nargs == 1) tab = cmd_e2(tab);
	else if(!strcmp(com, "E3") && nargs == 1) tab = cmd_e3(tab);
	else if(!strcmp(com, "V") && nargs == 1) tab = cmd_V(tab);
	else if(!strcmp(com, "D") && nargs == 1) tab = cmd_D(tab);
	else if(!strcmp(com, "R") && nargs == 1) tab = cmd_R(tab);
	else if(!strcmp(com, "q") && nargs == 1) tab = cmd_q(tab);

	return tab;
}


int main(void)
{
	Tab_BN *tab = NULL;
	char linha[MAX_LINHA];

	tab = iniciaTabuleiro();

	/* Enquanto houver tabuleiro ou comandos a executar */
	while(tab != NULL && fgets(linha, MAX_LINHA, stdin) != NULL)
	{
		tab = interpretar(tab, linha);
	}

	if(tab != NULL) tab = removeTabuleiro(tab);

	return 0;
}
