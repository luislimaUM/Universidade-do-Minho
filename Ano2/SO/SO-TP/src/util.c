#include <stdio.h>
#include <stdlib.h>
#include <string.h>
#include <unistd.h>
#include <wait.h>
#include <fcntl.h>
#include "util.h"

int clean_cmd(char *cmd, int size){
	int i = 0;
	int stop = 0;
	char c = '\0';
	for(i = 0; !stop && i < size; ++i){
		c = cmd[i];
		if((c >= 'a' && c <= 'z') || (c >= 'A' && c <= 'Z'))
			stop = 1;
		else
			cmd[i] = '\0';
	}
	--i;
	return i;
}

// Identificação das opções de redirecionamento
int special_cmd (char *mode) {
	int r=0;
	if(mode==NULL)
		return 0;

	if(mode[0] == '2')
		r = special_cmd(mode+1);
	else {
		if((mode[0] == '<') || (mode[0] == '>') )
			r = 1;
		else {
			if(mode[0] == '|')
				r = 2;
			else
				if(mode[0] == ';')
					r = 3;
		}
	}
	return r;
}

// Abertura/criação de um ficheiro e redirecionamento para os descritores de saída
// file - Nome do ficheiro
// descriptor - Descritor que se pretende substituir (1 ou 2)
// append - Modo com que o open() deve abrir o ficheiro
int openfd2w(char *file, int descriptor, int append) {
	int fd = -1;
	int flags = (O_CREAT | O_WRONLY);

	if(descriptor == OUT_DESC || descriptor == ERROR_DESC) {
		if(append)
			flags = (flags | O_APPEND);
		else 
			flags = (flags | O_TRUNC);

		fd = open(file, flags, 0766);
		if(fd > 0) {
			dup2(fd, descriptor);
		}
	}
	return fd;
}

// Abertura do ficheiro e redirecionamento do descritor de saída para o ficheiro
int openfd2r(char *file) {
	int fd = -1;
	fd = open(file, O_RDONLY);
	if(fd > 0) {
		dup2(fd, IN_DESC);
	}
	return fd;
}

// Redirecionamento de descritores
int redirect_d(char *redir_cmd, char *file) {
	int ss = 0;
	int desc = 1; // Descritores de saída
	int append = 0;
	// Descritor de entrada

	if( (!redir_cmd) || (!file) )
		return -1;

	if(redir_cmd[ss] == '<')
		openfd2r(file);
	else {
		// Descritores de saída
		if(redir_cmd[ss] == '2') {
			desc = 2;
			++ss;
		}

		if(redir_cmd[ss] && (redir_cmd[ss] == '>') ) {
			++ss;
			if(redir_cmd[ss] && (redir_cmd[ss] == '>') )
				append = 1;
			openfd2w(file, desc, append);
		}
	}
	return 1;
}

// Redirecionamento com pipes
int redirect_p(char **cmd) {
	int r = 1, fork_v = -1;
	int pd[2];
	if(!pipe(pd)) {
		fork_v = fork();
		if(fork_v < 0)
			exit(-1);
		if(fork_v == 0){
			close(pd[0]);
			dup2(pd[1], OUT_DESC);
			execvp(cmd[0], cmd);
			exit(-1);
		}
		r = fork_v;
		close(pd[1]);
		dup2(pd[0], IN_DESC);
	}
	else
		r = -1;
	return r;
}
