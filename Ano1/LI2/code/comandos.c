#include <stdio.h>

#include "comandos.h"


/* Tamanho máximo do buffer */
#define MAX_BUFFER 1024


/** Limpa o buffer */
void limpaBuffer()
{
	char ch;

	while((ch = getchar()) != '\n'  && ch != EOF);
}


/** Comando que carrega um tabuleiro a partir do stdin
 *
 *  @param tab   Tabuleiro de jogo
 *
 *  @return Tabuleiro de jogo
 */
Tab_BN *cmd_c(Tab_BN *tab)
{
	unsigned short linhas = 0, colunas = 0;
	int i = 0, j = 0, seg = -1, nargs = 0;
	char buffer[MAX_BUFFER], linha[MAX_BUFFER], estado, *res;

	res = fgets(linha, MAX_BUFFER, stdin);
	if(res != NULL && sscanf(linha, "%hu %hu%[^\n]", &linhas, &colunas, buffer) == 2)
	{
		if(dimensoesValidas(linhas, colunas))
		{
			defineDimensoes(tab, linhas, colunas);

			for(i = 1; i <= linhas; i++) {
				nargs = fscanf(stdin, "%d", &seg);
				if (nargs == 1) defineNumSegmentosLinha(tab, i, seg);
			}

			for(j = 1; j <= colunas; j++) {
				nargs = fscanf(stdin, "%d", &seg);
				if (nargs == 1) defineNumSegmentosColuna(tab, j, seg);
			}

			limpaBuffer();

			for(i = 1; i <= linhas; i++)
			{
				res = fgets(linha, MAX_BUFFER, stdin);

				if (res != NULL)
				{
					for(j = 1; j <= colunas; j++) {
						estado = linha[j - 1];
						defineEstado(tab, estado, i, j);
					}
				}
			}
		}
	}

	return tab;
}


/** Comando que imprime um tabuleiro para o stdout
 *
 *  @param tab   Tabuleiro de jogo
 *
 *  @return Tabuleiro de jogo
 */
Tab_BN *cmd_m(Tab_BN *tab)
{
	imprimeTabuleiro(tab);

	return tab;
}


/** Carrega um tabuleiro a partir de um ficheiro
  *
  * @param nomeFicheiro   Nome do ficheiro que contem o tabuleiro
  *
  * @return Tabuleiro representado no ficheiro em caso de sucesso, NULL
  *         caso contrário
  */
Tab_BN *carregaTabuleiro(char *nomeFicheiro)
{
	int erro = 0, numSeg = 0, dummyInt = 0;
	short linhas = -1, colunas = -1, i = 0, j = 0;
	char buf[MAX_BUFFER], estado;
	FILE *f = NULL;
	Tab_BN *tab = NULL;
	
	f = fopen(nomeFicheiro, "r");
	
	if(f == NULL) erro = 1;
	
	/* Ler linhas e colunas do tabuleiro */
	if(!erro && fscanf(f, "%hd %hd%[^\n]", &linhas, &colunas, buf) == 2)
	{
		if(dimensoesValidas(linhas, colunas))
		{
			tab = iniciaTabuleiro();
			if(tab != NULL) defineDimensoes(tab, linhas, colunas);
			else erro = 1;
		}
	}
	else erro = 1;

	for(i = 1; !erro && i <= linhas; i++)
	{
		if(fscanf(f, "%d", &numSeg) == 1)
			defineNumSegmentosLinha(tab, i, numSeg);
		else
			erro = 1;
	}

	for(j = 1; !erro && j <= colunas; j++)
	{
		if(fscanf(f, "%d", &numSeg) == 1)
			defineNumSegmentosColuna(tab, j, numSeg);
		else
			erro = 1;
	}
	
	/* Lê o \n do final da linha */
	dummyInt = fscanf(f, "%c", &estado); dummyInt++;
	/* Começar a ler os caracteres do ficheiro */
	for(i = 1; !erro && i <= linhas; i++)
	{
		for(j = 1; !erro && j <= colunas; j++)
		{
			if(fscanf(f, "%c", &estado) == 1)
				defineEstado(tab, estado, i, j);
			else
				erro = 1;
		}

		/* Lê o \n do final da linha */
		dummyInt = fscanf(f, "%c", &estado); dummyInt++;
	}
	
	if(erro) tab = removeTabuleiro(tab);
	
	if(f != NULL) fclose(f);
	
	return tab;
}


