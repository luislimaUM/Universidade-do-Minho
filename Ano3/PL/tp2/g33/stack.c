#include <stdlib.h>

/* if ou ciclo */
typedef struct item{
	int nr;
	struct item *downIF;
} *Item;

Item se = NULL;
Item ciclo = NULL;

void insertIF(int nr){
	Item i = (Item)malloc(sizeof(struct item));
	i->nr = nr;
	i->downIF = se;
	se = i;
}

int rmvIF(){
	int res = se->nr;
	se = se->downIF;
	return res;
}

int tellmeIF(){
	return se->nr;
}

void insertWHILE(int nr){
	Item c = (Item)malloc(sizeof(struct item));
	c->nr = nr;
	c->downIF = ciclo;
	ciclo = c;
}

int rmvWHILE(){
	int res = ciclo->nr;
	ciclo = ciclo->downIF;
	return res;
}
