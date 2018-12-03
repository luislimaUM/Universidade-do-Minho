BEGIN {RS="<TRANSACCAO>";FS="[<>]"}
NR>1	{split($3,data,"[-./]");
		if(data[2]!=""){
			resultado[data[2]][data[1]]++;
		}
		}
END{
	for(mes in resultado)
		for(dia in resultado[mes])
			if(dia!= null){
				print "Na data " dia "/" mes " existem " resultado[mes][dia] " entradas.";
			}
}