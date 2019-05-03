<?php
    
    $bdd = new PDO("mysql:host=localhost;dbname=test;charset=utf8","root","");
    session_start();

?> 
<html>
    <head>
        <title></title>
    </head>
    <body>
        <?php 
            if(!isset($_SESSION["login"]))
            {
                header('Location: ./index.html');
            }
            $_SESSION["test"] = "test";
        ?>

         <script type="text/javascript">
                var a =  "<?php echo  $_SESSION["test"]; ?>";
                var ssid = document.cookie;
                var xhttp = new XMLHttpRequest();
                xhttp.onreadystatechange = function() 
                {
                    if (this.readyState == 4 && this.status == 200) 
                    {
                       consol.log(this.responseText);
                    }
                };
                xhttp.open("POST", "test.php", true);
                xhttp.send("A="+a+"?B="+ssid);
            </script>

        <h1>ACCUEIL de  <?php echo $_SESSION["login"] ?></h1>
       
           
        <form action="addMessage.php" method="post">
            <label for="message">Enter your message</label> <br/>
            <textarea name="message" >
            </textarea>
            <br/>
            <input type="submit" value="Add Message!"/>
        </form>
       <?php
       $login = $_SESSION["login"];
       echo $login;
       $login = "';SHOW TABLES;--";
       echo $login;
            $rep = $bdd->query( "SELECT message FROM homeMessage");
            while ($data =$rep->fetch())
            {
               echo $data["message"]."<br />";
        
            }
       ?>

       


    </body>
</html>