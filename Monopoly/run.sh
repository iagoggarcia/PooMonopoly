#!/bin/bash
# Limpiar clases antiguas
rm -f monopoly/*.class

# Compilar todos los .java del paquete monopoly
javac monopoly/*.java partida/*.java

# Si la compilación fue exitosa, ejecutar el programa principal
if [ $? -eq 0 ]; then
    java monopoly.MonopolyETSE comandos_P2_1.txt
else
    echo "Error de compilación. No se ejecutará el programa."
fi
