function Inscription(){
    $('#message').text("");
    var email = $('#email').val();
    var pseudo = $('#pseudo').val();
    var password = $('#password').val();
    var confirm = $('#confirm').val();
    if(password!==confirm){
            $('#message').text("Failed: Password and confirmation must be equal!");
    }
    else{
        $.ajax({
                url: './ActionServlet',
                method: 'POST',
                data: {
                        action: 'inscription',
                        email:email,
                        pseudo:pseudo,
                        password:password
                },
                dataType: 'json',
                error: function(){
                    alert("Error while sending sign up request");
                }
        }).done(function (data) {
                var reponse = data.inscrit;
                if (reponse.inscrit === "true") {
                        window.location = "index.html";
                } else {
                       $('#message').text("Failed: Email or password invalid!");
                }
        });
    }
}



function getUser(){
    console.log($('#form').serializeJSON());
    console.log(JSON.stringify($('#form').serializeJSON()));
    return JSON.stringify($('#form').serializeJSON());
}
function alerte(){
    var a=getUser();
    alert(a);
}