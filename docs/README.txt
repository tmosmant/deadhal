Application Android Deadhal
===========================

R�alis�e par :
-BOUSRY Fazal
-FRICOTTEAU Vincent
-MOSMANT Thomas
-REMY Micha�l


Pr�sentation
============

L'application permet de construire et de visualiser des labyrinthes
pouvant �tre utilis� pour une application ludique mais �galement pour
cartographier l'int�rieur des b�timents et pouvoir s'y rep�rer.


Installation
============

Il y a 2 m�thodes pour lancer l'application sur son appareil Android :

	1) R�cup�rer le fichier .apk qui se trouve dans le repertoire bin
	de l'archive et le mettre sur votre appareil.
	Autoriser l'installation d'applications issues de sources
	inconnues de mani�re � pouvoir l'installer.
	
	2) Importer le projet Android sur un IDE avec le SDK. Activer le 
	mode debogage USB de son appareil, le connecter � son ordinateur
	et lancer le projet en tant qu'application Android.

Enfin, vous pouvez copier les exemples de plans fournis dans le
r�pertoire maps du projet sur votre appareil. Soit dans le dossier
"deadhal" de votre m�moire interne pour qu'ils soient automatiquement
reconnus, soit n'importe o� sur votre appareil pour ensuite les ouvrir
� partir d'un navigateur de fichier.
	
	
Utilisation
===========

Le menu "Edition" permet de modifier le labyrinthe, il faut que le
cadenas en haut � droite soit d�v�rouill�.
Dans ce menu on peut : 
	-s�l�ctionner une salle en faisant une pression longue sur
	celle-ci, de mani�re � la d�placer ou � modifier sa taille.
	-ajouter, modifier, s�lectionner des salles et des couloirs via le
	menu de droite
	-s�lectionner le sens des couloirs lors de l'ajout de ceux-ci.
	
Le menu "Navigation" permet de visualiser et de parcourir le 
labyrinthe, il faut que le cadenas en haut � droite soit v�rouill�.
Dans ce menu on peut :
	-placer le robot avec une pression longue dans l'une des salles
	-supprimer le robot en faisant une pression longue sur lui
	-d�placer le point en faisant glisser son doigt sur l'�cran
	-et en activant le mode acc�l�rom�tre via le menu option de
	votre appareil, vous pouvez d�placer le robot en penchant votre
	appareil
	-trouver le plus court chemin d'une salle vers une autre (si 
	possible) avec le bouton en haut � droite.
	
Le menu "Open" permet d'ouvrir un labyrinthe d�j� existant sur votre
appareil en s�lectionant le fichier. Il est aussi possible de 
partager, renommer et supprimer un labyrinthe en effectuant une 
pression longue sur le nom du labyrinthe.

Le menu "Save" permet de sauvegarder un labyrinthe en lui affectant
un nom. Si le nom est d�j� existant une fen�tre s'ouvre pour vous
proposer de remplacer le fichier ou non.

Le menu "Help" permet � l'utilisateur d'avoir un guide d'utilisation
int�gr� dans l'application.

Le menu "About" donne les informations au sujet de l'application.

Enfin, � partir d'un explorateur de fichier, il est possible d'ouvrir
les fichier .dh avec l'application deadhal, ce qui a pour effet de les
afficher dans l'application et de les importer dans le r�pertoire de
l'application.