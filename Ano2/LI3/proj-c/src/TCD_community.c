#include "interface.h"
#include "users.h"
#include "array.h"
#include <stdio.h>
#include <string.h>
#include <stdlib.h>
#include <libxml/xmlmemory.h>
#include <libxml/parser.h>
#include <common.h>
#include <date.h>

#define BAL 0              //Balanceada
#define ESQ (-1)		   //Ramo esquerdo mais pesado	
#define DIR 1 			   //Ramo direito mais pesado
#define UPMOD 2            	//Up Votes
#define DOWNMOD 3             //Down Votes


typedef struct allPost{
	long idPost; 			//identificador post
	char *title; 			//titulo do post - so para perguntas
	int typePost;			//tipo:1-Pergunta,2-Resposta
	long idUser;			//id User que publicou Post
	long idAnswer;			//so para respostas se for resposta temos que guardar idPergunta
	char *timestamp;		//timestamp do post
	//int votes;              //Up Votes - Down Votes
	int nAnswers;			//so para perguntas, se for uma resposta contem -1;
	int score;				//Up Votes - Down Votes
	int nComments;			//so para respostas - perguntas contem -1 
	char *tag;				//tags relativas ao post

	int bal;				//fator de balanceamento
	int height;				//altura do nodo - ajuda no calculo do balanceamento
	struct allPost *left;	//apontador arv esq
	struct allPost *right;	//apontador arv dir
}*Post;

typedef struct TCD_community{
	Post post; 				//apontador para estrutura onde é guardada os posts 
	TAD_Users user;			//apontador para estrutura onde são guardados todos os utilizadores 
}TCD_community;

TAD_community init(){
	
	TAD_community tad = malloc(sizeof(struct TCD_community));
	tad -> post = NULL;
	tad -> user = initU();
	return tad;
}

//criar nodo posts
Post novoPost(long id, int type, long userId, char *title, long answerId, char *timestamp,int sc,char *tags,int nC){
	Post p = malloc(sizeof(struct allPost));
	p -> idPost = id;
	p -> title = mystrdup(title);
	p -> typePost = type;
	p -> idUser = userId;
	p -> idAnswer = answerId;
	p -> timestamp = mystrdup(timestamp);
	//p -> votes = 0;
	p -> score = sc;
	p -> tag = mystrdup(tags);
	p -> nComments = nC;
	if(type == 1){
		p -> nAnswers = 0;
	}else{
		p -> nAnswers = -1;
	} 

	p->bal = BAL;
	p->height = 1;
	p->left = NULL;
	p->right = NULL;

	return p;
}

int height(Post p){
	int e = 0;
	int d = 0;

	if(p){
		if(p->left) e = p->left->height;
		if(p->right) d = p->right->height;
		return(e > d) ? (e+1) : (d+1);
	}

	return 0;
}

Post rotateRight(Post p){
	Post aux;

	aux = p->left;
	p -> left = aux -> right;
	aux -> right = p;

	return aux;
}

Post rotateLeft(Post p){
	Post aux;

	aux = p->right;
	p -> right = aux -> left;
	aux -> left = p;

	return aux;
}

