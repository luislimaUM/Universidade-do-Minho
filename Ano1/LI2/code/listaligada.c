
#include <stdlib.h>

#include "listaligada.h"



/* --------------------  ESTRUTURAS DE DADOS  -------------------- */


/** Estrutura que define o nodo de uma lista ligada. Esta 
 *  contém um apontador para os dados que se pretendem armazenar
 *  no nodo e um apontador para o nodo seguinte
 */
typedef struct sNodo
{
    void *dados;
    struct sNodo *seg;
} NODO;



/** Estrutura que define uma lista ligada. Contém um apontador para
 *  a cabeça da lista e o número de nodos da lista ligada
 */
typedef struct sLLigada
{
    unsigned int num_nodos;
    NODO *nodo;
} LLIGADA;


/* --------------------------------------------------------------- */



/** Inicia uma lista ligada. Coloca o número de nodos a zero
 *  e prepara a lista para a inserção e remoção de dados
 *
 *  @return Apontador para a lista ligada se tudo correu bem ou
 *          NULL caso contrário
 */
void *cria_LLigada(void)
{
    LLIGADA *lista = NULL;

    lista = malloc(sizeof(LLIGADA));

    if(lista != NULL)
    {
        lista->num_nodos = 0;
        lista->nodo = NULL;
    }

    return lista;
}



/** Elimina uma lista ligada. Toda a memória alocada para a lista 
 *  ligada é libertada
 *
 *  @param lista  Lista ligada que vai ser destruida
 */
void elimina_LLigada(void **lista)
{
	LLIGADA *aux = NULL;
    NODO *anterior = NULL, *actual = NULL;
	
	aux = *lista;
    actual = aux->nodo;

    while(actual != NULL)
    {
        anterior = actual;
        actual = actual->seg;

        free(anterior);
    }

    free(aux);
    *lista = NULL;
}



/** Insere um elemento à cabeça da lista ligada. No momento da
 *  inserção não é efectuada qualquer cópia dos dados, pelo que
 *  estes devem ser armazenados enquanto for utilizada a lista
 *  ligada correspondente
 *
 *  @param lista  Lista ligada onde vai ser inserido o elemento
 *  @param dados  Dados que vão ser inseridos à cabeça da lista
 *
 *  @return 1 se os dados forem inseridos, 0 caso contrário
 */
int insere_cabeca_LLigada(void *lista, void *dados)
{
	LLIGADA *aux = NULL;
    NODO *novo = NULL;

    novo = malloc(sizeof(NODO));
    
    if(novo == NULL) return 0;

	aux = lista;
	
    novo->dados = dados;
    novo->seg = aux->nodo;
	
	aux->nodo = novo;
	aux->num_nodos++;
	
    return 1;
}



/** Remove um elemento à cabeça da lista ligada. Se a lista
 *  tiver elementos, o elemento que está à cabeça é removido
 *
 *  @param lista  Lista ligada onde vai ser removido o elemento
 *
 *  @return O elemento que está à cabeça se a lista não for vazia,
 *          NULL caso contrário
 */
void *remove_cabeca_LLigada(void *lista)
{
	LLIGADA *aux = NULL;
    NODO *actual = NULL;
	void *dados = NULL;
	
	aux = lista;
	actual = aux->nodo;
	
    if(actual != NULL)
    {
		dados = actual->dados;
		
		aux->nodo = actual->seg;
		free(actual);
		
		aux->num_nodos--;
    }
	
    return dados;
}



/** Devolve o elemento que está à cabeça da lista ligada
 *
 *  @param lista  Lista Ligada que contém um determinado elemento
 *
 *  @return O elemento que está à cabeça se a lista não for vazia
 */
void *ver_cabeca_LLigada(void *lista)
{
	LLIGADA *aux = NULL;
	NODO *actual = NULL;
	void *dados = NULL;
	
	aux = lista;
	actual = aux->nodo;
	
	if(actual != NULL) dados = actual->dados;
	
	return dados;
}



/** Verifica se a lista ligada está vazia
 *
 *  @param lista  Lista ligada que se pretende verificar se está vazia
 *
 *  @return 1 se está vazia, 0 caso contrário
 */
int esta_vazia(void *lista)
{
	LLIGADA *aux = NULL;
	
	aux = lista;
	
	return aux->num_nodos == 0;
}