/** Comando que carrega um tabauleiro a partir de um ficheiro
  *
  * @param tab           Tabuleiro sobre o qual vai ser executado o comando
  * @param nomeFicheiro  Nome do ficheiro que contem o tabuleiro
  *
  * @return Tabuleiro carregado do ficheiro em caso de sucesso ou o estado
  *         anterior caso contrário
  */
Tab_BN *cmd_l(Tab_BN *tab, char *nomeFicheiro)
{
	Tab_BN *novoTab = NULL;

	novoTab = carregaTabuleiro(nomeFicheiro);
	
	if(novoTab != NULL)
	{
		tab = removeTabuleiro(tab);
		tab = novoTab;
	}

	return tab;
}


/** Grava um tabuleiro num ficheiro
 *
 *  @param tab   Tabuleiro que se pretende gravar em ficheiro
 *  @param nome  Nome do ficheiro
 */
void grava_ficheiro(Tab_BN *tab, char *nome)
{
	short linhas = -1, colunas = -1;
	short i = 0, j = 0;
	FILE *f = NULL;
	
	f = fopen(nome, "w");
	if(f != NULL)
	{
		linhas  = getNumLinhas(tab);
		colunas = getNumColunas(tab);
		fprintf(f, "%d %d\n", linhas, colunas);
		
		for(i = 1; i < linhas; i++)
			fprintf(f, "%d ", getNumSegmentosLinha(tab, i));
		fprintf(f, "%d\n", getNumSegmentosLinha(tab, i));

		for(j = 1; j < colunas; j++)
			fprintf(f, "%d ", getNumSegmentosColuna(tab, j));
		fprintf(f, "%d\n", getNumSegmentosColuna(tab, j));

		for(i = 1; i <= linhas; i++)
		{
			for(j = 1; j <= colunas; j++)
				fprintf(f, "%c", getEstado(tab, i, j));
			
			fprintf(f, "\n");
		}

		fclose(f);
	}
}


/** Comando que permite gravar um tabuleiro num ficheiro
 *
 *  @param tab   Tabuleiro que se pretende gravar em ficheiro
 *  @param nomeFicheiro  Nome do ficheiro que se pretende gravar
 *
 *  @return Tabuleiro sem qualquer modificação
 */
Tab_BN *cmd_e(Tab_BN *tab, char *nomeFicheiro)
{	
	if(existeTabuleiro(tab))	
		grava_ficheiro(tab, nomeFicheiro);

	return tab;
}


/** Comando que coloca o estado de todas as grelhas de uma determinada linha
 *  que ainda não estão determinadas como sendo água
 *
 *  @param tab       Tabuleiro de jogo
 *  @param numLinha  Número da linha
 *
 *  @return Tabuleiro de jogo
 */
Tab_BN *cmd_h(Tab_BN *tab, int numLinha)
{
	int j = 0, numColunas = 0;

	colocaMarcaHistorico(tab);

	if(linhaValida(tab, numLinha))
	{
		numColunas = getNumColunas(tab);

		for(j = 1; j <= numColunas; j++)
		{
			if(valorNaoDeterminado(tab, numLinha, j)) {
				guardaCasaHistorico(tab, numLinha - 1, j - 1);
				marcaAgua(tab, numLinha, j);
			}
		}
	}

	return tab;
}


/** Comando que coloca o estado de todas as grelhas de uma determinada coluna
 *  que ainda não estão determinadas como sendo água
 *
 *  @param tab        Tabuleiro de jogo
 *  @param numColuna  Número da coluna
 *
 *  @return Tabuleiro de jogo
 */
Tab_BN *cmd_v(Tab_BN *tab, int numColuna)
{
	int i = 0, numLinhas = 0;

	colocaMarcaHistorico(tab);

	if(colunaValida(tab, numColuna))
	{
		numLinhas = getNumLinhas(tab);

		for(i = 1; i <= numLinhas; i++)
		{
			if(valorNaoDeterminado(tab, i, numColuna)) {
				guardaCasaHistorico(tab, i - 1, numColuna - 1);
				marcaAgua(tab, i, numColuna);
			}
		}
	}

	return tab;
}


/** Comando que coloca que define o estado do tabuleiro na linha e
 *  coluna passados como parâmetro
 *
 *  @param tab       Tabuleiro de jogo
 *  @param estado    Caracter que representa um estado
 *  @param linha     Número da linha
 *  @param coluna    Número da coluna 
 *
 *  @return Tabuleiro de jogo
 */
