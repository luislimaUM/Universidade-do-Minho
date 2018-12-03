%{

%}

%%

'm			{ printf(" am"); }
're 		{ printf(" are"); }
n't			{ printf(" not"); }
't			{ printf(" not"); }
've			{ printf(" have"); }
'll			{ printf(" will"); }
It's 		{ printf("It is"); }
it's 		{ printf("it is"); }
He's 		{ printf("He is"); }
he's 		{ printf("he is"); }
She's 		{ printf("She is"); }
she's 		{ printf("she is"); }

%%

int yywrap(){
	return 1;
}

int main(){
	yylex();
	return 0;
}