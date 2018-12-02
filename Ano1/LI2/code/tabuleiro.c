#include <stdio.h>
#include <stdlib.h>

#include "tabuleiro.h"
#include "listaligada.h"


/* Máximo número de linhas do tabuleiro */
#define MAX_LINHAS    100

/* Máximo número de colunas do tabuleiro */
#define MAX_COLUNAS    100


#define NAODET         '.'   /*  Estado do tabuleiro valor não determinado  */
#define AGUA           '~'   /*           Estado do tabuleiro água          */
#define OCUPADO        'o'   /*          Estado do tabuleiro ocupado        */
#define SUBMARINO      'O'   /*         Estado do tabuleiro submarino       */
#define BARCO_CIMA     '^'   /*     Estado da parte de cima de um barco     */
#define BARCO_BAIXO    'v'   /*     Estado da parte de baixo de um barco    */
#define BARCO_ESQ      '<'   /*     Estado da parte esquerda de um barco    */
#define BARCO_DIR      '>'   /*      Estado da parte direita de um barco    */
#define BARCO_CENTRAL  '#'   /*     Estado da parte central de um barco     */


#define LINHAS(tab)   tab->dim.linhas     /* Linhas do tabuleiro  */
#define COLUNAS(tab)  tab->dim.colunas    /* Colunas do tabuleiro */

#define SEGLINHA(tab, i)   tab->seg.seg_linhas[i]   /*     Número de segmentos da linha i    */
#define SEGCOLUNA(tab, j)  tab->seg.seg_colunas[j]  /*    Número de segmentos da coluna j    */
#define ESTADO(tab, x, y)  tab->estado[x][y]        /*  Estado do tabuleiro na posição (x,y) */
#define HISTORICO(tab)     tab->historico           /*   Histórico de jogadas do tabuleiro   */


#define COORDENADA_X(casa)  casa->x    /* Coordenada em xx de uma casa */
#define COORDENADA_Y(casa)  casa->y    /* Coordenada em yy de uma casa */
#define ESTADO_CASA(casa)   casa->estado    /* Estado de uma casa */



/* --------------------  ESTRUTURAS DE DADOS  -------------------- */

/* Estrutura que define as dimensões de um tabuleiro.
 * Contém o tamnho do tabuleiro (número de linhas e número de colunas)
 */
typedef struct sDimensoes
{
	short colunas;
	short linhas;
} Dimensoes;


/* Estrutura que contém a informação do número de segmentos em cada
 * linha e em cada coluna
 */
typedef struct sSegmentos
{
	int seg_linhas[MAX_LINHAS];
	int seg_colunas[MAX_COLUNAS];
} Segmentos;


/* Estrutura de um tabuleiro de jogo */
struct sTab_BN
{
	Dimensoes dim;
	Segmentos seg;
	char estado[MAX_LINHAS][MAX_COLUNAS];
	void *historico;
};


/** Estrutura de uma casa. Uma casa contém as coordenadas em que ela se
 *  encontra bem como o seu estado
 */
typedef struct sCasa
{
	char x;
	char y;
	char estado;
} Casa;



/** Inicia um tabuleiro de jogo
 *
 *  @return  Um novo tabuleiro de jogo, com o número de linhas e colunas
 *           definidas como 0
 */
Tab_BN *iniciaTabuleiro()
{
	int i = 0, j = 0;
    Tab_BN *tab = NULL;

    tab = malloc(sizeof(Tab_BN));
    
    if(tab != NULL)
    {
    	LINHAS(tab)  = 0;
    	COLUNAS(tab) = 0;

        for(i = 1; i <= MAX_LINHAS; i++)
        {
        	defineNumSegmentosLinha(tab, i, 0);

            for(j = 1; j <= MAX_COLUNAS; j++)
                defineEstado(tab, NAODET, i, j);
        }

        for(j = 1; j <= MAX_COLUNAS; j++)
        	defineNumSegmentosColuna(tab, j, 0);

        HISTORICO(tab) = cria_LLigada();
    }

    return tab;
}


