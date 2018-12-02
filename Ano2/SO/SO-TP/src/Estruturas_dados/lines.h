#ifndef _LINES_
#define _LINES_

#define MAX_SIZE_LINE 256
#define MAX_SIZE_STRING 2048

struct lines;
typedef struct lines *Lines;

Lines init_lines();
int add_line(Lines, char *);
int add_string(Lines, char *,unsigned int);
char *get_line(Lines,unsigned int);
int get_occup(Lines);
void free_lines(Lines);


#endif