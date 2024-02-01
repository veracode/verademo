# Proyecto Verademo con Integración de Veracode en Azure DevOps

## Descripción

Este proyecto se basa en un fork del repositorio Verademo de Veracode, donde se ha modificado el pipeline para incluir la integración con la plataforma Veracode. El objetivo es automatizar el proceso de compilación, prueba y escaneo de seguridad del código Java utilizando Maven y las herramientas de Veracode.

![alt text](verademo-azuredevops-demo.drawio.png)

## Requerimientos o Requisitos
Antes de comenzar con la integración en Azure DevOps, asegúrate de tener los siguientes requisitos:

- Una cuenta activa en Veracode.
- Acceso y credenciales para el repositorio fork de Verademo en GitHub. [Instrucciones aquí](https://docs.veracode.com/r/c_api_credentials3)
- Tener instalado y configurado Maven en Agente de Azure DevOps.
- Acceso a Azure DevOps con permisos para crear y editar pipelines.
- El plugin "Veracode Upload and Scan" debe estar activado en el Marketplace de Azure DevOps. [Instrucciones aquí](https://docs.veracode.com/r/t_install_azure_devops_extension)

## Creación Pipeline en Azure DevOps

Sigue estos pasos para crear el pipeline en Azure DevOps:

### Crea un nuevo pipeline:

- Dirígete a la sección "Pipelines" en Azure DevOps.
- Selecciona "New Pipeline" y sigue los pasos para conectar tu repositorio de GitHub.

### Configuración del Pipeline:

- Selecciona el archivo de configuración del pipeline (azure-pipelines.yml).
- Modifica el archivo para incluir las etapas necesarias, como la compilación con Maven y las tareas de escaneo con Veracode.


### Configuración de Variables:

- Crea un grupo de variables en la sección de Library de Azure DevOps para almacenar las credenciales y configuraciones necesarias. Asegúrate de incluir las variables requeridas para Veracode, como VERACODE_API_ID y VERACODE_API_KEY.
- El pipeline propuesto, usa un grupo de variables llamado: VERACODE_API

### Creación de Service Conenction:

- En la configuración del proyecto, ir a Pipelines > Service connections.
- Crear un nue Service Connections presionando "New service conenction"
- Seleccionar el plugin "Veracode Platform" e instalar. Cualquier duda, consulta la documentación [aquí](https://docs.veracode.com/r/Create_a_Service_Connection_in_Azure_DevOps)

### Activación de Plugin de Veracode:

- Ir al Marketplace de Azure DevOps y asegúrate de tener instalado y activado el plugin "Veracode Upload and Scan". Configura cualquier parámetro adicional según tus necesidades.

## Ejecución

Una vez configurado el pipeline, puedes ejecutarlo manualmente o configurar desencadenadores automáticos basados en eventos en tu repositorio de código.

Para ejecutar manualmente:

- Navega a la sección "Pipelines" en Azure DevOps.
- Selecciona el pipeline creado y haz clic en "Run Pipeline".

## Revisión de Resultados

Después de la ejecución del pipeline, puedes revisar los resultados en Azure DevOps:

- En la sección "Pipelines", selecciona el pipeline y examina las etapas y tareas para asegurarte de que se hayan completado correctamente.
- Verifica la consola de salida para los resultados del escaneo de Veracode y cualquier mensaje de error.

## Colaboradores
- [Erick Arroyo](https://github.com/erickarroyo1)
- [Saúl Quintero](https://github.com/saqpsaqp)