/** Remove um tabuleiro de jogo. Liberta toda a memória alocada pelo tabuleiro
 *
 *  @param tab  Tabuleiro que se pretende remover
 *
 *  @return NULL
 */
Tab_BN *removeTabuleiro(Tab_BN *tab)
{
	void *dados = NULL;

    if(tab != NULL)
    {
    	dados = remove_cabeca_LLigada(HISTORICO(tab));
		while(dados != NULL)
		{
			free(dados);
			dados = remove_cabeca_LLigada(HISTORICO(tab));
		}
		
		elimina_LLigada( &(HISTORICO(tab)) );

        free(tab);
        tab = NULL;
    }

    return tab;
}


/** Verifica se um tabuleiro é válido (se existe tabuleiro). Um tabuleiro é 
 *  considerado válido se o seu estado estiver iniciado e se as suas dimensões 
 *  estão definidas
 *
 *  @param tab  Tabuleiro que se pretende verificar se é válido
 *
 *  @return 1 se o tabuleiro é válido, 0 caso contrário
 */
int existeTabuleiro(Tab_BN *tab)
{
	return tab != NULL && COLUNAS(tab) != 0;
}


/** Verifica se as dimensões podem ser aplicadas a um tabuleiro
 *
 *  @param linhas   Número de linhas do tabuleiro
 *  @param colunas  Número de colunas do tabuleiro
 *
 *  @return 1 se as dimensões podem ser aplicadas a um tabuleiro, 0 caso 
 *          contrário
 */
int dimensoesValidas(int linhas, int colunas)
{
	return linhas  > 0 && linhas  <= MAX_LINHAS && 
	       colunas > 0 && colunas <= MAX_COLUNAS;
}


/** Verifica se existe o número de linha no tabuleiro
 *
 *  @param tab  Tabuleiro de jogo
 *  @param num  Número da linha a verificar
 *
 *  @return 1 se existe uma linha no tabuleiro com o mesmo numLinha, 0 caso
 *          contrário
 */
int linhaValida(Tab_BN *tab, int numLinha)
{
	return numLinha > 0  && numLinha <= LINHAS(tab);
}


/** Verifica se existe o número de coluna no tabuleiro
 *
 *  @param tab  Tabuleiro de jogo
 *  @param num  Número da coluna a verificar
 *
 *  @return 1 se existe uma coluna no tabuleiro com o mesmo numColuna, 0 caso
 *          contrário
 */
int colunaValida(Tab_BN *tab, int numColuna)
{
	return numColuna > 0  && numColuna <= COLUNAS(tab);
}


/** Devolve o número de colunas do tabuleiro
 *
 *  @param tab  Tabuleiro de jogo
 *  
 *  @return 'n' que representa o número de colunas do tabuleiro
 */
short getNumColunas(Tab_BN *tab)
{
	return COLUNAS(tab);
}


/** Devolve o número de linhas do tabuleiro
 *
 *  @param tab  Tabuleiro de jogo
 *  
 *  @return 'n' que representa o número de colunas do tabuleiro
 */
short getNumLinhas(Tab_BN *tab)
{
	return LINHAS(tab);
}


/** Define as dimensões de um tabuleiro
 *
 *  @param tab      Tabuleiro que se pretende definir as dimensões
 *  @param colunas  Número de linhas do tabuleiro
 *  @param linhas   Número de colunas do tabuleiro
 */
void defineDimensoes(Tab_BN *tab, int linhas, int colunas)
{
	LINHAS(tab) = linhas;
	COLUNAS(tab) = colunas;
}


/** Verifica se o estado do tabuleiro é não determinado na linha e coluna
 *  passados como parâmetros
 *
 *  @param tab     Tabuleiro de jogo
 *  @param linha   Número da linha a verificar
 *  @param coluna  Número da coluna a verificar
 *
 *  @return 1 se valor do estado é não determinado, 0 caso contrário
 */
