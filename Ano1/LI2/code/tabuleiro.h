
#ifndef __TABULEIRO_H__
#define __TABULEIRO_H__


/* Definião do tipo Tab_BN que representa a estrutura de um tabuleiro */
typedef struct sTab_BN Tab_BN;


/* Inicia um tabuleiro de jogo */
Tab_BN *iniciaTabuleiro();

/* Remove um tabuleiro de jogo. Liberta toda a memória alocada pelo tabuleiro */
Tab_BN *removeTabuleiro(Tab_BN *tab);

/* Verifica se um tabuleiro é válido (se existe tabuleiro). Um tabuleiro é 
considerado válido se o seu estado estiver iniciado e se as suas dimensões
estão definidas */
int existeTabuleiro(Tab_BN *tab);

/* Verifica se as dimensões podem ser aplicadas a um tabuleiro */
int dimensoesValidas(int linhas, int colunas);

/* Verifica se existe o número de linha no tabuleiro */
int linhaValida(Tab_BN *tab, int numLinha);

/* Verifica se existe o número de coluna no tabuleiro */
int colunaValida(Tab_BN *tab, int numColuna);

/* Devolve o número de colunas do tabuleiro */
short getNumColunas(Tab_BN *tab);

/* Devolve o número de linhas do tabuleiro */
short getNumLinhas(Tab_BN *tab);

/* Define as dimensões de um tabuleiro */
void defineDimensoes(Tab_BN *tab, int linhas, int colunas);

/* Verifica se o estado do tabuleiro é não determinado na linha e coluna 
passados como parâmetros */
int valorNaoDeterminado(Tab_BN *tab, int linha, int coluna);

/* Verifica se o estado do tabuleiro é água na linha e coluna passados
como parâmetros */
int valorAgua(Tab_BN *tab, int linha, int coluna);

/* Verifica se o estado do tabuleiro está definido como ocupado na linha
e coluna passados como parâmetros */
int valorOcupado(Tab_BN *tab, int linha, int coluna);

/* Marca o estado de uma casa do tabuleiro como sendo água */
void marcaAgua(Tab_BN *tab, int linha, int coluna);

/* Marca o estado de uma casa do tabuleiro como sendo ocupado */
void marcaOcupado(Tab_BN *tab, int linha, int coluna);

/* Define o número de segmentos de uma determinada linha */
void defineNumSegmentosLinha(Tab_BN *tab, int linha, int numSeg);

/* Retorna o número de segmentos de uma determinada linha */
int getNumSegmentosLinha(Tab_BN *tab, int linha);

/* Define o número de segmentos de uma determinada coluna */
void defineNumSegmentosColuna(Tab_BN *tab, int coluna, int numSeg);

/* Retorna o número de segmentos de uma determinada coluna */
int getNumSegmentosColuna(Tab_BN *tab, int coluna);

/* Define o estado do tabuleiro na linha e na coluna passados como parâmetros */
void defineEstado(Tab_BN *tab, char estado, int linha, int coluna);

/* Retorna o estado do tabuleiro na linha e na coluna passados como parâmetros */
char getEstado(Tab_BN *tab, int linha, int coluna);

/* Conta o número de segmentos de uma determinada linha */
int contaNumSegmentosLinha(Tab_BN *tab, int linha);

/* Conta o número de segmentos de uma determinada coluna */
int contaNumSegmentosColuna(Tab_BN *tab, int coluna);

/* Conta o número de casas não determinadas de uma determinada linha */
int contaNumNaoDetrminadosLinha(Tab_BN *tab, int linha);

/* Conta o número de casas não determinadas de uma determinada coluna */
int contaNumNaoDetrminadosColuna(Tab_BN *tab, int coluna);

/* Marca todas as casas de uma determinada linha como sendo água,
caso o seu valor não esteja determinado */
int marcaAguaLinha(Tab_BN *tab, int linha);

/* Marca todas as casas de uma determinada coluna como sendo água, 
caso o seu valor não esteja determinado */
int marcaAguaColuna(Tab_BN *tab, int coluna);

/* Marca as diagonais de um segmento como sendo água, caso seja possível */
int marcaDiagonaisAgua(Tab_BN *tab, int linha, int coluna);

/* Marca as casas adjacentes de um segmento como sendo água, caso seja possível */
int marcaAdjacentesAgua(Tab_BN *tab, int linha, int coluna);

/* Coloca o segmento correspondente numa casa onde tem de existir necessariamente um segmento */
int colocaSegmento(Tab_BN *tab, int linha, int coluna);

/* Imprime um tabuleiro */
void imprimeTabuleiro(Tab_BN *tab);

/* Guarda uma casa no histórico do tabuleiro */
int guardaCasaHistorico(Tab_BN *tab, int x, int y);

/* Coloca uma marca no histórico do tabuleiro. Uma marca separa as casas
guardadas no histórico segundo uma determinada regra (definida pelo utilizador) */
int colocaMarcaHistorico(Tab_BN *tab);

/* Verifica se no topo do histório está uma marca */
int eMarcaHistorico(Tab_BN *tab);

/* Regressa ao estado anterior do tabuleiro. O estado de todas as casas
é alterado até ser encontrada uma marca no historico do tabuleiro */
int regressaTabuleiroAnterior(Tab_BN *tab);


#endif
