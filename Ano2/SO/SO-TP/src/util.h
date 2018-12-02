#ifndef _UTIL_
#define _UTIL_

#define IN_DESC 0
#define OUT_DESC 1
#define ERROR_DESC 2
#define LENGHT_ARGS 32
#define BUFFER_SIZE 1024
#define START_DESC 3

int clean_cmd(char *,int);
int special_cmd (char *);
int openfd2w(char *,int , int );
int openfd2r(char *);
int redirect_d(char *, char *);
int redirect_p(char **);


#endif