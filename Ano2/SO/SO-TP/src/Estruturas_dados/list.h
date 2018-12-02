#ifndef _LIST_
#define _LIST_

struct list;
typedef struct list *List;

List add_list(List,unsigned int );
List next_list(List);
unsigned int *list2array(List,int *);
void free_list(List);
void print_list(List);

#endif