int valorNaoDeterminado(Tab_BN *tab, int linha, int coluna)
{
	return ESTADO(tab, linha - 1, coluna - 1) == NAODET;
}


/** Verifica se o estado do tabuleiro é água na linha e coluna
 *  passados como parâmetros
 *
 *  @param tab     Tabuleiro de jogo
 *  @param linha   Número da linha a verificar
 *  @param coluna  Número da coluna a verificar
 *
 *  @return 1 se valor do estado é água, 0 caso contrário
 */
int valorAgua(Tab_BN *tab, int linha, int coluna)
{
	return ESTADO(tab, linha - 1, coluna - 1) == AGUA;
}


/** Verifica se o estado do tabuleiro está definido como ocupado
 *  na linha e coluna passados como parâmetros
 *
 *  @param tab      Tabuleiro de jogo
 *  @param linha    Número da linha a verificar
 *  @param coluna   Número da coluna a verificar
 *
 *  @return 1 se o valor do estado é ocupado, 0 caso contrário
 */
int valorOcupado(Tab_BN *tab, int linha, int coluna)
{
	return ESTADO(tab, linha - 1, coluna - 1) == OCUPADO;
}


/** Marca o estado de uma casa do tabuleiro como sendo água
 *
 *  @param tab     Tabuleiro de jogo
 *  @param linha   Número da linha
 *  @param coluna  Número da coluna
 */
void marcaAgua(Tab_BN *tab, int linha, int coluna)
{
	ESTADO(tab, linha - 1, coluna - 1) = AGUA;
}


/** Marca o estado de uma casa do tabuleiro como sendo ocupado
 *
 *  @param tab     Tabuleiro de jogo
 *  @param linha   Número da linha
 *  @param coluna  Número da coluna
 */
void marcaOcupado(Tab_BN *tab, int linha, int coluna)
{
	ESTADO(tab, linha - 1, coluna - 1) = OCUPADO;
}


/** Define o número de segmentos de uma determinada linha
 *
 *  @param tab     Tabuleiro de jogo
 *  @param linha   Número da linha
 *  @param numSeg  Número de segmentos
 */
void defineNumSegmentosLinha(Tab_BN *tab, int linha, int numSeg)
{
	SEGLINHA(tab, linha - 1) = numSeg;
}


/** Retorna o número de segmentos de uma determinada linha
 *
 *  @param tab      Tabuleiro de jogo
 *  @param linha    Número da linha
 *
 *  @return  Número de segmentos da linha
 */
int getNumSegmentosLinha(Tab_BN *tab, int linha)
{
	return SEGLINHA(tab, linha -1);
}


/** Define o número de segmentos de uma determinada coluna
 *
 *  @param tab     Tabuleiro de jogo
 *  @param coluna  Número da coluna
 *  @param numSeg  Número de segmentos
 */
void defineNumSegmentosColuna(Tab_BN *tab, int coluna, int numSeg)
{
	SEGCOLUNA(tab, coluna - 1) = numSeg;
}


/** Retorna o número de segmentos de uma determinada coluna
 *
 *  @param tab      Tabuleiro de jogo
 *  @param coluna    Número da coluna
 *
 *  @return  Número de segmentos da coluna
 */
int getNumSegmentosColuna(Tab_BN *tab, int coluna)
{
	return SEGCOLUNA(tab, coluna -1);
}


/** Define o estado do tabuleiro na linha e na coluna passados
 *  como parâmetros
 *
 *  @param tab     Tabuleiro de jogo
 *  @param estado  Estado que se pretende definir
 *  @param linha   Número da linha
 *  @param coluna  Número da coluna
 */
void defineEstado(Tab_BN *tab, char estado, int linha, int coluna)
{
	ESTADO(tab, linha - 1, coluna - 1) = estado;
}


/** Retorna o estado do tabuleiro na linha e na coluna passados
 *  como parâmetros
 *
 *  @param tab     Tabuleiro de jogo
 *  @param linha   Número da linha
 *  @param coluna  Número da coluna
 *
 *  @return Estado do tabuleiro
 */
