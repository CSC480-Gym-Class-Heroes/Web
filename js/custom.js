$(document).ready(function() {
    $.ajax({
    	type: "GET",
		dataType: "jsonp",
        url: "http://pi.cs.oswego.edu:8080/Gym/getcurrentcount?gym=glimmerglass",
    success: (function(data) {
       $('.current').append(data);
       $('.total').append(data);
       $('.average').append(data);
    }
});
});