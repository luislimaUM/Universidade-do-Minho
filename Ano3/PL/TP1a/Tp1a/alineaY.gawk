#A saida com mais registos.

BEGIN {RS="<TRANSACCAO>";FS="[<>]";max=0;}
NR>1	{
		saidas[$23]++;
		}
END{
	for(saida in saidas)
		print "Saida: " saida " -> " saidas[saida];
		if(saidas[saida] > max){
			resultado = saida;
			max = saidas[saida];
		}
		print "A saida mais frequente é " resultado;
}