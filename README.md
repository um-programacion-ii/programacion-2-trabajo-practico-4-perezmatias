[![Review Assignment Due Date](https://classroom.github.com/assets/deadline-readme-button-22041afd0340ce965d47ae6ef1cefeee28c7c493a6346c4f15d667ab976d596c.svg)](https://classroom.github.com/a/Vg2EF-QZ)
# 🚀 Trabajo Práctico: Sistema de Gestión de Biblioteca con Spring Framework

![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.4.5-green)
![Java](https://img.shields.io/badge/Java-21-orange)
![Maven](https://img.shields.io/badge/Maven-3.9.0-red)
![JUnit5](https://img.shields.io/badge/JUnit-5.10.1-green)
![Mockito](https://img.shields.io/badge/Mockito-5.8.0-blue)

## ⚠️ Importante: Antes de Comenzar

1. **Lectura Completa**
   - Es **OBLIGATORIO** leer la consigna completa antes de comenzar a trabajar
   - Asegúrate de entender todos los requisitos y etapas
   - Consulta las dudas antes de iniciar el desarrollo

2. **Configuración del Repositorio**
   - La rama `main` debe estar protegida
   - No se permiten pushes directos a `main`
   - Todo el desarrollo debe realizarse en ramas feature
   - Los cambios deben integrarse mediante Pull Requests

## 🔧 Configuración Inicial del Repositorio

### 1. Protección de la Rama Main
1. En "Branch name pattern" escribir `main`
2. Marcar la siguiente opción:
   - ✓ Require a pull request before merging
3. Hacer clic en "Create"

> 💡 **Nota**: La protección de la rama main es obligatoria y asegura que:
> - No se puedan hacer cambios directos en la rama main
> - Todos los cambios deben hacerse a través de Pull Requests
> - Esto ayuda a mantener un historial de cambios ordenado y a seguir buenas prácticas de desarrollo

### 2. Configuración de Issues y Pull Requests
1. Ir a Settings > General
2. En la sección "Features":
   - ✓ Habilitar Issues
   - ✓ Habilitar Pull Requests
3. En la sección "Pull Requests":
   - ✓ Habilitar "Allow merge commits"
   - ✓ Habilitar "Allow squash merging"
   - ✓ Deshabilitar "Allow rebase merging"

### 3. Configuración de Project Board
1. Ir a la pestaña "Projects"
2. Crear nuevo proyecto "Sistema de Gestión de Biblioteca"
3. Configurar las siguientes columnas:
   - To Do
   - In Progress
   - Code Review
   - Done

### 4. Configuración de Milestones
1. Ir a la pestaña "Milestones"
2. Crear los siguientes milestones:
   - Etapa 1: Configuración y Modelos
   - Etapa 2: Repositories y Services
   - Etapa 3: Controllers
   - Etapa 4: Testing y Documentación

### 5. Configuración de Labels
1. Ir a Issues > Labels
2. Crear las siguientes etiquetas:
   - `enhancement` (verde)
   - `bug` (rojo)
   - `documentation` (azul)
   - `testing` (amarillo)
   - `setup` (gris)
   - `model` (morado)
   - `service` (naranja)
   - `controller` (rosa)
   - `repository` (turquesa)

### 6. Configuración de Templates
1. Verificar que los templates estén correctamente ubicados:
   ```
   .github/
   ├── ISSUE_TEMPLATE/
   │   └── issue_template.yml
   └── PULL_REQUEST_TEMPLATE/
       └── pull_request_template.yml
   ```

### 7. Configuración de Git Local
```bash
# Configurar el repositorio remoto
git remote add origin <URL_DEL_REPOSITORIO>

# Crear y cambiar a la rama main
git checkout -b main

# Subir la rama main
git push -u origin main

# Crear rama de desarrollo
git checkout -b develop

# Subir la rama develop
git push -u origin develop
```

## 🎯 Objetivo General

Desarrollar un sistema de gestión de biblioteca utilizando Spring Framework, implementando una arquitectura en capas y aplicando los principios SOLID. El sistema deberá manejar diferentes tipos de recursos bibliográficos, préstamos y usuarios, utilizando una base de datos en memoria para la persistencia de datos.

## ⏰ Tiempo Estimado y Entrega

- **Tiempo estimado de realización:** 24-30 horas
- **Fecha de entrega:** 14/05/2025 a las 13:00 hs

### Desglose estimado por etapa:
- Configuración inicial y modelos: 6-7 horas
- Repositories y Services: 7-9 horas
- Controllers y Endpoints: 6-7 horas
- Testing y documentación: 5-7 horas

> 💡 **Nota**: Esta estimación considera la experiencia adquirida en trabajos anteriores y la complejidad de implementar una arquitectura en capas con Spring Framework. El tiempo se ha ajustado considerando que no se requiere implementación de persistencia real.

## 👨‍🎓 Información del Alumno
- **Nombre y Apellido**: [Matias Agustin Perez]
- **Legajo**: [61218]
- **Repositorio GitHub**: [https://github.com/um-programacion-ii/programacion-2-trabajo-practico-4-perezmatias.git]
## 📋 Requisitos Previos

- Java 21 o superior
- Maven 3.9.0 o superior
- Conocimientos básicos de:
  - Programación orientada a objetos
  - Principios SOLID
  - Spring Framework básico
  - REST APIs

## 🧩 Tecnologías y Herramientas

- Spring Boot 3.4.5
- Spring Web
- Spring Test
- Lombok (opcional)
- JUnit 5.10.1
- Mockito 5.8.0
- Git y GitHub

## 📘 Etapas del Trabajo

### Etapa 1: Configuración del Proyecto y Modelos Base

#### Objetivos
- Configurar un proyecto Spring Boot
- Implementar los modelos base del sistema
- Aplicar el principio SRP (Single Responsibility)

#### Tareas
1. Crear proyecto Spring Boot con las dependencias necesarias
2. Implementar las siguientes clases modelo:
   - `Libro` (id, isbn, titulo, autor, estado)
   - `Usuario` (id, nombre, email, estado)
   - `Prestamo` (id, libro, usuario, fechaPrestamo, fechaDevolucion)

#### Ejemplo de Implementación
```java
// Modelo base
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Libro {
    private Long id;
    private String isbn;
    private String titulo;
    private String autor;
    private EstadoLibro estado;
}

public enum EstadoLibro {
    DISPONIBLE,
    PRESTADO,
    EN_REPARACION
}
```

### Etapa 2: Implementación de Repositories y Services

#### Objetivos
- Implementar la capa de servicios usando interfaces
- Aplicar el principio ISP (Interface Segregation)
- Implementar la capa de repositorios
- Aplicar el principio DIP (Dependency Inversion)

#### Tareas
1. Crear interfaces de repositorio:
   - `LibroRepository`
   - `UsuarioRepository`
   - `PrestamoRepository`

2. Implementar servicios:
   - Crear interfaces de servicio:
     - `LibroService`
     - `UsuarioService`
     - `PrestamoService`
   - Implementar clases concretas:
     - `LibroServiceImpl`
     - `UsuarioServiceImpl`
     - `PrestamoServiceImpl`

#### Ejemplo de Implementación
```java
// Interface del repositorio
public interface LibroRepository {
    Libro save(Libro libro);
    Optional<Libro> findById(Long id);
    Optional<Libro> findByIsbn(String isbn);
    List<Libro> findAll();
    void deleteById(Long id);
    boolean existsById(Long id);
}

// Implementación del repositorio en memoria
@Repository
public class LibroRepositoryImpl implements LibroRepository {
    private final Map<Long, Libro> libros = new HashMap<>();
    private Long nextId = 1L;
    
    @Override
    public Libro save(Libro libro) {
        if (libro.getId() == null) {
            libro.setId(nextId++);
        }
        libros.put(libro.getId(), libro);
        return libro;
    }
    
    @Override
    public Optional<Libro> findById(Long id) {
        return Optional.ofNullable(libros.get(id));
    }
    
    @Override
    public Optional<Libro> findByIsbn(String isbn) {
        return libros.values().stream()
            .filter(libro -> libro.getIsbn().equals(isbn))
            .findFirst();
    }
    
    @Override
    public List<Libro> findAll() {
        return new ArrayList<>(libros.values());
    }
    
    @Override
    public void deleteById(Long id) {
        libros.remove(id);
    }
    
    @Override
    public boolean existsById(Long id) {
        return libros.containsKey(id);
    }
}

// Interface del servicio
public interface LibroService {
    Libro buscarPorIsbn(String isbn);
    List<Libro> obtenerTodos();
    Libro guardar(Libro libro);
    void eliminar(Long id);
    Libro actualizar(Long id, Libro libro);
}

// Implementación del servicio
@Service
public class LibroServiceImpl implements LibroService {
    private final LibroRepository libroRepository;
    
    public LibroServiceImpl(LibroRepository libroRepository) {
        this.libroRepository = libroRepository;
    }
    
    @Override
    public Libro buscarPorIsbn(String isbn) {
        return libroRepository.findByIsbn(isbn)
            .orElseThrow(() -> new LibroNoEncontradoException(isbn));
    }
    
    @Override
    public List<Libro> obtenerTodos() {
        return libroRepository.findAll();
    }
    
    @Override
    public Libro guardar(Libro libro) {
        return libroRepository.save(libro);
    }
    
    @Override
    public void eliminar(Long id) {
        libroRepository.deleteById(id);
    }
    
    @Override
    public Libro actualizar(Long id, Libro libro) {
        if (!libroRepository.existsById(id)) {
            throw new LibroNoEncontradoException(id);
        }
        libro.setId(id);
        return libroRepository.save(libro);
    }
}
```

### Etapa 3: Implementación de Controllers

#### Objetivos
- Implementar la capa de controladores REST
- Aplicar el principio DIP (Dependency Inversion)
- Manejar excepciones HTTP

#### Tareas
1. Crear controladores REST:
   - `LibroController`
   - `UsuarioController`
   - `PrestamoController`

2. Implementar endpoints básicos:
   - GET /api/libros
   - GET /api/libros/{id}
   - POST /api/libros
   - PUT /api/libros/{id}
   - DELETE /api/libros/{id}

#### Ejemplo de Implementación
```java
@RestController
@RequestMapping("/api/libros")
public class LibroController {
    private final LibroService libroService;
    
    public LibroController(LibroService libroService) {
        this.libroService = libroService;
    }
    
    @GetMapping
    public List<Libro> obtenerTodos() {
        return libroService.obtenerTodos();
    }
    
    @GetMapping("/{id}")
    public Libro obtenerPorId(@PathVariable Long id) {
        return libroService.buscarPorId(id);
    }
    
    @PostMapping
    public Libro crear(@RequestBody Libro libro) {
        return libroService.guardar(libro);
    }
    
    @PutMapping("/{id}")
    public Libro actualizar(@PathVariable Long id, @RequestBody Libro libro) {
        return libroService.actualizar(id, libro);
    }
    
    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        libroService.eliminar(id);
    }
}
```

### Etapa 4: Testing y Documentación

#### Objetivos
- Implementar tests unitarios y de integración
- Documentar la API y el código
- Asegurar la calidad del código

#### Tareas
1. Implementar tests:
   - Tests unitarios para servicios
   - Tests unitarios para repositorios
   - Tests de integración para controladores

2. Documentar:
   - Documentar endpoints con comentarios
   - Actualizar README con instrucciones
   - Documentar arquitectura y decisiones de diseño

#### Ejemplo de Test
```java
@ExtendWith(MockitoExtension.class)
class LibroServiceImplTest {
    @Mock
    private LibroRepository libroRepository;
    
    @InjectMocks
    private LibroServiceImpl libroService;
    
    @Test
    void cuandoBuscarPorIsbnExiste_entoncesRetornaLibro() {
        // Arrange
        String isbn = "123-456-789";
        Libro libroEsperado = new Libro(1L, isbn, "Test Book", "Test Author", EstadoLibro.DISPONIBLE);
        when(libroRepository.findByIsbn(isbn)).thenReturn(Optional.of(libroEsperado));
        
        // Act
        Libro resultado = libroService.buscarPorIsbn(isbn);
        
        // Assert
        assertNotNull(resultado);
        assertEquals(isbn, resultado.getIsbn());
        verify(libroRepository).findByIsbn(isbn);
    }
    
    @Test
    void cuandoBuscarPorIsbnNoExiste_entoncesLanzaExcepcion() {
        // Arrange
        String isbn = "123-456-789";
        when(libroRepository.findByIsbn(isbn)).thenReturn(Optional.empty());
        
        // Act & Assert
        assertThrows(LibroNoEncontradoException.class, () -> 
            libroService.buscarPorIsbn(isbn)
        );
    }
}
```

## ✅ Entrega y Flujo de Trabajo con GitHub

1. **Configuración del Repositorio**
   - Proteger la rama `main`
   - Crear template de Issues y Pull Requests

2. **Project Kanban**
   - `To Do`
   - `In Progress`
   - `Code Review`
   - `Done`

3. **Milestones**
   - Etapa 1: Configuración y Modelos
   - Etapa 2: Repositories y Services
   - Etapa 3: Controllers
   - Etapa 4: Testing y Documentación

4. **Issues y Pull Requests**
   - Crear Issues detallados para cada funcionalidad
   - Asociar cada Issue a un Milestone
   - Implementar en ramas feature
   - Revisar código antes de merge

## ✅ Requisitos para la Entrega

- ✅ Implementación completa de todas las etapas
- ✅ Código bien documentado
- ✅ Todos los Issues cerrados
- ✅ Todos los Milestones completados
- ✅ Pull Requests revisados y aprobados
- ✅ Project actualizado
- ✅ README.md completo con:
  - Instrucciones de instalación
  - Requisitos del sistema
  - Ejemplos de uso
  - Documentación de endpoints

## 📚 Recursos Adicionales

- [Documentación de Spring Boot](https://spring.io/projects/spring-boot)
- [REST API Best Practices](https://restfulapi.net/)
- [Spring Boot Testing](https://spring.io/guides/gs/testing-web/)
- [JUnit 5 User Guide](https://junit.org/junit5/docs/current/user-guide/)
- [Mockito Documentation](https://javadoc.io/doc/org.mockito/mockito-core/latest/org/mockito/Mockito.html)
- [Spring Boot Test Documentation](https://docs.spring.io/spring-boot/docs/current/reference/html/features.html#features.testing)
- [Testing Spring Boot Applications](https://www.baeldung.com/spring-boot-testing)

## 📋 Guía de Testing

### 1. Testing de Servicios
- Usar `@ExtendWith(MockitoExtension.class)`
- Mockear dependencias con `@Mock`
- Inyectar mocks con `@InjectMocks`
- Seguir el patrón Arrange-Act-Assert
- Probar casos positivos y negativos
- Verificar interacciones con mocks

### 2. Testing de Controladores
- Usar `@WebMvcTest` para pruebas de integración
- Mockear servicios con `@MockBean`
- Usar `MockMvc` para simular peticiones HTTP
- Verificar respuestas HTTP y contenido JSON
- Probar diferentes escenarios de error

### 3. Testing de Repositorios
- Probar operaciones CRUD básicas
- Verificar manejo de IDs
- Probar búsquedas y filtros
- Validar comportamiento con datos inválidos

### 4. Buenas Prácticas de Testing
- Nombres descriptivos para tests
- Un assert por test cuando sea posible
- Cobertura de código significativa
- Tests independientes y aislados
- Uso de `@BeforeEach` para setup común
- Documentación de casos de prueba

## 🏗️ Cómo funciona el sistema

### Descripción general de la arquitectura

El sistema está diseñado con una arquitectura en capas para una clara separación de responsabilidades, facilitando la mantenibilidad, escalabilidad y testeabilidad. Se han aplicado los principios SOLID en la medida de lo posible:

1.  **Capa de Modelo (`com.biblioteca.sistemagestion.modelo`):**
   * Contiene las clases POJO (Plain Old Java Objects) que representan las entidades del dominio: `Libro`, `Usuario`, `Prestamo`.
   * Incluye los `enum` para los estados de las entidades: `EstadoLibro`, `EstadoUsuario`.
   * Estas clases son simples contenedores de datos, a menudo con la ayuda de Lombok para getters, setters y constructores. Su única responsabilidad es representar los datos (SRP).

2.  **Capa de Repositorio (Acceso a Datos - `com.biblioteca.sistemagestion.repositorios`):**
   * Define las interfaces (`LibroRepository`, `UsuarioRepository`, `PrestamoRepository`) que establecen el contrato para las operaciones de persistencia (CRUD y búsquedas).
   * Las implementaciones (`LibroRepositoryImpl`, etc.) utilizan colecciones en memoria (`HashMap`) para simular una base de datos. Se encargan de la lógica de almacenamiento, recuperación y generación de IDs.
   * Anotadas con `@Repository` para ser gestionadas como beans de Spring.
   * Esta capa abstrae los detalles de la persistencia de la capa de servicio (DIP).

3.  **Capa de Servicio (Lógica de Negocio - `com.biblioteca.sistemagestion.servicios`):**
   * Define las interfaces (`LibroService`, etc.) que exponen las operaciones de negocio.
   * Las implementaciones (`LibroServiceImpl`, etc.) contienen la lógica de negocio principal, validaciones, orquestación de llamadas a los repositorios y el lanzamiento de excepciones personalizadas.
   * Anotadas con `@Service` y dependen de las interfaces de Repositorio (DIP), no de sus implementaciones.
   * Cada servicio se enfoca en una entidad o área de negocio específica (SRP).

4.  **Capa de Presentación (Controladores REST - `com.biblioteca.sistemagestion.controladores`):**
   * Expone la funcionalidad del sistema a través de una API RESTful.
   * Clases como `LibroController`, `UsuarioController`, `PrestamoController` están anotadas con `@RestController` y `@RequestMapping`.
   * Manejan las peticiones HTTP, validan entradas (usando DTOs si es necesario), delegan la lógica a la capa de Servicio y formatean las respuestas HTTP (generalmente JSON).
   * Dependen de las interfaces de la capa de Servicio (DIP).

5.  **Manejo de Excepciones:**
   * **Excepciones Personalizadas (`com.biblioteca.sistemagestion.excepciones`):** Se definen excepciones específicas como `LibroNoEncontradoException`, `RecursoDuplicadoException`, `OperacionNoPermitidaException` para representar errores de negocio.
   * **Manejador Global (`com.biblioteca.sistemagestion.web.excepciones.RestExceptionHandler`):** Una clase anotada con `@ControllerAdvice` intercepta las excepciones lanzadas por los servicios/controladores y las traduce en respuestas HTTP estandarizadas y con los códigos de estado apropiados (ej. 404, 400, 409).

6.  **DTOs (`com.biblioteca.sistemagestion.dtos`):**
   * Se utiliza `PrestamoRequestDTO` para encapsular los datos de entrada al crear un préstamo.

### Componentes Principales

* **`Libro`, `Usuario`, `Prestamo`**: Clases de entidad que modelan los datos principales.
* **`EstadoLibro`, `EstadoUsuario`**: Enums para los estados de las entidades.
* **`LibroRepository`, `UsuarioRepository`, `PrestamoRepository` (y sus `Impl`)**: Interfaces e implementaciones para el acceso a datos en memoria.
* **`LibroService`, `UsuarioService`, `PrestamoService` (y sus `Impl`)**: Interfaces e implementaciones para la lógica de negocio.
* **`LibroController`, `UsuarioController`, `PrestamoController`**: Controladores REST que exponen los endpoints de la API.
* **`PrestamoRequestDTO`**: DTO para la creación de préstamos.
* **`RestExceptionHandler`**: Manejador global de excepciones para la API.
* **`ServicioNotificacionesConsola`**: Servicio para mostrar notificaciones (ej. de préstamos, aunque su integración completa con la lógica de negocio no fue el foco principal de esta etapa de controladores).
* **`ExecutorService`**: Configurado en `BibliotecaSpringApplication` para el envío asíncrono de notificaciones.

### Flujo de trabajo del sistema (Ejemplo: Crear un nuevo Libro)
1.  Un cliente envía una petición `POST` a `/api/libros` con los datos del libro en formato JSON.
2.  `LibroController` recibe la petición. El `@RequestBody` es deserializado a un objeto `Libro`.
3.  El controlador invoca `libroService.crearLibro(libro)`.
4.  `LibroServiceImpl` valida los datos (ej. ISBN no duplicado, campos no vacíos). Si falla, lanza una excepción (`RecursoDuplicadoException`, `IllegalArgumentException`).
5.  Si la validación es exitosa, `LibroServiceImpl` llama a `libroRepository.save(libro)`.
6.  `LibroRepositoryImpl` asigna un ID al libro (si es nuevo) y lo guarda en su `HashMap` interno. Devuelve el libro con ID.
7.  El servicio devuelve el libro guardado al controlador.
8.  `LibroController` construye una respuesta HTTP 201 (Created) con la URI del nuevo recurso y el libro creado en el cuerpo.
9.  Si en algún punto se lanzó una excepción (ej. `RecursoDuplicadoException`), `RestExceptionHandler` la intercepta y devuelve una respuesta HTTP de error apropiada (ej. 409 Conflict).

## 🚀 Cómo ponerlo en funcionamiento

### Requisitos Previos
* **Java Development Kit (JDK):** Versión 21 o superior.
* **Apache Maven:** Versión 3.9.0 o superior.
* **Git:** Para clonar el repositorio.
* **IDE (Recomendado):** IntelliJ IDEA, Eclipse IDE (con soporte Maven), o Visual Studio Code (con Extension Pack for Java).

### Puesta en Marcha
1.  **Clonar el Repositorio:**
    ```bash
    git clone https://github.com/um-programacion-ii/programacion-2-trabajo-practico-4-perezmatias.git
    cd programacion-2-trabajo-practico-4-perezmatias
    ```
2.  **Importar como Proyecto Maven en el IDE:**
   * Abre tu IDE y selecciona la opción para importar un proyecto Maven existente.
   * Navega hasta la carpeta del proyecto clonado y selecciónala. El IDE debería reconocer el `pom.xml`.

### Proceso de Compilación
Maven se encarga de la compilación.
1.  Abre una terminal en la raíz del proyecto (donde está el `pom.xml`).
2.  Ejecuta:
    ```bash
    mvn clean package
    ```
    Esto compilará el código, ejecutará los tests y creará un archivo `.jar` ejecutable en la carpeta `target/`. Si solo quieres compilar sin empaquetar ni correr tests (útil durante el desarrollo):
    ```bash
    mvn clean compile
    ```

### Ejecución de la Aplicación
1.  **Usando el Plugin de Maven (para desarrollo):**
    Desde la raíz del proyecto en la terminal:
    ```bash
    mvn spring-boot:run
    ```

La aplicación iniciará un servidor Tomcat embebido y estará disponible por defecto en `http://localhost:8080`.

## 🧪 Cómo probar cada aspecto desarrollado / Ejemplos de Uso API

La API REST se puede probar con herramientas como Postman, Insomnia, o `curl` desde la terminal. A continuación, se muestran ejemplos para los endpoints principales.

**(Asegúrate de que la aplicación Spring Boot esté corriendo antes de probarlos).**

### 1. Libros (`/api/libros`)

* **Crear Libro:**
   * `POST /api/libros`
   * `Content-Type: application/json`
   * Body:
       ```json
       {
           "isbn": "978-0132350884",
           "titulo": "Clean Code",
           "autor": "Robert C. Martin",
           "estado": "DISPONIBLE"
       }
       ```
   * Respuesta Exitosa (201 Created): Libro creado con ID. Cabecera `Location` con URI.
   * Respuesta Error (ISBN duplicado): HTTP 409, cuerpo JSON de error.
   * Respuesta Error (Título vacío): HTTP 400, cuerpo JSON de error.

* **Obtener Todos los Libros:**
   * `GET /api/libros`
   * Respuesta Exitosa (200 OK): Array JSON de libros.

* **Obtener Libro por ID:**
   * `GET /api/libros/{id}` (ej. `/api/libros/1`)
   * Respuesta Exitosa (200 OK): Objeto JSON del libro.
   * Respuesta Error (ID no encontrado): HTTP 404, cuerpo JSON de error.

* **Actualizar Libro:**
   * `PUT /api/libros/{id}` (ej. `/api/libros/1`)
   * `Content-Type: application/json`
   * Body (campos a actualizar):
       ```json
       {
           "titulo": "Clean Code: A Handbook of Agile Software Craftsmanship",
           "autor": "Robert C. Martin (Uncle Bob)",
           "estado": "EN_REPARACION"
       }
       ```
   * Respuesta Exitosa (200 OK): Objeto JSON del libro actualizado.
   * Respuesta Error (ID no encontrado): HTTP 404.

* **Eliminar Libro:**
   * `DELETE /api/libros/{id}` (ej. `/api/libros/1`)
   * Respuesta Exitosa (204 No Content): Sin cuerpo.
   * Respuesta Error (ID no encontrado): HTTP 404.

### 2. Usuarios (`/api/usuarios`)

* **Crear Usuario:**
   * `POST /api/usuarios`
   * `Content-Type: application/json`
   * Body:
       ```json
       {
           "nombre": "Ana Torres",
           "email": "ana.torres@example.com",
           "estado": "ACTIVO"
       }
       ```
   * Respuesta Exitosa (201 Created): Usuario creado con ID.
   * Respuesta Error (Email duplicado): HTTP 409.
   * Respuesta Error (Nombre/Email vacío): HTTP 400.

* **Obtener Todos los Usuarios:** `GET /api/usuarios`
* **Obtener Usuario por ID:** `GET /api/usuarios/{id}` (Maneja 404)
* **Actualizar Usuario:** `PUT /api/usuarios/{id}` (Maneja 404 para ID, 409 para email duplicado de otro usuario)
* **Eliminar Usuario:** `DELETE /api/usuarios/{id}` (Maneja 404)

### 3. Préstamos (`/api/prestamos`)

* **Realizar Préstamo:**
   * `POST /api/prestamos`
   * `Content-Type: application/json`
   * Body (`PrestamoRequestDTO`):
       ```json
       {
           "libroId": 1,  // ID de un libro existente y DISPONIBLE
           "usuarioId": 1, // ID de un usuario existente y ACTIVO
           "fechaDevolucionSugerida": "2025-07-15" // Formato YYYY-MM-DD, fecha futura
       }
       ```
   * Respuesta Exitosa (201 Created): Objeto `Prestamo` creado.
   * Respuesta Error:
      * HTTP 404 si `libroId` o `usuarioId` no existen.
      * HTTP 400 si el libro no está `DISPONIBLE` (via `RecursoNoDisponibleException`).
      * HTTP 400 si `fechaDevolucionSugerida` es inválida (via `IllegalArgumentException`).
      * HTTP 400 si falta algún campo requerido en el DTO (via `NullPointerException` o `MethodArgumentNotValidException` si se usa Bean Validation).

* **Registrar Devolución:**
   * `POST /api/prestamos/{prestamoId}/devolver` (ej. `/api/prestamos/101/devolver`)
   * Respuesta Exitosa (200 OK): Objeto `Prestamo` (conceptualmenmte devuelto). El estado del libro asociado se actualiza a `DISPONIBLE` (o `RESERVADO` si hay reservas).
   * Respuesta Error (ID de préstamo no encontrado): HTTP 404.

* **Obtener Todos los Préstamos:** `GET /api/prestamos`
* **Obtener Préstamo por ID:** `GET /api/prestamos/{id}` (Maneja 404)
* **Obtener Préstamos por Usuario:** `GET /api/prestamos/usuario/{usuarioId}`
* **Obtener Préstamos por Libro:** `GET /api/prestamos/libro/{libroId}`

### Evidencia de Pruebas Automatizadas Superadas

Todas las pruebas unitarias (para repositorios y servicios) y las pruebas de integración (para controladores) implementadas como parte de la Etapa 4 pasan correctamente. La ejecución de `mvn test` en la rama `main` (o `develop` final) finaliza con `BUILD SUCCESS` y 0 fallos/errores.
 ```
[INFO]
[INFO] Results:
[INFO]
[INFO] Tests run: 95, Failures: 0, Errors: 0, Skipped: 0
[INFO]
[INFO] ------------------------------------------------------------------------
[INFO] BUILD SUCCESS
[INFO] ------------------------------------------------------------------------
[INFO] Total time:  5.971 s
[INFO] Finished at: 2025-05-15T23:49:50-03:00
[INFO] ------------------------------------------------------------------------
 ```
## 📝 Consideraciones Éticas sobre el Uso de IA

El uso de Inteligencia Artificial (IA) en este trabajo práctico debe seguir las siguientes pautas:

1. **Transparencia**
   - Documentar el uso de IA en el desarrollo
   - Explicar las modificaciones realizadas al código generado
   - Mantener un registro de las herramientas utilizadas

2. **Aprendizaje**
   - La IA debe usarse como herramienta de aprendizaje
   - Comprender y ser capaz de explicar el código generado
   - Utilizar la IA para mejorar la comprensión de conceptos

3. **Integridad Académica**
   - El trabajo final debe reflejar tu aprendizaje
   - No se permite la presentación de código sin comprensión
   - Debes poder explicar y defender cualquier parte del código

4. **Responsabilidad**
   - Verificar la corrección del código generado
   - Asegurar que el código cumple con los requisitos
   - Mantener la calidad y estándares de código

5. **Desarrollo Individual**
   - La IA puede usarse para facilitar el aprendizaje
   - Documentar el proceso de desarrollo
   - Mantener un registro del progreso

## Uso de Asistencia de IA (Google Gemini)

Siguiendo las pautas de integridad académica y transparencia establecidas para este trabajo práctico, se declara el uso de la herramienta de inteligencia artificial Google Gemini como asistente durante el desarrollo.

La asistencia de IA se utilizó específicamente en las siguientes áreas:

* **Resolución de Errores:** Ayuda en la identificación y corrección de errores de compilación y runtime encontrados durante la codificación.
* **Estructuración de Tareas:** Sugerencias para organizar y describir los Issues de GitHub correspondientes a cada etapa del desarrollo.
* **Estructura del Proyecto:** Recomendaciones sobre la adopción de la estructura estándar de paquetes y carpetas para proyectos Java (`src/main/java`, etc.).
* **Guía y Planificación:** Asistencia en la interpretación inicial de los requisitos y en la planificación del desarrollo de las funcionalidades solicitadas.

**Autoría del Código:**

Es importante destacar que **el codigo presentado y logica utilizada son de autoria propia**. La IA funcionó como una herramienta de apoyo para superar bloqueos (errores), organizar el trabajo y obtener guía sobre convenciones estándar.

## 📝 Licencia

Este trabajo es parte del curso de Programación II de Ingeniería en Informática. Uso educativo únicamente.