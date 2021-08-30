

  function gup( name )
{
  name = name.replace(/[\[]/,"\\\[").replace(/[\]]/,"\\\]");
  var regexS = "[\\?&]"+name+"=([^&#]*)";
  var regex = new RegExp( regexS );
  var results = regex.exec( window.location.href );
  if( results == null )
    return "";
  else
    return results[1];
}


function getJsonOfStore(store){
        var datar = new Array();
        var jsonDataEncode = "";
        var records = store.getRange();
        for (var i = 0; i < records.length; i++) {
            datar.push(records[i].data);
        }
        jsonDataEncode = Ext.JSON.encode(datar);

        return jsonDataEncode;
    }