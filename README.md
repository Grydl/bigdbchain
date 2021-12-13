# Bigchaindb
Projet Java BigchainDB

## Créer un réseau BigchainDB

### Installation VM

Créer une VM avec Ubuntu 18.04

Se connecter à cette VM en ssh et noter les adresses IP externes de chaque nœud.

Ouvrir les ports  : **26656**  et  **9984**

``ssh -i PATH_TO_PRIVATE_KEY USERNAME@EXTERNAL_IP``

### Installation Docker

Installer les packages afin d’utiliser apt à accéder au repository  via HTTPS
``sudo apt-get install apt-transport-https ca-certificates curl software-properties-common``

Ajouter la clé GPG officiel de Docker
``curl -fsSL https://download.docker.com/linux/ubuntu/gpg | sudo apt-key add -``

Ajouter le repository à APT sources
``sudo add-apt-repository  "deb [arch=amd64] https://download.docker.com/linux/ubuntu  $(lsb_release -cs) stable"``

Mettre à jour ensuite la base de données APT
``sudo apt-get update``

Installer Docker Community Edition(CE)
``sudo apt-get -y install docker-ce``

Ajouter docker au programmes de démarrage du Serveur
``sudo systemctl status docker``

Vérifier ensuite si Docker est bien installer sur l’application
``sudo docker run hello-world``

Pour utiliser la commande docker sans devoir ajouter sudo
- ``sudo usermod -aG docker ${USER}``
- ``Installer pip``
- ``sudo apt-get -y install python-pip``
- ``sudo pip install pip --upgrade``

Installer docker compose
``sudo pip install docker-compose``

Tester si l’installation est ok
``docker-compose --version``

## Lancer Bigchaindb Docker

Exécuter la commande ci-dessous pour démmarrer BigchainDB :

``docker pull grydl/bigchaindb:all-in-one``

```
docker run \
--detach \
--name grydl_bigchaindb \
--publish 9984:9984 \
--publish 9985:9985 \
--publish 27017:27017 \
--publish 26657:26657 \
--volume $HOME/bigchaindb_docker/mongodb/data/db:/data/db \
--volume $HOME/bigchaindb_docker/mongodb/data/configdb:/data/configdb \
--volume $HOME/bigchaindb_docker/tendermint:/tendermint \
grydl/bigchaindb:all-in-one
```

Avec docker-compose , demarrer Bigchaindb avec la commande ci-dessous, 
ne pas oublier de se placer dans le repertoire ./docker avant 

``docker-compose -up &``

Vérifier que vous le container docker est bien démmarer

```docker ps```

Connecter à l’adresse ci dessous et vérifier que cela fonctionne  
http://localhost:9984/
ou
http://votre_ip_publique:9984/

**Stopper ensuite le container**

``docker ps``

Récupérer le container_id

``docker stop container_id``

Stopper le container avec la commande ci-dessous dans le cas d'utilisationde  docker-compose
docker.
ne pas oublier de se placer dans le repertoire ./docker avant

``docker-compose down ``

## Configurer le réseau Bigchainbb

Maintenant nous pouvons faire la configuration du réseau

Chaque bigchaindb est identifier par :
- **hostname :** il s’agit du nom de domaine or de son IP
pub_key.value : la clé publique du node Tendermint, la clé privée se trouve dans le repertoire .
*.tendermint/config/priv_validator.json*

- **node_id :** l’identifiant du noeud Tendermint, pour obtenir le node_id , il faut lancer la commande

``tendermint show_node_id``


Il faut maintenant créer le fichier **genesis**  qui se trouve dans le répertoire  :
*tendermint/config/genesis.json*

Une fois le fichier genesis rempli , il faut le partager avec tous les membres, les membres doivent le mettre dans le repertoire :
/.tendermint/config

Tous les membres partagent le même **chain_id** , **genesis time**, et la liste des validators

Il faut ensuite modifier le fichier : *tendermint/config/config.toml*

```
moniker = "Name of our node"
create_empty_blocks = false
log_level = "main:info,state:info,*:error"

persistent_peers = "<Member 1 node id>@<Member 1 hostname>:26656,\
<Member 2 node id>@<Member 2 hostname>:26656,\
<Member N node id>@<Member N hostname>:26656,"

send_rate = 102400000
recv_rate = 102400000

recheck = false
```



# Intallation de BigchainDB

Il est aussi possible d'installer BigChainDB sur son poste local plutôt que d'utiliser lîmage Docker.
En suivant le guide ci-dessous

Mettre à jour la machine

```
sudo apt update
sudo apt full-upgrade
```

Installer python

``
sudo apt install -y python3-pip libssl-dev
``

Mise à jour de pip a une version ulterieur à 19 car Bigdbchain à besoin de gevent

``
sudo pip3 install -U pip
``

Installer la derniere version de Bigchaindb

``
sudo pip3 install bigchaindb==2.0.0
``

Vérifier que bigchaindb est bien installée

``
bigchaindb --version
``

Configurer

``
bigchaindb configure
``

A la question API Server bind? (default `localhost:9984`).
Repondre :  **0.0.0.0:9984**

### Installation MongoDB

Lancer la commande ci-dessous pour installer mongodb

``
sudo apt install mongodb
``

### Installer Tendermint

Lancer la commande ci-dessous
```
sudo apt install -y unzip
wget https://github.com/tendermint/tendermint/releases/download/v0.31.5/tendermint_v0.31.5_linux_amd64.zip
unzip tendermint_v0.31.5_linux_amd64.zip
rm tendermint_v0.31.5_linux_amd64.zip
sudo mv tendermint /usr/local/bin
```

Configurer Tendermint
Lancer la commande

```
tendermint init
```
