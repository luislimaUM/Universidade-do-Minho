#include "lines.h"
#include <stdio.h>
#include <stdlib.h>
#include <string.h>

#define START_SIZE 20 // Tamanho inicial das variáveis dinâmicas

// Armazena as linhas obtidas a partir da leitura de uma fonte de texto/dados
typedef struct lines {
	char **lines;
	unsigned int size;
	unsigned int occup;
	char buffer[MAX_SIZE_STRING];
	// Guardar o estado da linha a ser inserida, caso de a linha estar atualmente incompleta (falta '\n')
} *Lines;


/* FUNÇÕES AUXILIARES */

// Limpar o conteúdo de um array de char's
void clean_line(char *line, int size){
	int i = 0;
	if(line){
		for(i = 0; i < size; ++i){
			line[i] = '\0';
		}
	}
}


// Divide linhas consecutivas
// Devolve o índice de começo da próxima linha ou -1
int truncate_line(char *str, char t, int start) {
	int size = strlen(str);
	int i = start;
	int r = -1;
	if(size < MAX_SIZE_STRING && start < size) {
		for(i = start ; ((i < size) && (str[i] != t)) && (str[i] != '\0') ; ++i);
		if(str[i] == t)
			r = i;
	}
	return r;
}

/*
 * Gets - Neste caso não são extritamente necessários, mas permitem dividir
 *  	  as duas estruturas em ficheiros separados, uma vez que não há
 *		  acessos directos por parte das funções da estrutura data_file.
 */

char *get_line(Lines ll, unsigned int line) {
	if(line < ll->occup)
		return ll->lines[line];
	return NULL;
}

// Apontador para o buffer
char *get_buffer(Lines ll) {
	return ll->buffer;
}

int get_occup(Lines ll) {
	return ll->occup; 
}

/* FUNÇÕES DE ESTRUTURAÇÃO/MANIPULAÇÃO DE MEMÓRIA */

Lines init_lines() {
	int i = 0;
	Lines new = NULL;
	new = (Lines) malloc(sizeof(struct lines));
	if(new) {
		new->size = START_SIZE;
		new->occup = 0;
		new->lines = (char **) calloc(START_SIZE, sizeof(char *));
		for(i = 0 ; i < START_SIZE ; i++)
			new->lines[i] = NULL;
		clean_line(new->buffer, MAX_SIZE_LINE);
	}
	return new;
}

// Ajusta o tamanho dos campos da estrutura
Lines resize_lines(Lines ll) {
	Lines new = ll;
	unsigned int size = 0, occup = 0;
	unsigned int new_size = 0;

	if(new) {
		size = new->size;
		occup = new->occup;
		new_size = size*2;
		if(occup+1 >= size-1) {
			new->lines = (char **) realloc(new->lines, sizeof(char *)*new_size);
			for(unsigned int i = occup; i < new_size; ++i)
				new->lines[i] = NULL;
			new->size = new_size;
		}
	}
	return new;
}

// Libertação de memória da estrutura
void free_lines(Lines ll) {
	unsigned int i = 0;
	unsigned int occup = ll->occup;
	if(ll) {
		if(ll->lines) {
			for(i = 0 ; i < occup ; ++i) {
				if(ll->lines[i])
					free(ll->lines[i]);
			}
			free(ll->lines);
		}
		free(ll);
	}
}

char *_strndup (const char *s, size_t n)
{
  size_t len = strnlen(s, n) + (size_t)1;
  char *new = NULL;
  new = (char *) calloc((len),(sizeof(char)));
  if (new == NULL)
    return NULL;
  new[len] = '\0';
  return (char *) memcpy (new, s, len);
}

// Adição de linhas
int add_line(Lines ll, char *line){
	int i = 0, j = 0;
	int occup = 0, size = 0, buffer_size = 0;

	if(ll && line){
		i=0;
		occup = ll->occup;
		size = ll->size;

		if((occup+1) >= (size-1))
			ll = resize_lines(ll);

		buffer_size = strlen(ll->buffer);

		if(buffer_size > 0){
			// Só para tentar completar o buffer
			for(i = 0; (i < MAX_SIZE_STRING) && (j == 0) && (line[i]!='\0'); ++i){
				if(line[i] == '\n'){
					ll->lines[occup++] = (char *) calloc(buffer_size+i+2, sizeof(char));
					strncpy(ll->lines[occup-1], ll->buffer, buffer_size);
					strncat(ll->lines[occup-1], line, i+1);
					clean_line(ll->buffer, MAX_SIZE_STRING);
					ll->occup = occup;
					j = i+1;
				}
			}
			// Não foram encontrados '\n' na linha line
			if(j == 0){
				if((buffer_size+i) >= (MAX_SIZE_STRING-1)){
					ll->lines[occup++] = strndup(ll->buffer, buffer_size);
					ll->occup = occup;
					strncpy(ll->buffer, line, i);
					j = i+1;
				}
			}
		}

		for(;i < MAX_SIZE_STRING && line[i] != '\0'; ++i){
			if(line[i] == '\n'){

				size = ll->size;
				if((occup+1) >= (size-1))
					ll = resize_lines(ll);

				ll->lines[occup++] = strndup(line+j, i-j+1);
				ll->occup = occup;
				j = i+1;
			}
		}
		// Se ainda existem dados na string
		if(j <= i){
			strncat(ll->buffer, line+j, i-j+1);
		}
	}
	ll->occup = occup;
	return 0;
}

// Adiciona linhas como strings, mesmo que contenha várias mudanças de linha
int add_string(Lines ll, char *string, unsigned int cmd){
	Lines new = ll;
	int r = 0;
	unsigned int s_lines = 0,o_lines = 0;
	char *line = NULL;
	int max_size = MAX_SIZE_STRING;
	int size = strlen(string);
	int size_line = 0;
	int new_size = 0;

	if(size < max_size && size>0)
		max_size = size;

	if(new){
		s_lines = new->size;
		o_lines = new->occup;

		if(cmd <= s_lines)
			o_lines = cmd;

		if((o_lines+1) >= (s_lines-1))
			new = resize_lines(new);

		line = new->lines[o_lines];

		if(line){
			size_line = strlen(line);
		}
		else
			size_line = 0;
		new_size = size+size_line+1;

		if(size_line == 0)
			line = strndup(string,max_size);
		else{
			line = (char *) realloc(line, sizeof(char)*new_size);
			strncat(line, string, new_size);
		}

		new->lines[o_lines] = line;

		if(o_lines >= new->occup)
			new->occup++;
	}
	else
		r=-1;
	return r;
}


// Impressão de todas as linhas
void print_all_lines(Lines ll) {
	int i = 0;
	int occup = ll->occup;
	for(i = 0 ; i < occup ; ++i) {
		printf("%d %s", i, ll->lines[i]);
	}
}