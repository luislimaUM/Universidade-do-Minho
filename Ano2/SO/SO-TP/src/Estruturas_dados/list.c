#include <stdio.h>
#include <stdlib.h>

// Guarda os comandos que dependem de outro
typedef struct list {
	unsigned int val;
	struct list *next;
} *List;

unsigned int *list2array(List l, int *size_array) {
	List aux = NULL;
	unsigned int *a = NULL;
	int size = 0, i = 0;
	aux = l;
	if(aux) {
		while(aux) {
			aux = aux->next;
			++size;
		}
		aux = l; i = 0;
		a = (unsigned int *) calloc(size, sizeof(unsigned int));
		while(aux) {
			a[i++] = aux->val;
			aux = aux->next;
		}
	}
	*size_array = size;
	return a;
}

// Cria uma nova estrutura de list com um determinado valor
List new_list(unsigned int valor) {
	List new = NULL;
	new = (List) malloc(sizeof(struct list));
	if(new != NULL){
		new->val = valor;
		new->next = NULL;
	}
	return new;
}

// Adiciona no fim da lista o valor
List add_list(List list, unsigned int valor) {
	List new = NULL, aux = NULL;
	new = new_list(valor);
	aux = list;

	if(list == NULL){
		list = new;
	}
	else {
		while(aux->next != NULL){
			aux = aux->next;
		}
		aux->next = new;
	}
	return list;
}

// Libertação da memória da estrutura
void free_list(List list){
	List aux = NULL, ant = NULL;
	aux = list;
	while(aux){
		ant = aux;
		aux = aux->next;
		free(ant);
	}
}

// Impressão da lista
void print_list(List list){
	List aux = list;
	while(aux != NULL){
		printf("%d->", aux->val);
		aux = aux->next;
	}
	printf("\n");
}