# Falabella-beer-machine

## Descripción

Para resolver el desafío propuesto, se decidí utilizar Java en conjunto con el framework Spring Boot para la implementación del API solicitada.

Utilicé TDD como metodoloía de desarrollo del API logrando un code coverage del 90% de las clases y 70% de los métodos

## Instrucciones

Para ejecutar el servicio primero se debe generar el archivo .jar, para esto ejecutamos `mvn package` el cual ejecutará todos los test definidos y empaquetara la aplicación en el archivo beer-machine.jar dentro del directorio target.

Una vez empaquetada la aplicación se puede ejecutar utilizando docker, para esto primero se debe crear la imagen con el comando `docker build -t falabella-docker . ` y luego para ejecutarla se debe utilizar el comando `docker run -p 8080:8080 -t falabella-docker`. Si todo sale bien la aplición estara disponible en [localhost:8080/beers](http://localhost:8080/beers)
