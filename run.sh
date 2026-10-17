#!/bin/bash
# Compila e executa o Java Inventory Manager (persistencia em SQLite)

SRC_DIR="src"
OUT_DIR="out"
CP="lib/sqlite-jdbc-3.46.1.3.jar"

echo "Compilando..."
mkdir -p $OUT_DIR
javac -cp "$CP" -d $OUT_DIR -sourcepath $SRC_DIR $SRC_DIR/Main.java

if [ $? -eq 0 ]; then
    echo "Compilacao ok. Iniciando a aplicacao..."
    java --enable-native-access=ALL-UNNAMED -cp "$OUT_DIR:$CP" Main
else
    echo "Falha na compilacao."
    exit 1
fi
