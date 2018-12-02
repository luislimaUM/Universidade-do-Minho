
#ifndef __COMANDOS_H__
#define __COMANDOS_H__

#include "tabuleiro.h"


/* Comando que carrega um tabuleiro a partir do stdin */
Tab_BN *cmd_c(Tab_BN *tab);

/* Comando que imprime um tabuleiro para o stdout */
Tab_BN *cmd_m(Tab_BN *tab);

/* Comando que carrega um tabauleiro a partir de um ficheiro */
Tab_BN *cmd_l(Tab_BN *tab, char *nomeFicheiro);

/* Comando que permite gravar um tabuleiro num ficheiro */
Tab_BN *cmd_e(Tab_BN *tab, char *nomeFicheiro);

/* Comando que coloca o estado de todas as grelhas de uma determinada linha
que ainda não estão determinadas como sendo água */
Tab_BN *cmd_h(Tab_BN *tab, int numLinha);

/* Comando que coloca o estado de todas as grelhas de uma determinada coluna
que ainda não estão determinadas como sendo água */
Tab_BN *cmd_v(Tab_BN *tab, int numColuna);

/* Comando que coloca que define o estado do tabuleiro na linha e 
coluna passados como parâmetro */
Tab_BN *cmd_p(Tab_BN *tab, char estado, int linha, int coluna);

/* Aplicação da estratégia 1. Esta estratégia coloca água que se deduza que
vai existir à volta de todos os segmentos de barcos já colocados */
Tab_BN *cmd_e1(Tab_BN *tab);

/* Aplicação da estratégia 2. Esta estratégia coloca água nas linhas e colunas em
que todos os segmentos de barcos já foram colocados */
Tab_BN *cmd_e2(Tab_BN *tab);

/* Aplicação da estratégia 3. Esta estratégia coloca segmentos de barcos nas linhas e colunas
nas quais todos os espaços vazios têm que conter segmentos de barcos para que o número
correspondente seja respeitado */
Tab_BN *cmd_e3(Tab_BN *tab);

/* Comando V que verifica se um tabuleiro apresenta uma solução válida */
Tab_BN *cmd_V(Tab_BN *tab);

/* Aplica o comando resolve. Este comando apenas aplica todas as estratégias
até o tabuleiro não ser mais modificado */
Tab_BN *cmd_R(Tab_BN *tab);

/* Comando que anula um comando que modificou o estado do tabuleiro. 
Se não existirem comandos para anular, é reportada uma mensagem de erro */
Tab_BN *cmd_D(Tab_BN *tab);

/* Comando que elimina um tabuleiro e sai do jogo */
Tab_BN *cmd_q(Tab_BN *tab);


#endif
