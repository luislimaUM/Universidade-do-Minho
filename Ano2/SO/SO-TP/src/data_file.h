#ifndef _DATA_FILE_
#define _DATA_FILE_

struct data_file;
typedef struct data_file *Data_file;

Data_file init_data();
int add_output(Data_file, char *,unsigned int);
int read_input(Data_file);
void print_data(Data_file);
char *get_comd(Data_file, unsigned int);
unsigned int get_comd_occup(Data_file df);
char *get_filetext_line(Data_file, unsigned int);
int get_line_occup(Data_file df);
unsigned int *get_dependences(Data_file,int ,int *);
int get_input_from(Data_file ,int );
void free_data(Data_file);
void print_clean(Data_file);

#endif