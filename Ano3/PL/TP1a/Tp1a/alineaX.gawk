#O dinheiro que cada operador ganhou.

BEGIN {RS="<TRANSACCAO>";FS="[<>]"}
NR>1	{
		split($27,valorInicial,",");e=valorInicial[1];c=valorInicial[2]*0.01;
		split($31,valorInicial,",");de=valorInicial[1];dc=valorInicial[2]*0.01;
		valorSemIva = (e+c)-(de-dc);

		operadores[$39] += valorSemIva;
		}
END{
	for(operador in operadores)
		print "O operador " operador " ganhou " operadores[operador] " €.";
}