//insere novo artigo e colocado em 'post' apontador para o local desse artigo
Post inserePost(Post p,long id,int type,long userId, char *title,long answerId,char *time,int score,char *tags,int nC, Post *post){

	//se posts for null temos que criar raiz
	if(!p){
		*post = novoPost(id,type,userId,title,answerId,time,score,tags,nC);
		return (*post);
	}else if(p->idPost > id){
		//inserçao no ramo esquerdo 
		p->left = inserePost(p->left,id,type,userId,title,answerId,time,score,tags,nC,post);
		p->bal = height(p->right)-height(p->left);

		//bem balanceado
		if(p->bal >= ESQ && p->bal <= DIR) p->height = height(p);

		//mal balanceado
		if(p->bal < ESQ){
			if(p->left->bal == ESQ){ //Rotaçao simples
				p = rotateRight(p);

				//atualizar variaveis de altura e balanceamento 
				p->right->height = height(p->right);
				p->height = height(p);
				p->bal = height(p->right)-height(p->left);
				p->right->bal = height(p->right->right)-height(p->right->left);
			}else{
				//rotaçao dupla
				p->left = rotateLeft(p->left);
				p = rotateRight(p);

				//atualizar alturas e fatores de balanceamento
				p->left -> height = height(p->left);
				p->right -> height = height(p->right);
				p->height = height(p);

				p->bal = height(p->right)-height(p->left);
				p->left->bal = height(p->left->right)-height(p->left->left);
				p->right->bal = height(p->right->right)-height(p->right->left);
			}
		}

	}else{
		//inserçao ramo direito
		p->right = inserePost(p->right,id,type,userId,title,answerId,time,score,tags,nC,post);
		p->bal = height(p->right)-height(p->left);

		//bem balanceado
		if(p->bal >= ESQ && p->bal <= DIR) p->height = height(p);

		//mal balanceado
		if(p->bal > DIR){
			if(p->right->bal == DIR){ //Rotaçao simples
				p = rotateLeft(p);

				//atualizar variaveis de altura e balanceamento 
				p->left->height = height(p->left);
				p->height = height(p);
				p->bal = height(p->right)-height(p->left);
				p->left->bal = height(p->left->right)-height(p->left->left);
			}else{
				//rotaçao dupla
				p->right = rotateRight(p->right);
				p = rotateLeft(p);

				//atualizar alturas e fatores de balanceamento
				p->left -> height = height(p->left);
				p->right -> height = height(p->right);
				p->height = height(p);

				p->bal = height(p->right)-height(p->left);
				p->left->bal = height(p->left->right)-height(p->left->left);
				p->right->bal = height(p->right->right)-height(p->right->left);
			}
		}
	}


	return p;

}

Post getPost(Post p,long id){
	int flag = 0;
	Post aux = NULL;

	while(p && !flag){
		if(p->idPost == id){
			//ja encontrou nao precisa de continuar o ciclo
			flag = 1;
			aux = p;
		}else if(p->idPost > id) p = p -> left;
			else p = p -> right;
	}

	return aux;
}

void incrementaAnswer(Post *p){
	(*p)->nAnswers++;
	return;
}

//responsavel por inserir informaçoes dos artigos
void parsePost(TAD_community tad, xmlDocPtr doc, xmlNodePtr cur){

	long userId;
	char *title;
	char *timestamp;
	char *tags;
	long id;
	long answerId;
	int type;
	int score;
	int nC;
	Post pt;

	cur = cur -> xmlChildrenNode;
	while(cur){
		//percorrer todas as rows
		if((!xmlStrcmp(cur->name,(const xmlChar *)"row"))){
			
			type = atoi((char*)xmlGetProp(cur,(const unsigned char *)"PostTypeId"));
			if(type == 1 || type == 2){
			//atol - char para long
			id = atol((char*)xmlGetProp(cur,(const unsigned char *)"Id"));
			userId = atol((char*)xmlGetProp(cur,(const unsigned char *)"OwnerUserId"));
			timestamp = (char*)xmlGetProp(cur,(const unsigned char *)"CreationDate");
			score = atoi((char*)xmlGetProp(cur,(const unsigned char *)"Score"));
			nC = atoi((char*)xmlGetProp(cur,(const unsigned char *)"CommentCount"));			

			contemUser(tad->user,userId,timestamp,id);//incrementa/atualiza lasts contador de posts

			//porque se for tipo 2 é resposta e nao contem titulo e sim id da pergunta
			if(type==1){
				//pergunta
				answerId = -1;
				if((char*)xmlGetProp(cur,(const unsigned char *)"Title") == NULL) title = NULL;
				else title = (char*)xmlGetProp(cur,(const unsigned char *)"Title");
				tags = (char*)xmlGetProp(cur,(const unsigned char *)"Tags");
			}else if(type == 2){
				//resposta -> temos que buscar id da pergunta
				answerId = atol((char*)xmlGetProp(cur,(const unsigned char *)"ParentId"));
				title = NULL;
				tags=NULL;
				Post p = getPost(tad->post,answerId);
				if(p) incrementaAnswer(&p);
			}
				tad->post = inserePost(tad->post,id,type,userId,title,answerId,timestamp,score,tags,nC,&pt);
			}
		}

		cur = cur -> next;
	}
	return;

}

