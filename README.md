# Projet Services Web et RMI

## Installation et utilisation des services

Le projet se compose de 3 projets développés avec Maven (pour faciliter la gestion des dépendances) :
- Banque, Database et EiffelCorp. Ce dernier contient le service web IfsCarsService, le service RMI présenté précédemment, ainsi que le contenu de l’application web.
Ces 3 parties sont situées dans le même projet mais sont indépendantes.

Un dump de la base de données (structures + données) est fourni. Les informations de connexion sont en en-tête de la classe fr.uge.database.DataBase du projet Database.

Pour qu’un service web se connecte à une base de données il est nécessaire d’installer un driver JDBC sur le serveur. Cela se traduit ici par l’ajout dans le répertoire lib/ de Tomcat d’un fichier .jar. Dans ce projet nous utilisons une base postgreSQL et le jar en question se situe dans les dépendances Maven du projet Database. Il est également disponible sur le repos Maven officiel à cette adresse : https://mvnrepository.com/artifact/org.postgresql/postgresql

Lorsque tout est installé et que les 3 projets sont ajoutés au serveur Tomcat, il faut le démarrer PUIS lancer la classe fr.uge.ifsCars.Server du projet EiffelCorp en tant qu’application Java pour démarrer le binding des objets RMI.
Si la connexion à la base de données a réussie il devrait apparaître dans les logs le message « Connected to database ! », sinon un message d’erreur s’affiche.

L’application web est disponible en local à l’adresse suivante : http://localhost:port/EiffelCorp/
port = port utilisé par le serveur Tomcat (par défaut 8080).

## Contenu du projet
- Les 3 projets Java (Banque, Database et EiffelCorp).
- Un dump PostgreSQL pour la base de données.
- Un dossier doc contenant le rapport, le manuel utilisateur et la documentation technique au format Javadoc.
- Ce readme.
