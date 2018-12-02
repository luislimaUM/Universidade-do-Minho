#include "Estruturas_dados/lines.h"
#include "Estruturas_dados/list.h"
#include "data_file.h"
#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>
#include <sys/wait.h>
#include <string.h>
#include <ctype.h>

#define START_SIZE 20  // Tamanho inicial das variáveis dinâmicas
#define INVALID_CMD -1 // Usado para identificar os comandos inválidos

// Armazena as linhas do input/ficheiro
// Identifica os comandos presentes
// Guardar os resultados obtidos da execução dos comandos. 
typedef struct data_file {
	struct lines *file_txt;	// Dados de input
	struct lines *result;	// Resultados obtidos
	unsigned int *comds;	// Array com as posições dos comandos em file_txt (apontador para a linha do comando)
	List *dependenc;		// Dependências entre comandos
							// Permite identificar os comandos que dependem no indice (indice-(write)->{x,y,z})
	int *input_from;		// Array que indica se o comando do indice i deve receber dados
							// de outro comando e de qual comando, -1 indica que não recebe dados
 	unsigned int size_comds;
	unsigned int occup_comds;
} *Data_file;


/* FUNÇÕES DE ESTRUTURAÇÃO/MANIPULAÇÃO DE MEMÓRIA */

// Retorna um apontador para a estrutura criada
// Em caso de erro retorna NULL
Data_file init_data(){
	int i = 0;
	Data_file new = NULL;
	new = (Data_file) malloc(sizeof(struct data_file));
	if(new) {
		new->file_txt = init_lines();
		new->result = init_lines();

		new->size_comds = START_SIZE;
		new->occup_comds = 0;
		new->comds = (unsigned int *) calloc(START_SIZE, sizeof(unsigned int));
		for(i = 0; i < START_SIZE; ++i)
			new->comds[i] = INVALID_CMD;
		new->input_from = (int *) calloc(START_SIZE, sizeof(int));
		for(i = 0; i < START_SIZE; ++i)
			new->input_from[i] = INVALID_CMD;
		new->dependenc = (List *) calloc(START_SIZE, sizeof(List));
	}
	return new;
}

// Ajusta o tamanho dos arrays de comandos e da lista de dependências
// Retorna valor inteiro a indicar se a execução foi bem sucedida
int resize_comds(Data_file df){
	Data_file new = df;
	int r = 1;
	unsigned int i = 0;
	unsigned int s_comds = 0, o_comds = 0;
	unsigned int new_size = 0;

	if(df){
		s_comds = df->size_comds;
		o_comds = df->occup_comds;
		new_size = s_comds*2;

		if(o_comds >= s_comds-1){
			new->dependenc = (List *) realloc(df->dependenc, sizeof(List)*new_size);
			for(i = o_comds; i < new_size; ++i)
				new->dependenc[i] = NULL;
			new->size_comds = new_size;
			new->comds = (unsigned int *) realloc(df->comds, sizeof(unsigned int)*new_size);
			for(i = o_comds; i < new_size; ++i)
				new->comds[i] = 0;
			new->size_comds = new_size;
			new->input_from = (int *) realloc(df->input_from, sizeof(int)*new_size);
			for(i = o_comds; i < new_size; ++i)
				new->input_from[i] = INVALID_CMD;
			new->size_comds = new_size;
		}
		if(!new->comds) // Erro na alocação de memória
			r = -1;
	}
	else // A estrutura está vazia
		r = -1;
	return r;
}

// Libertação de memória da estrutura
void free_data(Data_file df){
	unsigned int i=0;
	if(df){
		if(df->file_txt)
			free_lines(df->file_txt);
		if(df->result)
			free_lines(df->result);
		if(df->comds)
			free(df->comds);
		if(df->dependenc){
			for(i = 0;i < df->size_comds; ++i){
				free_list(df->dependenc[i]);
			}
			free(df->dependenc);
		}
		if(df->input_from)
			free(df->input_from);
		free(df);
	}
}

/* FUNÇÕES DE ADIÇÃO DE DADOS */

// Adicionar dependência no indice cmd
void add_dependence(Data_file df, int cmd, int new_depend){
	List depends = NULL;
	if(df && cmd >= 0 && new_depend >= 0){
		depends = df->dependenc[cmd];
		df->dependenc[cmd] = add_list(depends, new_depend);
		df->input_from[new_depend] = cmd;
	}
}

// Adicionar um comando à lista de comandos
// line - posição de memória em que a linha foi armazenada na estrutura Lines
// Permite identificar directamente em que posição estão os comandos
int add_comds(Data_file df){
	unsigned int i = 0;
	int cmd_occup = 0, cmd_size = 0;
	int comand_depend = 0;
	unsigned int text_size = 0;
	int r = 1;
	char line[MAX_SIZE_STRING];
	char *aux = NULL, *savept = NULL, *token = NULL;
	char pipe = 'n';

	if(df && df->file_txt && df->result){
		text_size = get_occup(df->file_txt);
		// Percorre as linhas de file_txt
		for(i = 0; i < text_size; ++i){
			strncpy(line, get_line(df->file_txt,i), MAX_SIZE_LINE);
			aux = line;
			// Add comand
			if(aux[0] == '$'){
				cmd_occup = df->occup_comds;
				cmd_size = df->size_comds;
				//Identificar se existem pipes
				token = strtok_r(aux,"$ ",&savept);
				if(token[0] == '|'){
					add_dependence(df, cmd_occup-1, cmd_occup);
				}
				else
				{
					if(isdigit(token[0])){
						sscanf(token, "%d%c", &comand_depend, &pipe);
						if(pipe == '|'){
							add_dependence(df, cmd_occup-comand_depend, cmd_occup);
							pipe = '0';
						}
					}
				}
				if(cmd_occup >= cmd_size-1)
					resize_comds(df);
				df->comds[cmd_occup++] = i;
				df->occup_comds++;
			}
		}
	}
	else
		r = -1;
	return r;
}