void parseUser(TAD_community tad, xmlDocPtr doc, xmlNodePtr cur){

	long id;
	int reputation;
	char *name;
	char *bio;
	TAD_Users pt;

	cur = cur -> xmlChildrenNode;

	while(cur){
		//percorrer todas as rows
		if((!xmlStrcmp(cur->name,(const xmlChar *)"row"))){
			
			id = atol((char*)xmlGetProp(cur,(const unsigned char *)"Id"));
			name = (char*)xmlGetProp(cur,(const unsigned char *)"DisplayName");
			bio = (char*)xmlGetProp(cur,(const unsigned char *)"AboutMe");
			if(bio==NULL) bio = "Não tenho bio";
			reputation = atoi((char*)xmlGetProp(cur,(const unsigned char *)"Reputation"));

			tad -> user = insereUser(tad->user,id,name,bio,reputation,&pt);
		}
		cur = cur -> next;
	}
	return;
}

/*
void atualizaVote(Post *p,int vote){
	(*p)->votes+=vote;
	return;
}

void parseVotes(TAD_community tad, xmlDocPtr doc, xmlNodePtr cur){

	long pId;
	int voteType;
	Post p;

	cur = cur -> xmlChildrenNode;

	while(cur){
		//percorrer todas as rows
		if((!xmlStrcmp(cur->name,(const xmlChar *)"row"))){
			pId = atol((char*)xmlGetProp(cur,(const unsigned char *)"PostId"));
			voteType = atoi((char*)xmlGetProp(cur,(const unsigned char *)"VoteTypeId"));
			if(voteType == UPMOD){
				p = getPost(tad->post,pId);
				if(p){	
					atualizaVote(&p,1);
				}
				//se nao existir Id nao faz nada avança
			}else if (voteType == DOWNMOD){
				p = getPost(tad->post,pId);
				if(p){	
					atualizaVote(&p,-1);
				}
			}

		}
		cur = cur -> next;
	}
	return;
}
*/

TAD_community load(TAD_community tad, char* path){
	xmlDocPtr doc1,doc2/*,doc3*/;
	xmlNodePtr cur1,cur2/*,cur3*/;
	char pathU[100],pathP[100],pathV[100];
	strcpy(pathP,path);
	strcpy(pathU,path);
	strcpy(pathV,path);

	strcat(pathP,"/Posts.xml");
	strcat(pathU,"/Users.xml");
	strcat(pathV,"/Votes.xml");

	doc2 = xmlParseFile(pathU);
	//assegurar que parse foi feito corretamente
	if(doc2 == NULL){
		fprintf(stderr, "Document not parsed sucessfuly. \n");
		exit(-1);

	}
	cur2 = xmlDocGetRootElement(doc2);
	if(cur2 == NULL){
		fprintf(stderr, "empty document\n");
		xmlFreeDoc(doc2);
		exit(-1);
	}
	if(xmlStrcmp(cur2->name, (const xmlChar *) "users")){
		fprintf(stderr, "document of wrong type, root node != users\n");
		xmlFreeDoc(doc2);
		exit(-1);
	}
	printf("COMEÇA PARSER USER\n");
	//fizemos o parse do documento corretamente
	parseUser(tad,doc2,cur2);
	xmlFreeDoc(doc2);
	printf("FEITO\n");

	/*
	doc3 = xmlParseFile(pathV);

	if(doc3 == NULL){
		fprintf(stderr, "Document not parsed sucessfuly. \n");
		exit(-1);

	}

	cur3 = xmlDocGetRootElement(doc3);
	if(cur3 == NULL){
		fprintf(stderr, "empty document\n");
		xmlFreeDoc(doc3);
		exit(-1);
	}
	if(xmlStrcmp(cur3->name, (const xmlChar *) "votes")){
		fprintf(stderr, "document of wrong type, root node != votes\n");
		xmlFreeDoc(doc3);
		exit(-1);
	}

	printf("COMEÇA PARSER VOTES\n");
	//parseVotes(tad,doc3,cur3);
	xmlFreeDoc(doc3);
	printf("FEITO\n");
	*/
	doc1 = xmlParseFile(pathP);

	if(doc1 == NULL){
		fprintf(stderr, "Document not parsed sucessfuly. \n");
		exit(-1);

	}

	cur1 = xmlDocGetRootElement(doc1);
	if(cur1 == NULL){
		fprintf(stderr, "empty document\n");
		xmlFreeDoc(doc1);
		exit(-1);
	}

	if(xmlStrcmp(cur1->name, (const xmlChar *) "posts")){
		fprintf(stderr, "document of wrong type, root node != posts\n");
		xmlFreeDoc(doc1);
		exit(-1);
	}
	printf("COMEÇA PARSER POST\n");
	parsePost(tad,doc1,cur1);
	xmlFreeDoc(doc1);
	printf("FEITO\n");
	
	return tad;
}
//querie1
//se pergunta retorna titulo post e nome do men 
//se resposta retorna titulo e nome do utilizador da pergunta 
STR_pair info_from_post(TAD_community tad, int id){
	STR_pair new;
	Post p = getPost(tad->post,id);
	if(p){ 
		if(p->typePost == 1){
			char *name = getName(tad->user,p->idUser);
			new = create_str_pair(p->title,name);
		}else{
			p = getPost(tad->post,p->idAnswer);
			char *name = getName(tad->user,p->idUser);
			new = create_str_pair(p->title,name);
		}
	}
	return new;
}
//querie 2 
LONG_list top_most_active(TAD_community tad,int N){
	long id[N];
	int nP[N];
	int i,j = 0;
	LONG_list l;
	//inicializar array com numero de posts
	for(i=0;i<N;i++){
		nP[i] = -1;
	}
	mostActive(tad->user,N,id,nP);
	l = create_list(N);
	for(i=N-1;i>=0;i--){
		set_list(l,j,id[i]);
		j++;
	}
	return l;
}

