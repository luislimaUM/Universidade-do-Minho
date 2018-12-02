#include "users.h"
#include "interface.h"
#include <stdlib.h>
#include <user.h>
#include <common.h>
#include <stdio.h>
#include <string.h>
#include <libxml/xmlmemory.h>
#include <libxml/parser.h>
#include <date.h>

#define BAL 0              //Balanceada
#define ESQ (-1)		   //Ramo esquerdo mais pesado	
#define DIR 1 			   //Ramo direito mais pesado

typedef struct allUser{
	long idU;				//identificador do User
	char *name;				//Nome do User
	int numPosts;			//numero total de posts deste utilizador
	char *bio;				//biografia user
	int reputation;

	long last10[10];		//ultimos 10 posts do user
	Date last10Time[10];	//timestamps dos ultimos 10 posts do user	
	int bal;
	int height;
	struct allUser *left;
	struct allUser *right;
}*User;

TAD_Users initU(){
	return NULL;
}

TAD_Users novoNodoC(long id, char* name, char* bio,int rep){
	TAD_Users new = malloc(sizeof(struct allUser));
	new -> idU = id;
	//aloca e duplica 
	new -> name = mystrdup(name);
	new -> bio = mystrdup(bio);
	new -> numPosts = 0;
	new -> reputation = rep;
	int i;

	for(i=0;i<10;i++){
		new -> last10[i] = 0;
		new -> last10Time[i] = createDate(0,0,0);
	}

	new -> bal = BAL;
	new -> height = 1;
	new -> left = NULL;
	new -> right = NULL;
	return new;
}

int heightU(TAD_Users us){
	int e = 0;
	int d = 0;

	if(us){
		if(us->left) e = us->left->height;
		if(us->right) d = us->right->height;
		return(e > d) ? (e+1) : (d+1);
	}

	return 0;
}

TAD_Users rotateRightU(TAD_Users p){
	TAD_Users aux;

	aux = p->left;
	p -> left = aux -> right;
	aux -> right = p;

	return aux;
}

TAD_Users rotateLeftU(TAD_Users p){
	TAD_Users aux;

	aux = p->right;
	p -> right = aux -> left;
	aux -> left = p;

	return aux;
}

TAD_Users insereUser(TAD_Users us, long id, char *name,char *bio,int rep,TAD_Users *pt){

	if(!us){
		(*pt) = novoNodoC(id,name,bio,rep);
		return (*pt);
	}else if(us->idU > id){
		//inserçao ramo esquerdo
		us->left = insereUser(us->left,id,name,bio,rep,pt);
		us->bal = heightU(us->right)-heightU(us->left);

		//bem balanceado
		if(us->bal >= ESQ && us->bal <= DIR) us->height = heightU(us);

		//mal balanceado
		if(us->bal < ESQ){
			if(us->left->bal == ESQ){ //Rotaçao simples
				us = rotateRightU(us);

				//atualizar variaveis de altura e balanceamento 
				us->right->height = heightU(us->right);
				us->height = heightU(us);
				us->bal = heightU(us->right)-heightU(us->left);
				us->right->bal = heightU(us->right->right)-heightU(us->right->left);
			}else{
				//rotaçao dupla
				us->left = rotateLeftU(us->left);
				us = rotateRightU(us);

				//atualizar alturas e fatores de balanceamento
				us->left -> height = heightU(us->left);
				us->right -> height = heightU(us->right);
				us->height = heightU(us);

				us->bal = heightU(us->right)-heightU(us->left);
				us->left->bal = heightU(us->left->right)-heightU(us->left->left);
				us->right->bal = heightU(us->right->right)-heightU(us->right->left);
			}
		}
	}else{
		//inserçao ramo direito
		us->right = insereUser(us->right,id,name,bio,rep,pt);
		us->bal = heightU(us->right)-heightU(us->left);

		//bem balanceado
		if(us->bal >= ESQ && us->bal <= DIR) us->height = heightU(us);

		//mal balanceado
		if(us->bal > DIR){
			if(us->right->bal == DIR){ //Rotaçao simples
				us = rotateLeftU(us);

				//atualizar variaveis de altura e balanceamento 
				us->left->height = heightU(us->left);
				us->height = heightU(us);
				us->bal = heightU(us->right)-heightU(us->left);
				us->left->bal = heightU(us->left->right)-heightU(us->left->left);
			}else{
				//rotaçao dupla
				us->right = rotateRightU(us->right);
				us = rotateLeftU(us);

				//atualizar alturas e fatores de balanceamento
				us->left -> height = heightU(us->left);
				us->right -> height = heightU(us->right);
				us->height = heightU(us);

				us->bal = heightU(us->right)-heightU(us->left);
				us->left->bal = heightU(us->left->right)-heightU(us->left->left);
				us->right->bal = heightU(us->right->right)-heightU(us->right->left);
			}
		}
	}

	return us;

}

