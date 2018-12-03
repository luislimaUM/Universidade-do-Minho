#include <search.h>

typedef struct variavel {
	char nomeFuncao[20], nomeVariavel[20], tipo[20];
	int enderecoStack;
} *Variavel

void inicializar(){
	hcreate(50);
}