char getEstado(Tab_BN *tab, int linha, int coluna)
{
	return ESTADO(tab, linha -1, coluna - 1);
}


/** Conta o número de segmentos de uma determinada linha
 *
 *  @param tab    Tabuleiro de jogo
 *  @param linha  Número da linha
 *
 *  @return Número total de segmentos de uma determinada linha
 */
int contaNumSegmentosLinha(Tab_BN *tab, int linha)
{
	int j = 0, i = linha - 1, total = 0;
	int colunas = COLUNAS(tab);

	for(j = 0; j < colunas; j++)
	{
		if(ESTADO(tab, i, j) != AGUA && ESTADO(tab, i, j) != NAODET)
			total++;
	}

	return total;
}


/** Conta o número de segmentos de uma determinada coluna
 *
 *  @param tab    Tabuleiro de jogo
 *  @param coluna  Número da coluna
 *
 *  @return Número total de segmentos de uma determinada coluna
 */
int contaNumSegmentosColuna(Tab_BN *tab, int coluna)
{
	int j = coluna - 1, i = 0, total = 0;
	int linhas = LINHAS(tab);

	for(i = 0; i < linhas; i++)
	{
		if(ESTADO(tab, i, j) != AGUA && ESTADO(tab, i, j) != NAODET)
			total++;
	}

	return total;
}


/** Conta o número de casas não determinadas de uma determinada linha
 *
 *  @param tab   Tabuleiro de jogo
 *  @param linha  Número da linha
 *
 *  @return Número total de casas não determinadas de uma determinada linha
 */
int contaNumNaoDetrminadosLinha(Tab_BN *tab, int linha)
{
	int i = linha - 1, j = 0, total = 0;
	int colunas = COLUNAS(tab);

	for(j = 0; j < colunas; j++)
	{
		if(ESTADO(tab, i, j) == NAODET)
			total++;
	}

	return total;
}


/** Conta o número de casas não determinadas de uma determinada coluna
 *
 *  @param tab   Tabuleiro de jogo
 *  @param coluna  Número da coluna
 *
 *  @return Número total de casas não determinadas de uma determinada coluna
 */
int contaNumNaoDetrminadosColuna(Tab_BN *tab, int coluna)
{
	int i = 0, j = coluna - 1, total = 0;
	int linhas = LINHAS(tab);

	for(i = 0; i < linhas; i++)
	{
		if(ESTADO(tab, i, j) == NAODET)
			total++;
	}

	return total;
}


/** Marca todas as casas de uma determinada linha como sendo água,
 *  caso o seu valor não esteja determinado
 *
 *  @param tab    Tabuleiro de jogo
 *  @param linha  Número da linha
 *
 * @return 1 se o tabuleiro mudou de estado, 0 caso contrário
 */
int marcaAguaLinha(Tab_BN *tab, int linha)
{
	int i = linha - 1, j = 0, mudou = 0;
	int colunas = COLUNAS(tab);

	for(j = 0; j < colunas; j++)
	{
		if(ESTADO(tab, i, j) == NAODET)
		{
			guardaCasaHistorico(tab, i, j);
			ESTADO(tab, i, j) = AGUA;
			mudou = 1;
		}
	}

	return mudou;
}


/** Marca todas as casas de uma determinada coluna como sendo água,
 *  caso o seu valor não esteja determinado
 *
 *  @param tab    Tabuleiro de jogo
 *  @param coluna  Número da coluna
 *
 * @return 1 se o tabuleiro mudou de estado, 0 caso contrário
 */
int marcaAguaColuna(Tab_BN *tab, int coluna)
{
	int i = 0, j = coluna - 1, mudou = 0;
	int linhas = LINHAS(tab);

	for(i = 0; i < linhas; i++)
	{
		if(ESTADO(tab, i, j) == NAODET)
		{
			guardaCasaHistorico(tab, i, j);
			ESTADO(tab, i, j) = AGUA;
			mudou = 1;
		}
	}

	return mudou;
}