char* getName(TAD_Users us,long id){
	int flag = 0;
	char * name = NULL;

	while(us && !flag){
		if(us->idU == id){
			flag = 1;
			name = mystrdup(us->name);
		}else if(us->idU > id) us = us -> left;
			else us = us -> right;
	}
	return name;
}

//incrementar total de utilizadores, é passado o nodo do utilizador que tera o numero de contribuiçoes incrementado
void incrementaT(TAD_Users *us){
	(*us)->numPosts++;
	return;
}

int smaller1(Date a, Date b){
	
	if(get_year(a) < get_year(b)) return 1; //comparar anos
	if(get_year(a) == get_year(b) && get_month(a) < get_month(b)) return 1; //comparar meses mas anos tem de ser iguais
	if(get_year(a) == get_year(b) && get_month(a) == get_month(b) && get_day(a) <= get_day(b)) return 1; //comparar dias
	
	return 0;
}

int estaNoTop(long id[],long idU,int N){
	int flag = 0,i;

	for(i=0;i<N && !flag;i++){
		if(id[i]==idU){
			flag = 1;
		}
	}
	return flag;
}

//ordenar o 1 apenas
void ordenaTime(long id[], Date time[],int N){
	int i,c;
	Date aux;

	for(i=0;i<N-1 && !smaller1(time[i],time[i+1]);i++){
		aux = time[i];
		time[i] = time[i+1];
		time[i+1] = aux;
		//ordena id
		c = id[i];
		id[i] = id[i+1];
		id[i+1] = c;
	}
	return;
}

//atualiza arrays dos ultimos 10 posts do user
void atualizaP(TAD_Users *us,char *time,long idPost){
	int day,month,year;
	int N = 10;

	sscanf(time,"%d-%d-%d",&year,&month,&day);
	Date d = createDate(day,month,year);
	Date aux = (*us)->last10Time[0];
	if(smaller1(aux,d) && !estaNoTop((*us)->last10,idPost,N)){
		//temos que adicionar
		(*us)->last10[0] = idPost;
		(*us)->last10Time[0] = d;
		ordenaTime((*us)->last10,(*us)->last10Time,N);
		}
	return;
}

//verifica se utilizador ja existe, se sim, incrementa seu numero de contribuiçoes
int contemUser(TAD_Users us, long id, char* time, long idPost){
	int flag = 0;

	while(us && !flag){
		if(us->idU==id){
			flag = 1;
			incrementaT(&us);
			atualizaP(&us,time,idPost);
		}else if(us->idU > id) us = us -> left;
			else us = us -> right;
	}
	return flag;
}

//ordena valor apenas da 1º posiçao, restantes ja estao ordenados
void ordena(long id[],int pt[],int N){
	int i;
	int c;
	for(i=0;i<N-1 && pt[i]>pt[i+1];i++){
		c = pt[i];
		pt[i] = pt[i+1];
		pt[i+1] = c;
		//ordena id
		c = id[i];
		id[i] = id[i+1];
		id[i+1] = c;
	}

	return;
}

void mostActive(TAD_Users tad,int N,long id[],int p[]){

	if(tad){
		if(!estaNoTop(id,tad->idU,N) && tad->numPosts > p[0]){
			//se nao estiver e for maior, temos que atualizar 
			//insere/remove no topo e ordena lista
			id[0] = tad->idU;
			p[0] = tad->numPosts;
			ordena(id,p,N);
		}
		mostActive(tad->left,N,id,p);
		mostActive(tad->right,N,id,p);
	}

	return;
}

void getUserIds(TAD_Users us, long id, long ids[],int N,char **b){
	int flag = 0,i,j=0;

	while(us && !flag){
		if(us->idU==id){
			flag = 1;
		}else if(us->idU > id) us = us -> left;
			else us = us -> right;
	}
	if(us){
		for(i=N-1;i>=0;i--){
			ids[i] = us->last10[j];
			j++;
		}
		(*b) = us->bio;
	}
	return;
}

int getReputation(TAD_Users us, long id){
	int flag = 0;
	int aux = 0;

	while(us && !flag){
		if(us->idU==id){
			flag = 1;
			aux = us->reputation;
		}else if(us->idU > id) us = us -> left;
			else us = us -> right;
	}
	return aux;
}

void freeUser(TAD_Users *u){
	if(*u){
		freeUser(&((*u)->left));
		freeUser(&((*u)->right));
		free((*u)->name);
		free((*u)->bio);
		free(*u);
	}
}