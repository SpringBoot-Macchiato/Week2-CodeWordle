# CodeWordle

Una aplicación educativa inspirada en el clásico juego Wordle, enfocada en el aprendizaje de términos relacionados con programación y tecnología.

## 🎯 Descripción

CodeWordle permite a los usuarios jugar juegos de adivinanza de palabras con vocabulario técnico de diferentes temas como Java, Spring, DevOps y Bases de Datos. El juego sigue las reglas clásicas de Wordle pero con palabras específicas del mundo de la programación.

## 🚀 Características

- **Temas Educativos**: Java, Spring Framework, DevOps, Bases de Datos
- **Palabras de 5 letras**: Todas las palabras están estandarizadas a 5 letras
- **Retroalimentación Visual**: Sistema de colores para letras correctas, presentes y ausentes
- **Interfaz Dinámica**: UI que se adapta automáticamente a diferentes longitudes de palabra
- **Teclado Virtual**: Teclado interactivo para facilitar la entrada
- **Persistencia**: Base de datos H2 para almacenar partidas e intentos
- **Validación Robusta**: Validación tanto en cliente como servidor

## 🛠️ Stack Tecnológico

- **Java 21**
- **Spring Boot 3.5.7** (Web, Validation, JDBC)
- **Base de datos H2** (en memoria)
- **Spring JDBC** con JdbcTemplate
- **JSP** con Jakarta Tag Library (JSTL)
- **Tailwind CSS** para estilos (via CDN)
- **Lombok** para simplificación de modelos

## 📋 Requisitos Previos

- Java 21 o superior
- Maven 3.6 o superior
- Navegador web moderno

## 🚀 Instalación y Ejecución

### 1. Compilar y ejecutar
```bash
mvn clean compile
mvn spring-boot:run
```

### 2. Acceder a la aplicación
Abrir el navegador y navegar a: `http://localhost:8080`

## 🎮 Cómo Jugar

1. **Seleccionar Tema**: En la página principal, elige un tema de programación
2. **Ingresar Intentos**: Usa el teclado virtual o tu teclado físico para ingresar palabras
3. **Recibir Retroalimentación**:
   - 🟩 Verde: Letra correcta en posición correcta
   - 🟨 Amarillo: Letra correcta en posición incorrecta
   - ⬜ Gris: Letra no presente en la palabra
4. **Ganar o Perder**: Tienes 6 intentos para adivinar la palabra

## 📁 Estructura del Proyecto

```
src/main/java/com/crudzaso/codewordle/
 ├─ controller/       # Controladores MVC y REST
 ├─ service/         # Lógica de negocio
 ├─ repository/      # Acceso a datos
 ├─ model/           # Modelos de datos
src/main/resources/
 ├─ application.properties
 ├─ schema.sql       # Esquema de base de datos
 ├─ data.sql         # Datos iniciales
src/main/webapp/WEB-INF/views/
 ├─ home.jsp         # Vista principal
 └─ game.jsp         # Vista del juego
```

## 🔧 Endpoints Principales

### MVC Endpoints
- `GET /` - Página principal con selección de temas
- `POST /start-game` - Iniciar nueva partida
- `GET /game/{id}` - Página del juego

### REST Endpoints
- `POST /game/{id}/guess` - Enviar intento (AJAX)

## 🧪 Testing

Para ejecutar las pruebas unitarias:

```bash
mvn test
```

## 📊 Base de Datos

La aplicación utiliza H2 Database en memoria. Para acceder a la consola H2 durante el desarrollo:

1. Ejecutar la aplicación
2. Navegar a: `http://localhost:8080/h2-console`
3. JDBC URL: `jdbc:h2:mem:testdb`
4. Usuario: `sa`
5. Contraseña: (vacío)

## 🎨 Personalización

### Agregar Nuevos Temas

1. Agregar tema en `src/main/resources/data.sql`:
```sql
INSERT INTO themes (name, description) VALUES
('NUEVO_TEMA', 'Descripción del nuevo tema');
```

2. Agregar palabras del tema:
```sql
INSERT INTO words (word, theme_id) VALUES
('PALAB1', 5),
('PALAB2', 5);
```

### Modificar Estilos

Los estilos están implementados con Tailwind CSS. Modifica las clases en los archivos JSP para personalizar la apariencia.