/** Marca as diagonais de um segmento como sendo água, caso seja possível
 *
 *  @param tab      Tabuleiro de jogo
 *  @param linha    Número da linha do segmento
 *  @param coluna   Número da coluna do segmento
 *
 *  @return 1 se o estado do tabuleiro mudou, 0 caso contrário
 */
int marcaDiagonaisAgua(Tab_BN *tab, int linha, int coluna)
{
	int mudou = 0;
	int i = linha - 1, j = coluna - 1;

	if(i - 1 >= 0)
	{
		if(j - 1 >= 0 && ESTADO(tab, i - 1, j - 1) == NAODET) {
			guardaCasaHistorico(tab, i - 1, j - 1);
			ESTADO(tab, i - 1, j - 1) = AGUA;
			mudou = 1;
		}

		if(j + 1 < COLUNAS(tab) && ESTADO(tab, i - 1, j + 1) == NAODET) {
			guardaCasaHistorico(tab, i - 1, j + 1);
			ESTADO(tab, i - 1, j + 1) = AGUA;
			mudou = 1;
		}
	}

	if(i + 1 < LINHAS(tab))
	{
		if(j - 1 >= 0 && ESTADO(tab, i + 1, j - 1) == NAODET) {
			guardaCasaHistorico(tab, i + 1, j - 1);
			ESTADO(tab, i + 1, j - 1) = AGUA;
			mudou = 1;
		}

		if(j + 1 < COLUNAS(tab) && ESTADO(tab, i + 1, j + 1) == NAODET) {
			guardaCasaHistorico(tab, i + 1, j + 1);
			ESTADO(tab, i + 1, j + 1) = AGUA;
			mudou = 1;
		}
	}

	return mudou;
}


/** Marca as casas adjacentes de um segmento como sendo água, caso seja possível
 *
 *  @param tab      Tabuleiro de jogo
 *  @param linha    Número da linha do segmento
 *  @param coluna   Número da coluna do segmento
 *
 *  @return 1 se o estado do tabuleiro mudou, 0 caso contrário
 */
int marcaAdjacentesAgua(Tab_BN *tab, int linha, int coluna)
{
	int mudou = 0;
	int i = linha - 1, j = coluna - 1;
	char estado = ESTADO(tab, i, j);
	int centralOcupado = (estado == BARCO_CENTRAL || estado == OCUPADO);

	if(i - 1 >= 0)
	{
		if(ESTADO(tab, i - 1, j) == NAODET && estado != BARCO_BAIXO && !centralOcupado) {
			guardaCasaHistorico(tab, i - 1, j);
			ESTADO(tab, i - 1, j) = AGUA;
			mudou = 1;
		}
	}

	if(i + 1 < LINHAS(tab))
	{
		if(ESTADO(tab, i + 1, j) == NAODET && estado != BARCO_CIMA && !centralOcupado) {
			guardaCasaHistorico(tab, i + 1, j);
			ESTADO(tab, i + 1, j) = AGUA;
			mudou = 1;
		}
	}

	if(j - 1 >= 0)
	{
		if(ESTADO(tab, i, j - 1) == NAODET && estado != BARCO_DIR && !centralOcupado) {
			guardaCasaHistorico(tab, i, j - 1);
			ESTADO(tab, i, j - 1) = AGUA;
			mudou = 1;
		}
	}

	if(j + 1 < COLUNAS(tab))
	{
		if(ESTADO(tab, i, j + 1) == NAODET && estado != BARCO_ESQ && !centralOcupado) {
			guardaCasaHistorico(tab, i, j + 1);
			ESTADO(tab, i, j + 1) = AGUA;
			mudou = 1;
		}
	}

	return mudou;
}


/** Verifica se na posição (i, j) do tabuleiro pode ser colocado um submarino
 *
 *  @param tab    Tabuleiro de jogo
 *  @param i      Número da linha do tabuleiro (índice)
 *  @param j      Número da coluna do tablueiro (índice)
 *
 *  @return 1 caso seja possível colocar um submarino, 0 caso contrário
 */
