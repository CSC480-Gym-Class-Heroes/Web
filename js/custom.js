function sendAjax() {
	
    $.ajax({
        url: "http://pi.cs.oswego.edu:8080/Gym/getcurrentcount?gym=glimmerglass",
        type: "GET",
		dataType: "text",
		
    success: function(data) {
       $("#current").append(data);
       $("#total").append(data);
       $("#average").append(data);
    },
    error:function(data,status,er) {
            alert("error: "+data+" status: "+status+" er:"+er);
        }
	});
}