
 document.getElementById("createRally").hidden = true;

function newRally() {
    document.getElementById("createRally").hidden = false;
};

function createRally() {
    $('#message').text("");
    var rally = $('#rally').val();
    var description = $('#description').val();
    var place = $('#place').val();
    var date = $('#date').val();
    var time = $('#time').val();
    var radio = $('#radio').val();
    var email = $('#email').val();
    var password = $('#password').val();
    if (true) { //Verify whether there are errors
        $('#message').text("Failed");
    } else {
        $.ajax({
            url: './ActionServlet',
            method: 'POST',
            data: {
                action: 'create rally',
                rally: rally,
                description: description,
                place: place,
                date: date,
                time: time,
                radio: radio,
                email: email,
                password: password
            },
            dataType: 'json',
            error: function () {
                alert("Error while sending new rally request");
            }
        }).done(function (data) {
            var reponse = data.createRally;
            if (reponse.rallyCreated === "true") {
                document.getElementById("createRally").hidden = true;
            } else {
                $('#message').text("Ups we didn't succed to verify rally creation");
            }
        });
    }
}
;