//ve se 1º data é anterior a outra 
int smaller(Date a, Date b){
	
	if(get_year(a) < get_year(b)) return 1; //comparar anos
	if(get_year(a) == get_year(b) && get_month(a) < get_month(b)) return 1; //comparar meses mas anos tem de ser iguais
	if(get_year(a) == get_year(b) && get_month(a) == get_month(b) && get_day(a) <= get_day(b)) return 1; //comparar dias
	
	return 0;
}

LONG_pair somaLong(LONG_pair c1,LONG_pair c2){
	LONG_pair c3 = create_long_pair(get_fst_long(c1)+get_fst_long(c2),get_snd_long(c1)+get_snd_long(c2));
	return c3;
}

LONG_pair totalPost(Post p,Date begin, Date end){
	int day,month,year;
	LONG_pair c = create_long_pair(0,0);//(pergunta,resposta)

	if(p){
		sscanf(p->timestamp,"%d-%d-%d",&year,&month,&day);
		Date d = createDate(day,month,year);
		if(smaller(begin,d) && smaller(d,end)){
			//esta entre as duas datas
			if(p->typePost == 1){
				//pergunta
				set_fst_long(c,get_fst_long(c)+1);
			}else{
				//resposta
				set_snd_long(c,get_snd_long(c)+1);
			}
		}
		c = somaLong(c,totalPost(p->left,begin,end));
		c = somaLong(c,totalPost(p->right,begin,end));
	}
	return c;
}

//verificar correçao
LONG_pair total_posts(TAD_community tad, Date begin, Date end){
	Post p = tad->post;
	LONG_pair c = totalPost(p,begin,end);

	return c;
}

void mostVote(Post p,int N,long id[],int v[], Date begin, Date end){
	int day,month,year;

	if(p){
		sscanf(p->timestamp,"%d-%d-%d",&year,&month,&day);
		Date d = createDate(day,month,year);
		if(smaller(begin,d) && smaller(d,end)){
			if(!estaNoTop(id,p->idPost,N) && p->score > v[0]){
				//temos que adicionar
				id[0] = p->idPost;
				v[0] = p->score;
				ordena(id,v,N);
			}
		}
		mostVote(p->left,N,id,v,begin,end);
		mostVote(p->right,N,id,v,begin,end);
	}
}

LONG_list most_voted_answers(TAD_community tad, int N, Date begin, Date end){
	long id[N];
	int nV[N];
	int i;
	LONG_list l;

	//inicializar array com numero de posts, outro array nao é necessario
	for(i=0;i<N;i++){
		nV[i] = -1;
	}
	mostVote(tad->post,N,id,nV,begin,end);

	l = create_list(N);
	for(i=0;i<N;i++){
		set_list(l,i,id[i]);
	}
	return l;
}

