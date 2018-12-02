#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <wait.h>
#include <fcntl.h>
#include "util.h"

// Execução do(s) comando(s) de uma linha
void exec_cmd(char *line){
	int i = 0, j = 0, r = 0, pid = 0, status = 0;
	int cmd = 0; // Índice do comando no buffer
	int sub_cmds = 0; // Número de comandos de uma sequencia de pipes
	char *aux = line, *savept = NULL, *token = NULL;
	char *file = NULL;
	char *buffer[LENGHT_ARGS];

	for(i = 0 ; i < LENGHT_ARGS ; i++)
		buffer[i]=NULL;

	for(i = 0;i < LENGHT_ARGS && (token = strtok_r(aux, " \n\t", &savept)) != NULL; aux=NULL){
		r = special_cmd(token);
		//Verifica se a primeira posição do token é um comado expecial
		if(r > 0){
			++i;
			if(r == 1) // Redirecionamento para ficheiros {>,<,>>,<<,2>,...}
			{
				file = strtok_r(NULL, " \n", &savept);
				redirect_d(token, file);
			}
			if(r == 2) // Redirecionamento para o próximo comando (pipe-|)
			{
				if((pid=redirect_p(buffer + cmd))>0){
					cmd = i;
					sub_cmds++;
				}
				else
					exit(-1);
			}
			if(r == 3) 	// Exec comand, neste caso o conjunto dos comandos é interpretado (;)
						// como um único comando ...
			{
				pid = fork();
				if(pid == 0) {
					execvp(buffer[cmd], buffer+cmd);
					exit(-1);
				}
				else {
					for(j=0;j<sub_cmds;++j){
						wait(&status);
						if(WEXITSTATUS(status) != 0)
							exit(-1);
					}
					// É necessário esperar senão o output pode-se sobrepor
					waitpid(pid, &status, WCONTINUED);
					if(WEXITSTATUS(status) != 0)
						exit(-1);
				}
				sub_cmds = 0;
				cmd = i;
			}
		}
		else{
			if(token[0]!='\0')
				buffer[i++] = token;
		}
	}

	execvp(buffer[cmd], buffer+cmd); // Last comand
	exit(-1);
}

int main(int argc,char *argv[]){
	int num_deps = 0,r = 0,i = 0;
	int pid = -1;
	int pd[2];
	char buffer[BUFFER_SIZE] = "\0";
	int status;

	if(argc == 3){
		//Número de descritores abertos em START_DESC+num_deps para escrita
		num_deps = atoi(argv[2]);
		if(!pipe(pd)){
			pid = fork();
			if(pid == 0){
				dup2(pd[1], OUT_DESC);
				close(pd[0]);
				exec_cmd(argv[1]);
			}
			else {
				close(0);
				close(pd[1]);
				while((r = read(pd[0], buffer, BUFFER_SIZE-2))){
					buffer[r] = '\0';
					//Enviar os dados para o processo pai
					write(1, buffer, r);
					//Enviar para cada nodo do conjunto de depenências o
					// resultado da execução do comando
					for(i = 0; i < num_deps; ++i)
						write(START_DESC + i, buffer,r);
				}
				close(pd[0]);
				for(i = 0; i < num_deps; ++i){
					close(START_DESC + i);
				}
			}
		}
	}
	else return -1;

	waitpid(pid, &status, WCONTINUED);
	if(WEXITSTATUS(status) !=0) 
		return -1;

	return 0;
}