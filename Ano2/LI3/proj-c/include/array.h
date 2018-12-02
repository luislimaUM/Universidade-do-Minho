typedef struct {
	long *array;
	char **times;
	int size;
	int used;
}Array;

void initArray(Array *, int);
int existe(long,Array *);
void insertArray(Array *, long, char*);
void freeArray(Array *);