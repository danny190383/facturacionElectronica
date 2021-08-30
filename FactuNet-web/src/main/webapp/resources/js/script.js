function redondeo2decimales(numero)
{
    var flotante = parseFloat(numero);
    var resultado = Math.round(flotante * 100) / 100;
    return resultado;
}
function onlyNumbers(event, control) {
    var keynum;
    if (event.shiftKey == 1) {
        return false;
    } //    
    if (window.event) {  //IE
        keynum = event.keyCode;
    }
    if (event.which) { //Netscape/Firefox/Opera
        keynum = event.which;
    }

    //permitir
    if (keynum == 8 //backspace
            || keynum == 9 //tab
            || keynum == 46 //delete
            || (keynum >= 35 && keynum <= 40) //end, home, left arrow, up arrow, right arrow, down arrow
            || (keynum >= 48 && keynum <= 57) //0-9	
            || (keynum >= 96 && keynum <= 105)) //numpad 0-9	
        return true;
    else
        return false;  //solo para valores enteros
    
/*    
 
    //para permitir decimales
     var checkdot = document.getElementById(control.id).value;

    //evitar que ingrese el PUNTO como primer caracter
    if ((keynum == 110 || keynum == 190) && (checkdot.length == 0))
        return false;

    //validar el punto decimal.. permitir solamente una vez
    var i = 0;
    var encontro = false;
    var cont = 0;
    for (i = 0; i < checkdot.length; i++) {
        if (checkdot[i] == '.')
        {
            encontro = true;
            if (keynum == 110 || keynum == 190 || cont == 2)
            {
                return false;
            }
        }
        if (encontro)
            cont = cont + 1;
    }
    if ((keynum == 110 || keynum == 190) && !encontro)
        return true;
    else
        return false;

*/
    //keychar = String.fromCharCode(keynum);

    // allow backspace, tab, delete, arrows, numbers and keypad numbers ONLY
    //if (keynum == 8 || keynum == 9 || keynum == 46 || (keynum >= 37 && keynum <= 40) || (keynum >= 48 && keynum <= 57) || (keynum >= 96 && keynum <= 105)) {
    /*if (keynum >= 48 && keynum <= 57) {
     return !isNaN(keychar);
     }
     else {
     event.preventDefault();
     }*/
}


/*************************************************************/
// NumeroALetras
// @author   Rodolfo Carmona
/*************************************************************/
function Unidades(num){

  switch(num)
  {
    case 1: return "UN";
    case 2: return "DOS";
    case 3: return "TRES";
    case 4: return "CUATRO";
    case 5: return "CINCO";
    case 6: return "SEIS";
    case 7: return "SIETE";
    case 8: return "OCHO";
    case 9: return "NUEVE";
  }

  return "";
}

function Decenas(num){

  decena = Math.floor(num/10);
  unidad = num - (decena * 10);

  switch(decena)
  {
    case 1:   
      switch(unidad)
      {
        case 0: return "DIEZ";
        case 1: return "ONCE";
        case 2: return "DOCE";
        case 3: return "TRECE";
        case 4: return "CATORCE";
        case 5: return "QUINCE";
        default: return "DIECI" + Unidades(unidad);
      }
    case 2:
      switch(unidad)
      {
        case 0: return "VEINTE";
        default: return "VEINTI" + Unidades(unidad);
      }
    case 3: return DecenasY("TREINTA", unidad);
    case 4: return DecenasY("CUARENTA", unidad);
    case 5: return DecenasY("CINCUENTA", unidad);
    case 6: return DecenasY("SESENTA", unidad);
    case 7: return DecenasY("SETENTA", unidad);
    case 8: return DecenasY("OCHENTA", unidad);
    case 9: return DecenasY("NOVENTA", unidad);
    case 0: return Unidades(unidad);
  }
}//Unidades()

function DecenasY(strSin, numUnidades){
  if (numUnidades > 0)
    return strSin + " Y " + Unidades(numUnidades)

  return strSin;
}//DecenasY()

function Centenas(num){

  centenas = Math.floor(num / 100);
  decenas = num - (centenas * 100);

  switch(centenas)
  {
    case 1:
      if (decenas > 0)
        return "CIENTO " + Decenas(decenas);
      return "CIEN";
    case 2: return "DOSCIENTOS " + Decenas(decenas);
    case 3: return "TRESCIENTOS " + Decenas(decenas);
    case 4: return "CUATROCIENTOS " + Decenas(decenas);
    case 5: return "QUINIENTOS " + Decenas(decenas);
    case 6: return "SEISCIENTOS " + Decenas(decenas);
    case 7: return "SETECIENTOS " + Decenas(decenas);
    case 8: return "OCHOCIENTOS " + Decenas(decenas);
    case 9: return "NOVECIENTOS " + Decenas(decenas);
  }

  return Decenas(decenas);
}//Centenas()

function Seccion(num, divisor, strSingular, strPlural){
  cientos = Math.floor(num / divisor)
  resto = num - (cientos * divisor)

  letras = "";

  if (cientos > 0)
    if (cientos > 1)
      letras = Centenas(cientos) + " " + strPlural;
    else
      letras = strSingular;

  if (resto > 0)
    letras += "";

  return letras;
}//Seccion()

function Miles(num){
  divisor = 1000;
  cientos = Math.floor(num / divisor)
  resto = num - (cientos * divisor)

  strMiles = Seccion(num, divisor, "UN MIL", "MIL");
  strCentenas = Centenas(resto);

  if(strMiles == "")
    return strCentenas;

  return strMiles + " " + strCentenas;

  //return Seccion(num, divisor, "UN MIL", "MIL") + " " + Centenas(resto);
}//Miles()

function Millones(num){
  divisor = 1000000;
  cientos = Math.floor(num / divisor)
  resto = num - (cientos * divisor)

  strMillones = Seccion(num, divisor, "UN MILLON", "MILLONES");
  strMiles = Miles(resto);

  if(strMillones == "")
    return strMiles;

  return strMillones + " " + strMiles;

  //return Seccion(num, divisor, "UN MILLON", "MILLONES") + " " + Miles(resto);
}//Millones()

function NumeroALetras(num){
  var data = {
    numero: num,
    enteros: Math.floor(num),
    centavos: (((Math.round(num * 100)) - (Math.floor(num) * 100))),
    letrasCentavos: "",
    letrasMonedaPlural: "",
    letrasMonedaSingular: ""
  };

  if (data.centavos > 0)
    data.letrasCentavos = "CON " + data.centavos + "/100";

  if(data.enteros == 0)
    return "" + data.letrasMonedaPlural + " " + data.letrasCentavos;
  if (data.enteros == 1)
    return Millones(data.enteros) + " " + data.letrasMonedaSingular + " " + data.letrasCentavos;
  else
    return Millones(data.enteros) + " " + data.letrasMonedaPlural + " " + data.letrasCentavos;
}//NumeroALetras()

function convertirLetras(control)
{
    //alert(control.id);
    var id = '#frmCabecera\\:txtCantidad';
    var id2 = '#frmCabecera\\:lblCantidad';
    //alert($(id).val());
    var a = NumeroALetras($(id).val());
    $(id2).html(a);
    
}


function setFocus(id){
    document.getElementById(id).focus();
}
