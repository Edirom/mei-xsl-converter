#!/bin/bash

VERSION_STYLESHEET=latest
VERSION_ODD=latest
VERSION_ENCODING_TOOLS=latest
VERSION_W3C_MUSICXML=latest
VERSION_MEILER=latest

MEI_SOURCES_HOME=/usr/share/xml/mei

#https://github.com/music-encoding/encoding-tools/releases/latest
if [ "$VERSION_ENCODING_TOOLS" = "latest" ] ; then 
    VERSION_ENCODING_TOOLS=$(curl "https://api.github.com/repos/music-encoding/encoding-tools/releases/latest" | grep -Po '"tag_name": "v\K.*?(?=")');    
    fi 
echo "Encoding tools version set to ${VERSION_ENCODING_TOOLS}" 
# download the required tei odd and stylesheet sources in the image and move them to the respective folders ( ${TEI_SOURCES_HOME})
curl -s -L -o /tmp/encoding.zip https://github.com/music-encoding/encoding-tools/archive/refs/tags/v${VERSION_ENCODING_TOOLS}.zip 
unzip /tmp/encoding.zip -d /tmp/encoding 
rm /tmp/encoding.zip 
mkdir -p  ${MEI_SOURCES_HOME}/music-stylesheets/encoding-tools 
cp -r /tmp/encoding/*/*  ${MEI_SOURCES_HOME}/music-stylesheets/encoding-tools 
rm -r /tmp/encoding

#https://github.com/w3c/musicxml/releases/latest
if [ "$VERSION_W3C_MUSICXML" = "latest" ] ; then 
    VERSION_W3C_MUSICXML=$(curl "https://api.github.com/repos/w3c/musicxml/releases/latest" | grep -Po '"tag_name": "v\K.*?(?=")');     
    fi 
echo "W3C Music XML version set to ${VERSION_W3C_MUSICXML}" 
# download the required stylesheet sources in the image and move them to the respective folders (${MEI_SOURCES_HOME})
curl -s -L -o /tmp/musicxml.zip https://github.com/w3c/musicxml/releases/download/v${VERSION_W3C_MUSICXML}/musicxml-${VERSION_W3C_MUSICXML}.zip 
unzip /tmp/musicxml.zip -d /tmp/musicxml 
rm /tmp/musicxml.zip 
mkdir -p  ${MEI_SOURCES_HOME}/music-stylesheets/w3c-musicxml/ 
cp -r /tmp/musicxml/*  ${MEI_SOURCES_HOME}/music-stylesheets/w3c-musicxml/ 
rm -r /tmp/musicxml

#https://github.com/rettinghaus/MEILER/releases/latest
if [ "$VERSION_MEILER" = "latest" ] ; then 
    VERSION_MEILER=$(curl "https://api.github.com/repos/rettinghaus/MEILER/releases/latest" | grep -Po '"tag_name": "v\K.*?(?=")');    
    fi 
echo "MEILER version set to ${VERSION_MEILER}" 
# download the required tei odd and stylesheet sources in the image and move them to the respective folders ( ${TEI_SOURCES_HOME})
curl -s -L -o /tmp/meiler.zip https://github.com/rettinghaus/MEILER/archive/refs/tags/v${VERSION_MEILER}.zip 
unzip /tmp/meiler.zip -d /tmp/meiler 
rm /tmp/meiler.zip 
mkdir -p  ${MEI_SOURCES_HOME}/music-stylesheets/meiler 
cp -r /tmp/meiler/*/*  ${MEI_SOURCES_HOME}/music-stylesheets/meiler 
rm -r /tmp/meiler

#https://github.com/Edirom/data-configuration - no releases, clone most recent version in dev branch and move to correct folder
git clone -b dev https://github.com/Edirom/data-configuration /tmp/data-configuration 
mkdir -p  ${MEI_SOURCES_HOME}/music-stylesheets/data-configuration 
cp -r /tmp/data-configuration/*  ${MEI_SOURCES_HOME}/music-stylesheets/data-configuration 
rm -r /tmp/data-configuration