int eSubmarino(Tab_BN *tab, int i, int j)
{
	int submarino = 1;
	
	if(j - 1 >= 0) submarino = (submarino && ESTADO(tab, i, j - 1) == AGUA);
	if(i - 1 >= 0) submarino = (submarino && ESTADO(tab, i - 1, j) == AGUA);
	if(j + 1 < COLUNAS(tab)) submarino = (submarino && ESTADO(tab, i, j + 1) == AGUA);
	if(i + 1 < LINHAS(tab))  submarino = (submarino && ESTADO(tab, i + 1, j) == AGUA);

	return submarino;
}


/** Verifica se na posição (i, j) do tabuleiro pode ser colocado a parte da esquerda
 *  de um barco
 *
 *  @param tab    Tabuleiro de jogo
 *  @param i      Número da linha do tabuleiro (índice)
 *  @param j      Número da coluna do tablueiro (índice)
 *
 *  @return 1 caso seja possível colocar a parte da esquerda de um barco, 0 caso contrário
 */
int eBarcoEsquerda(Tab_BN *tab, int i, int j)
{
	int esquerda = 1;
	
	if(j - 1 >= 0) esquerda = (ESTADO(tab, i, j - 1) == AGUA);
	
	esquerda = (esquerda && j + 1 < COLUNAS(tab) && (ESTADO(tab, i, j + 1) == OCUPADO || ESTADO(tab, i, j + 1) == BARCO_DIR || ESTADO(tab, i, j + 1) == BARCO_CENTRAL));

	return esquerda;
}


/** Verifica se na posição (i, j) do tabuleiro pode ser colocado a parte da direita
 *  de um barco
 *
 *  @param tab    Tabuleiro de jogo
 *  @param i      Número da linha do tabuleiro (índice)
 *  @param j      Número da coluna do tablueiro (índice)
 *
 *  @return 1 caso seja possível colocar a parte da direita de um barco, 0 caso contrário
 */
int eBarcoDireita(Tab_BN *tab, int i, int j)
{
	int direita = 1;
	
	direita = (j - 1 >= 0 && (ESTADO(tab, i, j - 1) == OCUPADO || ESTADO(tab, i, j - 1) == BARCO_ESQ || ESTADO(tab, i, j - 1) == BARCO_CENTRAL));

	if(j + 1 < COLUNAS(tab)) direita = (direita && ESTADO(tab, i, j + 1) == AGUA);

	return direita;
}


/** Verifica se na posição (i, j) do tabuleiro pode ser colocado a parte de cima
 *  de um barco
 *
 *  @param tab    Tabuleiro de jogo
 *  @param i      Número da linha do tabuleiro (índice)
 *  @param j      Número da coluna do tablueiro (índice)
 *
 *  @return 1 caso seja possível colocar a parte de cima de um barco, 0 caso contrário
 */
int eBarcoCima(Tab_BN *tab, int i, int j)
{
	int cima = 1;
	
	if(i - 1 >= 0) cima = (ESTADO(tab, i - 1, j) == AGUA);

	cima = (cima && i + 1 < LINHAS(tab) && (ESTADO(tab, i + 1, j) == OCUPADO || ESTADO(tab, i + 1, j) == BARCO_BAIXO || ESTADO(tab, i + 1, j) == BARCO_CENTRAL));

	return cima;
}


/** Verifica se na posição (i, j) do tabuleiro pode ser colocado a parte de baixo
 *  de um barco
 *
 *  @param tab    Tabuleiro de jogo
 *  @param i      Número da linha do tabuleiro (índice)
 *  @param j      Número da coluna do tablueiro (índice)
 *
 *  @return 1 caso seja possível colocar a parte de baixo de um barco, 0 caso contrário
 */
