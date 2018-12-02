#include <stdlib.h>
#include "array.h"

void initArray(Array *a, int size){
	a -> array = (long *)malloc(size * sizeof(long));
	a -> times = (char **)malloc(size * sizeof(char *));
	a -> used = 0;
	a -> size = size;
}

int existe(long element,Array *a){
	int i;
	for(i=0;i<a->used;i++){
		if(a->array[i]==element) return 1;
	}
	return 0;
}
//se ja existir nao insere
void insertArray(Array *a, long element,char *time){
	if(!existe(element,a)){
		if(a->used == a->size){
			a->size = a->size * 2;
			a->array = (long *)realloc(a->array,a->size * sizeof(long));
			a->times = (char **)realloc(a->times,a->size * sizeof(char*));
		}
	
		a->array[a->used] = element;
		a->times[a->used] = time;
		a->used++;
	}
}

void freeArray(Array *a){
	free(a->array);
	a->array = NULL;
	a->times = NULL;
	a->used = a->size = 0;
}