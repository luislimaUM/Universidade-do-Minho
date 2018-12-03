BEGIN {RS="<TRANSACCAO>";FS="[<>]"}
NR>1	{
		split($27,valorInicial,",");e=valorInicial[1];c=valorInicial[2]*0.01;
		split($31,valorInicial,",");de=valorInicial[1];dc=valorInicial[2]*0.01;
		valorSemIva = (e+c)-(de+dc);
		valorFinal = valorSemIva + valorSemIva*$35*0.01;

		split($3,data,"[-./]");
		resultado[data[2]] += valorFinal;
		}
END{
	for(mes in resultado)
		print "No mes " mes " gastamos " resultado[mes] " €.";
}