int eBarcoBaixo(Tab_BN *tab, int i, int j)
{
	int baixo = 1;
	
	baixo = (i - 1 >= 0 && (ESTADO(tab, i - 1, j) == OCUPADO || ESTADO(tab, i - 1, j) == BARCO_CIMA || ESTADO(tab, i - 1, j) == BARCO_CENTRAL));

	if(i + 1 < LINHAS(tab))  baixo = (baixo && ESTADO(tab, i + 1, j) == AGUA);

	return baixo;
}


/** Verifica se na posição (i, j) do tabuleiro pode ser colocado a parte central
 *  de um barco
 *
 *  @param tab    Tabuleiro de jogo
 *  @param i      Número da linha do tabuleiro (índice)
 *  @param j      Número da coluna do tablueiro (índice)
 *
 *  @return 1 caso seja possível colocar a parte central de um barco, 0 caso contrário
 */
int eBarcoCentral(Tab_BN *tab, int i, int j)
{
	int central = 0;

	if((j - 1 >= 0 && (ESTADO(tab, i, j - 1) == OCUPADO ||
		               ESTADO(tab, i, j - 1) == BARCO_ESQ ||
		               ESTADO(tab, i, j - 1) == BARCO_CENTRAL)
		) && (j + 1 < COLUNAS(tab) && (ESTADO(tab, i, j + 1) == OCUPADO ||
		               				   ESTADO(tab, i, j + 1) == BARCO_DIR ||
		               				   ESTADO(tab, i, j + 1) == BARCO_CENTRAL))) central = 1;

	if((i - 1 >= 0 && (ESTADO(tab, i - 1, j) == OCUPADO ||
		               ESTADO(tab, i - 1, j) == BARCO_CIMA ||
		               ESTADO(tab, i - 1, j) == BARCO_CENTRAL)
		) && (i + 1 < LINHAS(tab) && (ESTADO(tab, i + 1, j) == OCUPADO ||
		               				  ESTADO(tab, i + 1, j) == BARCO_BAIXO ||
		               				  ESTADO(tab, i + 1, j) == BARCO_CENTRAL))) central = 1;

	return central;
}


/** Coloca o segmento correspondente numa casa onde tem de existir
 *  necessariamente um segmento
 *
 *  @param tab    Tabuleiro de jogo
 *  @param linha  Número da linha
 *  @param coluna Número da coluna
 *
 *  @return 1 se o estado do tabuleiro mudou, 0 caso contrário
 */
int colocaSegmento(Tab_BN *tab, int linha, int coluna)
{
	int mudou = 0;
	int i = linha - 1, j = coluna - 1;

	if(eSubmarino(tab, i, j)) {
		guardaCasaHistorico(tab, i, j);
		ESTADO(tab, i, j) = SUBMARINO;
		mudou = 1;
	}
	else if(eBarcoCentral(tab, i, j)) {
		guardaCasaHistorico(tab, i, j);
		ESTADO(tab, i, j) = BARCO_CENTRAL;
		mudou = 1;
	}
	else if(eBarcoEsquerda(tab, i, j)) {
		guardaCasaHistorico(tab, i, j);
		ESTADO(tab, i, j) = BARCO_ESQ;
		mudou = 1;
	}
	else if(eBarcoDireita(tab, i, j)) { 
		guardaCasaHistorico(tab, i, j);
		ESTADO(tab, i, j) = BARCO_DIR;
		mudou = 1;
	}
	else if(eBarcoCima(tab, i, j)) {
		guardaCasaHistorico(tab, i, j);
		ESTADO(tab, i, j) = BARCO_CIMA;
		mudou = 1;
	}
	else if(eBarcoBaixo(tab, i, j)) {
		guardaCasaHistorico(tab, i, j);
		ESTADO(tab, i, j) = BARCO_BAIXO;
		mudou = 1;
	}

	return mudou;
}


/** Imprime um tabuleiro
 *
 *  @param tab    Tabuleiro que vai ser apresentado
 */
