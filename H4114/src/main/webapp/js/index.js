function Connect(){
        $('#message').text("");
        var email = $('#email').val();
        var password = $('#password').val();
        $.ajax({
                url: './ActionServlet',
                method: 'POST',
                data: {
                        action: 'connect',
                        email: email,
                        password: password
                },
                dataType: 'json',
                error: function(){
                    alert("Error while sending sign in request");
                }
        }).done(function (data) {
                alert("hey! I'm done with sign in");
                var reponse = data.connect;
                if (reponse.connect === "successful") {
                        window.location = "home.html";
                } else{
                        $('#message').text("Failed: Email or password invalid!");
                }
        });
}