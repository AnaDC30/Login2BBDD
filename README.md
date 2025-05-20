# 🔐 Login2BBDD

Este proyecto permite realizar autenticación de usuarios contra **dos bases de datos distintas**: una interna y otra externa. Además, registra los errores de inicio de sesión y muestra los usuarios que han iniciado sesión correctamente.

---

## 🚀 Funcionalidades

- Autenticación de usuarios contra dos bases de datos diferentes.
- Registro de intentos fallidos de inicio de sesión.
- Visualización de usuarios actualmente autenticados.

---

## 🧰 Tecnologías utilizadas

- **Lenguaje de programación**: Java
- **Framework**: Spring Boot
- **Gestión de dependencias**: Gradle
- **Entorno de desarrollo**: IntelliJ IDEA (carpeta `.idea` incluida)

---

## 📂 Estructura del proyecto

```
Login2BBDD/
├── .idea/                   # Configuración del entorno de desarrollo
├── app/                     # Código fuente de la aplicación
├── gradle/                  # Configuración de Gradle
├── .gitignore               # Archivos y carpetas ignorados por Git
├── build.gradle.kts         # Script de construcción de Gradle en Kotlin
├── gradle.properties        # Propiedades de configuración de Gradle
├── gradlew                  # Script para ejecutar Gradle en Unix
├── gradlew.bat              # Script para ejecutar Gradle en Windows
└── settings.gradle.kts      # Configuración de los módulos de Gradle
```

---

## ⚙️ Configuración e instalación

1. **Clonar el repositorio**:

   ```bash
   git clone https://github.com/AnaDC30/Login2BBDD.git
   ```

2. **Importar el proyecto** en tu IDE favorito (recomendado: IntelliJ IDEA).

3. **Configurar las conexiones a las bases de datos** en el archivo de propiedades correspondiente.

4. **Construir y ejecutar la aplicación** utilizando Gradle:

   ```bash
   ./gradlew bootRun
   ```

---

## 📝 Licencia

Este proyecto está bajo la licencia MIT. Consulta el archivo [LICENSE](LICENSE) para más información.

---

## 🤝 Contribuciones

Las contribuciones son bienvenidas. Por favor, abre un *issue* o envía un *pull request* para sugerir mejoras o reportar errores.

---

## 📫 Contacto

Para preguntas o sugerencias, puedes contactar a la autora a través de su perfil de GitHub: [AnaDC30](https://github.com/AnaDC30)
