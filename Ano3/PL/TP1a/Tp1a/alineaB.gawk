BEGIN {RS="<TRANSACCAO>";FS="[<>]"}
NR>1	{
		resultado[$23];
		}
END{
	for(lugar in resultado) print lugar;
}