// Recebe uma string de tamanho variado
// Divide-a por linhas e adiciona cada linha à estrutura
// Retorna um inteiro que pode indicar vários estados da estrutura:
// 	< 0 - operação mal sucedida
// 	0 - operação bem sucedida (todos os dados foram adicionados com sucesso)
// 	1 - operação bem sucedida (há dados que não forma totalmente adicionados
// 		ou seja, a última linha da string não está completa)
int add_text(Data_file df, char *text){
	int r = 0;
	int size_txt = strlen(text);

	// Prevenir possíveis erros ...
	if(size_txt > MAX_SIZE_STRING)
		size_txt = MAX_SIZE_STRING;

	char aux[size_txt+1];
	strncpy(aux, text, size_txt);

	aux[size_txt] = '\0';

	if(df && size_txt > 0){
		add_line(df->file_txt, aux);
	}
	else{
		// A string text está vazia ou a estrutura df==NULL
		r = -1;
	}
	return r;
}

int read_input(Data_file df){
	int i = 0;
	char line[MAX_SIZE_LINE];
	// Leitura do input e duplicação da informação do ficheiro.
	while((i = read(0, line, MAX_SIZE_LINE-1))){
		line[i] = '\0';
		add_text(df, line);
	}
	// No caso da última linha não terminar com '\n' é necessário terminar
	// a linha de modo a ser inserida no array de strings
	i = strlen(line);
	if(i >= 2 && line[i-2] != '\n'){
		line[0] = '\n';
		line[1] = '\0';
		add_text(df, line);
	}
	// Adição dos comandos
	add_comds(df);
	return 1;
}

// Adicionar o output dos comandos (pode ter várias linhas)
// String é guardada como um todo
int add_output(Data_file df, char *out, unsigned int cmd){
	int r = -1;
	if(df && df->result)
		r = add_string(df->result, out, cmd);
	return r;
}

/* GETS */

char *get_comd(Data_file df, unsigned int n){
	if( ( (df && df->file_txt) && df->comds) && n<df->occup_comds)
		return get_line(df->file_txt, df->comds[n]);
	return NULL;
}

unsigned int get_comd_occup(Data_file df){
	if(df)
		return df->occup_comds;
	return 0;
}

char *get_filetext_line(Data_file df,unsigned int n){
	if(df && df->file_txt)
		return get_line(df->file_txt,n);
	return NULL;
}

int get_line_occup(Data_file df){
	if(df && df->file_txt)
		return get_occup(df->file_txt);
	return -1;
}

unsigned int *get_dependences(Data_file df, int comand, int *size){
 	return list2array(df->dependenc[comand], size);
}

int get_input_from(Data_file df,int cmd){
 	if(cmd>=0 && df->size_comds>(unsigned int)cmd)
 		return df->input_from[cmd];
 	return -2;
}


/* FUNÇÕES DE PRINT */

// Impressão dos comandos
void print_comds(Data_file df){
	unsigned int i = 0;
	printf("\n");
	for(i = 0; i < df->occup_comds; ++i){
		printf("Comand %d: %s", i, get_comd(df, i));
	}
}

// Impressão do resultado das operações e das linhas do notebook
void print_data(Data_file df){
	int i = 0;
	unsigned int out_i = 0;
	int size_text = get_line_occup(df);
	char *line = NULL,*aux = NULL,empty[2]="\0";
	int size_str = 0;

	for(i = 0, out_i = 0; i < size_text && (line = get_line(df->file_txt, i));++i){
		printf("%s", line);
		// Verifica se se trata de um comando
		if(line[0] == '$'){
			aux = get_line(df->result, out_i++);

			if(aux){
				size_str = strlen(aux);
			}
			else{
				size_str = -1;
				aux=empty;
			}

			if( size_str>0 && aux[size_str-1]=='\n')
				printf(">>>\n%s<<<\n", aux);
			else
				printf(">>>\n%s\n<<<\n", aux);

			if((i+1) < size_text){
				line = get_line(df->file_txt, i+1);
				if(line && !strncmp(line, ">>>\n", 4)){
					i += 2;
					while(i < size_text && line && strncmp(line, "<<<\n", 4)){
						line = get_filetext_line(df, i);
						++i;
					}
					--i;
				}
			}
		}
	}
}

// Impressão as operações das linhas do notebook
void print_clean(Data_file df){
	int i = 0;
	int size_text = get_line_occup(df);
	char *line = NULL;

	for(i = 0; i < size_text && (line = get_line(df->file_txt, i));++i){
		printf("%s", line);
		// Verifica se se trata de um comando
		if(line[0] == '$'){
			if((i+1) < size_text){
				line = get_line(df->file_txt, i+1);
				if(line && !strncmp(line, ">>>\n", 4)){
					i += 2;
					while(i < size_text && line && strncmp(line, "<<<\n", 4)){
						line = get_filetext_line(df, i);
						++i;
					}
					--i;
				}
			}
		}
	}
}