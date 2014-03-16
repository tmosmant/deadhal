Application Android Deadhal
===========================

Réalisée par :
-BOUSRY Fazal
-FRICOTTEAU Vincent
-MOSMANT Thomas
-REMY Michaël


Présentation
============

L'application permet de construire et de visualiser des labyrinthes
pouvant être utilisé pour une application ludique mais également pour
cartographier l'intérieur des bâtiments et pouvoir s'y repérer.


Installation
============

Il y a 2 méthodes pour lancer l'application sur son appareil Android :

	1) Récupérer le fichier .apk qui se trouve dans le repertoire bin
	de l'archive et le mettre sur votre appareil.
	Autoriser l'installation d'applications issues de sources
	inconnues de manière à pouvoir l'installer.
	
	2) Importer le projet Android sur un IDE avec le SDK. Activer le 
	mode debogage USB de son appareil, le connecter à son ordinateur
	et lancer le projet en tant qu'application Android.

Enfin, vous pouvez copier les exemples de plans fournis dans le
répertoire maps du projet sur votre appareil. Soit dans le dossier
"deadhal" de votre mémoire interne pour qu'ils soient automatiquement
reconnus, soit n'importe où sur votre appareil pour ensuite les ouvrir
à partir d'un navigateur de fichier.
	
	
Utilisation
===========

Le menu "Edition" permet de modifier le labyrinthe, il faut que le
cadenas en haut à droite soit dévérouillé.
Dans ce menu on peut : 
	-séléctionner une salle en faisant une pression longue sur
	celle-ci, de manière à la déplacer ou à modifier sa taille.
	-ajouter, modifier, sélectionner des salles et des couloirs via le
	menu de droite
	-sélectionner le sens des couloirs lors de l'ajout de ceux-ci.
	
Le menu "Navigation" permet de visualiser et de parcourir le 
labyrinthe, il faut que le cadenas en haut à droite soit vérouillé.
Dans ce menu on peut :
	-placer le robot avec une pression longue dans l'une des salles
	-supprimer le robot en faisant une pression longue sur lui
	-déplacer le point en faisant glisser son doigt sur l'écran
	-et en activant le mode accéléromètre via le menu option de
	votre appareil, vous pouvez déplacer le robot en penchant votre
	appareil
	-trouver le plus court chemin d'une salle vers une autre (si 
	possible) avec le bouton en haut à droite.
	
Le menu "Open" permet d'ouvrir un labyrinthe déjà existant sur votre
appareil en sélectionant le fichier. Il est aussi possible de 
partager, renommer et supprimer un labyrinthe en effectuant une 
pression longue sur le nom du labyrinthe.

Le menu "Save" permet de sauvegarder un labyrinthe en lui affectant
un nom. Si le nom est déjà existant une fenêtre s'ouvre pour vous
proposer de remplacer le fichier ou non.

Le menu "Help" permet à l'utilisateur d'avoir un guide d'utilisation
intégré dans l'application.

Le menu "About" donne les informations au sujet de l'application.

Enfin, à partir d'un explorateur de fichier, il est possible d'ouvrir
les fichier .dh avec l'application deadhal, ce qui a pour effet de les
afficher dans l'application et de les importer dans le répertoire de
l'application.