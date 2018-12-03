BEGIN {RS="<TRANSACCAO>";FS="[<>]";IGNORECASE=1;total=0}

NR>1 && $43~"Parque"	{
	split($27,valorInicial,",");e=valorInicial[1];c=valorInicial[2]*0.01;
	split($31,valorInicial,",");de=valorInicial[1];dc=valorInicial[2]*0.01;
	valorSemIva = (e+c)-(de-dc);
	total += valorSemIva + valorSemIva*$35*0.01}

END{print "No mes gastou " total " €."}