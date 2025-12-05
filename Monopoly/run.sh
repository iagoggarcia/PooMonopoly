#!/bin/bash

# Limpiar TODAS las clases antiguas del proyecto
find . -name "*.class" -delete

# Compilar TODO el proyecto
javac $(find . -name "*.java")

# Si la compilación fue exitosa, ejecutar
if [ $? -eq 0 ]; then
    java monopoly.MonopolyETSE comandos_P2_1.txt
else
    echo "Error de compilación. No se ejecutará el programa."
fi

