%{

%}
lixo .|\n
%%

[\+\-\*\/\;\{\}\=\(\)\[\]\%]	{return yytext[0];}
(?i:F)			{return F;}
(?i:INT)		{return INT;}
(?i:BOOL)		{return BOOL;}
(?i:TRUE)		{return TRUE;}
(?i:FALSE)		{return FALSE;}
(?i:EQUAL)		{return EQUAL;}
(?i:DIFFERENT)	{return DIFFERENT;}
(?i:SUP)		{return SUP;}
(?i:INF)		{return INF;}
(?i:SUPEQUAL)	{return SUPEQUAL;}
(?i:INFEQUAL)	{return INFEQUAL;}
(?i:AND)		{return AND;}
(?i:OR)			{return OR;}
(?i:WRITE)		{return WRITE;}
(?i:READ)		{return READ;}
(?i:IF)			{return IF;}
(?i:ELSE)		{return ELSE;}
(?i:WHILE)		{return WHILE;}
[a-zA-z]+		{yylval.sval = strdup(yytext); 	return id;}
[-]?[0-9]+			{yylval.ival = atoi(yytext);	return num;} 
{lixo}			{;}

%%
int yywrap() {
	return 1;
}