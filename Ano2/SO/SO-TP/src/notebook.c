#include <stdio.h>
#include <stdlib.h>
#include <unistd.h>     /*chamadas ao sistema: defs e decls essenciais*/
#include <sys/wait.h>
#include <string.h>
#include <fcntl.h>
#include <signal.h>
#include "data_file.h"
#include "util.h"

#define NODE "./bin/node"

// Execução do(s) comando(s) de uma linha
void exec_node(char *cmd, int input_pipe, int *output_pipes, int size){
	int j = 0, cmd_size = 0;
	char num_deps[10] = "0";
	char *token = NULL;
	// A linha pode ter os comandos de pipe
	cmd_size = strlen(cmd);
	j = clean_cmd(cmd,cmd_size);
	token = cmd + j;
	// Substituição do descritor stdin pelos descritores do pipe criado
	if(input_pipe >= 0){
		dup2(input_pipe, IN_DESC);
	}
	// Substituição dos descritores 3...x pelos descritores de pipe de dependências
	for(j = 0; j < size; ++j){
		dup2(output_pipes[j], START_DESC + j);
	}
	snprintf(num_deps, 10, "%d", size);
	//Execução do nodo
	execlp(NODE, NODE, token, num_deps, NULL);
	exit(-1);
}

// Comandos são todos executados antes de ler o output (possibilidade de concorrência)
int exec_cmds(Data_file d){
	int i = 0, j = 0, pid = 0, r = 0;
	int cmd_size = 0 , node = 0;
	char line[BUFFER_SIZE];
	int input_from = -1;
	unsigned int *write_to = NULL;
	int size_write_to = 0;
	int pd[2];
	int status, ret = 0;

	cmd_size = get_comd_occup(d);
	//Pipes de input para receber os dados
	int pipes4this[cmd_size];
	//Pipes que permitem a comunicação entre os nodos
	int pipes4node[cmd_size][2];
	//Pipes de output para um determinado nodo
	int output_pipes[cmd_size];

	// Para cada comando
	for(i = 0; i < cmd_size; ++i){
		// Criação dos pipes
		if(!pipe(pd)){
			// Array de dependências do comando i
			write_to = get_dependences(d, i, &size_write_to);
			// Criação dos pipes para output
			for(j = 0; j < size_write_to; ++j){
				node = write_to[j];
				pipe(pipes4node[node]);
				output_pipes[j] = pipes4node[node][1];
			}
			// Se o comando recebe input de algum comando
			input_from = get_input_from(d,i);
			if(input_from >= 0)
				input_from = pipes4node[i][0];
			// Linha de comando do comando i
			strncpy(line, get_comd(d,i), BUFFER_SIZE-1);
			pid = fork();
			if(pid == 0){
				// Pipe de escrita de resultados do comando
				dup2(pd[1], OUT_DESC);
				close(pd[0]);
				// Exucução do nodo
				exec_node(line, input_from, output_pipes, size_write_to);
			}
			else{
				// Close dos pipes para output dos nodos
				for(j = 0; j < size_write_to; ++j)
					close(output_pipes[j]);
				// Libertar memória do array
				if(size_write_to>0)
					free(write_to);
				// Close do pipe para input do nodo
				if(input_from != -1)
					close(input_from);
				// Close do pipe para output do nodo
				close(pd[1]);
				// Pipe de leitura de resultados do comando
				pipes4this[i] = pd[0];
			}
		}
	}
	// Leitura dos resultados
	for(i = 0; i < cmd_size; ++i){
		while((r = read(pipes4this[i], line, BUFFER_SIZE-2))){
			line[r] = '\0';
			add_output(d, line, i);
		}
		close(pipes4this[i]);
	}

	for(i=0;i<cmd_size;i++){
		wait(&status);
		if(WEXITSTATUS(status) !=0) 
			ret = -1;
	}
	return ret;
}

int main (int argc, char *argv[]){

	int notebook_r = 0, notebook_w = 0, r = 0;
	Data_file df = init_data();

	// Ficheiro argumento como stdin
	if(argc >= 2) {
		notebook_r = open(argv[1], O_RDONLY);
			if(notebook_r > -1) {
				dup2(notebook_r, 0);
			}
	}

	// Leitura dos dados do ficheiro
	read_input(df);

	// Execução dos comandos armazenados em d
	r = exec_cmds(df);

	if(r!=-1){
		// Redirecionar saída para o ficheiro argumento
		notebook_w = open(argv[1], O_WRONLY | O_TRUNC);
		if(notebook_w > -1) {
			dup2(notebook_w, 1);
		}
		// Imprime os resultado da execução dos comandos
		print_data(df);
	}

	// Libertar a memória
	free_data(df);

	return 0;
}