//querie 5
USER get_user_info(TAD_community com, long id){
	int N = 10;
	long ids[N];
	char* bio = (char*)malloc(sizeof(char*));
	
	getUserIds(com->user,id,ids,N,&bio);

	USER u = create_user(bio,ids);

	return u;
}

void mostAnswered(Post p, int N, long id[], int n[], Date b, Date e){
	int day,month,year;

	if(p){
		sscanf(p->timestamp,"%d-%d-%d",&year,&month,&day);
		Date d = createDate(day,month,year);
		if(smaller(b,d) && smaller(d,e)){
			if(!estaNoTop(id,p->idPost,N) && p->nAnswers > n[0]){
				//temos que adicionar
				id[0] = p->idPost;
				n[0] = p->nAnswers;
				ordena(id,n,N);
			}
		}
		mostAnswered(p->left,N,id,n,b,e);
		mostAnswered(p->right,N,id,n,b,e);
	}

}

//querie 7
LONG_list most_answered_questions(TAD_community tad,int N,Date begin,Date end){
	long id[N];
	int nA[N];
	int i;
	LONG_list l;

	for(i=0;i<N;i++){
		nA[i] = -1;
	}
	mostAnswered(tad->post,N,id,nA,begin,end);
	l = create_list(N);
	int j = 0;
	for(i=N-1;i>=0;i--){
		set_list(l,j,id[i]);
		j++;
	}

	return l;
}

void bestAnswer(Post p, long id, float *max, long *idBest, TAD_Users us){
	
	if(p && (p->typePost == 2) && (p->idAnswer == id)){
		int rep = getReputation(us,p->idUser);
		float aux = (p->score * 0.45) + (rep * 0.25) + (p->score * 0.2) + (p->nComments * 0.1);
		if(aux > *max){
			*max = aux;
			*idBest = p->idPost;
		}
	}
	if (p->left) bestAnswer(p->left,id,max,idBest,us); 
	if (p->right) bestAnswer(p->right,id,max,idBest,us);

	return;
}

long better_answer(TAD_community tad, long id){
	float *max = malloc(sizeof(float));
	long *idBest = malloc(sizeof(long));
	(*idBest) = -1;
	bestAnswer(tad->post,id,max,idBest,tad->user);

	return *idBest;
}

//querie 4 
void questions_tag(Post p, char* tag, Date b, Date e,long id[],int *c){
	int year,month,day;
	
	if(p){
		sscanf(p->timestamp,"%d-%d-%d",&year,&month,&day);
		Date d = createDate(day,month,year);
		if((p->typePost == 1) && smaller(b,d) && smaller(d,e)){
			if(p->tag && strstr(p->tag,tag)){
				int aux = *c;
				id[aux] = p -> idPost;
				(*c)++;
			}
		}
	}

	if(p->left) questions_tag(p->left,tag,b,e,id,c);
	if(p->right) questions_tag(p->right,tag,b,e,id,c);
}

int nrQuestions(Post p, char* tag, Date b, Date e){
	int year,month,day,c = 0;
	
	if(p){
		sscanf(p->timestamp,"%d-%d-%d",&year,&month,&day);
		Date d = createDate(day,month,year);
		if((p->typePost == 1) && smaller(b,d) && smaller(d,e)){
			if(p->tag && strstr(p->tag,tag)){
				c++;
			}
		}
	}

	if(p->left) c+=nrQuestions(p->left,tag,b,e);
	if(p->right) c+=nrQuestions(p->right,tag,b,e);

	return c;
}


LONG_list questions_with_tag(TAD_community tad, char* tag, Date begin, Date end){
	int i,j,c = 0;
	int N = nrQuestions(tad->post,tag,begin,end);
	long id[N];
	questions_tag(tad->post,tag,begin,end,id,&c);
	
	LONG_list l = create_list(c);
	j = c - 1; 
	for(i=0;i<c;i++){
		set_list(l,j,id[i]);
		j--;
	}

	return l;
}

//querie 8
void swap(char *str1,char *str2){
	char* temp = str1;
	str1 = str2;
	str2 = temp;
}

void swap1(long *str1,long *str2){
	long* temp = str1;
	str1 = str2;
	str2 = temp;
}

