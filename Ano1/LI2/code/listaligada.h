
#ifndef __LISTALIGADA_H__
#define __LISTALIGADA_H__


/* Inicia uma lista ligada. Coloca o número de nodos a zero
 * e prepara a lista para a inserção e remoção de dados
 */
void *cria_LLigada(void);

/* Elimina uma lista ligada. Toda a memória alocada para a lista 
 * ligada é libertada
 */
void elimina_LLigada(void **lista);

/* Insere um elemento à cabeça da lista ligada. No momento da
 * inserção não é efectuada qualquer cópia dos dados, pelo que
 * estes devem ser armazenados enquanto for utilizada a lista
 * ligada correspondente
 */
int insere_cabeca_LLigada(void *lista, void *dados);

/* Remove um elemento à cabeça da lista ligada. Se a lista
 * tiver elementos, o elemento que está à cabeça é removido
 */
void *remove_cabeca_LLigada(void *lista);

/* Devolve o elemento que está à cabeça da lista ligada */
void *ver_cabeca_LLigada(void *lista);

/* Verifica se a lista ligada está vazia */
int esta_vazia(void *lista);


#endif
