#!/bin/bash

# Script pour construire et lancer le jeu depuis le dossier parent

# Chemin vers le dossier RType
RTYPE_DIR="RType"

# Vérifier si Ant est installé
if ! command -v ant &> /dev/null; then
    echo "Ant n'est pas installé. Veuillez installer Ant et réessayer."
    exit 1
fi

# Changer de répertoire pour le dossier RType
cd "$RTYPE_DIR" || { echo "Impossible de se déplacer dans le dossier $RTYPE_DIR"; exit 1; }

# Construire le projet avec Ant
echo "Construction du projet..."
ant compile

# Vérifier si la construction a réussi
if [ $? -ne 0 ]; then
    echo "La construction du projet a échoué."
    exit 1
fi

# Lancer le jeu
echo "Lancement du jeu..."
ant run

# Vérifier si le lancement a réussi
if [ $? -ne 0 ]; then
    echo "Le lancement du jeu a échoué."
    exit 1
fi

echo "Le jeu a été lancé avec succès."