Tab_BN *cmd_p(Tab_BN *tab, char estado, int linha, int coluna)
{
	colocaMarcaHistorico(tab);
	
	if(linhaValida(tab, linha) && colunaValida(tab, coluna))
	{
		guardaCasaHistorico(tab, linha - 1, coluna - 1);
		defineEstado(tab, estado, linha, coluna);
	}

	return tab;
}


/** Aplica a estratégia 1
 *
 *  @param tab  Tabuleiro de jogo
 *
 *  @return 1 se o estado do tabuleiro foi modificado, 0 caso contrário
 */
int aplicaEst1(Tab_BN *tab)
{
	short mudou = 0;
	short i = 0, j =0;
	short linhas = 0, colunas = 0;
	
	linhas  = getNumLinhas(tab);
	colunas = getNumColunas(tab);
	
	for(i = 1; i <= linhas; i++)
	{
		for(j = 1; j <= colunas; j++)
		{
			if(!valorNaoDeterminado(tab, i, j) && !valorAgua(tab, i, j))
			{
				mudou = marcaDiagonaisAgua(tab, i, j) || mudou;
				mudou = marcaAdjacentesAgua(tab, i, j) || mudou;
			}
		}
	}

	return mudou;
}


/** Aplicação da estratégia 1. Esta estratégia coloca água que se deduza que
 *  vai existir à volta de todos os segmentos de barcos já colocados
 *
 *  @param tab   Tabuleiro sobre o qual vai ser executado o comando
 *
 *  @return Tabuleiro resultante da aplicação do comando estratégia 1
 */
Tab_BN *cmd_e1(Tab_BN *tab)
{
	if(existeTabuleiro(tab))
	{
		colocaMarcaHistorico(tab);
		aplicaEst1(tab);
	}

	return tab;
}


/** Aplica a estratégia 2
 *
 *  @param tab  Tabuleiro de jogo
 *
 *  @return 1 se o estado do tabuleiro foi modificado, 0 caso contrário
 */
int aplicaEst2(Tab_BN *tab)
{
	short mudou = 0;
	short i = 0, j =0;
	short linhas = 0, colunas = 0;
	int numSeg = 0, numSegAux = 0;
	
	linhas  = getNumLinhas(tab);
	colunas = getNumColunas(tab);
	
	for(i = 1; i <= linhas; i++)
	{
		numSeg = getNumSegmentosLinha(tab, i);
		numSegAux = contaNumSegmentosLinha(tab, i);

		if(numSegAux >= numSeg) mudou = marcaAguaLinha(tab, i) || mudou;
	}

	for(j = 1; j <= colunas; j++)
	{
		numSeg = getNumSegmentosColuna(tab, j);
		numSegAux = contaNumSegmentosColuna(tab, j);

		if(numSegAux >= numSeg) mudou = marcaAguaColuna(tab, j) || mudou;
	}

	return mudou;
}


/** Aplicação da estratégia 2. Esta estratégia coloca água nas linhas e colunas em
 *  que todos os segmentos de barcos já foram colocados
 *
 *  @param tab   Tabuleiro sobre o qual vai ser executado o comando
 *
 *  @return Tabuleiro resultante da aplicação do comando estratégia 2
 */
Tab_BN *cmd_e2(Tab_BN *tab)
{
	if(existeTabuleiro(tab))
	{
		colocaMarcaHistorico(tab);
		aplicaEst2(tab);
	}

	return tab;
}


/** Aplica a estratégia 3
 *
 *  @param tab  Tabuleiro de jogo
 *
 *  @return 1 se o estado do tabuleiro foi modificado, 0 caso contrário
 */
