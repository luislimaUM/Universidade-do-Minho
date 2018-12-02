################################################################################
# Makefile
################################################################################
FLAGS= -g -Wextra -Wpedantic -Wall
GCC= gcc $(FLAGS)

#Files
BIN=bin/
BUILD=build/
SRC=src/
TESTS=tests/
ESTR_DADOS=$(SRC)Estruturas_dados/

MAIN=$(SRC)notebook.c
EXEC=notebook
NODE=$(BIN)node

DATA_FILE_C=$(SRC)data_file.c
DATA_FILE_H=$(SRC)data_file.h
DATA_FILE_O=$(BUILD)data_file.o

LINES_C=$(ESTR_DADOS)lines.c
LINES_H=$(ESTR_DADOS)lines.h
LINES_O=$(BUILD)lines.o

LIST_C=$(ESTR_DADOS)list.c
LIST_H=$(ESTR_DADOS)list.h
LIST_O=$(BUILD)list.o

UTIL_C=$(SRC)util.c
UTIL_H=$(SRC)util.h
UTIL_O=$(BUILD)util.o

NODE_C=$(SRC)node.c

DEPS=$(DATA_FILE_O) $(LINES_O) $(LIST_O) $(UTIL_O)

default: all

all: notebook node

#Notebook
notebook: desktop $(MAIN) estruturas data_file util
	$(GCC) -o $(EXEC) $(MAIN) $(DEPS)

#Node
node: desktop util $(NODE_C) $(BIN)
	$(GCC) -o $(NODE) $(NODE_C) $(UTIL_O)

#Estrutura de dados para guardar o estado do ficheiro
data_file: desktop $(BIN) $(DATA_FILE_C) $(DATA_FILE_H)
	$(GCC) -c $(DATA_FILE_C) -o $(DATA_FILE_O)

#Funções auxiliares usadas no Notebook e no Node
util: desktop $(BIN) $(UTIL_C) $(UTIL_H)
	$(GCC) -c $(UTIL_C) -o $(UTIL_O)

#Estruturas de dados auxiliares
estruturas: lines list

lines: desktop $(BIN) $(LINES_C) $(LINES_H)
	$(GCC) -c $(LINES_C) -o $(LINES_O)

list: desktop $(BIN) $(LIST_C) $(LIST_H)
	$(GCC) -c $(LIST_C) -o $(LIST_O)

#Funções auxiliares
desktop:
	@mkdir -v -p $(BIN) $(BUILD)

teste: all
	./$(EXEC) $(TESTS)ex1.nb
	./$(EXEC) $(TESTS)ex2.nb

clean:
	@rm -f $(BIN)* $(BUILD)* $(EXEC)