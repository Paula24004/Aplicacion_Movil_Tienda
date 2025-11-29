**Descripción de la Aplicación**

La aplicación móvil Sportwear es una tienda deportiva que permite visualizar un catálogo de productos, revisar tallas disponibles, ver el stock por talla, agregar productos al carrito y convertir el precio de cada producto desde pesos chilenos (CLP) a dólares (USD) y euros (EUR).
La app está desarrollada con Kotlin y Jetpack Compose, y se conecta a un backend construido con Spring Boot.

El backend se encuentra desplegado en una instancia EC2 de AWS. Debido a que trabajamos en un laboratorio, la instancia no permanece encendida, por lo que debe iniciarse manualmente cada vez que se necesite utilizar la API.

**Iniciar la instancia AWS y levantar el backend**

Iniciar la instancia en AWS.
Ingresar a la consola de AWS, ir al servicio EC2, seleccionar "Instances", buscar la instancia correspondiente al backend y presionar "Start Instance".
La instancia debe encenderse porque en el laboratorio se apaga automáticamente.

Obtener la IP pública.
Luego de iniciar la instancia, seleccionarla y copiar la Public IPv4 Address que aparece en los detalles de la máquina.

Abrir una terminal en la carpeta donde está la clave .pem.
La clave .pem es necesaria para conectarse mediante SSH a la instancia. Abrir una terminal (PowerShell, CMD o Git Bash) dentro de la carpeta donde está el archivo.

Conectarse a la instancia por SSH.
En la terminal escribir:
ssh -i "nombre_clave.pem" ubuntu@IP_PUBLICA
Reemplazar nombre_clave.pem por el nombre del archivo real, e IP_PUBLICA por la dirección IP obtenida desde AWS.

Confirmar la conexión.
El sistema preguntará si se desea continuar con la conexión. Escribir yes.
Si la autenticación es correcta, se abrirá la sesión dentro del servidor.

Entrar a la carpeta del backend.
Dentro del servidor escribir:
cd Sportwear
Esa carpeta contiene el proyecto backend ya compilado.

Ejecutar la aplicación backend.
Para levantar la API:
nohup java -jar target/Sportwear-0.0.1-SNAPSHOT.jar &
Este comando ejecuta la aplicación en segundo plano para que continúe activa incluso si se cierra la terminal.

Verificar que el backend está corriendo.
Escribir:
ps -ef | grep java
Si aparece una línea haciendo referencia al archivo jar sin errores, el backend está funcionando correctamente.

Confirmar desde el navegador.
Abrir en el navegador:
http://IP_PUBLICA:8080/swagger-ui/index.html
Si Swagger carga correctamente, la API está activa y lista para ser utilizada por la aplicación móvil.