int aplicaEst3(Tab_BN *tab)
{
	short mudou = 0;
	int i = 0, j = 0;
	int linhas = 0, colunas = 0;
	int numSeg = 0, numSegAux = 0, numNaoDet = 0;

	linhas = getNumLinhas(tab);
	colunas = getNumColunas(tab);

	for(i = 1; i <= linhas; i++)
	{
		numSeg = getNumSegmentosLinha(tab, i);
		numSegAux = contaNumSegmentosLinha(tab, i);
		numNaoDet = contaNumNaoDetrminadosLinha(tab, i);

		if(numSeg - numSegAux == numNaoDet && numNaoDet != 0)
		{
			for(j = 1; j <= colunas; j++)
			{
				if(valorNaoDeterminado(tab, i, j)) {
					guardaCasaHistorico(tab, i - 1, j - 1);
					marcaOcupado(tab, i, j);
					mudou = 1;
				}
			}
		}
	}

	for(j = 1; j <= colunas; j++)
	{
		numSeg = getNumSegmentosColuna(tab, j);
		numSegAux = contaNumSegmentosColuna(tab, j);
		numNaoDet = contaNumNaoDetrminadosColuna(tab, j);

		if(numSeg -numSegAux == numNaoDet && numNaoDet != 0)
		{
			for(i = 1; i <= linhas; i++)
			{
				if(valorNaoDeterminado(tab, i, j)) {
					guardaCasaHistorico(tab, i - 1, j - 1);
					marcaOcupado(tab, i, j);
					mudou = 1;
				}
			}
		}
	}

	for(i = 1; i <= linhas; i++) {
		for(j = 1; j <= colunas; j++) {
			if(valorOcupado(tab, i, j))
				mudou = colocaSegmento(tab, i, j) || mudou;
		}
	}

	return mudou;
}


/** Aplicação da estratégia 3. Esta estratégia coloca segmentos de barcos nas linhas e colunas
 *  nas quais todos os espaços vazios têm que conter segmentos de barcos para que o número
 *  correspondente seja respeitado
 *
 *  @param tab   Tabuleiro sobre o qual vai ser executado o comando
 *
 *  @return Tabuleiro resultante da aplicação do comando estratégia 3
 */
Tab_BN *cmd_e3(Tab_BN *tab)
{
	if(existeTabuleiro(tab))
	{
		colocaMarcaHistorico(tab);
		aplicaEst3(tab);
	}

	return tab;
}


/** Comando V que verifica se um tabuleiro apresenta uma solução válida
 *
 *  @param tab   Tabuleiro de jogo
 *
 *  @return Tabuleiro sem qualquer alteração
 */
Tab_BN *cmd_V(Tab_BN *tab)
{
	int erro = 0;
	int i = 0, j = 0, linhas = 0, colunas = 0;
	int numSeg = 0, numSegAux = 0;

	if(existeTabuleiro(tab))
	{
		linhas = getNumLinhas(tab);
		colunas = getNumColunas(tab);

		for(i = 1;!erro && i <= linhas; i++)
		{
			numSeg = contaNumSegmentosLinha(tab, i);
			numSegAux = getNumSegmentosLinha(tab, i);

			if(numSeg > numSegAux) erro = 1;
		}

		for(j = 1;!erro && j <= colunas; j++)
		{
			numSeg = contaNumSegmentosColuna(tab, j);
			numSegAux = getNumSegmentosColuna(tab, j);

			if(numSeg > numSegAux) erro = 1;
		}
	}

	printf("%s\n", erro ? "NAO" : "SIM");

	return tab;
}


/** Comando que anula um comando que modificou o estado do tabuleiro.
 *  Se não existirem comandos para anular, é reportada uma mensagem de erro
 *
 *  @param tab   Tabuleiro sobre o qual vai ser executado o comando
 *
 *  @return Tabuleiro resultante da execução do comando
 */
Tab_BN *cmd_D(Tab_BN *tab)
{	
	if(existeTabuleiro(tab))
		regressaTabuleiroAnterior(tab);	
	
	return tab;
}


/** Aplica o comando resolve. Este comando apenas aplica todas as estratégias
 *  até o tabuleiro não ser mais modificado
 *
 *  @param tab  Tabuleiro de jogo
 *
 *  @return 1 se o tabuleiro não tem solução, 0 caso contrário
 */
Tab_BN *cmd_R(Tab_BN *tab)
{
	int mudou = 0;
	
	do
	{
		mudou = 0;

		mudou = aplicaEst1(tab) || mudou;
		mudou = aplicaEst2(tab) || mudou;
		mudou = aplicaEst3(tab) || mudou;
	}
	while(mudou);
	
	return tab;
}


/** Comando que elimina um tabuleiro e sai do jogo
 *
 *  @param tab   Tabuleiro que se pretende eliminar
 *
 *  @return NULL
 */
Tab_BN *cmd_q(Tab_BN *tab)
{
    return removeTabuleiro(tab);
}