void imprimeTabuleiro(Tab_BN *tab)
{
	short linhas = 0, colunas = 0;
	short i = 0, j = 0;

	linhas = LINHAS(tab);
	colunas = COLUNAS(tab);

	for(i = 0; i < linhas; i++)
	{
		for(j = 0; j < colunas; j++)
			printf("%c", ESTADO(tab, i, j));

		printf(" %d\n", SEGLINHA(tab, i));
	}

	for(j = 0; j < colunas; j++)
		printf("%d", SEGCOLUNA(tab, j));

	printf("\n");
}


/** Guarda uma casa no histórico do tabuleiro
 *
 *  @param tab  Tabuleiro de jogo
 *  @param x    Coordenada em x
 *  @param y    Coordenada em y
 *
 *  @return 1 se a casa foi guardada no histórico, 0 caso contrário
 */
int guardaCasaHistorico(Tab_BN *tab, int x, int y)
{
	int guarda = 0;
	Casa *casa = NULL;
	
	casa = malloc(sizeof(Casa));
	
	if(casa != NULL)
	{
		COORDENADA_X(casa)   = x;
		COORDENADA_Y(casa)   = y;
		ESTADO_CASA(casa)    = ESTADO(tab, x, y);
		
		guarda = insere_cabeca_LLigada(HISTORICO(tab), casa);
		
		if(!guarda) free(casa);
	}
	
	return guarda;
}


/** Coloca uma marca no histórico do tabuleiro. Uma marca separa as casas
 *  guardadas no histórico segundo uma determinada regra (definida pelo
 *  utilizador)
 *
 *  @param tab  Tabuleiro de jogo
 *
 *  @return 1 se a marca foi colocada com sucesso, 0 caso contrário
 */
int colocaMarcaHistorico(Tab_BN *tab)
{
	int coloca = 0;
	Casa *casa = NULL;
	
	casa = malloc(sizeof(Casa));
	
	if(casa != NULL)
	{
		COORDENADA_X(casa)   =  -1;
		COORDENADA_Y(casa)   =  -1;
		ESTADO_CASA(casa)    =  -1;
		
		coloca = insere_cabeca_LLigada(HISTORICO(tab), casa);
		
		if(!coloca) free(casa);
	}
	
	return coloca;
}


/** Verifica se no topo do histório está uma marca
 *
 *  @param tab  Tabuleiro de jogo
 *
 *  @return 1 se for uma marca, 0 caso contrário
 */
int eMarcaHistorico(Tab_BN *tab)
{
	Casa *casa = NULL;
	
	casa = ver_cabeca_LLigada(HISTORICO(tab));
	
	return casa != NULL && COORDENADA_X(casa) == -1;
}


/** Regressa ao estado anterior do tabuleiro. O estado de todas as casas
 *  é alterado até ser encontrada uma marca no historico do tabuleiro
 *
 *  @param tab  Tabuleiro de jogo
 *
 *  @return 1 se o tabuleiro foi modificado e voltou ao estado anterior, 0
 *          caso contrário
 */
int regressaTabuleiroAnterior(Tab_BN *tab)
{
	short x = 0, y = 0;
	int regressou = 1;
	Casa *casa = NULL;

	/* Retirar todas as marcas até encontrar uma casa que 
	 tenha sido modificada*/
	casa = remove_cabeca_LLigada( HISTORICO(tab) );
	while(casa != NULL && COORDENADA_X(casa) == -1)
	{
		free(casa);
		casa = remove_cabeca_LLigada( HISTORICO(tab) );
	}
	
	if(casa == NULL) regressou = 0;
	else 
	{
		do 
		{
			x = COORDENADA_X(casa);
			y = COORDENADA_Y(casa);
			ESTADO(tab, x, y)    = ESTADO_CASA(casa);
			
			free(casa);
			casa = remove_cabeca_LLigada(HISTORICO(tab));
		} while(casa != NULL && COORDENADA_X(casa) != -1);
		
		/* Remover marca */
		if(casa != NULL) free(casa);
	}
	
	return regressou;
}
