#!/bin/bash

VERSION_MEILER=$(curl "https://api.github.com/repos/rettinghaus/MEILER/releases/latest" | grep -Po '"tag_name": "v\K.*?(?=")')
curl -s -L -o /tmp/meiler.zip https://github.com/rettinghaus/MEILER/archive/refs/tags/v${VERSION_MEILER}.zip
sudo mkdir -p  /usr/share/xml/mei/music-stylesheets/meiler
unzip /tmp/meiler.zip -d /tmp/meiler
sudo cp -r /tmp/meiler/*/*  /usr/share/xml/mei/music-stylesheets/meiler
rm -r /tmp/meiler*
