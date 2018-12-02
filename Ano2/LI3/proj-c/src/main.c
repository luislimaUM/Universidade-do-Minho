#include <stdio.h>
#include <date.h>
#include <stdlib.h>
#include "interface.h"


int main(int argc,char **argv){
  TAD_community tad = init();
  
  //argc-1 porque primeiro argumento é nome do programa
  char *docname;
  if(argc <= 1){
  	printf("Usage: %s docname incorrect\n", argv[0]);
  	return 0;
  }
  docname = argv[1];
 	tad = load(tad,docname);
  
  /*querie 1
  printf("Dados do Post com id 796430:\n");
  STR_pair name = info_from_post(tad,796430);
  printf("Titulo: \n%s \nNome User:\n%s\n", get_fst_str(name),get_snd_str(name));
	free_str_pair(name);
  */
  /* querie 2 
  int i,N = 10;
  LONG_list l = top_most_active(tad,N);
  printf("Top 10 mais utilizadores\n");
  for(i=0;i<N;i++){
    printf("%dº - %ld\n", i, get_list(l,i));
  }
  free_list(l);
  */
  
  /*querie 3
  Date begin = createDate(01,01,2014);
  Date end = createDate(31,12,2014);
  LONG_pair c = total_posts(tad,begin,end);
  printf("Total Perguntas:%ld \nTotal Respostas:%ld\n", get_fst_long(c),get_snd_long(c));
  free_long_pair(c);
  free_date(begin);
  free_date(end);
  */

  /*querie 4
  Date begin = createDate(1,3,2013);
  Date end = createDate(31,3,2013);
  LONG_list l = questions_with_tag(tad,"package-management",begin,end);
  int i = 0;

  free_list(l);
  free_date(begin);
  free_date(end);
  */
  
  /*querie 5
  int i;
  USER s = get_user_info(tad,449);
  long *ids = get_10_latest_posts(s);
  for(i=0;i<10;i++){
    printf("%dº - %ld\n", i, ids[i]);
    }
    printf("%s\n", get_bio(s));
  free(ids);
  free_user(s);
  */

  /*querie 6
  int i,N = 5;
  Date begin = createDate(1,11,2015);
  Date end = createDate(30,11,2015);
  LONG_list l = most_voted_answers(tad,N,begin,end);
  printf("Top %d mais votos\n",N);
  for(i=N-1;i>=0;i--){
    printf("%dº - %ld\n", i, get_list(l,i));
  }
  free_date(begin);
  free_date(end);
  free_list(l);
  */

  /*querie 7
  int i,N=100;
  Date begin = createDate(1,1,2012);
  Date end = createDate(31,12,2012);
  LONG_list l = most_answered_questions(tad,N,begin,end);
  printf("Top %d mais Respostas\n",N);
  for(i=0;i<N;i++){
    printf("%dº - %ld\n", i, get_list(l,i));
  }
  free_date(begin);
  free_date(end);
  free_list(l);
  */

  /*querie 8 - da os primeiros N que encontra, se nao tiver o numero total pedido
  //da apenas o numero de ids que encontrar
  int N = 10,i;
  LONG_list l = contains_word(tad,"kde",N);
  for(i=0;i<N;i++){
    printf("%dº - %ld\n", i, get_list(l,i));
  }
  free_list(l);
  */

  /*querie 9
  int i,N = 10;
  LONG_list l = both_participated(tad,87,5691,N);
  free_list(l);
  */

  /*querie 10
  long l = better_answer(tad,30334);
  printf("%ld\n", l);
  */

  //querie 11

  clean(tad);

  return 0;
} 
