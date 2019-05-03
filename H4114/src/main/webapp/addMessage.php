<?php
    session_start();
    if (isset($_SESSION["login"]))
    {
        try
        {
            $message =$_POST["message"];
            $bdd = new PDO("mysql:host=localhost;dbname=test;charset=utf8","root","");
            $bdd->setAttribute(PDO::ATTR_ERRMODE,PDO::ERRMODE_EXCEPTION);
            $sql =  "INSERT INTO homeMessage  VALUES (NULL, '".$message."', '".$_SESSION["login"]."')";
            $bdd->exec($sql);
            header('Location: ./accueil.php');
        
        }
        catch(PDOException $e)
        {
            echo $sql."<br />".$e->getMessage();
        }
    
    }

?>