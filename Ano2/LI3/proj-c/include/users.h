typedef struct allUser * TAD_Users;

TAD_Users initU();
TAD_Users insereUser(TAD_Users,long,char*,char*,int,TAD_Users*);
char* getName(TAD_Users,long);
int contemUser(TAD_Users, long,char*,long);
void incrementaT(TAD_Users *);
void mostActive(TAD_Users,int,long *,int *);
void ordena(long *,int *,int );
int estaNoTop(long *,long ,int);
void getUserIds(TAD_Users,long, long *,int,char**);
int getReputation(TAD_Users, long);
void freeUser(TAD_Users *);