void ordenaIds(long id[],char **times,int N){
	int year1,month1,day1;
	int year2,month2,day2;
	int i,j;
	for(i=0;i<N-1;i++){
		for(j=0;j<N-i-1;j++){
			sscanf(times[j],"%d-%d-%d",&year1,&month1,&day1);
			Date d = createDate(day1,month1,year1);
			sscanf(times[j+1],"%d-%d-%d",&year2,&month2,&day2);
			Date e = createDate(day2,month2,year2);
			if(!smaller(d,e)){
				swap(times[i],times[i+1]);
				swap1(&id[i],&id[i+1]);
			}
		}
	}
}

void contains(Post p, char* word, long id[],char **timestamp, int N,int *c){

	if((*c)<N && p && (p->typePost == 1) && strstr(p->title,word)){
		int aux = *c;
		id[aux] = p -> idPost;
		timestamp[aux] = p -> timestamp;
		(*c)++;
	}
	if(p->left) contains(p->left,word,id,timestamp,N,c);
	if(p->right) contains(p->right,word,id,timestamp,N,c);
}


LONG_list contains_word(TAD_community tad, char* word, int N){
	long id[N];
	int c = 0;
	char **timestamp = malloc(N * sizeof(char**));
	LONG_list l;

	contains(tad->post,word,id,timestamp,N,&c);
	ordenaIds(id,timestamp,c);
	
	l = create_list(c);
	int i,j = c - 1;
	for(i=0;i<c;i++){
		set_list(l,j,id[i]);
		j--;
	}

	return l;
}

//querie 9 - temos dois Posts pois precisamos da cabeça da estrutura, porque sem isso ao fazer getPost estavamos a procurar so em metade da estrutura(p->left || p->right)
void both(Post p,Post p1,long id1,long id2, Array *a1,Array *a2){
	
	if(p){
		if(p->idUser == id1 && p->typePost == 1){
			insertArray(a1,p->idPost,p->timestamp);
		}else if(p->idUser == id1 && p->typePost == 2){
			Post post = getPost(p1,p->idAnswer);
			if(post)insertArray(a1,post->idPost,post->timestamp);
		}else if(p->idUser == id2 && p->typePost == 1){
			insertArray(a2,p->idPost,p->timestamp);
		}else if(p->idUser == id2 && p->typePost == 2){
			Post post = getPost(p1,p->idAnswer);
			if(post)insertArray(a2,post->idPost,post->timestamp);
		}
	}
	if(p->left) both(p->left,p1,id1,id2,a1,a2);
	if(p->right) both(p->right,p1,id1,id2,a1,a2);
}

void sortTime(long id[], Date time[],int N){
	int i,c;
	Date aux;

	for(i=0;i<N-1 && !smaller(time[i],time[i+1]);i++){
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

int participated(int N, Array a1, Array a2,long id[], Date timestamp[]){
	int i,c = 0;
	int year1,month1,day1;

	for(i=0;i<a1.used;i++){
		if(existe(a1.array[i],&a2)){ 
			sscanf(a1.times[i],"%d-%d-%d",&year1,&month1,&day1);
			Date d = createDate(day1,month1,year1);
			if(smaller(timestamp[0],d)){
				//queremos as ultimas perguntas - temos de adicionar
				id[0] = a1.array[i];
				timestamp[0] = d;
				sortTime(id,timestamp,N);
				c++;
			}
		}
	}
	return c;
}

LONG_list both_participated(TAD_community tad, long id1, long id2, int N){
	Array a1,a2;
	long id[N];
	Date timestamp[N];
	int i,j = 0;
	for(i = 0;i<N;i++){
		timestamp[i]  = createDate(0,0,0);
	}

	initArray(&a1,1);
	initArray(&a2,1);

	both(tad->post,tad->post,id1,id2,&a1,&a2);
	int c = participated(N,a1,a2,id,timestamp);
	//começar do fim, pode nao ter todos as N respostas 
	LONG_list l = create_list(c);
	i = N-1;
	while(c){
		set_list(l,j,id[i]);
		c--;
		i--;
		j++;
	}

	return l;
}

void freePost(Post *p){
	if(*p){
		freePost(&((*p)->left));
		freePost(&((*p)->right));
		free(*p);
	}
}

TAD_community clean(TAD_community tad){
	freePost(&(tad->post));
	freeUser(&(tad->user));
	return NULL;
}


